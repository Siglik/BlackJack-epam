package org.qqq175.blackjack.action.implemented.player;

import static org.qqq175.blackjack.action.ActionResult.ActionType.REDIRECT;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.persistence.dao.util.JSPPathManager;
import org.qqq175.blackjack.persistence.dao.util.PhotoManager;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.UserId;

public class ChangeAvatarAction implements Action {

	public ChangeAvatarAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		Part part = null;
		try {
			part = request.getPart(StringConstant.PARAMETER_PHOTO);
		} catch (IOException e) {
			// TODO LOG HERE
		} catch (ServletException e) {
			// no file - it's ok
		} catch (IllegalStateException e) {
			// file is too big
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
