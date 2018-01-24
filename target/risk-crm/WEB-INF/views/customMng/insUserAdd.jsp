<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!doctype html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>
客户管理-账号管理-用户管理-创建用户
</title>

<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!-- <script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script> -->
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
<style type="text/css">
.input_table input{width:222px;}

.myError_info {
/*     background: url("${ctx}/resources/image/error.png") 0 3px no-repeat; */
    color: #ed7563;
    font-size: 12px;
    height: 20px;
    line-height: 20px;
    text-indent: 18px;
    position: inherit;
    left: 10px;
    bottom: 0;
    z-index: 1;
}

</style>
<script type="text/javascript">
/*页面初始化*/
$(function() {
	$("#role_select_box").on("click","p",function(){
		$("#roleId").val($(this).attr("data-id"));
	})
	
	//表单验证
	//登录名验证
	$.validator.addMethod("isOnlyLoginName", function(value,element) {
		var ok = false;
		$.ajax({
    		url: "${ctx}/custom/isOnlyLoginName",
	        data: JSON.stringify(
	        	{
	        		"loginName":value,
	        		"insId":"${insId}"
	        	}
	        ),
	        dataType : 'json',
			contentType: 'application/json', 
		    type: 'post',
		    async: false,
			success : function(data) {
				var data = eval(data);
				if("1000" == data){
					ok = true;
				}else{
					ok = false;
				}
			},
            error: function (data) {
                ok = false;
            }
		})
		return ok;
	}, "此用户名已存在，请尝试其它用户名");
	$.validator.addMethod("checkLoginName", function(value,element) {
		var loginName = $.trim($("#loginName").val());
		//var reg=/^[a-zA-Z0-9]{8,18}$/; 
		var reg=/^(?=.*?[a-zA-Z])(?=.*?[0-9])[a-zA-Z0-9]{1,18}$/
		var regg=/^[a-zA-Z]{1,18}$/
		if(reg.test(loginName) || regg.test(loginName)){
			return true;
		}else{
			return false;
		}
		return true;
	}, "用户名必须为18位以内,字母或者数字字母组合");
	$.validator.addMethod("checkName", function(value,element) {
		var name = $.trim($("#name").val());
		//var reg=/^[a-zA-Z0-9]{8,18}$/; 
		//var reg=/^[\u4E00-\u9FA5]{1,18}$/
		var reg=/^[\u4E00-\u9FA5]{1,18}(?:·[\u4E00-\u9FA5]{1,18})*$/;
		var regg=/^[a-zA-Z]{1,18}$/
		if(reg.test(name)==false && regg.test(name)==false){
			return false;
		}
		return true;
	}, "姓名必须为18位以内的英文或中文");
	$.validator.addMethod("isPassword2", function(value,element) {
		var password = $("#password").val();
		//var reg=/^[a-zA-Z0-9]{8,18}$/; 
		var reg=/^(?=.*?[a-zA-Z])(?=.*?[0-9])[a-zA-Z0-9]{8,18}$/
		if(reg.test(password)==false){
			return false;
		}
		return true;
	}, "密码必须为8-18位,数字字母组合");
	$.validator.addMethod("passwordSame2", function(value,element) {
		var password = $("#password").val();
		var password2 = $("#password2").val();
		if (password == password2) return true;
		return false;
	}, "两次密码输入不一致");
	$.validator.addMethod("isRoleId", function(value,element) {
		var roleId = $("#roleId").val();
		if (null!=roleId&&""!=roleId&&"0000"!=roleId) return true;
		return false;
	}, "请选择角色");
	$.validator.addMethod("isMobPhone", function(value,element) {
		var passport = /(^1[34578]{1}[0-9]{9}$)/;
		return this.optional(element) || (passport.test(value));
	}, "请输入正确的手机号码");
	$.validator.addMethod("isEmail", function(value,element) {
		var passport = /^([0-9A-Za-z_\-])+(\.[0-9A-Za-z_\-]+)*@([0-9A-Za-z_\-])+((\.\w+)+)$/;
		return this.optional(element) || (passport.test(value));
	}, "请输入正确的电子邮箱");
	//新增弹窗验证
	$("#userAddForm").validate({
		rules: {
			loginName:{
				required: true,
				isOnlyLoginName:true,
				checkLoginName: true
			},name:{
				required: true,
				checkName:true
			},password:{
				required: true,
		      	isPassword2:true
			},password2:{
				required: true,
		      	passwordSame2: true
			},roleId:{
				required: true,
				isRoleId: true
			},phone:{
				required: true,
				isMobPhone: true
			},email:{
				isEmail: true
			}
		},
		messages: {
		  loginName:{
              required:"请输入用户名"
          },
          name:{
              required:"请输入姓名"
          },
          password:{
              required:"请输入密码"
          },
          password2:{
              required:"请确认密码"
          },
          roleId:{
              required:"请选择角色"
          },
          phone:{
              required:"请输入手机号"
          }
	    },
	 	errorPlacement: function(error, element) { 
	 		if(element.is("input[name=loginName]")){
	 			error.appendTo($("#loginName_error")); 
	 		}
	 		if(element.is("input[name=name]")){
	 			error.appendTo($("#name_error")); 
	 		}
	 		if(element.is("input[name=password]")){
	 			error.appendTo($("#password_error")); 
	 		}
	 		if(element.is("input[name=password2]")){
	 			error.appendTo($("#password2_error")); 
	 		}
	 		if(element.is("input[name=roleId]")){
	 			error.appendTo($("#roleId_error")); 
	 		}
	 		if(element.is("input[name=phone]")){
	 			error.appendTo($("#phone_error")); 
	 		}
	 		if(element.is("input[name=email]")){
	 			error.appendTo($("#email_error")); 
	 		}
	 	},
	});
})

