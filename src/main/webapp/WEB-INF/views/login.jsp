<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="commons/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>中诚信信用风险管理系统后台</title>
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!--     <script type="text/javascript" src="${ctx}/resources/js/jquery-1.8.3.min.js"></script> -->
<style type="text/css">
.login_tab .login_icon1{
	width:300px;
	height: 36px;
}
.login_tab .login_icon2{
	width:300px;
	height: 36px;
}
.login_tab .login_icon3{
	width:140px;
	height: 36px;
}
</style>
<script type="text/javascript">
var zIndex = 2000;
function alert(msg, callback) {
    var layerFlag = false;
    var alertDiv = "";
    var layer = $("#layer");

    if (!layer.get(0)) {
        alertDiv += "<div class='layer' id='layer' style='display: block;'></div>";
    } else {
        layer.css("display", "block");
    }

    $(".popup").each(function(){
        if ('none' == $(this).css("display")) {return;}
        if (zIndex == $(this).css("z-index")) {
            layerFlag = true;
            $(this).css("z-index",0);
        }
    });

    alertDiv +=
        "<div class='popup popup2' id='myPopupAlert' style='display: block;'>" +
        "<a href='javaScript:;' class='colse'></a>" +
        "<div style='height:100px;'>"+
        "<p style='word-break:break-word; line-height:25px; margin:0 15px;'>"+msg+"</p>" +
        "</div>"+
        "<div class='popup_btn' style='width:75px; margin-top:50px;'>" +
        "<a href='javaScript:;' class='a1 fl'>确定</a>" +
        "</div>" +
        "</div>";
    $(".login_body").append(alertDiv);

    $(document).on("click", "#myPopupAlert", function () {
        $("#myPopupAlert").remove();
        if (!layerFlag){
            $("#layer").css("display", "none");
        }
        $(".popup").each(function(){
            $(this).css("z-index",zIndex);
        });
        //
        if (callback) {
            //
            window.location.href = callback;
        }
    });
}

//登陆验证
function doLogin() {
	var account = $("#account").val();
	var password = $("#password").val();
	var verifyCode = $("#verifyCode").val();
	if(isNull(account)){
		$("#error").html("请输入用户名!");
		return;
	}
	if(isNull(password) ){
		$("#error").html("请输入密码!");
		return;
	}
	if(isNull(verifyCode) ){
		$("#error").html("请输入验证码!");
		return;
	}
	var checkNameData = checkLoginName(account);
	if (checkNameData.code != "0000" )  {
		$("#error").html(checkNameData.msg);
		return;
	}
    $.ajax({
        url: "${ctx}/checkLogin",
        type: "POST",
        dataType: "json",
        data: {
			"account":account,
			"password":password,
			"verifyCode":verifyCode
		},
        success: function(data) {
            console.log(data);
            if(data.code == 200) {
            	window.location.href = data.url;
            }
            else{
                $("#error").html(data.msg);
            }
        }
    });
}
//检验登录名状态
function checkLoginName(account){
	var result = null;
	$.ajax({
  		url :"${ctx }/checkLoginName",
  		data:{
	    	"account":account
	    },
	    datatype: 'json',  
	    type: 'post',
		async : false,
		success : function(data) {
			result = data;
		}
	});
	return result;
}
// 验证码验证 
// function checkVerifyCode(verifyCode){
// 	var result = "notNull";
// 	$.ajax({
// 	    url: "${ctx}/getVerifyCode",
// 	 	type: "POST",
// 	  	data: {
//      		"verifyCode":verifyCode
//      },
//      async : false,
// 	    success: function(data) {
// 	    	result = data;
// 	    }
//   	});
// 	return result
// }
//获取验证码   
function changeImg(){
    var img = document.getElementById("img"); 
    img.src = "${ctx}/authImage?date=" + new Date();
}

//回车登录
function enterlogin(){
    if (event.keyCode == 13){
        event.returnValue=false;
        event.cancel = true;
        doLogin();
    }
}

//判空
function isNull(data){
	if(null == data || "" == data || "undefined" == typeof(data) || 0 == data){
		return true;
	}else{
		return false;
	}
}

$(function(){
	var href=location.href;  
    if(href.indexOf("kickout")>0){  
        loginAlert("您的账号在另外的设备登录，您已下线。若非本人操作，请马上重新登录并修改密码！");  
    }   
// 	function kickout(){  
// 	    var href=location.href;  
// 	    if(href.indexOf("kickout")>0){  
// 	        alert("您的账号在另一台设备上登录，您被挤下线，若不是您本人操作，请立即修改密码！");  
// 	    }   
// 	 }  
// 	 window.onload=kickout(); 
})
//退出登录
function onlinePersonLoginOut(){
	$.post('${ctx}/logout', function(result) {
        window.location.href='${ctx }/login';
	}, 'json');
}
var zIndex = 2000;
function loginAlert(msg, callback) {
    var layerFlag = false;
    var alertDiv = "";
    var layer = $("#layer");

    if (!layer.get(0)) {
        alertDiv += "<div class='layer' id='layer' style='display: block;'></div>";
    } else {
        layer.css("display", "block");
    }

    $(".popup").each(function(){
        if ('none' == $(this).css("display")) {return;}
        if (zIndex == $(this).css("z-index")) {
            layerFlag = true;
            $(this).css("z-index",0);
        }
    });

    alertDiv +=
        "<div class='popup popup2' id='myPopupAlert' style='display: block;'>" +
        "<a href='javaScript:;' class='colse'></a>" +
        "<div style='height:100px;'>"+
        "<p style='word-break:break-word; line-height:25px; margin:0 15px;'>"+msg+"</p>" +
        "</div>"+
        "<div class='popup_btn' style='width:75px; margin-top:50px;'>" +
        "<a href='javaScript:onlinePersonLoginOut();' class='a1 fl'>确定</a>" +
        "</div>" +
        "</div>";
    $("body").append(alertDiv);

    $(document).on("click", "#myPopupAlert", function () {
        $("#myPopupAlert").remove();
        if (!layerFlag){
            $("#layer").css("display", "none");
        }
        $(".popup").each(function(){
            $(this).css("z-index",zIndex);
        });
        //
        if (callback) {
            //
            window.location.href = callback;
        }
    });
}
</script>
</head>
<body class="login_body"  onkeydown="enterlogin();">
	<div class="login_box">
    	<h2>中诚信信用风险管理系统后台</h2>
        <div class="login_content">
        	<form id="frm">
            	<table class="login_tab">
                	<tbody>
                		<tr>
                        	<td><input id="account" name="account" value="" placeholder="用户名" class="login_icon1" type="text"></td>
                        </tr>
                        <tr>
                        	<td><input id="password" name="password" value="" placeholder="密码" class="login_icon2" type="password"></td>
                        </tr>
                        <tr>
                        	<td>
	                            <input id="verifyCode" value="" placeholder="验证码" class="login_icon3 login_icon4 fl" type="text">
						        <a href='javascript:changeImg()'>
						        	<img src="${ctx}/authImage" id="img" alt="" class="code_img fr">
						        </a>
                            </td>
                        </tr>
                        <tr class="login_tr">
                        	<td><p class="error_title" id="error"></p></td>
                        </tr>
                        <tr>
                        	<td>
                        		<a href="javascript:doLogin();" class="login_btn fl" >登录</a>
                            </td>	
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </div>
</body>
</html>
