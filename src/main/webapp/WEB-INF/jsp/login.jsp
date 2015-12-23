<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	pageContext.setAttribute("appctx", application.getContextPath());
	out.print(pageContext.getAttribute("appctx"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>第三方登录演示</title>
<link href="../css/all.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/qrcode.js"></script>
<script type="text/javascript">

	function authWindow(url, width, height) {
		
		var params = {
			width: width || 600, 
			height: height || 450,
			left: 0,
			top: 0,
			
			toolbar: "no",
			menubar: "no",
			location: "no",
			status: "no",
			scrollbars: "no",
			
			resizable: "no"
		}
		
		// 弹窗居中
		params.left = ($(window).width() - params.width) / 2;
		params.top = ($(window).height() - params.height) /2;
		
		// 将参数对象转换成参数字符串"top=5,left=10,scrollbar=no,"
		function winParams(params) {
			var ret = "";
			for (var p in params) {
				ret += p + "= " + params[p] + ",";
			}
			ret.substr(0, ret.length - 1);
			return ret;
		}
		
		window.open (url, // 要打开的页面
				null, // 子窗口句柄
				winParams(params)// 参数
		); 
	}
	
	function showPic(sUrl){ 
		var qr = qrcode(10, 'M');
		qr.addData(url);
		qr.make();
		
		var x,y; 
		x = event.clientX; 
		y = event.clientY; 
		document.getElementById("Layer1").style.left = x; 
		document.getElementById("Layer1").style.top = y; 
		document.getElementById("Layer1").innerHTML = "<img src=\"" +  qr.createImgTag() + "\">"; 
		document.getElementById("Layer1").style.display = "block"; 
	} 
	function hiddenPic(){ 
		document.getElementById("Layer1").innerHTML = ""; 
		document.getElementById("Layer1").style.display = "none"; 
	} 
	
	//var dom=document.createElement('DIV');
	
	//var element=document.getElementById("qrcode");
//	element.appendChild(dom);
</script>
</head>
<body>
<div id="banner">
	<div id="plat_box">
		<div id="baidu" class="plat"><a href="${baiduAuthUrl}">百度</a></div>
		<div id="renren" class="plat"><a href="${githubAuthUrl}">GitHub</a></div>
		<div id="qq" class="plat" ><a href="javascript:alert('请自己去实现^_^');">QQ</a></div>
		 
		
		<div id="wx" class="plat"><a href="${wechatAuthUrl}">微信</a></div>
	</div>
</div>
<form id="login_box" method="post" action="login" autocomplete="off">
	<div id="err_info">
		<c:if test="${not empty errMsg}">${errMsg}</c:if>
	</div>
	<div><label>账号：</label><input type="text" name="account" value="${account}" /></div>
	<div><label>密码：</label><input type="text" name="passwd" onfocus="this.type='password'" /></div>
	<div><input type="submit" value="登  录" /></div>
	<div id="reg"><a href="javascript:alert('请自己去实现^_^');">新用户注册</a></div>
</form>
</body>
</html>