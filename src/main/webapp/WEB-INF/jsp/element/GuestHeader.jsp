<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${curLocale}" />
<fmt:setBundle basename="conf.i18n.jsp" />
<header>
	<div class="header-top">
		<img src="/blackjack/img/main-logo.png" alt="logo" class="logo">
		<p class="tm">THE QASINO</p>
		<ul class="gorizontal-menu right" id="login-buttons">
			<li id="login-top"><a href="#modal"
				class="button login modal_login" id="modal_login"><fmt:message key="header.button.login" /></a></li>
			<li id="join-top"><a href="/blackjack/$/register"
				class="button join"><fmt:message key="header.button.join" /></a></li>
		</ul>
	</div>
	<div class="header-bottom">
		<a href="#" class="button" id="show-menu"><fmt:message key="header.button.menu" /></a>
		<h1>
			<span class="nav-elem"><fmt:message key="healer.slogan" /></span>
		</h1>
		<div class="dropdown">
            <a href="#" class="button language" id="lang-btn">${sessionScope.curLocale.language}</a>
            <div class="dropdown-content" id="lang-menu">
                <a href="/blackjack/$/setlocale?locale=En">En</a> 
                <a href="/blackjack/$/setlocale?locale=Ru">Ru</a>
            </div>
        </div>
	</div>
</header>