package org.qqq175.blackjack.service.implemented.main;

import static org.qqq175.blackjack.service.ActionResult.ActionType.FORWARD;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.logic.main.RegisterLogic;
import org.qqq175.blackjack.persistence.dao.util.JSPPathManager;
import org.qqq175.blackjack.service.Action;
import org.qqq175.blackjack.service.ActionResult;

/**
 * Perform user registration(if params got) or show registration form(if no
 * params)
 * 
 * @author qqq175
 *
 */
public class RegisterAction implements Action {
	private static final String BIG_FILE = "File is too big";
	private static final String UNABLE_TO_SAVE_FILE = "Unable to save file";
	private static Logger log = LogManager.getLogger(RegisterAction.class);

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
				log.error(UNABLE_TO_SAVE_FILE, e);
			} catch (ServletException e) {
				// no file - it's ok;
			} catch (IllegalStateException e) {
				log.warn(BIG_FILE, e);
				request.setAttribute(StringConstant.ATTRIBUTE_ERROR_REGISTRATION, "message.error.bigfile");
				request.setAttribute(StringConstant.ATTRIBUTE_MAIN_FORM, JSPPathManager.getProperty("form.register"));

				result = new ActionResult(FORWARD, JSPPathManager.getProperty("page.main"));
			}

			// if no file - continue registration
			if (result == null) {
				RegisterLogic.Result registerResult = rLogic.registerUser(params, part);
				if (registerResult == RegisterLogic.Result.OK) {
					result = new ActionResult(FORWARD, JSPPathManager.getProperty("command.login"));
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
