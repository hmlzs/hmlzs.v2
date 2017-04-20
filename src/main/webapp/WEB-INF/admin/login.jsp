<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>系统登录</title>

    <script>
        if (window.parent != window) {
            window.parent.location = window.location;
        }
    </script>
    <jsp:include page="_script.jspf"></jsp:include>

</head>
<body>
    <div>
        <form action="/cn/admin/login" method="post">
            <p>用户名：<input type="text" name="account" /></p>
            <p>密&nbsp;&nbsp;&nbsp;码：<input type="password" name="password" /></p>
            <p><input type="submit" value="登录"></p>
        </form>
    </div>
</body>
</html>