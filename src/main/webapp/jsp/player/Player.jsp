<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>The Quasino!</title>
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
    <jsp:include page="/jsp/element/LoggedHeader.jsp" />
    <div class="container">
        <!-- IMPORT LEFT MENU -->
        <jsp:include page="/jsp/element/PlayerSideMenu.jsp" />
        
        <jsp:include page='${not empty mainform ? mainform : "/jsp/player/Welcome.jsp"}' />
        
        <!-- IMPORT ASIDE HERE -->
        <jsp:include page="/jsp/element/Aside.jsp" /> 
    </div>
    
    <!-- chat is here (At game page)-->
    
    <!-- IMPORT FOOTER -->
    <jsp:include page="/jsp/element/Footer.jsp" />
    <script type="text/javascript" src="/blackjack/script/page-style.js"
        charset="utf-8"></script>
</body>
</html>