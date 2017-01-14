<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Black jack!</title>
<link href="/blackjack/css/style.css" rel="stylesheet" />
<link href="/blackjack/css/error.css" rel="stylesheet" />
</head>
<body>
	<div id="error">
		<h1>Opps! Something happens!</h1>
		<p class="code">
			Request from ${pageContext.errorData.requestURI} is failed <br />
			Servlet name: ${pageContext.errorData.servletName} <br /> 
			Status code: ${pageContext.errorData.statusCode} <br /> 
			Exception: ${pageContext.exception} <br />
		</p>
		<p class="description">Message from exception:
			${pageContext.exception.message}</p>
		<p>
			<a href="/blackjack/$/" class="button">Go to main page</a>
		</p>
	</div>
</body>
</html>