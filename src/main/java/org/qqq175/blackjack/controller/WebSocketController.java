package org.qqq175.blackjack.controller;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.qqq175.blackjack.action.BlackJackSessionHandler;
import org.qqq175.blackjack.persistence.entity.User;

@ServerEndpoint(value = "/game/api", configurator = GetHttpSessionConfigurator.class)
public class WebSocketController {
	private Session wsSession;
	private HttpSession httpSession;

	private static BlackJackSessionHandler sessionHandler = BlackJackSessionHandler.getInstance();

	@OnOpen
	public void open(Session session, EndpointConfig config) {
		this.wsSession = session;
		this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		User user = (User) httpSession.getAttribute("user");
		if (user != null) {
			System.out.println(user.getDisplayName());
		}
		sessionHandler.addSession(session);
	}

	@OnClose
	public void close(Session session) {
		sessionHandler.removeSession(session);
	}

	@OnError
	public void onError(Throwable error) {
		error.printStackTrace();
	}

	@OnMessage
	public void handleMessage(String message, Session session) {
		JSONParser reader = new JSONParser();
		JSONObject jsonMessage;
		try {
			jsonMessage = (JSONObject) reader.parse(message);

			if ("add".equals(jsonMessage.get("action"))) {

			}

			if ("remove".equals(jsonMessage.get("action"))) {

			}

			if ("toggle".equals(jsonMessage.get("action"))) {

			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
