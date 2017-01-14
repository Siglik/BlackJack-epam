<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>The Quasino!</title>
<link href="/blackjack/css/style.css" rel="stylesheet" />
</head>
<body>
	<div id="shadow"></div>
    <!-- IMPORT HEADER -->
    <jsp:include page="/jsp/element/LoggedHeader.jsp" />
	<div class="container">
		<!-- IMPORT LEFT MENU -->
		<jsp:include page="/jsp/element/PlayerSideMenu.jsp" />
		
		<jsp:include page="${mainform}" />
		
		
		<!-- IMPORT ASIDE HERE -->
		<jsp:include page="/jsp/element/Aside.jsp" /> 
	</div>
    
    <!-- chat is here (At game page)-->
    
	<!-- IMPORT FOOTER -->
	<jsp:include page="/jsp/element/Footer.jsp" />
</body>
</html>