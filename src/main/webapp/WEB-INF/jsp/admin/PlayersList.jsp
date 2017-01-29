<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${curLocale}" />
<fmt:setBundle basename="conf.i18n.jsp" />
<link href="/blackjack/css/user-list.css" rel="stylesheet" />
<main class="column-center" id="statistic">
<h2 class="title"><span class="tm"><fmt:message key="playerslist.header" /></span></h2>
	<div class="list">
		<table class="list">
			<thead>
				<tr>
					<th><fmt:message key="playerslist.email" /></th>
					<th class="big-screen"><fmt:message key="playerslist.name" /></th>
					<th class="big-screen"><fmt:message key="playerslist.displayname" /></th>
					<th class="big-screen"><fmt:message key="playerslist.balance" /></th>
					<th class="big-screen"><fmt:message key="playerslist.rating" /></th>
					<th><fmt:message key="playerslist.rank" /></th>
					<th>&nbsp;</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${users}" varStatus="loop">
					<tr class='data-row${loop.index % 2 == 1 ? " odd" : ""}'
						data-user-id="${item.id.value}">
						<td>${item.email}</td>
						<td class="big-screen">${item.firstName}<span> </span>${item.lastName}</td>
						<td class="big-screen">${item.displayName}</td>
						<td class="big-screen">$${item.accountBalance}</td>
						<td class="big-screen"><fmt:formatNumber type="number"
								maxFractionDigits="3" minFractionDigits="3"
								value="${item.rating}" /></td>
						<td>${item.type}</td>
						<c:choose>
							<c:when test="${item.active =='true'}">
								<td><a href='#' class="ban-btn ban"><fmt:message key="playerslist.ban" /></a></td>
							</c:when>
							<c:otherwise>
								<td><a href="#" class="ban-btn unban"><fmt:message key="playerslist.unban" /></a></td>
							</c:otherwise>
						</c:choose>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="list-control">
			<ul class="pagination">
			    <li><fmt:message key="label.page" />:</li>
				<c:set var="curPage" value="${empty param.page ? 1 : param.page}"
					scope="page" />
				<c:forEach begin="1" end="${pageCount}" varStatus="loop">
					<li ${curPage == loop.index ? "class='current'" : "" }><a
						href="/blackjack/$/admin/playerslist?page=${loop.index}">${loop.index}</a></li>
				</c:forEach>
			</ul>
		</div>
	</div>
</main>
<script src="/blackjack/script/players-list.js" charset="utf-8"
	type="text/javascript"></script>
