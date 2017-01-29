<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="${curLocale}" />
<fmt:setBundle basename="conf.i18n.jsp" />

<main class="column-center"> 
    <a href="/blackjack/$/game/leave"
	   class="button leave-game"><fmt:message key="button.leavegame" /></a>
    <div id="black-jack"></div>
</main>
<script
	src='/blackjack/script/locale/game-init-${not empty sessionScope.curLocale ? sessionScope.curLocale.language : "en"}.js'
	charset="utf-8"></script>
<script src="/blackjack/script/bljack.js" charset="utf-8"></script>
