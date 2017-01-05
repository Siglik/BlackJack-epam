<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="css/startpage.css" />
<div id="login">
	<h4>Вход в систему:</h4>
	<form method="post" action="login">
		<div class=form-item>
			<div class="input">
				<input name="login" id="login" type="text" value="" />
			</div>
			<div class="inputname">Имя пользователя:</div>
		</div>
		<div class=form-item>
			<div class="input">
				<input name="password" id="password" type="password" value="" />
			</div>
			<div class="inputname">Пароль:</div>
		</div>
		<p class="error">${loginError}</p>
		<div class=submit>
			<input type="submit" value="Войти" />
		</div>
	</form>
</div>