package org.qqq175.blackjack.logic.chat;

import java.util.Date;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.persistence.dao.DAOFactory;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.Message;
import org.qqq175.blackjack.persistence.entity.id.GameId;
import org.qqq175.blackjack.persistence.entity.id.MessageId;
import org.qqq175.blackjack.persistence.entity.id.UserId;

public class MessageLogic {
	private static Logger log = LogManager.getLogger(MessageLogic.class);

	public MessageLogic() {
	}

	public MessageId saveNewMessage(GameId gameId, UserId sender, String text) {
		Message message = new Message();
		message.setGameId(gameId);
		message.setUserId(sender);
		String escaped = StringEscapeUtils.escapeHtml4(text);
		message.setText(escaped);
		message.setTime(new Date());

		DAOFactory daoFactory = Settings.getInstance().getDaoFactory();
		MessageId messageId = null;
		try {
			messageId = daoFactory.getMesssageDAO().create(message);
		} catch (DAOException e) {
			log.error("Uable to save message!", e);
		}
		return messageId;
	}

}
