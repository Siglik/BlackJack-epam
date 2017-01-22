package org.qqq175.blackjack;

public interface StringConstant {
	public static final String ATTRIBUTE_USER = "user";
	public static final String ATTRIBUTE_ERROR_LOGIN = "loginError";
	public static final String ATTRIBUTE_ERROR_PAYMENT = "paymentError";
	public static final String ATTRIBUTE_ERROR_REGISTRATION = "regError";
	public static final String ATTRIBUTE_PHOTO_ERROR = "photoError";
	public static final String ATTRIBUTE_PASSWORD_MESSAGE = "passMessage";
	public static final String ATTRIBUTE_PERSONAL_ERROR = "personalError";
	public static final String ATTRIBUTE_MAIN_FORM = "mainform";
	public static final String ATTRIBUTE_USERSTAT = "userstat";
	public static final String ATTRIBUTE_LOCALE = "curLocale";
	public static final String ATTRIBUTE_POPUP_MESSAGE = "popupMessage";
	public static final String ATTRIBUTE_JSON = "json";
	public static final String ATTRIBUTE_PAGE_COUNT = "pageCount";
	public static final String ATTRIBUTE_USERS = "users";
	public static final String ATTRIBUTE_TOTAL = "total";
	public static final String ATTRIBUTE_OPERATIONS = "operations";
	public static final String ATTRIBUTE_GAME = "game";

	public static final String PARAMETER_EMAIL = "email";
	public static final String PARAMETER_PASSWORD = "password";
	public static final String PARAMETER_PASSWORD_REPEAT = "passrepeat";
	public static final String PARAMETER_PASSWORD_OLD = "old-password";
	public static final String PARAMETER_PASSWORD_NEW = "new-password";
	public static final String PARAMETER_LOCALE = "locale";
	public static final String PARAMETER_FIRST_NAME = "first-name";
	public static final String PARAMETER_LAST_NAME = "last-name";
	public static final String PARAMETER_DISPLAY_NAME = "display-name";
	public static final String PARAMETER_PHOTO = "photo";
	public static final String PARAMETER_ID = "id";

	public static final String PARAMETER_PAYMENT_SUM = "sum";
	public static final String PARAMETER_PAYMENT_CARD_NUMBER = "number";
	public static final String PARAMETER_PAYMENT_EXPR_MONTH = "exp-month";
	public static final String PARAMETER_PAYMENT_EXPR_YEAR = "expr-year";
	public static final String PARAMETER_PAYMENT_CVV = "cvv";
	public static final String PARAMETER_PAYMENT_CARDHOLDER = "cardholder";
	public static final String PARAMETER_PAYMENT_TYPE = "operation";
	public static final String PARAMETER_PAGE = "page";

	public static final String COOKIE_LOCALE = "locale";

	public static final String SCOPE_DEFAULT = "main";
	public static final String ACTION_DEFAULT = "index";

	public static final String PATTERN_EMAIL = "^[\\w\\.-_]+@\\w+\\.[\\.\\w]+$";
	public static final String PATTERN_PASSWORD = "^\\w{6,}$";
	public static final String PATTERN_NAME = "^[а-яА-ЯёЁa-zA-Z]{2,127}$";
	public static final String PATTERN_DISPLAY_NAME = "^[а-яА-ЯёЁa-zA-Z0-9][а-яА-ЯёЁa-zA-Z0-9_\\.-]{2,255}[а-яА-ЯёЁa-zA-Z0-9]$";
	public static final String PATTERN_PHOTO_EXTENSION = "(.png$)|(.jpg$)";

	public static final String PATTERN_PAYMENT_SUM = "^[0-9]{1,6}(\\.[0-9]{1,2})?$";
	public static final String PATTERN_CARD = "(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$";
	public static final String PATTERN_MONTH = "^(1[0-2]|0?[1-9])$";
	public static final String PATTERN_YEAR_2 = "^[0-9]{2}$";
	public static final String PATTERN_CARD_CVV = "^[0-9]{3,4}$";
	public static final String PATTERN_CARDHOLDER = "^[a-zA-Z]{2,64}[ ]+[a-zA-Z]{2,64}$";
	public static final String PATTERN_PAYMENT_OPERATION = "(^payment$)|(^withdrawal$)";
}
