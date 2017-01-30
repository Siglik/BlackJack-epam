<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<fmt:setLocale value="${curLocale}" />
<fmt:setBundle basename="conf.i18n.jsp" />
<nav class="left-menu column-left sidebar" id="left-menu">
	<ul class="vertical-menu top">
	    <c:if test="${inGame}">
                <li><a href='/blackjack/$/game/leave' class='button leave-game'><fmt:message key='button.leavegame'/></a>
        </c:if>
		<li><a href="/blackjack/$/" class="button menu-item"><fmt:message key="menu.homepage" /></a></li>
		<c:choose>
           <c:when test="${curGameType == 'SOLO'}">
                <li><a href="/blackjack/$/game/newsolo" class="button menu-item" style="background-color:green;"><fmt:message
                    key="menu.play.solo" /></a></li>
                <li><a href="/blackjack/$/game/newmulti" class="button menu-item"><fmt:message
                    key="menu.play.multi" /></a></li>
           </c:when>
           <c:when test="${curGameType == 'MULTI'}">
                <li><a href="/blackjack/$/game/newsolo" class="button menu-item"><fmt:message
                    key="menu.play.solo" /></a></li>
                <li><a href="/blackjack/$/game/newmulti" class="button menu-item" style="background-color:green;"><fmt:message
                    key="menu.play.multi" /></a></li>
           </c:when>
           <c:otherwise>
                <li><a href="/blackjack/$/game/newsolo" class="button menu-item"><fmt:message
                    key="menu.play.solo" /></a></li>
                <li><a href="/blackjack/$/game/newmulti" class="button menu-item"><fmt:message
                    key="menu.play.multi" /></a></li>
           </c:otherwise>
        </c:choose>
		<li><a href="/blackjack/$/game/rules" class="button menu-item"><fmt:message key="menu.rules" /></a></li>
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