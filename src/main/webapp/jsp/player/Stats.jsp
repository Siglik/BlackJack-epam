<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<jsp:useBean id="photoManager" class="org.qqq175.blackjack.persistence.dao.util.PhotoManager" scope="page"/>

<link href="/blackjack/css/stats.css" rel="stylesheet" />
<main class="column-center" id="statistic">
<h2 class="title"><span class="data">${user.displayName}</span> statictics</h2>
<figure class="player-logo">
	<img src="${photoManager.findPhotoRelativePath(user.id)}">
</figure>
<table class="stats">
	<tr>
		<td class="stat-name">Name:</td>
		<td class="stat-value"><span class="data">${user.firstName} ${user.lastName}</span></td>
	</tr>
	<tr>
		<td class="stat-name">Rank:</td>
		<td class="stat-value"><span class="data">${user.type}</span></td>
	</tr>
	<tr>
		<td class="stat-name">Games:</td>
		<td class="stat-value"><span class="data">${userstat.win + userstat.loss + userstat.tie}</span></td>
	</tr>
	<tr>
		<td class="stat-name">Wins (blackjacks):</td>
		<td class="stat-value"><span class="data">${userstat.win}(${userstat.blackjack})</span></td>
	</tr>
	<tr>
		<td class="stat-name">Lost:</td>
		<td class="stat-value"><span class="data">${userstat.loss}</span></td>
	</tr>
	<tr>
		<td class="stat-name">Ties:</td>
		<td class="stat-value"><span class="data">${userstat.tie}</span></td>
	</tr>
	<tr class="important-row">
		<td class="stat-name">Rating:</td>
		<td class="stat-value"><fmt:formatNumber type="number"
				maxFractionDigits="3" minFractionDigits="3" value="${user.rating}" /><br></td>
	</tr>
	<tr class="important-row">
		<td class="stat-name">Balance:</td>
		<td class="stat-value">$${user.accountBalance}</td>
	</tr>
</table>
</main>
