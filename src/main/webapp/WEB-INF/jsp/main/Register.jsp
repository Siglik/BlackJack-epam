<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link href="/blackjack/css/register.css" rel="stylesheet" />
<main class="column-center main-content">
<form action="/blackjack/$/register" method="post" id="register"
	enctype="multipart/form-data">
	<fieldset>
		<legend>Registration</legend>
		<ul>
			<li><label for="photo">Select photo to upload: </label> <input
				type="file" name="photo" accept="image/jpeg,image/png" size="60" /></li>
			<li><label for="email" class="required">E-mail:</label> <input
				type="email" name="email" placeholder="Enter your e-mail" id="email"
				pattern="[\w\.-_]+@\w+\.[\.\w]+" required /></li>
			<li><label for="password" class="required">Password:</label> <input
				type="password" name="password" placeholder="Enter your password"
				id="password" pattern="\w{6,}" required /></li>
			<li><label for="passrepeat" class="required">Repeat
					password:</label> <input type="password" name="passrepeat"
				placeholder="Repeat the password" id="passrepeat" required /></li>
			<li><label for="first-name" class="required">First Name:</label>
				<input type="text" name="first-name"
				placeholder="Enter your first name" id="first-name"
				pattern="[а-яА-ЯёЁa-zA-Z]{2,127}" required /></li>
			<li><label for="last-name" class="required">Last Name:</label> <input
				type="text" name="last-name" placeholder="Enter your last name"
				id="last-name" pattern="[а-яА-ЯёЁa-zA-Z]{2,127}" required /></li>
			<li><label for="display-name">Display name:</label> <input
				type="text" name="display-name" placeholder="Enter display name"
				id="display-name"
				pattern="[а-яА-ЯёЁa-zA-Z0-9][а-яА-ЯёЁa-zA-Z0-9_\.-]{2,255}[а-яА-ЯёЁa-zA-Z0-9]" /></li>
			<li class="center">
				<button name="submit"
					onclick="validate(document.forms['register'])" title="Register"
					class="submit" id="submit">Register</button>
			</li>
		</ul>
		<p class="error">${regError}</p>
	</fieldset>
</form>
</main>
<script src="/blackjack/script/register.js" charset="utf-8"
	type="text/javascript"></script>