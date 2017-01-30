<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<fmt:setLocale value="${curLocale}"/>
<fmt:setBundle basename="conf.i18n.jsp"/>

<main class="column-center">
    <div id="black-jack"></div>
    <c:if test="${showChat}">
        <div id="gamechat"></div>
    </c:if>
</main>
<script src='/blackjack/script/locale/game-init-${not empty sessionScope.curLocale ? sessionScope.curLocale.language : "en"}.js' charset="utf-8"></script>
<script src="/blackjack/script/bljack.js" charset="utf-8"></script>
<c:if test="${showChat}">
    <script src="/blackjack/script/gamechat.js" charset="utf-8"></script>
</c:if>
