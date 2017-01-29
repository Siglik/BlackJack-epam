<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ctg" uri="/WEB-INF/tld/custom.tld" %>
<fmt:setLocale value="${curLocale}" />
<fmt:setBundle basename="conf.i18n.jsp" />

<jsp:useBean id="photoManager"
	class="org.qqq175.blackjack.persistence.dao.util.PhotoManager"
	scope="page" />

<link href="/blackjack/css/settings.css" rel="stylesheet" />
<main class="column-center main-content">
<form action="/blackjack/$/player/changeavatar" method="post"
	id="avatar" enctype="multipart/form-data" class="settings">
	<fieldset>
		<legend><fmt:message key="settings.avatar" /></legend>
		<figure class="player-logo">
			<ctg:user-avatar />
		</figure>
		<ul>
			<li class="center">
			     <label for="photo"><fmt:message key="settings.label.upload" /></label> 
			     <input type="file" name="photo" accept="image/jpeg,image/png" size="60" /></li>
			<li class="center">
			    <fmt:message key="settings.button.upload" var="upload" />
				<button name="submit" title="${upload}" class="submit" id="submit">${upload}</button>
			</li>
		</ul>
		<p class="val-error">
			<c:out value="${sessionScope.photoError}" />
			<c:remove var="photoError" scope="session" />
		</p>
	</fieldset>
</form>
<form action="/blackjack/$/player/changepassword" method="post"
	id="password" class="settings">
	<fieldset>
		<legend><fmt:message key="settings.password" /></legend>
		<ul>
			<li>
			  <label for="old-password" class="required"><fmt:message key="settings.label.password.old" /></label> 
			  <input type="password" name="old-password"
				placeholder="Enter your password" id="old-password" pattern="\w{6,}"
				required />
			</li>
			<li>
			  <label for="new-password" class="required"><fmt:message key="settings.label.password.new" /></label> 
			  <input type="password" name="new-password"
				placeholder="Enter new password" id="new-password" pattern="\w{6,}"
				required />
			</li>
			<li>
			  <label for="passrepeat" class="required"><fmt:message key="settings.label.password.repeat" /></label>
			  <input type="password" name="passrepeat"
				placeholder="Repeat new password" id="passrepeat" required />
		    </li>
			<li class="center">
			     <fmt:message key="settings.button.apply" var="apply" />
				<button name="submit" onclick="validate(document.forms['password'])"
					title="${apply}" class="submit" id="submit">${apply}</button>
			</li>
		</ul>
		<p class="val-error">
			<c:out value="${sessionScope.passMessage}" />
			<c:remove var="passMessage" scope="session" />
		</p>
	</fieldset>
</form>
<form action="/blackjack/$/player/changepersonal" method="post"
	id="personal" class="settings">
	<fieldset>
		<legend><fmt:message key="settings.personal" /></legend>
		<ul>
			<li>
			  <label for="first-name" class="required"><fmt:message key="settings.label.firstname" /></label>
			  <input type="text" name="first-name"
				placeholder="Enter your first name" id="first-name"
				pattern="[а-яА-ЯёЁa-zA-Z]{2,127}" value="${user.firstName}" required />
			</li>
			<li>
			  <label for="last-name" class="required"><fmt:message key="settings.label.lastname" /></label>
			  <input type="text" name="last-name" placeholder="Enter your last name"
				id="last-name" pattern="[а-яА-ЯёЁa-zA-Z]{2,127}"
				value="${user.lastName}" required />
			</li>
			<li>
			  <label for="display-name"><fmt:message key="settings.label.displayname" /></label>
			  <input type="text" name="display-name" placeholder="Enter display name"
				id="display-name"
				pattern="[а-яА-ЯёЁa-zA-Z0-9][а-яА-ЯёЁa-zA-Z0-9_\.-]{2,255}[а-яА-ЯёЁa-zA-Z0-9]"
				value="${user.displayName}" />
			</li>
			<li class="center">
			    <fmt:message key="settngs.button.save" var="save" />
				<button name="submit" onclick="validate(document.forms['personal'])"
					title="${save}" class="submit" id="submit">${save}</button>
			</li>
		</ul>
		<p class="val-error">
			<c:out value="${sessionScope.personalError}" />
			<c:remove var="personalError" scope="session" />
		</p>
	</fieldset>
</form>
</main>
<script src="/blackjack/script/settings.js" charset="utf-8"
	type="text/javascript"></script>