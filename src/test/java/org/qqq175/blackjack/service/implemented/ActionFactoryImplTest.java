/**
 * 
 */
package org.qqq175.blackjack.service.implemented;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qqq175.blackjack.controller.CommandParser.CommandContext;
import org.qqq175.blackjack.service.Action;

/**
 * @author qqq175
 *
 */
public class ActionFactoryImplTest {
	private static List<CommandContext> badCommands;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		badCommands = new ArrayList<>();
		badCommands.add(new CommandContext(null, null, true));
		badCommands.add(new CommandContext(null, null, false));
		badCommands.add(new CommandContext("scope", "action", false));
		badCommands.add(new CommandContext("main", "login", true));
		badCommands.add(new CommandContext("main", null, false));
		badCommands.add(new CommandContext("main", "index", true));
		badCommands.add(new CommandContext("player", "hahaha", false));
		badCommands.add(new CommandContext("admin", "hahaha", false));
		badCommands.add(new CommandContext("game", "hahaha", false));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		badCommands = null;
	}

	@Test
	public void defineActionTestBad() {
		ActionFactoryImpl factory = new ActionFactoryImpl();
		for (CommandContext context : badCommands) {
			Action action = factory.defineAction(context);
			Assert.assertSame(MainActionEnum.INDEX.getAction(), action);
		}
	}

	@Test
	public void defineActionTestGood() {
		ActionFactoryImpl factory = new ActionFactoryImpl();
		Action action = factory.defineAction(new CommandContext("main", "index", false));
		Assert.assertSame(MainActionEnum.INDEX.getAction(), action);

		action = factory.defineAction(new CommandContext("main", "login", false));
		Assert.assertSame(MainActionEnum.LOGIN.getAction(), action);

		action = factory.defineAction(new CommandContext("MAIN", "LOGIN", false));
		Assert.assertSame(MainActionEnum.LOGIN.getAction(), action);

		action = factory.defineAction(new CommandContext("PLAYER", "sEtTiNgS", false));
		Assert.assertSame(PlayerActionEnum.SETTINGS.getAction(), action);

		action = factory.defineAction(new CommandContext("admin", "playerinfo", false));
		Assert.assertSame(AdminActionEnum.PLAYERINFO.getAction(), action);

		action = factory.defineAction(new CommandContext("game", "game", false));
		Assert.assertSame(GameActionEnum.GAME.getAction(), action);
	}

}
