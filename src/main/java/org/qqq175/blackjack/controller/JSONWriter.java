package org.qqq175.blackjack.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

public class JSONWriter {

	public void writeJSON(HttpServletResponse response, JSONObject json) throws IOException {
		response.setContentType("application/json");
		PrintWriter writer = response.getWriter();
		writer.write(json.toJSONString());
		writer.flush();
		writer.close();
	}
}
