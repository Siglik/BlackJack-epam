<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${curLocale}" />
<fmt:setBundle basename="conf.i18n.jsp" />
<link href="/blackjack/css/login.css" rel="stylesheet" />
<div id="modal" class="popupContainer" style="display: none;">
	<header class="popupHeader">
		<span class="header_title"><fmt:message key="header.button.login" /></span> <span class="modal_close"><i
			class="fa fa-times"></i></span>
	</header>

	<section class="popupBody">
		<div class="user_login">
			<form method="post" action="/blackjack/$/login" id="login-form">
				<label><fmt:message key="login.label.email" /></label> <input type="text" name="email"><br>
				<label><fmt:message key="login.label.password" /></label> <input type="password" name="password"><br>
				<div class="action_btns">
					<div class="one_half">
						<a class="btn btn_red" href="javascript:{}"
							onclick="document.getElementById('login-form').submit();"><fmt:message key="login.button.submit" /></a>
					</div>
				</div>
			</form>
			<div class="error-message">
				<p class="error-message">
					<c:out value="${sessionScope.loginError}" />
					<c:remove var="loginError" scope="session" />
				</p>
			</div>
		</div>
	</section>
</div>

<script src="/blackjack/script/login.js" charset="utf-8"></script>