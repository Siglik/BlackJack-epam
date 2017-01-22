<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<main class="column-center" id="black-jack"></main>
<script src='/blackjack/script/locale/game-init-${not empty sessionScope.curLocale ? sessionScope.curLocale.language : "en"}.js' charset="utf-8"></script>
<script src="/blackjack/script/bljack.js" charset="utf-8"></script>
