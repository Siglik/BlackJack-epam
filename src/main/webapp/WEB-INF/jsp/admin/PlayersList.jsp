<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<link href="/blackjack/css/user-list.css" rel="stylesheet" />
<main class="column-center" id="statistic">
<h2 class="title">
	<span class="tm">Players list - Список</span>
	</h3>
	<div class="list">
		<table class="list">
			<thead>
				<tr>
					<th>E-mail</th>
					<th class="big-screen">Name</th>
					<th class="big-screen">Display name</th>
					<th class="big-screen">Balance</th>
					<th class="big-screen">Rating</th>
					<th>Rank</th>
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
								<td><a href='#' class="ban-btn ban">ban</a></td>
							</c:when>
							<c:otherwise>
								<td><a href="#" class="ban-btn unban">unban</a></td>
							</c:otherwise>
						</c:choose>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="list-control">
			<ul class="pagination">
				<c:set var="curPage" value="${empty param.page ? 1 : param.page}"
					scope="page" />
				Page:
				<c:forEach begin="1" end="${pageCount}" varStatus="loop">
					<li ${curPage == loop.index ? "class='current'" : "" }><a
						href="/blackjack/$/admin/playerslist?page=${loop.index}">${loop.index}</a></li>
				</c:forEach>
			</ul>
		</div>
</main>
<script src="/blackjack/script/players-list.js" charset="utf-8"
	type="text/javascript"></script>
