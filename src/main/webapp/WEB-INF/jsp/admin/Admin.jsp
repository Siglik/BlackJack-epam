<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>The Quasino!</title>
<script type="text/javascript"
	src='/blackjack/script/locale/messages-${not empty sessionScope.curLocale ? sessionScope.curLocale.language : "en"}.js'
	charset="utf-8"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="/blackjack/script/jquery.leanModal.min.js" charset="utf-8"></script>
<link rel="stylesheet"
	href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css" />
<link href="/blackjack/css/style.css" rel="stylesheet" />
</head>
<body>
	<div id="shadow"></div>
	<!-- IMPORT HEADER -->
	<jsp:include page="/WEB-INF/jsp/element/LoggedHeader.jsp" />
	<div class="container">
		<!-- IMPORT LEFT MENU -->
		<jsp:include page="/WEB-INF/jsp/element/AdminSideMenu.jsp" />

		<jsp:include
			page='${not empty mainform ? mainform : "/WEB-INF/jsp/player/Welcome.jsp"}' />

		<!-- IMPORT ASIDE HERE -->
		<jsp:include page="/WEB-INF/jsp/element/Aside.jsp" />
	</div>

	<!-- chat is here (At game page)-->

	<!-- IMPORT FOOTER -->
	<jsp:include page="/WEB-INF/jsp/element/Footer.jsp" />
	<script type="text/javascript" src="/blackjack/script/page-style.js"
		charset="utf-8"></script>
	<script type="text/javascript">
		let
		popupMessage = '${sessionScope.popupMessage}';
		if (popupMessage.length > 0) {
			alert(popupMessage);
		}
		<c:remove var="popupMessage" scope="session" />
	</script>
</body>
</html>