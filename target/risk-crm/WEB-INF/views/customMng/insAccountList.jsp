<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!doctype html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>客户管理-账号管理 </title>

<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!-- <script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script> -->
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>

<script type="text/javascript">
/*页面初始化*/
$(function() {
	$("#currentPage").val(1);
	/*列表数据*/
	findAllInsAccountList();
})

/*列表数据*/
function findAllInsAccountList() {
    $.ajax({
        url : "${ctx}/custom/findAllInsAccountList",
        type : 'POST',
        data : {
        	"pageSize":10,//每页展示数
        	"currentPage": $("#currentPage").val(),//当前页
            "keyWord": $("#keyWord").val()//关键字搜索
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
    	var name = data[i].name;
    	var nameStr = "";
        if(null==name||typeof(name)=="undefined"||""==name){
        	name = "";
        }else{
        	if(name.length>24){
        		nameStr = description.substring(0,21)+"...";
        	}else{
        		nameStr = name;
        	}
        }
        var creater = data[i].creater;
        if(null==creater||typeof(creater)=="undefined"||""==creater){
        	creater = "";
        }
        var examinestate = data[i].examinestate;
    	var res="使用中"; 
        if(examinestate==1){
    		res="待审核"; 
        }else if(examinestate==2){
    		res="拒绝"; 
        }else if(examinestate==3){
    		res="使用中"; 
        }
        htmlContent += "<tr>";
        htmlContent += "<td>" + (parseInt(i) + 1)+ "</td>";
        htmlContent += "<td title='"+data[i].name+"'>" + name + "</td>";
        htmlContent += "<td>" + creater + "</td>";
        htmlContent += "<td>" + res + "</td>";
        htmlContent += "<td class='operate' >";
        htmlContent += "<shiro:hasPermission name='/custom/toModuleAllocationPage'><a href='javaScript:;' onclick=moduleAllocationIns('"+data[i].id+"') >模块分配</a></shiro:hasPermission>";
        htmlContent += "<shiro:hasPermission name='/custom/toInsUserManagePage'><a href='javaScript:;' onclick=insUserManage('"+data[i].id+"') >用户管理</a></shiro:hasPermission>";
        htmlContent += "<shiro:hasPermission name='/custom/toInsRoleManagePage'><a href='javaScript:;' onclick=insRoleManage('"+data[i].id+"') >角色管理</a></shiro:hasPermission>";
        htmlContent += "<shiro:hasPermission name='/custom/toInsIPManagePage'><a href='javaScript:;' onclick=insIPManage('"+data[i].id+"') >IP管理</a></shiro:hasPermission>";
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
	findAllInsAccountList();
}

function searchAccountList(){
	//查询
	findAllInsAccountList();
}

//模块分配
function moduleAllocationIns(id){
	window.location.href = "${ctx}/custom/toModuleAllocationPage?id=" + id;
}
//用户管理、
function insUserManage(id){
	if(checkIsHaveModule(id)){
		$.ajax({
	        url : "${ctx}/custom/checkInsRoleListIsNotHave",
	        type : 'POST',
	        data : {
	            "insId": id //机构id
	        },
	        async : false,
	        success : function(data) { 
	        	if(null !=data && typeof(data) != "undefined" && "" != data && "0000"==data){
	        		window.location.href = "${ctx}/custom/toInsUserManagePage?id="+id;
	        	}else{
	        		alert("该机构尚未创建角色，请先进入角色管理创建角色后再进入用户管理");
	        	}
	        }
	    });
	}else{
		alert("该机构尚未分配模块，请先分配模块后再进入用户管理");
	}
}

//角色管理
function insRoleManage(id){
	if(checkIsHaveModule(id)){
		window.location.href = "${ctx}/custom/toInsRoleManagePage?id="+id;
	}else{
		alert("该机构尚未分配模块，请先分配模块后再进入角色管理");
	}
}

//IP管理
function insIPManage(id){
	if(checkIsHaveModule(id)){
		window.location.href = "${ctx}/custom/toInsIPManagePage?id="+id;
	}else{
		alert("该机构尚未分配模块，请先分配模块后再进入IP管理");
	}
}

//查询该该机构是否已分配有模块，若未分配，则先进性模块分配才能进行用户管理和角色管理和ip管理
function checkIsHaveModule(insId){
	var moduleFlag = false;
	$.ajax({
        url : "${ctx}/custom/checkIsHaveModule",
        type : 'POST',
        data : {
            "insId": insId //机构id
        },
        async : false,
        success : function(data) { 
        	if(null !=data && typeof(data) != "undefined" && "" != data && "0000"==data){
        		//已有分配模块时，true；否则false
        		moduleFlag = true;
        	}
        }
    });
	return moduleFlag;
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
            </h3>
            <div class="shade main_minHeight">
            	<div class="search_box clear">
                        <shiro:hasPermission name='/custom/insAccountManager'>
	                    <div class="search_btn fr">
	                    	<a href="javaScript:searchAccountList();" class="fr">查询</a>
	                    	<input type="text" class="fr" id="keyWord" placeholder="机构名称/创建人"/>
	                    </div>
                        </shiro:hasPermission>
                </div>
                <div class="module_height" id="tableContent">
                	<form>
                		<input type="hidden" id="currentPage" name="currentPage" value="1"/>
                    	<table class="module_table">
                        	<thead>
                            	<tr>
                                    <th class="module_width1">序号</th>
                                    <th>机构名称</th>
                                    <th>负责人</th>
                                    <th>机构状态</th>
                                    <th class="module_width3">操作</th>
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
<!-- 启用停用 start -->
<div class="popup popup2" id="popup">
	<a href="javaScript:;" class="close"></a>
    <p>错误提示</p>
    <div class="popup_btn">
    	<a href="javaScript:;" class="a1 fl">确定</a>
        <a href="javaScript:;" class="a2 fr">取消</a>
    </div>
</div>
<!-- 启用停用 end -->
</body>
</html>
