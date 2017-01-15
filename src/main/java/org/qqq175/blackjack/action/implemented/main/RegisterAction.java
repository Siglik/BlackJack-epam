package org.qqq175.blackjack.action.implemented.main;

import static org.qqq175.blackjack.action.ActionResult.ActionType.FORWARD;
import static org.qqq175.blackjack.action.ActionResult.ActionType.REDIRECT;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.logic.main.RegisterLogic;
import org.qqq175.blackjack.persistence.dao.util.JSPPathManager;
import org.qqq175.blackjack.persistence.dao.util.Settings;

public class RegisterAction implements Action {

	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		ActionResult result = null;
		Map<String, String[]> params = request.getParameterMap();

		if (params.isEmpty()) {
			request.setAttribute(StringConstant.ATTRIBUTE_MAIN_FORM, JSPPathManager.getProperty("form.register"));
			result = new ActionResult(FORWARD, JSPPathManager.getProperty("page.main"));
		} else {
			RegisterLogic rLogic = new RegisterLogic();
			Part part = null;
			try {
				part = request.getPart(StringConstant.PARAMETER_PHOTO);
			} catch (IOException e) {
				// TODO LOG HERE
			} catch (ServletException e) {
				// no file - it's ok
			} catch (IllegalStateException e) {
				// file is too big
				request.setAttribute(StringConstant.ATTRIBUTE_ERROR_REGISTRATION, "message.error.bigfile");
				request.setAttribute(StringConstant.ATTRIBUTE_MAIN_FORM, JSPPathManager.getProperty("form.register"));
				result = new ActionResult(FORWARD, JSPPathManager.getProperty("page.main"));
			}

			// if no file - continue registration
			if (result == null) {
				RegisterLogic.Result registerResult = rLogic.registerUser(params, part);
				if (registerResult == RegisterLogic.Result.OK) {
					result = new ActionResult(REDIRECT, Settings.getInstance().getContextPath());
				} else {
					request.setAttribute(StringConstant.ATTRIBUTE_ERROR_REGISTRATION, registerResult.getMessage());
					request.setAttribute(StringConstant.ATTRIBUTE_MAIN_FORM, JSPPathManager.getProperty("form.register"));
					result = new ActionResult(FORWARD, JSPPathManager.getProperty("page.main"));
				}
			}
		}
		return result;
	}

}
