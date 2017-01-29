<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ctg" uri="/WEB-INF/tld/custom.tld" %>
<fmt:setLocale value="${curLocale}" />
<fmt:setBundle basename="conf.i18n.jsp" />

<link href="/blackjack/css/balance.css" rel="stylesheet" />
<main class="column-center" id="statistic">
<h2 class="title">
	<span class="data">${sessionScope.user.displayName}</span>
	<fmt:message key="balance.header" />
</h2>
<figure class="player-logo">
	<ctg:user-avatar />
</figure>
<table class="stats">
	<tr class="important-row">
		<td class="stat-name"><fmt:message key="balance.balance" />:</td>
		<td class="stat-value">$${sessionScope.user.accountBalance}</td>
	</tr>
	<tr>
		<td class="stat-name"><fmt:message key="balance.total.put" />:</td>
		<td class="stat-value">$<span class="data">${total.payment}</span></td>
	</tr>
	<tr>
		<td class="stat-name"><fmt:message key="balance.total.withdrawal" />:</td>
		<td class="stat-value">$<span class="data">${total.withdrawal}</span></td>
	</tr>
	<tr>
		<td class="stat-name"><fmt:message key="balance.total.win" />:</td>
		<td class="stat-value">$<span class="data">${total.win}</span></td>
	</tr>
	<tr>
		<td class="stat-name"><fmt:message key="balance.total.loss" />:</td>
		<td class="stat-value">$<span class="data">${total.loss}</span></td>
	</tr>
</table>
<a href="#" class="button payment" id="toogle-payment"><fmt:message
		key="balance.button.payment" /></a> <br>
<div id="payment-form"
	${empty sessionScope.paymentError ? "class=\"hidden\"":""}>
	<form class="payment" method="get"
		action="/blackjack/$/player/payment/">

		<label><fmt:message key="balance.form.sum" />:</label> 
		<input type="text" name="sum" size="9" pattern="^[0-9]{1,6}(\.[0-9]{1,2})?$" required />
		<span class="wrapper"> &nbsp;</span> 
		<label><fmt:message key="balance.form.card" />:</label> 
		<input type="text" name="number"
			size="18"
			pattern="^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\d{3})\d{11})$"
			required /> 
		<br> 
		<label><fmt:message key="balance.form.expirience" />:</label> <input type="text" name="exp-month" size="2" pattern="^(1[0-2]|0?[1-9])$" required />
		/
		<input type="text" name="expr-year" size="2" pattern="[0-9]{2}" required />
		<span class="wrapper">&nbsp;</span> 
		<label><fmt:message key="balance.form.cvv" />:</label>
		<input type="text" name="cvv" size="4" pattern="[0-9]{3,4}" required /><br>
		<label><fmt:message key="balance.form.cardholder" />:</label>
		<input type="text" name="cardholder" pattern="[a-zA-Z]{2,64}[ ]+[a-zA-Z]{2,64}" required /><br>
		<div class="bth-container">
			<button name="operation" class="button" value="payment"><fmt:message key="balance.form.pay" /></button>
			<button name="operation" class="button" value="withdrawal"><fmt:message key="balance.form.withdrawal" /></button>
		</div>
		<div class="error">
			<p class="error">
				<c:out value="${sessionScope.paymentError}" />
				<c:remove var="paymentError" scope="session" />
			</p>
		</div>
	</form>
</div>
</main>

<script src="/blackjack/script/balance.js" charset="utf-8"></script>
