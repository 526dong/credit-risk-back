<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!doctype html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>系统管理-用户管理</title>
<link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!-- <script type="text/javascript" src="js/jquery-1.12.4.js"></script> -->
<script type="text/javascript" src="${ctx}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>


<script type="text/javascript">
/*页面初始化*/
$(function() {
	$("#userRole_select_box").on("click","p",function(){
		$("#roleId").val($(this).attr("data-id"));
	})
	$("#userState_select_box").on("click","p",function(){
		$("#userState").val($(this).attr("data-id"));
	})
	
	$("#currentPage").val(1);
	/*列表数据*/
	findSysUserList();
	
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


/*列表数据*/
function findSysUserList() {
    $.ajax({
        url : "${ctx}/user/findAll",
        type : 'POST',
        data : {
        	"pageSize":10,//每页展示数
        	"currentPage": $("#currentPage").val(),//当前页
            "keyWord": $("#keyWord").val(),//关键字搜索
            "roleId": $("#roleId").val(),//角色id
            "userState": $("#userState").val()//用户状态
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

//创建机构列表
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
        var phone = data[i].phone;
        if(null==phone||typeof(phone)=="undefined"||""==phone){
        	phone = "";
        }
        var email = data[i].email;
        if(null==email||typeof(email)=="undefined"||""==email){
        	email = "";
        }else{
        	if(email.length>11){
        		email = email.substring(0,11)+"...";
        	}
        }
        var createTime = data[i].createTime;
        if(null==createTime||typeof(createTime)=="undefined"||""==createTime){
        	createTime = "";
        }
        var state = data[i].status;
        var res = "";
        if(state == 0){
        	res="使用中";
        }else if(state == 1){
        	res="停用";
        }else{
        	res="锁定";
        }
        htmlContent += "<tr>";
        htmlContent += "<td>" + (parseInt(i) + 1)+ "</td>";
        htmlContent += "<td>" + loginName + "</td>";
        htmlContent += "<td>" + name + "</td>";
        htmlContent += "<td>" + roleName + "</td>";
        htmlContent += "<td>" + phone + "</td>";
        htmlContent += "<td title='"+data[i].email+"'>" + email + "</td>";
        htmlContent += "<td>" + createTime + "</td>";
        htmlContent += "<td>" + res + "</td>";
        htmlContent += "<td class='operate' >";
        htmlContent += "<shiro:hasPermission name='/user/toLookDetailSysUserPage'>";
        htmlContent += "<a style='margin-left: 8px;' href='javaScript:;' onclick=lookDetailSysUser('"+data[i].id+"') >查看</a>";
        htmlContent += "</shiro:hasPermission>";
        if(state == 1){
        	htmlContent += "<shiro:hasPermission name='/user/toEditSysUserPage'>";
        	htmlContent += "<a style='margin-left: 8px;' href='javaScript:;' onclick=editSysUser('"+data[i].id+"') >修改</a>";
        	htmlContent += "</shiro:hasPermission>";
		}
		htmlContent += "<shiro:hasPermission name='/user/updateUserPwd'>";
        htmlContent += "<a style='margin-left: 8px;' href='javaScript:;' onclick=resetSysUserPassWord('"+data[i].id+"','"+data[i].loginName+"','"+data[i].name+"') >重置密码</a>";
        htmlContent += "</shiro:hasPermission>";
        htmlContent += "<shiro:hasPermission name='/user/stopUseSysUser'>";
       if(state == 0){
    	   htmlContent += "<a style='margin-left: 8px;' href='javaScript:;' onclick=stopUseSysUse('"+data[i].id+"') >停用</a>";
       }else if(state == 1){
    	   htmlContent += "<a style='margin-left: 8px;' href='javaScript:;' onclick=startUseSysUse('"+data[i].id+"','"+data[i].loginName+"') >启用</a>";
	   	}else if(state == 2){
	   		htmlContent += "<a href='javaScript:;' onclick=unlockSysUse('"+data[i].id+"','"+data[i].loginName+"') >解锁</a>";
	   	}
	   	htmlContent += "</shiro:hasPermission>";
        htmlContent += "<a style='margin-left: 8px;' href='javaScript:;' onclick=deleteSysUse('"+data[i].id+"') >删除</a>";
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
	findSysUserList();
}

//查询操作
function searchSysUserList(){
	//查询
	findSysUserList();
}

//新增用户
function addSysUser(){
	window.location.href = "${ctx}/user/toAddSysUserPage";
}

//查看详细
function lookDetailSysUser(userId){
	window.location.href = "${ctx}/user/toLookDetailSysUserPage?userId="+userId;
}

//修改用户信息
function editSysUser(userId){
	window.location.href = "${ctx}/user/toEditSysUserPage?userId="+userId;
}

//重置密码
function resetSysUserPassWord(userId,loginName,name){
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
			url : "${ctx}/user/updateUserPwd",
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

//停用用户
function stopUseSysUse(userId){
	$("#userId").val(userId);
	$("#userFlag").val("stopUse");
	myFnPopup('#popup','停用后该用户名将无法使用任何功能，确认停用？');
}

//启用用户
function startUseSysUse(userId,loginName){
	$("#userId").val(userId);
	$("#userFlag").val("startUse");
	var msg = "是否确认启用用户:"+loginName+"?"
	myFnPopup('#popup',msg);
}

//解锁用户
function unlockSysUse(userId,loginName){
	$("#userId").val(userId);
	$("#userFlag").val("unlock");
	$("#loginName").val(loginName);
	myFnPopup('#popup','确认解锁用户？');
}

//删除用户
function deleteSysUse(userId){
	$("#userId").val(userId);
	$("#userFlag").val("delete");
	myFnPopup('#popup','删除用户后不可恢复，确认删除用户？');
}

//停用/启用/解锁/删除
function updateUseSatate(){
	var flag = $("#userFlag").val();
	var userId = $("#userId").val();
	var loginName = $("#loginName").val();
	var href = "${ctx}/user/manager";
	if("stopUse" == flag){
		$.ajax({
			url : "${ctx}/user/stopUseSysUser",
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
			url : "${ctx}/user/startUseSysUser",
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
			url : "${ctx}/user/unLockSysUser",
			type : 'POST',
			data : {
					"id":userId
				},
			success : function(data) {
				myFnShutDown('#popup');
				if (data == "1000" || 1000 == data) {
					alert("用户&nbsp;"+loginName+"&nbsp;已解锁!",href);
				} else {
					alert("用户&nbsp;"+loginName+"&nbsp;解锁失败!",href);
				}
			}
		});
	}else if("delete" == flag){
		$.ajax({
			url : "${ctx}/user/deleteSysUser",
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
	<%@ include file="../../commons/topHead.jsp"%>
	<!--页面头部 end -->
    <!-- center.html start -->
    <div class="main_center">
    	<!--页面左侧导航栏 start -->
		<%@ include file="../../commons/leftNavigation.jsp"%>
		<!-- 页面左侧导航栏 end -->
		
        <!-- 右侧内容.html start -->
        <div class="right_content">
            <h3 class="place_title">
                <span>当前位置：</span>
                <a href="javascript:void(0);">系统管理</a>
                <span>></span>
                <a href="${ctx}/user/manager">用户管理</a>
            </h3>
            <div class="shade">
				<div class="container-fluid">
                	<div class="row">
                    	<div class="col-lg-4 col-sm-4 col-md-4">
                            <div class="search_son search_son1">
                                <strong style="padding-left:22px;">用户状态</strong>
                                <div class="down_menu">
                                    <div class="select_btn">
                                    <input type="hidden" id="userState">
                                        <span data-id="0000">用户状态-全部</span>
                                    </div>
                                    <div class="select_list" id="userState_select_box">
                                    	<p data-id="0000">用户状态-全部</p>
                                        <p data-id="0">用户状态-使用中</p>
                                        <p data-id="1">用户状态-停用</p>
                                        <p data-id="2">用户状态-锁定</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-4 col-sm-4 col-md-4">
                            <div class="search_son search_son1 search_align">
                                <strong>角色</strong>
                                <div class="down_menu">
                                    <div class="select_btn">
                                    	<input type="hidden" id="roleId">
                                        <span data-id="0000">全部</span>
                                    </div>
                                    <div class="select_list" id="userRole_select_box">
                                        <p data-id="0000">全部</p>
                                        <c:forEach var="item" items="${roleBgList }">
											<p data-id="${item.id }">${item.name }</p>
										</c:forEach>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-4 col-sm-4 col-md-4">
                        	<div class="search_btn search_btn1 fr">
                                <a href="javaScript:searchSysUserList();" class="fr">查询</a>
                                <input class="fr" type="text" id="keyWord" placeholder="请输入关键字：用户名/姓名">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="shade main_mar">
            	<div class="clear systemManage">
            		<a href="javaScript:addSysUser();" class="grade_btn fr">创建用户</a>
                </div>
                <div class="module_height" id="tableContent">
                	<form>
                		<input type="hidden" id="currentPage" name="currentPage" value="1"/>
                    	<table class="module_table">
                        	<thead>
                            	<tr>
                                    <th class="module_width1">序号</th>
                                    <th>用户名</th>
                                    <th>姓名</th>
                                    <th>角色</th>
                                    <th>手机号码</th>
                                    <th>邮箱</th>
                                    <th>创建时间</th>
                                    <th>用户状态</th>
                                    <th class="module_width4">操作</th>
                                </tr>
                            </thead>
                            <tbody id="htmlContent">
                            	
                            	
                            </tbody>
                        </table>
                    </form>
                </div>
                <!-- 分页.html start -->
                <%@include file="../../commons/tabPage.jsp" %>
                <!-- 分页.html end -->
            </div>
            <!-- footer.html start -->
            <%@ include file="../../commons/foot.jsp"%>
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
	<input type="hidden" id="loginName">
    <p></p>
    <div class="popup_btn">
    	<a href="javaScript:updateUseSatate();" class="a1 fl">确定</a>
        <a href="javaScript:;" class="a2 fr">取消</a>
    </div>
</div>
<!-- 启用停用删除 end -->
</body>
</html>
