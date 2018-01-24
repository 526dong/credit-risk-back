<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!doctype html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>客户管理-账号管理-角色管理 </title>

<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!-- <script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script> -->
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>

<script type="text/javascript">
/*页面初始化*/
$(function() {
	$("#currentPage").val(1);
	/*列表数据*/
	findAllInsRoleList();
})

/*列表数据*/
function findAllInsRoleList() {
    $.ajax({
        url : "${ctx}/custom/findAllInsRoleList",
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
        var name = data[i].name;
        if(null==name||typeof(name)=="undefined"||""==name){
        	name = "";
        }
        var description = data[i].description;
        if(null==description||typeof(description)=="undefined"||""==description){
        	description = "";
        }else{
        	if(description.length>18){
        		description = description.substring(0,18)+"...";
        	}
        }
        var createTime = data[i].createTime;
        if(null==createTime||typeof(createTime)=="undefined"||""==createTime){
        	createTime = "";
        }
        htmlContent += "<tr>";
        htmlContent += "<td>" + (parseInt(i) + 1)+ "</td>";
        htmlContent += "<td>" + name + "</td>";
        htmlContent += "<td title='"+data[i].description+"'>" + description + "</td>";
        htmlContent += "<td>" + createTime + "</td>";
        htmlContent += "<td class='operate' >";
        htmlContent += "<shiro:hasPermission name='/custom/toInsRoleUpdatePage'><a href='javaScript:;' onclick=insRoleEdit('"+data[i].id+"') >修改</a></shiro:hasPermission>";
        htmlContent += "<shiro:hasPermission name='/custom/toPermissionAssignPage'><a href='javaScript:;' onclick=insRoleResource('"+data[i].id+"') >设置权限</a></shiro:hasPermission>";
        htmlContent += "<shiro:hasPermission name='/custom/deleteInsRole'><a href='javaScript:;' onclick=insRoleDelete('"+data[i].id+"') >删除</a></shiro:hasPermission>";
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
	findAllInsRoleList();
}

//创建角色
function addInsRole(){
	window.location.href="${ctx}/custom/toInsRoleAddPage?insId="+$("#insId").val();
}

//修改角色
function insRoleEdit(roleId){
	window.location.href="${ctx}/custom/toInsRoleUpdatePage?roleId="+roleId+"&&insId="+$("#insId").val();
}

//设置权限
function insRoleResource(roleId){
	window.location.href="${ctx}/custom/toPermissionAssignPage?roleId="+roleId+"&&insId="+$("#insId").val();
}

//删除角色
function insRoleDelete(roleId){
	$("#roleId").val(roleId);
	fnPopup("#popup","确认删除？");
}

function deleteInsRoleConfirm(){
	var roleId = $("#roleId").val();
	$.ajax({
		url :"${ctx}/custom/deleteInsRole",
	    data:JSON.stringify(
	    		{
    		    	"id":roleId
    		    }
	    	),
	    datatype: 'json',
	    contentType: 'application/json',
	    type: 'post',
	    cache: true,
	    async : true,
		success : function(data) {
			var href = "${ctx}/custom/toInsRoleManagePage?id="+$("#insId").val();
			if (data=="1000") {
				alert("删除成功！",href);
			}else if(data=="888"){
				alert("该角色下存在正常用户，无法删除！",href);
			}else{
				alert("删除失败！",href);
			}
		}
	});
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
                <a href="${ctx}/custom/toInsRoleManagePage?id=${insId }">角色管理</a>
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
                <shiro:hasPermission name='/custom/toInsRoleAddPage'>
                <a href="javaScript:addInsRole();" class="grade_btn fr">创建角色</a>
                </shiro:hasPermission>
            </div>
            <div class="shade main_mar main_paddTop">
            	<div class="module_height">
                	<form>
                		<input type="hidden" id="insId"  value="${insId }"/>
                		<input type="hidden" id="currentPage" name="currentPage" value="1"/>
                    	<table class="module_table">
                        	<thead>
                            	<tr>
                                    <th class="module_width1">序号</th>
                                    <th>角色名称</th>
                                    <th>角色描述</th>
                                    <th>创建时间</th>
                                    <th class="module_width5">操作</th>
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
<!-- 启用停用 start -->
<div class="popup popup2" id="popup">
	<input type="hidden" id="roleId">
	<a href="javaScript:;" class="close"></a>
    <p></p>
    <div class="popup_btn">
    	<a href="javaScript:deleteInsRoleConfirm();" class="a1 fl">确定</a>
        <a href="javaScript:;" class="a2 fr">取消</a>
    </div>
</div>
<!-- 启用停用 end -->
</body>
</html>
