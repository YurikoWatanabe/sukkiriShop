<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>スッキリ商店</title>
</head>
<body>
<h1>ユーザー登録</h1>
<form action="RegisterServlet" method="post">
<label for="userId">ユーザーID</label>
<input type="text" name="rUserId" id="userId"><br>

<label for="pass">パスワード</label>
<input type="password" name="rPass" id="pass"><br>

<label for="pass2">パスワード確認</label>
<input type="password" name="rPass2" id="pass2"><br>

<label for="email">メールアドレス</label>
<input type="email" name="rMail" id="email"><br>

<label for="text">名前</label>
<input type="text" name="rName" id="name"><br>

<label for="age">年齢</label>
<select name="rAge" id="age">
		<c:forEach var="i" begin="0" end="120">
			<c:choose>
				<c:when test="${i == 20}">
					<option value="${i}" selected>${i}</option>
				</c:when>			
				<c:otherwise>
					<option value="${i}">${i}</option>
				</c:otherwise>
			</c:choose>
		</c:forEach>
</select><br>
<input type="submit" value="ユーザー登録">
</form>
</body>
</html>