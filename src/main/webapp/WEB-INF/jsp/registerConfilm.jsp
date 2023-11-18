<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="model.User" %>
<%--
<%
User user = (User) session.getAttribute("user");
%>
 --%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>スッキリ商店</title>
</head>
<body>
<h1>登録内容確認</h1>
<p>
ユーザーID:　<c:out value="${rUserId}" /><br>
メールアドレス:　<c:out value="${rMail}" /><br>
名前:　<c:out value="${rName}" /><br>
年齢:　<c:out value="${rAge}" /><br>
</p>
<form action="RegisterServlet" method="post">
<input type="hidden" name="action" value="done">
<input type="submit" value="登録">
</form>

</body>
</html>