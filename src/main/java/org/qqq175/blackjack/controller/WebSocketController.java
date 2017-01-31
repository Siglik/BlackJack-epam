package org.qqq175.blackjack.controller;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.implemented.chat.ChatSessionHandler;
import org.qqq175.blackjack.game.impl.BlackJackGame;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.GameId;
import org.qqq175.blackjack.pool.GamePool;
import org.qqq175.blackjack.pool.UserPool;

/**
 * Chat application controller (WebSocket)
 * @author qqq175
 */
@ServerEndpoint(value = "/chat", configurator = GetHttpSessionConfigurator.class)
public class WebSocketController {
	private static Logger log = LogManager.getLogger(WebSocketController.class);
	private Session wsSession;
	private HttpSession httpSession;
	private GameId gameId;

	private static ChatSessionHandler sessionHandler = ChatSessionHandler.getInstance();
	
	/**
	 * Handles new sessions
	 * @param session
	 * @param config
	 */
	@OnOpen
	public void open(Session session, EndpointConfig config) {
		boolean isSessionRegistred = false;
		this.wsSession = session;
		this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		User user = (User) httpSession.getAttribute(StringConstant.ATTRIBUTE_USER);
		if (user != null) {
			User userFromPool = UserPool.getInstance().get(user.getId());
			if (userFromPool != null) {
				BlackJackGame game = GamePool.getInstance().get(user.getId());
				if (game != null) {
					/* if user and game online - create session and register it*/
					this.gameId = game.getId();
					sessionHandler.addSession(session, game.getId());
					log.debug("Connected to game " + game.getId().getValue());
					isSessionRegistred = true;
				}
			} else {
				log.warn("User " + user.getDisplayName() + "@" + user.getId().getValue() + " in pool is null. Closing session.");
			}
		} else {
			log.warn("Session is closing. User is null");
		}
		
		/* if session isn't registered - close it*/
		if (!isSessionRegistred) {
			try {
				session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "User is unautorized"));
			} catch (IOException e) {
				log.warn("Unable to close session " + session.getId());
			}
		}
	}

	/**
	 * handle session close
	 * @param session
	 */
	@OnClose
	public void close(Session session) {
		if (gameId != null) {
			log.debug("Clossing connection to game" + gameId.getValue());
			sessionHandler.removeSession(session, gameId);
		}
	}

	/**
	 * handle errors
	 * @param error
	 */
	@OnError
	public void onError(Throwable error) {
		log.error(error);
	}

	/**
	 * handle new incoming messages
	 * @param message
	 * @param session
	 */
	@OnMessage
	public void handleMessage(String message, Session session) {
		boolean isValid = false;
		User user = (User) httpSession.getAttribute("user");
		log.debug("Got message: " + message);
		if (user != null) {
			User userFromPool = UserPool.getInstance().get(user.getId());
			if (userFromPool != null) {
				BlackJackGame game = GamePool.getInstance().get(user.getId());
				if (game != null && game.getId().equals(gameId)) {
					/* if user and game is still valid - send message to subscriers*/
					sessionHandler.handleNewMessage(gameId, message, userFromPool);
					isValid = true;
				}
			}
			/*if user and game is invalid - close session*/
			if (!isValid) {
				sessionHandler.removeSession(session, gameId);
				this.close(session);
				try {
					session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "User is unautorized"));
				} catch (IOException e) {
					log.warn("Unable to close session " + session.getId());
				}
				log.warn("User " + user.getDisplayName() + "@" + user.getId().getValue() + " in pool is null. Closing session.");
			}
		}
	}

}
