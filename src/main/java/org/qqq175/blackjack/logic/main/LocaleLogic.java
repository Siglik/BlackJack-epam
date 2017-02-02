package org.qqq175.blackjack.logic.main;

import java.util.Locale;

/**
 * Contain supported locales and methods
 * 
 * @author qqq175
 *
 */
public class LocaleLogic {

	/**
	 * supporteed locales
	 * 
	 * @author qqq175
	 *
	 */
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

	/**
	 * return locale object according to locale string/ If not support - return
	 * default locale
	 * 
	 * @param localeStr
	 * @return
	 */
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
