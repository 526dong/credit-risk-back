<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!doctype html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>客户管理-账号管理-用户管理 </title>

<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!-- <script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script> -->
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>

<script type="text/javascript">
/*页面初始化*/
$(function() {
	$("#currentPage").val(1);
	/*列表数据*/
	findAllInsUserList();
	
	$.validator.addMethod("isPassword2", function(value,element) {
		var password = $("#newPassword").val();
		//var reg=/^[a-zA-Z0-9]{8,18}$/; 
		var reg=/^(?=.*?[a-zA-Z])(?=.*?[0-9])[a-zA-Z0-9]{8,18}$/
		if(reg.test(password)==false){
			return false;
		}
		return true;
	}, "密码必须为8-18位,数字字母组合");
	$.validator.addMethod("passwordSame2", function(value,element) {
		var password = $("#newPassword").val();
		var password2 = $("#newPassword2").val();
		if (password == password2) return true;
		return false;
	}, "两次密码输入不一致");
	$("#resetPassWordForm").validate({
	    rules: {
	    	newPassword:{
				required: true,
		      	isPassword2:true
			},newPassword2:{
				required: true,
		      	passwordSame2: true
			}
	    },
	    messages: {
	    	newPassword:{
	            required:"请输入新密码"
	        },
	        newPassword2:{
	            required:"请确认密码"
	        },
	    },
	    errorPlacement: function(error, element) { 
	 		if(element.is("input[name=newPassword]")){
	 			error.appendTo($("#newPassword_error")); 
	 		}
	 		if(element.is("input[name=newPassword2]")){
	 			error.appendTo($("#newPassword2_error")); 
	 		}
	 	},
    });
})
//显示用户账号上限设置
function fnShowUser(){
	var hasUserNum = $("#hasUserNum").val();
	var userNumLimit = $("#userNumLimit").val();
	$("#userNumLimitText").val(userNumLimit);
	$('.alter_user').show();
}
//隐藏用户账号上限设置
function fnHideUser(){
	$('.alter_user').hide();
}

//修改创建用户上限
function saveUserNum(){
	var hasUserNum = $("#hasUserNum").val();
	var userNumLimitText = $("#userNumLimitText").val();
	if(null == userNumLimitText || "" == userNumLimitText || 0 == parseInt(userNumLimitText) || "0" == userNumLimitText.substring(0,1)){
		alert("修改用户上限不能低于1个!");
		$("#userNumLimitText").val("0")
		return false;
	}else if(parseInt(userNumLimitText)<parseInt(hasUserNum)){
		alert("修改数量不能少于当前机构创建的账号数量，请在下方用户列表中删除部分账号后再修改数量!");
		//fnPopup("#popup2","当前机构创建的账号数量已超过修改数量，请在下方用户列表中删除部分账号后在修改数量!");
		return false;
	}else{
		$.ajax({
	  		url :"${ctx}/custom/saveEditUserNum",
			data : JSON.stringify(
					{
						"insId":$("#insId").val(),
						"insUserNum":userNumLimitText
					}		
			),
			type : "POST",
			dataType : 'json',
			async: false,
			contentType: 'application/json',
			success : function(data) {
				var data = eval(data);
				//这个地方不再进行弹窗提示成功与失败了，太多弹窗体验度下降
				if (data=="1000") {
					fnHideUser();
					$("#saveUserNum").html(userNumLimitText);
					$("#userNumLimit").val(userNumLimitText);
					$("#userNumLimitText").val(userNumLimitText);
				}else{
					fnHideUser();
					$("#saveUserNum").html(userNumLimitText);
					$("#userNumLimit").val(userNumLimitText);
					$("#userNumLimitText").val(userNumLimitText);
				}
			}
		});
	}
}

/*列表数据*/
function findAllInsUserList() {
    $.ajax({
        url : "${ctx}/custom/findAllInsUserList",
        type : 'POST',
        data : {
        	"pageSize":10,//每页展示数
        	"currentPage": $("#currentPage").val(),//当前页
            "insId": $("#insId").val()
        },
        async : false,
        success : function(data) { 
        	var htmlStr = createTable(data.rows);
            $("#htmlContent").html(htmlStr);
            var pageStr = creatPage(data.total, data.pageNo,data.totalPage);
            $("#pageDiv").html(pageStr);
        }
    });
}

