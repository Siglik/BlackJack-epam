<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="/WEB-INF/tld/custom.tld" %>
<fmt:setLocale value="${curLocale}" />
<fmt:setBundle basename="conf.i18n.jsp" />

<jsp:useBean id="photoManager" class="org.qqq175.blackjack.persistence.dao.util.PhotoManager" scope="page"/>

<link href="/blackjack/css/stats.css" rel="stylesheet" />
<main class="column-center" id="statistic">
<h2 class="title"><span class="data">${user.displayName}</span> <fmt:message key="stat.header" /></h2>
<figure class="player-logo">
	<ctg:user-avatar />
</figure>
<table class="stats">
	<tr>
		<td class="stat-name"><fmt:message key="stat.name" />:</td>
		<td class="stat-value"><span class="data">${user.firstName} ${user.lastName}</span></td>
	</tr>
	<tr>
		<td class="stat-name"><fmt:message key="stat.rank" />:</td>
		<td class="stat-value"><span class="data">${user.type}</span></td>
	</tr>
	<tr>
		<td class="stat-name"><fmt:message key="stat.games" />:</td>
		<td class="stat-value"><span class="data">${userstat.win + userstat.loss + userstat.tie}</span></td>
	</tr>
	<tr>
		<td class="stat-name"><fmt:message key="stat.win" /> (<fmt:message key="stat.blackjack" />):</td>
		<td class="stat-value"><span class="data">${userstat.win}(${userstat.blackjack})</span></td>
	</tr>
	<tr>
		<td class="stat-name"><fmt:message key="stat.lost" />:</td>
		<td class="stat-value"><span class="data">${userstat.loss}</span></td>
	</tr>
	<tr>
		<td class="stat-name"><fmt:message key="stat.tie" />:</td>
		<td class="stat-value"><span class="data">${userstat.tie}</span></td>
	</tr>
	<tr class="important-row">
		<td class="stat-name"><fmt:message key="stat.rating" />:</td>
		<td class="stat-value"><fmt:formatNumber type="number"
				maxFractionDigits="3" minFractionDigits="3" value="${user.rating}" /><br></td>
	</tr>
	<tr class="important-row">
		<td class="stat-name"><fmt:message key="balance.balance" />:</td>
		<td class="stat-value">$${user.accountBalance}</td>
	</tr>
</table>
</main>
