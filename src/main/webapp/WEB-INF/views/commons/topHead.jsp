<%@ page language="java" import="java.util.*" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<script type="text/javascript">
$(function(){
	$('#header_nav a').click(function(){
		$('#header_nav a').parent().removeClass('active');
		$(this).parent().addClass('active');
	});
})


</script>
<!-- header.html start -->
<div class="header_height"></div>
    <div class="header">
    	<div class="clear">
        	<h1 class="fl header_logo">
            	<a href="${ctx}/index">中诚信信用风险管理系统后台</a>
            </h1>
            <div class="header_nav_box fl">
                <ul class="header_nav clear" id="header_nav">
                	<c:forEach items="${permissionList }" var="per" varStatus="st" >
				    	<c:if test="${per.type == 1 && per.parentId eq 'manage-0' }">
						    <li >
							    <a href="javascript:void(0);"
							     onclick="clickHeader('${per.myselfId }','${st.index}');"
							     id="${per.myselfId }" 
							     >${per.permission_name }
							    </a>
						    </li>
				    	</c:if>  
				    </c:forEach>
                </ul>
            </div>
            <div class="header_login fr" id="header_login">
            	<span>${risk_crm_user.loginName }</span>	
            </div>
            <ul class="login_list" id="login_list">
                <li><a href="javascript:void(0);" onclick="showUpdatePswMIndex();">修改密码</a></li>
                <li><a href="javascript:void(0);" onclick="logout();" >退出登录</a></li>
            </ul>
        </div>	
</div>
<!-- header.html end -->
<!-- 遮罩层 start -->
<div class="layer" id="layerIndex"></div>
<!-- 遮罩层 end -->
<!-- 成功标识.html start -->
<div class="popup popup2" id="popup2Index">
	<a href="javaScript:;" onclick="firstLoginOut()" class="close"></a>
    <p></p>
    <div class="popup_btn delete_btn">
    	<a href="javaScript:;" onclick="firstLoginOut()" class="a1 fl">确定</a>
    </div>
</div>
<!-- 成功标识.html end -->
<!-- 重置密码 start -->
<div class="popup popup3" id="popup1Index">
	<a href="javaScript:;" onclick="myFnShutDownIndex('#popup1Index')" class="close"></a>
    <div class="popup_tab_box">
    	<form id="resetPassWordFormIndex">
    	<input type="hidden" id="userIdIndex" value="${risk_crm_user.id }" >
        	<table class="popup_tab">
            	<tbody>
                    <tr>
                    	<td class="td1"><strong>原密码</strong></td>
                        <td><input value="${risk_crm_user.password }" type="password" class="popup_tab_w" id="oldPasswordIndex" readonly="readonly"/>
                    </tr>
                    <tr>
                    	<td class="td1"><strong>新密码</strong></td>
                        <td><input type="password" class="popup_tab_w" id="newPasswordIndex" name="newPasswordIndex" placeholder="请输入新密码" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                        <div class="error_info" id="newPasswordIndex_error"></div>
                        </td>
                    </tr>
                    <tr>
                    	<td class="td1"><strong>确认密码</strong></td>
                        <td><input type="password" class="popup_tab_w" id="newPassword2Index" name="newPassword2Index" placeholder="请确认密码" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                        <div class="error_info" id="newPassword2Index_error"></div>
                        </td>
                    </tr>
                    <tr>
                    	<td class="td1"></td>
                        <td>
                        	<div class="popup_btn">
                                <a href="javaScript:;" onclick="saveUpdatePswIndex()" class="a1 fl">确定</a>
                                <a href="javaScript:;" onclick="myFnShutDownIndex('#popup1Index')" class="a2 fr">取消</a>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </form>
    </div>
