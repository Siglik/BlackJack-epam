<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${curLocale}" />
<fmt:setBundle basename="conf.i18n.jsp"/>

<nav class="left-menu column-left sidebar" id="left-menu">
	<ul class="vertical-menu top">
		<li><a href="/blackjack/$/" class="button menu-item"><fmt:message key="menu.homepage" /></a></li>
		<li><a href="#modal" class="button menu-item modal_login"><fmt:message key="menu.login" /></a></li>
		<li><a href="/blackjack/$/register" class="button menu-item"><fmt:message key="menu.register" /></a></li>
	</ul>
</nav>