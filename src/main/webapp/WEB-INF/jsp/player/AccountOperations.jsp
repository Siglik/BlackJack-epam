<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="list">
	<h2 class="subtitle">
		<span class="tm">Account operations list - Список</span>
	</h2>
	<table class="list">
		<thead>
			<tr>
				<th>Date</th>
				<th>Type</th>
				<th>Ammount</th>
				<th>Comment</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${operations}" varStatus="loop">
				<tr class='data-row${loop.index % 2 == 1 ? " odd" : ""}'
					data-user-id="${item.id.value}">
					<td>${item.time}</td>
					<td class="type">${item.type}</td>
					<td>${item.ammount}</td>
					<td class="comment">$${item.comment}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="list-control">
		<ul class="pagination">
			<c:set var="curPage" value="${empty param.page ? 1 : param.page}"
				scope="page" />
			<c:set var="curUser" value="${empty param.id ? 0 : param.id}"
                scope="page" />
			Page:
			<c:forEach begin="1" end="${pageCount}" varStatus="loop">
				<li ${curPage == loop.index ? "class='current'" : "" }><a
					href='?${empty param.id ? "" : "id="}${param.id}${empty param.id ? "" :"&"}page=${loop.index}'>${loop.index}</a></li>
			</c:forEach>
		</ul>
	</div>