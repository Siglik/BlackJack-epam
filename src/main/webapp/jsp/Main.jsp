<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Black jack!</title>
<link href="css/style.css" rel="stylesheet" />
</head>
<body>
	<div id="shadow"></div>
	<header>
		<div class="header-top">
			<img src="img/main-logo.png" alt="logo" class="logo">
			<p class="tm">THE QASINO</p>
			<ul class="gorizontal-menu right" id="login-buttons">
				<li><a href="#" class="button login">LOGIN</a></li>
				<li><a href="#" class="button join">JOIN NOW</a></li>
			</ul>
		</div>
		<div class="header-bottom">
			<a href="#" class="button" id="show-menu">Menu</a>
			<h1>
				<span class="nav-elem">Black Jack</span> &gt; <span class="nav-elem">Multiplayer
					game</span>
			</h1>
			<a href="#" class="button language">En</a>
		</div>
	</header>
	<div class="container">
		<nav class="left-menu column-left" id="left-menu">
			<ul class="vertical-menu top">
				<li><a href="#" class="button menu-item">Play solo</a></li>
				<li><a href="#" class="button menu-item">Play multiplayer</a></li>
				<li><a href="#" class="button menu-item">Game rules</a></li>
			</ul>
			<ul class="vertical-menu">
				<li><a href="#" class="button menu-item">About</a></li>
				<li><a href="#" class="button menu-item">Player statistic</a></li>
				<li><a href="#" class="button menu-item">Account balance</a></li>
			</ul>
		</nav>
		
		<jsp:include page="${mainform}" />
		
		
		<aside class="column-right">
			<article>
				<h3>Topic</h3>
				<p>
					Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do
					eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim
					ad minim veniam, quis nostrud exercitation...<br>
					<a href="#">(see more)</a>
				</p>
			</article>
		</aside>
	</div>
    
    <!-- chat is here -->

	<footer>
		<div class="copyright">
			&copy; Maksim Mikhalkou <a href="mailto:maxim.exe@gmail.com">maxim.exe@gmail.com</a>
		</div>
	</footer>

</body>
</html>
