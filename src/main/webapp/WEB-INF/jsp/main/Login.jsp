<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link href="/blackjack/css/login.css" rel="stylesheet" />
<div id="modal" class="popupContainer" style="display: none;">
	<header class="popupHeader">
		<span class="header_title">Login</span> <span class="modal_close"><i
			class="fa fa-times"></i></span>
	</header>

	<section class="popupBody">
		<div class="user_login">
			<form method="post" action="/blackjack/$/login" id="login-form">
				<label>Email</label> <input type="text" name="email"><br>
				<label>Password</label> <input type="password" name="password"><br>
				<div class="action_btns">
					<div class="one_half">
						<a class="btn btn_red" href="javascript:{}"
							onclick="document.getElementById('login-form').submit();">Login</a>
					</div>
				</div>
			</form>
			<div class="error">
				<p class="error">
					<c:out value="${sessionScope.loginError}" />
					<c:remove var="loginError" scope="session" />
				</p>
			</div>
		</div>
	</section>
</div>

<script src="/blackjack/script/login.js" charset="utf-8"></script>