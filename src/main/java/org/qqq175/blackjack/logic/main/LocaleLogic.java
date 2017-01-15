package org.qqq175.blackjack.logic.main;

import java.util.Locale;

public class LocaleLogic {

	enum SupportedLocale {
		EN(Locale.ENGLISH), RU(new Locale("ru")), DEFAULT(Locale.ENGLISH);
		private Locale locale;

		private SupportedLocale(Locale locale) {
			this.locale = locale;
		}

		public Locale getLocale() {
			return locale;
		}
	}

	public Locale getLocaleByString(String localeStr) {
		SupportedLocale localeEnum;
		try {
			localeEnum = SupportedLocale.valueOf(localeStr.toUpperCase());
		} catch (Exception e) {
			localeEnum = SupportedLocale.DEFAULT;
		}
		return localeEnum.getLocale();
	}

	public Locale getDefaultLocale() {
		return SupportedLocale.DEFAULT.getLocale();
	}
}
