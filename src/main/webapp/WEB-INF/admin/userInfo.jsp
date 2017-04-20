<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.core.domain.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    if (request.getAttribute("addStatus") != null) {
        if ((Integer)request.getAttribute("addStatus") == 1) {
            User user = new User();
            pageContext.setAttribute("user",user);
        }
    }
%>

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
            账号：<input type="text" name="accout" id="accout" value="${user.account}" <c:if test="${!empty user.account}">disabled="disabled"</c:if>/>
        </p>
        <p>
            姓名：<input type="text" name="name" id="name" value="${user.name}"/>
        </p>
        <c:if test="${empty user.account}">
            <p>
                密码：<input type="password" name="password" id="password" value="${user.password}"/>
            </p>
        </c:if>
        <p>
            手机：<input type="text" name="phone" id="phone" value="${user.phone}"/>
        </p>
        <p>
            邮箱：<input type="text" name="mail" id="mail" value="${user.mail}"/>
        </p>
        <p>
            描述：<input type="text" name="depict" id="depict" value="${user.depict}"/>
        </p>
        <input type="hidden" name="id" value="${user.id}"/>
    </form>
    <button id="userUpdate">提交</button>
</div>

<script src="http://apps.bdimg.com/libs/jquery/1.8.3/jquery.min.js"></script>
<script>

    $("#userUpdate").click(function() {
        $.ajax({
            type : 'POST',
            url : "/cn/admin/userUpdate",
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