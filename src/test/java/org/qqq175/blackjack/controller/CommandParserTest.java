/**
 * 
 */
package org.qqq175.blackjack.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.controller.CommandParser.CommandContext;

/**
 * @author qqq175
 *
 */
public class CommandParserTest {
	private static Map<String, CommandContext> inOutGood;
	private static List<String> badCommands;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		inOutGood = new HashMap<>();
		// good commands
		inOutGood.put("////////", new CommandContext(StringConstant.SCOPE_DEFAULT, StringConstant.ACTION_DEFAULT, false));
		inOutGood.put("/", new CommandContext(StringConstant.SCOPE_DEFAULT, StringConstant.ACTION_DEFAULT, false));
		inOutGood.put("/main/index", new CommandContext("main", "index", false));
		inOutGood.put("/index", new CommandContext(StringConstant.SCOPE_DEFAULT, "index", false));
		inOutGood.put("index", new CommandContext(StringConstant.SCOPE_DEFAULT, "index", false));
		inOutGood.put("/index/", new CommandContext(StringConstant.SCOPE_DEFAULT, "index", false));
		inOutGood.put("/scope/action", new CommandContext("scope", "action", false));
		inOutGood.put("/scope/action/", new CommandContext("scope", "action", false));
		// bad commands
		badCommands = new ArrayList<>();
		badCommands.add("/ddd/ddd/ddd/ddd");
		badCommands.add("/main/index/index");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		inOutGood = null;
		badCommands = null;
	}

	@Test
	public void parceTestGood() {
		CommandParser parser = new CommandParser();
		for (Map.Entry<String, CommandContext> entry : inOutGood.entrySet()) {
			CommandContext result = parser.parse(entry.getKey());
			Assert.assertEquals(entry.getValue(), result);
		}
	}

	@Test
	public void parceTestBad() {
		CommandContext expected = new CommandContext(null, null, true);
		CommandParser parser = new CommandParser();
		for (String query : badCommands) {
			CommandContext result = parser.parse(query);
			Assert.assertEquals(expected, result);
		}
	}

}
