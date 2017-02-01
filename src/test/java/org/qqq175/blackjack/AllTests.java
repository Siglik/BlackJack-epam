package org.qqq175.blackjack;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.qqq175.blackjack.controller.CommandParserTest;
import org.qqq175.blackjack.service.implemented.ActionFactoryImplTest;

@RunWith(Suite.class)
@SuiteClasses({ CommandParserTest.class, ActionFactoryImplTest.class })
public class AllTests {

}
