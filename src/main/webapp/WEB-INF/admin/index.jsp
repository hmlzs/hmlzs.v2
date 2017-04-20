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
    <p>欢迎：${user.account}</p>
    <p><a href="/cn/admin/userInfoShow">资料设置</a></p>
    <p><a href="/cn/admin/userAddShow">新增用户</a></p>
    <p><a href="/cn/admin/userChangePassWordShow">修改密码</a></p>
</div>
</body>
</html>