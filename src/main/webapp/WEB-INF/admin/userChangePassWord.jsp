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

<div>
    <form id="data" enctype="multipart/form-data">
        <p>
            旧密码：<input type="password" name="old" id="old" value=""/>
        </p>
        <p>
            新密码：<input type="password" name="new" id="new" value=""/>
        </p>
        <p>
            确认密码：<input type="password" name="password" id="password" value=""/>
        </p>
        <input type="hidden" name="id" value="${user.id}"/>
    </form>
    <button id="submit">提交</button>
</div>

<script src="http://apps.bdimg.com/libs/jquery/1.8.3/jquery.min.js"></script>
<script>

    $("#submit").click(function() {
        $.ajax({
            type : 'POST',
            url : "/cn/admin/userChangePassWord",
            dataType : 'json',
            data: $("#data").serialize(),
            success : function(data, status, xhr) {
                alert('success');
            }
        });
    });
</script>
</body>
</html>