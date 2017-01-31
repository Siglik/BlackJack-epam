package org.qqq175.blackjack.action.implemented.player;

import static org.qqq175.blackjack.action.ActionResult.ActionType.REDIRECT;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.persistence.dao.util.JSPPathManager;
import org.qqq175.blackjack.persistence.dao.util.PhotoManager;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.UserId;

/**
 * upload new user's avatar
 * 
 * @author qqq175
 *
 */
public class ChangeAvatarAction implements Action {
	private static final String BIG_FILE = "File is too big";
	private static final String UNABLE_TO_SAVE_FILE = "Unable to save file";
	private static Logger log = LogManager.getLogger(ChangeAvatarAction.class);

	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		Part part = null;
		try {
			part = request.getPart(StringConstant.PARAMETER_PHOTO);
		} catch (IOException e) {
			log.error(UNABLE_TO_SAVE_FILE, e);
		} catch (ServletException e) {
			log.warn(UNABLE_TO_SAVE_FILE, e);
			request.getSession().setAttribute(StringConstant.ATTRIBUTE_PHOTO_ERROR, "message.error.nofile");
		} catch (IllegalStateException e) {
			log.warn(BIG_FILE, e);
			request.getSession().setAttribute(StringConstant.ATTRIBUTE_PHOTO_ERROR, "message.error.bigfile");
		}

		UserId userId = null;
		User user = (User) request.getSession().getAttribute(StringConstant.ATTRIBUTE_USER);
		if (user != null) {
			userId = user.getId();
		}

		if (userId != null && part != null) {
			PhotoManager uploader = new PhotoManager();
			PhotoManager.Result result = uploader.uploadPhoto(part, userId);
			if (result != PhotoManager.Result.OK) {
				request.getSession().setAttribute(StringConstant.ATTRIBUTE_PHOTO_ERROR, "message.error.photomanager " + result.getMessage());
			}
		} else {
			request.getSession().setAttribute(StringConstant.ATTRIBUTE_PHOTO_ERROR, "message.error.nophoto");
		}

		String context = Settings.getInstance().getContextPath();
		return new ActionResult(REDIRECT, context + JSPPathManager.getProperty("command.settings"));
	}

}