//保存新增用户
function saveAddUser(){
	if($("#userAddForm").valid()){
		$.ajax({
			url : "${ctx}/custom/saveAddInsUser",
			type : 'POST',
			data : JSON.stringify(
				{
					"insId":$("#insId").val(),
					"loginName":$("#loginName").val(),
					"name":$("#name").val(),
					"password":$("#password").val(),
					"roleId":$("#roleId").val(),
					"phone":$("#phone").val(),
					"email":$("#email").val()
				}		
			),
			dataType : 'json',
			contentType: 'application/json',
			success : function(data) {
				var href = "${ctx}/custom/toInsUserManagePage?id="+$("#insId").val();
				if (data == "1000") {
					alert("用户新增成功!",href);
				}else{
					alert("用户新增失败!",href);
				}
			}
		});
	}
}
</script>
</head>
<body class="body_bg">
<div class="main">
	<!--页面头部 start -->
	<%@ include file="../commons/topHead.jsp"%>
	<!--页面头部 end -->
    <!-- center.html start -->
    <div class="main_center">
    	<!--页面左侧导航栏 start -->
		<%@ include file="../commons/leftNavigation.jsp"%>
		<!-- 页面左侧导航栏 end -->
		
        <!-- 右侧内容.html start -->
        <div class="right_content">
            <h3 class="place_title">
                <span>当前位置：</span>
                <a href="javascript:void(0);">客户管理</a>
                <span>></span>
                <a href="${ctx}/custom/insAccountManager">账号管理</a>
                <span>></span>
                <a href="${ctx}/custom/toInsUserManagePage?id=${insId }">用户管理</a>
                <span>></span>
                <a href="${ctx}/custom/toInsUserAddPage?insId=${insId }">创建用户</a>
            </h3>
            <div class="backtrack">
            	<a href="${ctx}/custom/toInsUserManagePage?id=${insId }">返回</a>
            </div>
            <div class="shade main_minHeight">
            	<div class="input_list_box">
                    <form id="userAddForm">
                    	<input type="hidden" id="insId" value="${insId }" >
                        <table class="input_table">
                            <tbody>
                                <tr>
                                    <td><strong>用户名<i>*</i></strong></td>
                                    <td>
                                        <input value="" type="text" id="loginName" name="loginName" placeholder="请输入用户名" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                                    </td>
                                    <td><p class="myError_info" id="loginName_error"></p></td>
                                </tr>
                                <tr>
                                    <td><strong>姓名<i>*</i></strong></td>
                                    <td><input value="" type="text" id="name" name="name" placeholder="请输入姓名" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" /></td>
                                    <td><p class="myError_info" id="name_error"></p></td>
                                </tr>
                                <tr>
                                    <td><strong>密码<i>*</i></strong></td>
                                    <td><input type="password" id="password" name="password" placeholder="请输入密码" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/></td>
                                    <td><p class="myError_info" id="password_error"></p></td>
                                </tr>
                                <tr>
                                    <td><strong>确认密码<i>*</i></strong></td>
                                    <td><input type="password" id="password2" name="password2" placeholder="请确认密码" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/></td>
                                    <td><p class="myError_info" id="password2_error"></p></td>
                                </tr>
                                <tr>
                                    <td><strong>角色<i>*</i></strong></td>
                                    <td>
                                        <div class="down_menu">
                                            <div class="select_btn">
                                            <input type="hidden" id="roleId"  name="roleId" >
                                                <span data-id="0000">请选择角色</span>
                                            </div>
                                            <div class="select_list" id="role_select_box" >
                                               <p data-id="0000">请选择角色</p>
                                                <c:forEach var="item" items="${roleList }">
													<p data-id="${item.id }">${item.name }</p>
												</c:forEach>
                                            </div>
                                        </div>
                                    </td>
                                     <td><p class="myError_info" style="display:block;" id="roleId_error"></p></td>
                                </tr>
                                <tr>
                                    <td><strong>手机号<i>*</i></strong></td>
                                    <td>
                                       <input value="" type="text" id="phone" name="phone" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                                    </td>
                                    <td><p class="myError_info" id="phone_error"></p></td>
                                </tr>
                                <tr>
                                    <td><strong>邮箱</strong></td>
                                    <td>
                                        <input value="" type="text" id="email" name="email" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                                    </td>
                                    <td><p class="myError_info" id="email_error"></p></td>
                                </tr>
                                <tr>
                                    <td><strong></strong></td>
                                    <td>
                                        <div class="input_btn clear">
                                            <a href="javaScript:saveAddUser();" class="fl makeSure">保存</a>
                                            <a href="${ctx}/custom/toInsUserManagePage?id=${insId }" class="fr cancel">取消</a>
                                        </div>
                                    </td>
                                </tr>
                                
                            </tbody>
                        </table>
                    </form>	
                </div>   
            </div>
            <!-- footer.html start -->
            <%@ include file="../commons/foot.jsp"%>
            <!-- footer.html end -->
        </div>
        <!-- 右侧内容.html end -->
    </div>
    <!-- center.html end -->
</div>
<!-- 遮罩层 start -->
<div class="layer" id="layer"></div>
<!-- 遮罩层 end -->
<!-- 成功标识.html start -->
<div class="popup popup2" id="popup2">
	<a href="javaScript:;" class="close"></a>
    <p></p>
    <div class="popup_btn delete_btn">
    	<a href="javaScript:;" class="a1 fl">确定</a>
    </div>
</div>
<!-- 成功标识.html end -->
</body>
</html>