//创建机构用户列表
function createTable(data) {
    var htmlContent = "";
    for (var i = 0; i < data.length; i++) {
    	var loginName = data[i].loginName;
        if(null==loginName||typeof(loginName)=="undefined"||""==loginName){
        	loginName = "";
        }
        var name = data[i].name;
        if(null==name||typeof(name)=="undefined"||""==name){
        	name = "";
        }
        var roleName = data[i].roleName;
        if(null==roleName||typeof(roleName)=="undefined"||""==roleName){
        	roleName = "";
        }
        var createTime = data[i].createTime;
        if(null==createTime||typeof(createTime)=="undefined"||""==createTime){
        	createTime = "";
        }
        var status = data[i].status;
    	var statusStr=""; 
        if(status==0){
        	statusStr="启用"; 
        }else if(status==1){
        	statusStr="停用"; 
        }else{
        	statusStr="锁定"; 
        }
        var userType = data[i].userType;
    	var userTypeStr=""; 
        if(userType==1){
        	userTypeStr="后台"; 
        }else{
        	userTypeStr="前台"; 
        }
        htmlContent += "<tr>";
        htmlContent += "<td>" + (parseInt(i) + 1)+ "</td>";
        htmlContent += "<td>" + loginName + "</td>";
        htmlContent += "<td>" + name + "</td>";
        htmlContent += "<td>" + roleName + "</td>";
        htmlContent += "<td>" + createTime + "</td>";
        htmlContent += "<td>" + statusStr + "</td>";
        htmlContent += "<td>" + userTypeStr + "</td>";
        htmlContent += "<td class='operate' >";
        htmlContent += "<shiro:hasPermission name='/custom/toInsUserLookDetailPage'><a href='javaScript:;' onclick=lookDetailInsUser('"+data[i].id+"') >查看</a></shiro:hasPermission>";
        if(status == 1){
        	htmlContent += "<shiro:hasPermission name='/custom/toInsUserUpdatePage'><a href='javaScript:;' onclick=editInsUser('"+data[i].id+"') >修改</a></shiro:hasPermission>";
		}
        htmlContent += "<shiro:hasPermission name='/custom/updatePassword'><a href='javaScript:;' onclick=resetPassWord('"+data[i].id+"','"+data[i].loginName+"','"+data[i].name+"') >重置密码</a></shiro:hasPermission><shiro:hasPermission name='/custom/stop'>";
        if(status == 0){
        	htmlContent += "<a href='javaScript:;' onclick=freezeInsUser('"+data[i].id+"') >停用</a>";
		}else if(status == 1){
			htmlContent += "<a href='javaScript:;' onclick=unfreezeInsUser('"+data[i].id+"','"+data[i].loginName+"') >启用</a>";
		}else if(status == 2){
			htmlContent += "<a href='javaScript:;' onclick=unlockInsUser('"+data[i].id+"','"+data[i].loginName+"') >解锁</a>";
		}
        htmlContent += "</shiro:hasPermission><shiro:hasPermission name='/custom/del'><a href='javaScript:;' onclick=deleteInsUser('"+data[i].id+"') >删除</a></shiro:hasPermission>";
        htmlContent += "</td>";
        htmlContent += "</tr>";
    }
    return htmlContent;
}

//分页跳转
function jumpToPage(curPage){
	if(typeof(curPage) != "undefined"){
    	$("#currentPage").val(curPage);
	}else{
		$("#currentPage").val(1);
	}
	//查询
	findAllInsUserList();
}

//创建机构用户
function addInsUser(){
	var hasUserNum = $("#hasUserNum").val();//已经拥有的
	var userNumLimit = $("#userNumLimit").val();//设置的最高数量
    if(0 == userNumLimit || null == userNumLimit || "" == userNumLimit){
		alert("请先设置用户账号上限！");
		return false; 
	}else if(parseInt(hasUserNum)>=parseInt(userNumLimit)){
		alert("请先增加最大账户数量！");
		return false; 
	}else{
		window.location.href="${ctx}/custom/toInsUserAddPage?insId="+$("#insId").val();
	}
}

