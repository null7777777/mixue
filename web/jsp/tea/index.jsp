<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();  
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	pageContext.setAttribute("basePath", basePath);
%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
	<base href="${basePath}">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>TEA SHOP</title>
	<link rel="stylesheet" href="bs/css/bootstrap.css">
	<script type="text/javascript" src="bs/js/jquery.min.js"></script>
	<script type="text/javascript" src="bs/js/bootstrap.js"></script> 
	<link href="css/tea/head_footer.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="js/tea/getCatalog.js"></script>
	<script type="text/javascript" src="js/tea/index.js"></script>
	<script type="text/javascript" src="js/tea/landing.js"></script>
	<link rel="stylesheet" href="css/tea/index.css" />
	<script type="text/javascript" src="js/tea/addcart.js"></script>
	
	<style type="text/css">
		.dropdown-menu{
			margin:0;
		}
	</style>

</head>
<body>

	<div class="container-fullid">
		<%@include file="header.jsp" %>
		<div class="wrapper">
			<!-- banner start -->
			<div class="banner" style="margin-top:-19px;">
				<div id="carousel-example-generic" class="carousel slide" data-ride="carousel">
					<!-- Indicators -->
					<ol class="carousel-indicators">
						<li data-target="#carousel-example-generic" data-slide-to="0" class="active"></li>
						<li data-target="#carousel-example-generic" data-slide-to="1"></li>
						<li data-target="#carousel-example-generic" data-slide-to="2"></li>
					</ol>
					<!-- Wrapper for slides -->
					<!-- <div class="carousel-inner" role="listbox">
						<div class="item active">
							<img src="images/tea/banner1.png" alt="...">
						</div>
						<div class="item">
							<img src="images/tea/banner2.png" alt="...">
						</div>
						<div class="item">
							<img src="images/tea/banner3.png" alt="...">
						</div>
						
					</div> -->
					
					<div class="carousel-inner" role="listbox">
					    <div class="item active">
					        <img src="images/tea/banner1.png" alt="..." style="width: 100%; height: auto;">
					    </div>
					    <div class="item">
					        <img src="images/tea/banner2.png" alt="..." style="width: 100%; height: auto;">
					    </div>
					    <div class="item">
					        <img src="images/tea/banner3.png" alt="..." style="width: 100%; height: auto;">
					    </div>
					</div>
					
				
				    <!-- Controls -->
					<a class="left carousel-control" href="#carousel-example-generic" role="button" data-slide="prev">
					    <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
					</a>
					<a class="right carousel-control" href="#carousel-example-generic" role="button" data-slide="next">
					    <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
					</a>
				</div>
			</div>
			<!-- main start -->
			<div class="main container">
				<div class="row">
					<div class="col-md-2 main-left">
						<h3>商品分类</h3>
						
						<ul id="catalog-list">
							<li><a href="TeaList">所有商品</a></li>
							
						</ul>
					</div>
					
					<div class="search col-md-4 col-md-offset-6" style="margin-top:20px;">
						<div class="input-group">
							<form action="TeaList2" method="get">
		     	 				<input style="float: left;width: 160px;" type="text" class="form-control" name="seachname" placeholder="输入要搜索商品">
		       					&nbsp;&nbsp;&nbsp;
		       					<span style="float: left;width: 40px;" class="btn btn-default" type="submit" ><img class="icon" src="images/tea/search-icon.png" alt=""></span>
							</form>
   						</div>
					</div> 
					<div class="col-md-10 main-right" style="margin-top:-52px;">
						<div class="pro col-md-12">
							<h3>推荐商品</h3>
							<div id="recTeas" class="pro-list">
								<ul> 
								
									
								</ul>
							</div>
								
						</div>
						<div class="pro col-md-12">
							<h3>新品上市</h3>
							<div id="newTeas" class="pro-list">
								<ul > 
									
								</ul>
							</div>
						</div>
					</div>
					
				</div>
			</div>
		</div>
		<br/><br/>
		
		<%@include file="footer.jsp" %>
	</div>
	<!--弹窗盒子start -->
	<div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
	  	<div class="modal-dialog modal-sm">
	    	<div class="modal-content" style="margin-top:300px;">
	    		<div class="modal-body" style="color:green;font-size:20px;">
	    		  	<img class="icon" src="images/tea/success-icon.png" alt="">
				  	 已成功加入购物车
				</div>
	    			
	      		<div class="modal-footer">
	      			<a href="javascript:void(0)" type="button" class="btn btn-md btn-default" data-dismiss="modal">返回继续购物</a>
			        <a href="jsp/tea/cart.jsp" type="button" class="btn btn-md btn-success">立即去结算</a>
			    </div>
	    	</div>
	  	</div>
	</div>
	<!--弹窗盒子end -->
</body>
</html>