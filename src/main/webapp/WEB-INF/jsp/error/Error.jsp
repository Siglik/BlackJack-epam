<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta http-equiv="refresh" content="5;url=/blackjack/" />
<title>Black jack!</title>
<link href="/blackjack/css/style.css" rel="stylesheet" />
<link href="/blackjack/css/error.css" rel="stylesheet" />
</head>
<body>
	<div id="error">
		<h1>Opps! Something is wrong!</h1>
		<p class="code">
			Request from <span class="data"><c:out
					value="${pageContext.errorData.requestURI}" /></span> is failed <br /> <br />
			Servlet name: <span class="data"><c:out
					value="${pageContext.errorData.servletName}" /></span> <br /> <br />
			Status code: <span class="data"><c:out
					value="${pageContext.errorData.statusCode}" /></span> <br /> <br />
			Error message: <span class="data"><c:out
					value="${requestScope['javax.servlet.error.message']}"/></span> <br /> <br />
			Exception: <span class="data"><c:out
					value="${pageContext.exception}" /></span> <br />
		</p>
		<p class="description">
			Message from exception: <span class="data">${pageContext.exception.message}</span>
		</p>
		<p>
			<a href="/blackjack/$/" class="button">Go to main page</a>
		</p>
		<p>You will be redirected to main page in 5 seconds.</p>
	</div>
	<div class="error-logo">
		<img src="/blackjack/img/bender-error.png" alt="Error page"
			class="error-logo">
	</div>
</body>
</html>