<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="${curLocale}" />
<fmt:setBundle basename="conf.i18n.jsp" />
<link href="/blackjack/css/register.css" rel="stylesheet" />
<main class="column-center main-content">
<form action="/blackjack/$/register" method="post" id="register"
	enctype="multipart/form-data">
	<fieldset>
		<legend>Registration</legend>
		<ul>
			<li class="center">
			  <label for="photo"><fmt:message key="settings.label.upload" />: </label> 
			  <input type="file" name="photo" accept="image/jpeg,image/png" size="60" />
			</li>
			<li>
			  <fmt:message key="settings.placeholder.email" var="placeholder" />
			  <label for="email" class="required"><fmt:message key="login.label.email" />:</label>
			  <input type="email" name="email" placeholder="${placeholder}" id="email"
				pattern="[\w\.-_]+@\w+\.[\.\w]+" required />
			</li>
			<li>
			  <fmt:message key="settings.placeholder.password" var="placeholder" />
			  <label for="password" class="required"><fmt:message key="login.label.password" />:</label> 
			  <input type="password" name="password" placeholder="${placeholder}"
				id="password" pattern="\w{6,}" required />
			</li>
			<li>
			  <fmt:message key="settings.placeholder.password.repeat" var="placeholder" />
			  <label for="passrepeat" class="required"><fmt:message key="register.label.password.repeat" />:</label> 
			  <input type="password" name="passrepeat"
				placeholder="${placeholder}" id="passrepeat" required />
			</li>
			<li>
			  <fmt:message key="settings.placeholder.firstname" var="placeholder" />
			  <label for="first-name" class="required"><fmt:message key="settings.label.firstname" />:</label>
			  <input type="text" name="first-name"
				placeholder="${placeholder}" id="first-name"
				pattern="[а-яА-ЯёЁa-zA-Z]{2,127}" required />
			</li>
			<li>
			  <fmt:message key="settings.placeholder.lastname" var="placeholder" />
			  <label for="last-name" class="required"><fmt:message key="settings.label.lastname" />:</label> 
			  <input type="text" name="last-name" placeholder="${placeholder}"
				id="last-name" pattern="[а-яА-ЯёЁa-zA-Z]{2,127}" required />
			</li>
			<li>
			  <fmt:message key="settings.placeholder.displayname" var="placeholder" />
			  <label for="display-name"><fmt:message key="settings.label.displayname" />:</label> 
			  <input type="text" name="display-name" placeholder="${placeholder}"
				id="display-name"
				pattern="[а-яА-ЯёЁa-zA-Z0-9][а-яА-ЯёЁa-zA-Z0-9_\.-]{2,255}[а-яА-ЯёЁa-zA-Z0-9]" />
			</li>
			<li class="center">
			    <fmt:message key="register.button.register" var="register" />
				<button name="submit"
					onclick="validate(document.forms['register'])" title="${register}"
					class="submit" id="submit">${register}</button>
			</li>
		</ul>
		<p class="val-error">${regError}</p>
	</fieldset>
</form>
</main>
<script src="/blackjack/script/register.js" charset="utf-8"
	type="text/javascript"></script>