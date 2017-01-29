<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="/WEB-INF/tld/custom.tld" %>
<fmt:setLocale value="${curLocale}" />
<fmt:setBundle basename="conf.i18n.jsp" />

<jsp:useBean id="photoManager"
	class="org.qqq175.blackjack.persistence.dao.util.PhotoManager"
	scope="page" />
<link href="/blackjack/css/info.css" rel="stylesheet" />
<main class="column-center" id="statistic">
<h2 class="title">
	<span class="data">${user.displayName}</span> <fmt:message key="playerinfo.header" />
</h2>
<figure class="player-logo">
	<ctg:user-avatar user='${user}'/>
</figure>
<table class="stats">
	<tr>
		<td class="stat-name"><fmt:message key="stat.name" />:</td>
		<td class="stat-value"><span class="data">${user.firstName}
				${user.lastName}</span></td>
	</tr>
	<tr>
		<td class="stat-name"><fmt:message key="stat.rank" />:</td>
		<td class="stat-value"><span class="data" id="rank">${user.type}</span></td>
	</tr>
	<tr>
	<td colspan="2"><a href="#" class="rank-btn" data-user-id="${user.id.value}"><fmt:message key="playerinfo.tooglerank"/></a></td>
	</tr>
	<tr class="important-row">
		<td class="stat-name"><fmt:message key="stat.rating" />:</td>
		<td class="stat-value"><fmt:formatNumber type="number"
				maxFractionDigits="3" minFractionDigits="3" value="${user.rating}" /><br></td>
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
		<td class="stat-name"><fmt:message key="balance.balance" />:</td>
		<td class="stat-value">$${user.accountBalance}</td>
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
<jsp:include page="/WEB-INF/jsp/player/AccountOperations.jsp" />
</main>
<script src="/blackjack/script/playerinfo.js" charset="utf-8"
    type="text/javascript"></script>