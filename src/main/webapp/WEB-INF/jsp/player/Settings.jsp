<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<jsp:useBean id="photoManager"
	class="org.qqq175.blackjack.persistence.dao.util.PhotoManager"
	scope="page" />

<link href="/blackjack/css/settings.css" rel="stylesheet" />
<main class="column-center main-content">
<form action="/blackjack/$/player/changeavatar" method="post"
	id="avatar" enctype="multipart/form-data" class="settings">
	<fieldset>
		<legend>Change avatar</legend>
		<figure class="player-logo">
			<img src="${photoManager.findPhotoRelativePath(user.id)}">
		</figure>
		<ul>
			<li class="center"><label for="photo">Select photo to
					upload: </label> <input type="file" name="photo"
				accept="image/jpeg,image/png" size="60" /></li>
			<li class="center">
				<button name="submit" title="Upload" class="submit" id="submit">Upload</button>
			</li>
		</ul>
		<p class="val-error">
			<c:out value="${sessionScope.photoError}" />
			<c:remove var="photoError" scope="session" />
		</p>
	</fieldset>
</form>
<form action="/blackjack/$/player/chagnepassword" method="post"
	id="password" class="settings">
	<fieldset>
		<legend>Change password</legend>
		<ul>
			<li><label for="old-password" class="required">Old
					password:</label> <input type="password" name="old-password"
				placeholder="Enter your password" id="old-password" pattern="\w{6,}"
				required /></li>
			<li><label for="new-password" class="required">New
					password:</label> <input type="password" name="new-password"
				placeholder="Enter new password" id="new-password" pattern="\w{6,}"
				required /></li>
			<li><label for="passrepeat" class="required">Repeat new
					password:</label> <input type="password" name="passrepeat"
				placeholder="Repeat new password" id="passrepeat" required /></li>
			<li class="center">
				<button name="submit" onclick="validate(document.forms['password'])"
					title="Apply" class="submit" id="submit">Apply</button>
			</li>
		</ul>
	</fieldset>
</form>
<form action="/blackjack/$/player/chagnepersonal" method="post"
	id="personal" class="settings">
	<fieldset>
		<legend>Change personal</legend>
		<ul>
			<li><label for="first-name" class="required">First Name:</label>
				<input type="text" name="first-name"
				placeholder="Enter your first name" id="first-name"
				pattern="[а-яА-ЯёЁa-zA-Z]{2,127}" value="${user.firstName}" required /></li>
			<li><label for="last-name" class="required">Last Name:</label> <input
				type="text" name="last-name" placeholder="Enter your last name"
				id="last-name" pattern="[а-яА-ЯёЁa-zA-Z]{2,127}"
				value="${user.lastName}" required /></li>
			<li><label for="display-name">Display name:</label> <input
				type="text" name="display-name" placeholder="Enter display name"
				id="display-name"
				pattern="[а-яА-ЯёЁa-zA-Z0-9][а-яА-ЯёЁa-zA-Z0-9_\.-]{2,255}[а-яА-ЯёЁa-zA-Z0-9]"
				value="${user.displayName}" /></li>
			<li class="center">
				<button name="submit" onclick="validate(document.forms['personal'])"
					title="Save" class="submit" id="submit">Save</button>
			</li>
		</ul>
	</fieldset>
</form>
</main>
<script src="/blackjack/script/settings.js" charset="utf-8"
	type="text/javascript"></script>