//查看用户
function editInsUser(userId){
	window.location.href="${ctx}/custom/toInsUserUpdatePage?userId="+userId+"&&insId="+$("#insId").val();
}

//修改用户
function lookDetailInsUser(userId){
	window.location.href="${ctx}/custom/toInsUserLookDetailPage?userId="+userId+"&&insId="+$("#insId").val();
}

//重置密码
function resetPassWord(userId,loginName,name){
	$("#newPassword_error").html("");
	$("#newPassword2_error").html("");
	$("#userId").val(userId);
	$("#loginNameFn").html(loginName);
	$("#nameFn").html(name);
	$("#newPassword").val("");
	$("#newPassword2").val("");
	myFnPopup('#popup1');
}

//保存修改后的密码
function saveUpdatePsw(){
	var userId = $("#userId").val();
	if($("#resetPassWordForm").valid()){
		var newPassword = $("#newPassword").val();
		$.ajax({
			url : "${ctx}/custom/updatePassword",
			type : 'POST',
			data : {
					"userId":userId,
					"newPassword":newPassword
				},
			success : function(data) {
				myFnShutDown('#popup1');
				if (data == "1000") {
					alert("密码修改成功！");
				} else {
					alert("密码修改失败！");
				}
			}
		});
	}
}

//停用
function freezeInsUser(userId){
	$("#userId").val(userId);
	$("#userFlag").val("stopUse");
	myFnPopup('#popup','停用后该用户名将无法登录，确认停用用户？');
}

//启用
function unfreezeInsUser(userId){
	$("#userId").val(userId);
	$("#userFlag").val("startUse");
	myFnPopup('#popup','确认启用用户？');
}

//解锁
function unlockInsUser(userId){
	$("#userId").val(userId);
	$("#userFlag").val("unlock");
	myFnPopup('#popup','确认解锁用户？');
}

//删除
function deleteInsUser(userId){
	$("#userId").val(userId);
	$("#userFlag").val("delete");
	myFnPopup('#popup','删除用户后不可恢复，确认删除用户？');
}

//停用或者启用或者解锁或者删除
function updateUseSatate(){
	var flag = $("#userFlag").val();
	var userId = $("#userId").val();
	var href = "${ctx}/custom/toInsUserManagePage?id="+$("#insId").val();
	if("stopUse" == flag){
		$.ajax({
			url : "${ctx}/custom/stopUseInsUser",
			type : 'POST',
			data : {
					"id":userId
				},
			success : function(data) {
				myFnShutDown('#popup');
				if (data == "1000" || 1000 == data) {
					alert("停用成功!",href);
				} else {
					alert("停用失败!",href);
				}
			}
		});
	}else if("startUse" == flag){
		$.ajax({
			url : "${ctx}/custom/startUseInsUser",
			type : 'POST',
			data : {
					"id":userId
				},
			success : function(data) {
				myFnShutDown('#popup');
				if (data == "1000" || 1000 == data) {
					alert("启用成功!",href);
				} else {
					alert("启用失败!",href);
				}
			}
		});
	}else if("unlock" == flag){
		$.ajax({
			url : "${ctx}/custom/unLockInsUser",
			type : 'POST',
			data : {
					"id":userId
				},
			success : function(data) {
				myFnShutDown('#popup');
				if (data == "1000" || 1000 == data) {
					alert("解锁成功!",href);
				} else {
					alert("解锁失败!",href);
				}
			}
		});
	}else if("delete" == flag){
		$.ajax({
			url : "${ctx}/custom/deleteInsUser",
			type : 'POST',
			data : {
					"id":userId
				},
			success : function(data) {
				myFnShutDown('#popup');
				if (data == "1000" || 1000 == data) {
					alert("删除成功!",href);
				} else {
					alert("删除失败!",href);
				}
			}
		});
	}
}

