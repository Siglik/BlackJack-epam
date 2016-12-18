<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="ctg" uri="/WEB-INF/tld/custom.tld" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
<ctg:user-table rows="${rw.size}" head="Revenue">
        
${rw.revenue}
    
</ctg:user-table >
<br/>
    <ctg:user-table>5 rub BulbaComp</ctg:user-table >
</body>
</html>
