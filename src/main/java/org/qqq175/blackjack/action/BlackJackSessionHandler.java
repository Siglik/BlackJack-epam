package org.qqq175.blackjack.action;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.Session;

import org.json.simple.JSONObject;

public class BlackJackSessionHandler {
	private static final BlackJackSessionHandler INSTANCE = new BlackJackSessionHandler();
	private int deviceId = 0;
	private final Set<Session> sessions = new HashSet<>();

	private BlackJackSessionHandler() {
		System.out.println("Handler created");
	}

	public static BlackJackSessionHandler getInstance() {
		return INSTANCE;
	}

	public void addSession(Session session) {
		sessions.add(session);
	}

	public void removeSession(Session session) {
		sessions.remove(session);
	}

	public void removeDevice(int id) {
	}

	public void toggleDevice(int id) {
	}

	private JSONObject createAddMessage(Object device) {
		JSONObject addMessage = new JSONObject();
		addMessage.put("action", "add");

		return addMessage;
	}

	private void sendToAllConnectedSessions(JSONObject message) {
		for (Session session : sessions) {
			sendToSession(session, message);
		}
	}

	private void sendToSession(Session session, JSONObject message) {
		try {
			session.getBasicRemote().sendText(message.toJSONString());
		} catch (IOException ex) {
			sessions.remove(session);
		}
	}

}