</div>
<!-- 重置密码 end -->
<!-- 启用停用删除 start -->
<div class="popup popup2" id="popupIndex">
	<a href="javaScript:;" onclick="myFnShutDownIndex('#popupIndex')" class="close"></a>
    <p></p>
    <div class="popup_btn">
    	<a href="javaScript:firstLoginOut();" class="a1 fl">确定</a>
        <a href="javaScript:;" onclick="myFnShutDownIndex('#popupIndex')" class="a2 fr">取消</a>
    </div>
</div>
<!-- 启用停用删除 end -->

<script type="text/javascript">
$(function(){
	$.validator.addMethod("isPasswordIndex", function(value,element) {
		var newPasswordIndex = $("#newPasswordIndex").val();
		//var reg=/^[a-zA-Z0-9]{8,18}$/; 
		var reg=/^(?=.*?[a-zA-Z])(?=.*?[0-9])[a-zA-Z0-9]{8,18}$/
		if(reg.test(newPasswordIndex)==false){
			return false;
		}
		return true;
	}, "密码必须8-16位,数字字母组合");
	$.validator.addMethod("passwordSameIndex", function(value,element) {
		var newPasswordIndex = $("#newPasswordIndex").val();
		var newPassword2Index = $("#newPassword2Index").val();
		if (newPasswordIndex == newPassword2Index) return true;
		return false;
	}, "两次密码输入不一致");
	$("#resetPassWordFormIndex").validate({
	    rules: {
	      newPasswordIndex: {
	      	required: true,
	      	isPasswordIndex:true
	      },
	      newPassword2Index: {
	      	required: true,
	      	passwordSameIndex: true
	      }
	    },
	    messages: {
	      newPasswordIndex:{
              required:"请输入新密码"
          },
          newPassword2Index:{
	          required:"请重复新密码"
	      }
	    },
	    errorPlacement: function(error, element) { 
	 		if(element.is("input[name=newPasswordIndex]")){
	 			error.appendTo($("#newPasswordIndex_error")); 
	 		}
	 		if(element.is("input[name=newPassword2Index]")){
	 			error.appendTo($("#newPassword2Index_error")); 
	 		}
	 	},
    });
	
})
function showUpdatePswMIndex(){
	//显式弹窗之前清除之前input中的输入
	$("#newPasswordIndex").val("");
	$("#newPassword2Index").val("");
	$("#newPasswordIndex_error").html("");
	$("#newPassword2Index_error").html("");
	myFnPopupIndex('#popup1Index');
}

function saveUpdatePswIndex(){
	var userId = $("#userIdIndex").val();
	if(null == userId || ""==userId || typeof(userId) == "undefined"){
		window.location.href = "${ctx}/logout";
	}else{
		if($("#resetPassWordFormIndex").valid()){
			var newPasswordIndex = $("#newPasswordIndex").val();
			$.ajax({
				url : "${ctx}/user/updateUserPwd",
				type : 'POST',
				data : {
						"userId":userId,
						"newPassword":newPasswordIndex
					},
				success : function(data) {
					myFnShutDownIndex('#popup1Index');
					if (data == "1000") {
						myFnPopupIndex('#popup2Index','密码修改成功！');
					} else {
						myFnPopupIndex('#popup2Index','密码修改失败！');
					}
				}
			});
		}
	}
}

//退出确认
function logout() {
	myFnPopupIndex('#popupIndex','确定退出登录？');
}
//退出登录
function firstLoginOut(){
	$.post('${ctx}/logout', function(result) {
        window.location.href='${ctx }/login';
	}, 'json');
}

//打开弹窗
function myFnPopupIndex(obj,hit){
	$('#layerIndex').show();
	$(obj).show();
	if(hit){
		$(obj).find('p').html(hit);
	}
	$(obj).find('a:eq(0)').click(function(){
		fnShutDown(obj);
	})
// 	$(obj).find('.a1').click(function(){
// 		fnShutDown(obj);
// 	});
	$(obj).find('.a2').click(function(){
		fnShutDown(obj);
	});
}
//关闭弹窗
function myFnShutDownIndex(obj){
	$('#layerIndex').hide();
	$(obj).hide();
}
</script>




