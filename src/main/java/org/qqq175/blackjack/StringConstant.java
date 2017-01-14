package org.qqq175.blackjack;

public interface StringConstant {
	public static final String ATTRIBUTE_USER = "user";
	public static final String ATTRIBUTE_ERROR_LOGIN = "loginError";
	public static final String ATTRIBUTE_ERROR_REGISTRATION = "regError";
	public static final String ATTRIBUTE_MAIN_FORM = "mainform";

	public static final String PARAMETER_EMAIL = "email";
	public static final String PARAMETER_PASSWORD = "password";
	public static final String PARAMETER_PASSWORD_REPEAT = "passrepeat";
	public static final String PARAMETER_LOCALE = "locale";
	public static final String PARAMETER_FIRST_NAME = "first-name";
	public static final String PARAMETER_LAST_NAME = "last-name";
	public static final String PARAMETER_DISPLAY_NAME = "display-name";
	public static final String PARAMETER_PHOTO = "photo";

	public static final String SCOPE_DEFAULT = "main";
	public static final String ACTION_DEFAULT = "index";

	public static final String PATTERN_EMAIL = "[\\w\\.-_]+@\\w+\\.[\\.\\w]+";
	public static final String PATTERN_PASSWORD = "\\w{6,}";
	public static final String PATTERN_NAME = "[а-яА-ЯёЁa-zA-Z]{2,127}";
	public static final String PATTERN_DISPLAY_NAME = "[а-яА-ЯёЁa-zA-Z0-9][а-яА-ЯёЁa-zA-Z0-9_\\.-]{2,255}[а-яА-ЯёЁa-zA-Z0-9]";
	public static final String PATTERN_PHOTO_EXTENSION = "(.png$)|(.jpg$)";
}
