<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${curLocale}" />
<fmt:setBundle basename="conf.i18n.jsp" />
<nav class="left-menu column-left sidebar" id="left-menu">
	<ul class="vertical-menu top">
		<li><a href="/blackjack/$/" class="button menu-item"><fmt:message key="menu.homepage" /></a></li>
		<li><a href="/blackjack/$/game/newsolo" class="button menu-item"><fmt:message key="menu.play.solo" /></a></li>
        <li><a href="/blackjack/$/game/newmulti" class="button menu-item"><fmt:message key="menu.play.multi" /></a></li>
		<li><a href="#" class="button menu-item"><fmt:message key="menu.rules" /></a></li>
	</ul>
	<ul class="vertical-menu">
		<li><a href="/blackjack/$/player/showstats"
			class="button menu-item"><fmt:message key="menu.stats" /></a></li>
		<li><a href="/blackjack/$/player/balance"
			class="button menu-item"><fmt:message key="menu.balance" /></a></li>
	</ul>
	<ul class="vertical-menu">
		<li><a href="/blackjack/$/admin/playerslist" class="button menu-item"><fmt:message key="menu.manage.players" /></a></li>
	</ul>
</nav>