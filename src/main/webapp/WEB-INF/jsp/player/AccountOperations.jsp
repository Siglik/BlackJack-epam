<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="${curLocale}" />
<fmt:setBundle basename="conf.i18n.jsp" />

<div class="list">
	<h2 class="subtitle">
		<span class="tm"><fmt:message key="acc.operation.list" /></span>
	</h2>
	<table class="list">
		<thead>
			<tr>
				<th><fmt:message key="acc.operation.date" /></th>
				<th><fmt:message key="acc.operation.type" /></th>
				<th><fmt:message key="acc.operation.ammount" /></th>
				<th><fmt:message key="acc.operation.comment" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${operations}" varStatus="loop">
				<tr class='data-row${loop.index % 2 == 1 ? " odd" : ""}'
					data-user-id="${item.id.value}">
					<td><fmt:formatDate type="both" value="${item.time}" dateStyle="short" timeStyle="medium" /></td>
					<td class="type">${item.type}</td>
					<td>${item.ammount}</td>
					<td class="comment">$${item.comment}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="list-control">
		<ul class="pagination">
		    <li><fmt:message key="label.page" />:</li>
			<c:set var="curPage" value="${empty param.page ? 1 : param.page}"
				scope="page" />
			<c:set var="curUser" value="${empty param.id ? 0 : param.id}"
                scope="page" />
			<c:forEach begin="1" end="${pageCount}" varStatus="loop">
				<li ${curPage == loop.index ? "class='current'" : "" }><a
					href='?${empty param.id ? "" : "id="}${param.id}${empty param.id ? "" :"&"}page=${loop.index}'>${loop.index}</a></li>
			</c:forEach>
		</ul>
	</div>
</div>