//打开弹窗
function myFnPopup(obj,hit){
	$('#layer').show();
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
function myFnShutDown(obj){
	$('#layer').hide();
	$(obj).hide();
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
            </h3>
            <div class="backtrack">
            	<a href="${ctx}/custom/insAccountManager">返回</a>
            </div>
            <div class="intoPackage_title shade">
                <h3 class="fl intoPackage_h3">
                    <span>机构名称</span>
                    <strong>${institution.name }</strong>
                </h3>
                <h3 class="fl">
                    <span>办公电话</span>
                    <strong>${institution.officePhoneAreacode }-${institution.officePhone }</strong>
                </h3>
                <h3 class="fl userUp">
                    <span>用户账号上限</span>
                    <strong id="saveUserNum">${userNum }</strong>
                    <a href="javaScript:;" class="alter_icon" onClick="fnShowUser()"></a>
                    <div class="alter_user">
                    	<span>用户账号上限</span>
                    	<input type="hidden"  id="hasUserNum" value="${hasUserNum}">
                    	<input type="hidden"  id="userNumLimit" value="${userNum}">
                        <input type="text" style="line-height:0px;" id="userNumLimitText" value="${userNum }" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'0')}else{this.value=this.value.replace(/\D/g,'')}"/>
                        <a href="javaScript:;" onClick="saveUserNum()">保存</a>
                        <a href="javaScript:;" onClick="fnHideUser()">取消</a>
                    </div>
                </h3>

                <a href="javaScript:addInsUser();" class="grade_btn fr">创建用户</a>
            </div>
            <div class="shade main_mar">
            	<div class="module_height main_paddTop">
                	<form>
                		<input type="hidden" id="insId"  value="${insId }"/>
                		<input type="hidden" id="currentPage" name="currentPage" value="1"/>
                    	<table class="module_table">
                        	<thead>
                            	<tr>
                                    <th class="module_width1">序号</th>
                                    <th>用户名</th>
                                    <th>姓名</th>
                                    <th>角色</th>
                                    <th>创建时间</th>
                                    <th>用户状态</th>
                                    <th>创建平台</th>
                                    <th class="module_width4">操作</th>
                                </tr>
                            </thead>
                            <tbody id="htmlContent">
                            	
                            	
                            </tbody>
                        </table>
                    </form>
                </div>
                <!-- 分页.html start -->
                <%@include file="../commons/tabPage.jsp" %>
                <!-- 分页.html end -->
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
<!-- 重置密码 start -->
<div class="popup popup3" id="popup1">
	<a href="javaScript:;" class="close"></a>
    <div class="popup_tab_box">
    	<form id="resetPassWordForm">
    	<input type="hidden" id="userId">
        	<table class="popup_tab">
            	<tbody>
                	<tr>
                    	<td class="td1"><strong>用户名</strong></td>
                        <td><span id="loginNameFn"></span></td>
                    </tr>
                    <tr>
                    	<td class="td1"><strong>姓名</strong></td>
                        <td><span id="nameFn"></span></td>
                    </tr>
                    <tr>
                    	<td class="td1"><strong>新密码</strong></td>
                        <td><input type="password" class="popup_tab_w" id="newPassword" name="newPassword" placeholder="请输入新密码" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                        <div class="error_info" id="newPassword_error"></div>
                        </td>
                    </tr>
                    <tr>
                    	<td class="td1"><strong>确认密码</strong></td>
                        <td><input type="password" class="popup_tab_w" id="newPassword2" name="newPassword2" placeholder="请确认密码" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                        <div class="error_info" id="newPassword2_error"></div>
                        </td>
                    </tr>
                    <tr>
                    	<td class="td1"></td>
                        <td>
                        	<div class="popup_btn">
                                <a href="javaScript:;" onclick="saveUpdatePsw()" class="a1 fl">确定</a>
                                <a href="javaScript:;" class="a2 fr">取消</a>
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
<div class="popup popup2" id="popup">
	<a href="javaScript:;" class="close"></a>
	<input type="hidden" id="userFlag">
    <p></p>
    <div class="popup_btn">
    	<a href="javaScript:updateUseSatate();" class="a1 fl">确定</a>
        <a href="javaScript:;" class="a2 fr">取消</a>
    </div>
</div>
<!-- 启用停用删除 end -->
</body>
</html>
