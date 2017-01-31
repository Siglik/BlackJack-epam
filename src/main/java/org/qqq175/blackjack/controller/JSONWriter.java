package org.qqq175.blackjack.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

/**
 * JSON writer
 * @author qqq175
 *
 */
public class JSONWriter {

	/**
	 * Writes JSON object to http response
	 * @param response
	 * @param json
	 * @throws IOException
	 */
	public void writeJSON(HttpServletResponse response, JSONObject json) throws IOException {
		response.setContentType("application/json");
		PrintWriter writer = response.getWriter();
		String output = json.toJSONString().replaceAll("\\\\/", "/").replaceAll(",", ",\n");
		writer.write(output);
		writer.flush();
		writer.close();
	}
}
