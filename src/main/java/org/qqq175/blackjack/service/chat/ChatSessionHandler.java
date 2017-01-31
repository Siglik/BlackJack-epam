package org.qqq175.blackjack.service.chat;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.qqq175.blackjack.logic.chat.MessageLogic;
import org.qqq175.blackjack.persistence.dao.util.PhotoManager;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.GameId;
import org.qqq175.blackjack.persistence.entity.id.MessageId;

/**
 * Handles chat websocket connections and messages
 * @author qqq175
 *
 */
public class ChatSessionHandler {
	private static Logger log = LogManager.getLogger(ChatSessionHandler.class);
	private static PhotoManager photoManager = new PhotoManager();
	private static final ChatSessionHandler INSTANCE = new ChatSessionHandler();
	private final Map<GameId, Set<Session>> chats = new ConcurrentHashMap<>();

	private ChatSessionHandler() {

	}

	public static ChatSessionHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * add new session to game chat
	 * @param session
	 * @param gameId
	 */
	public void addSession(Session session, GameId gameId) {
		if (chats.containsKey(gameId)) {
			Set<Session> chatSessions = chats.get(gameId);
			chatSessions.add(session);
		} else {
			Set<Session> chatSessions = Collections.newSetFromMap(new ConcurrentHashMap<Session, Boolean>());
			chatSessions.add(session);
			chats.put(gameId, chatSessions);
		}
	}

	/**
	 * remove session from game chat
	 * @param session
	 * @param gameId
	 */
	public void removeSession(Session session, GameId gameId) {
		Set<Session> chatSessions = chats.get(gameId);
		if (chatSessions != null) {
			chatSessions.remove(session);
			if (chatSessions.isEmpty()) {
				chats.remove(gameId, chatSessions);
			}
		}
	}

	/**
	 * handle new message - parse, add necessary data, store to database and send to game sessions
	 * @param gameId
	 * @param message
	 * @param user
	 */
	public void handleNewMessage(GameId gameId, String message, User user) {
		JSONParser reader = new JSONParser();
		JSONObject jsonMessage;
		try {
			jsonMessage = (JSONObject) reader.parse(message);

			MessageLogic messageLogic = new MessageLogic();
			String messageText = (String) jsonMessage.get("text");
			MessageId messageId = messageLogic.saveNewMessage(gameId, user.getId(), messageText);

			jsonMessage.put("id", messageId.getValue());
			jsonMessage.put("senderImg", photoManager.findPhotoRelativePath(user.getId()));
			jsonMessage.put("senderName", user.getDisplayName());
			String output = jsonMessage.toJSONString().replaceAll("\\\\/", "/");

			log.debug("message:\n" + output);
			this.sendToGameSessions(gameId, output);
		} catch (ParseException e) {
			log.error("Unable ro send message to game " + gameId.getValue(), e);
		}
	}

	/**
	 * send message to all game session
	 * @param gameId
	 * @param message
	 */
	private void sendToGameSessions(GameId gameId, String message) {
		Set<Session> chatSessions = chats.get(gameId);
		for (Session session : chatSessions) {
			try {
				session.getBasicRemote().sendText(message);
			} catch (IOException ex) {
				chatSessions.remove(session);
			}
		}

		// delete empty chats
		if (chatSessions.isEmpty()) {
			chats.remove(gameId, chatSessions);
		}
	}

}
