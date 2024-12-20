<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	pageContext.setAttribute("basePath", basePath);
%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<base href="${basePath}">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户中心</title>
<link rel="stylesheet" href="bs/css/bootstrap.css">
<script type="text/javascript" src="bs/js/jquery.min.js"></script>
<script type="text/javascript" src="bs/js/bootstrap.js"></script>		
<script type="text/javascript" src="js/tea/landing.js"></script>
<link href="css/tea/head_footer.css" rel="stylesheet" type="text/css">
<style type="text/css">
	/* 主体宽度控制 */
	.main-container {
		max-width: 800px;
		margin: 0 auto;
		padding: 20px;
	}

	/* 标题居中 */
	h1 {
		text-align: center;
		margin-bottom: 30px;
	}

	/* 用户信息表单样式 */
	.user-form {
		background-color: #f9f9f9;
		border: 1px solid #ddd;
		border-radius: 10px;
		padding: 20px;
		box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
	}

	.user-form h2 {
		text-align: center;
		margin-bottom: 20px;
		color: #333;
	}

	.user-form .form-group {
		margin-bottom: 15px;
	}

	.user-form label {
		font-weight: bold;
	}

	.user-form input[type="text"],
	.user-form input[type="number"],
	.user-form input[type="tel"],
	.user-form select {
		width: 100%;
		padding: 12px; /* 增加内边距 */
		font-size: 14px; /* 调整字体大小 */
		border: 1px solid #ccc;
		border-radius: 5px;
		box-sizing: border-box;
		height: 40px; /* 固定高度，确保文本有足够的空间 */
	}

	.user-form .submit-btn {
		display: block;
		width: 100%;
		padding: 10px;
		background-color: #007bff;
		border: none;
		border-radius: 5px;
		color: white;
		font-size: 16px;
		cursor: pointer;
		margin-top: 20px;
		transition: background-color 0.3s;
	}

	.user-form .submit-btn:hover {
		background-color: #0056b3;
	}

	/* 按钮居中 */
	.submit-btn-wrapper {
		text-align: center;
	}
</style>	
</head>
<body>
	<div class="container-fullid">
		<%@include file="header.jsp" %>
		<div class="main-container">
			<h1>用户中心</h1>
			
			<c:if test="${not empty sessionScope.user}">
				<div class="user-form">
					<h2>个人信息</h2>
					<form id="userForm" action="UpdateUserServlet" method="post">
						<input type="hidden" name="userId" value="${sessionScope.user.userId}" />
						<div class="form-group">
							<label for="userName">用户名:</label>
							<input type="text" class="form-control" id="userName" name="userName" value="${sessionScope.user.userName}" readonly>
						</div>
						<div class="form-group">
							<label for="name">姓名:</label>
							<input type="text" class="form-control" id="name" name="name" value="${sessionScope.user.name}">
						</div>
						<div class="form-group">
							<label for="sex">性别:</label>
							<select class="form-control" id="sex" name="sex">
								<option value="男" ${sessionScope.user.sex == '男' ? 'selected' : ''}>男</option>
								<option value="女" ${sessionScope.user.sex == '女' ? 'selected' : ''}>女</option>
							</select>
						</div>
						<div class="form-group">
							<label for="age">年龄:</label>
							<input type="number" class="form-control" id="age" name="age" value="${sessionScope.user.age}">
						</div>
						<div class="form-group">
							<label for="tell">电话:</label>
							<input type="tel" class="form-control" id="tell" name="tell" value="${sessionScope.user.tell}">
						</div>
						<div class="form-group">
							<label for="address">地址:</label>
							<input type="text" class="form-control" id="address" name="address" value="${sessionScope.user.address}">
						</div>
						<div class="form-group">
							<label for="enabled">状态:</label>
							<select class="form-control" id="enabled" name="enabled">
								<option value="y" ${sessionScope.user.enabled == 'y' ? 'selected' : ''}>启用</option>
								<option value="n" ${sessionScope.user.enabled == 'n' ? 'selected' : ''}>禁用</option>
							</select>
						</div>
						<div class="submit-btn-wrapper">
							<button type="submit" class="btn btn-primary submit-btn">保存更改</button>
						</div>
					</form>
				</div>
			</c:if>

			<c:if test="${empty sessionScope.user}">
				<p class="text-center">当前没有登录用户。</p>
			</c:if>
		</div>
		<%@include file="footer.jsp" %>
	</div>

</body>
</html>