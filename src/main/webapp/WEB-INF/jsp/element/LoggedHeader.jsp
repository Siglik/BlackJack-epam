<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<header>
	<div class="header-top">
		<img src="/blackjack/img/main-logo.png" alt="logo" class="logo">
		<p class="tm">THE QASINO</p>
		<ul class="gorizontal-menu right" id="login-buttons">
			<li id="logout-top"><a href="/blackjack/$/logout"
				class="button login">LOGOUT</a></li>
			<li id="settings-top"><a href="/blackjack/$/settings"
				class="button join">SETTINGS</a></li>
		</ul>
	</div>
	<div class="header-bottom">
		<a href="#" class="button" id="show-menu">Menu</a>
		<h1>
			<span class="nav-elem">Black Jack</span>
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