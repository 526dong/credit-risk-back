<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!doctype html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>客户管理-机构管理 </title>

<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!-- <script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script> -->
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>

<script type="text/javascript">
/*页面初始化*/
$(function() {
	$("#currentPage").val(1);
	/*列表数据*/
	findInsList();
})

/*列表数据*/
function findInsList() {
    $.ajax({
        url : "${ctx}/custom/findAllIns",
        type : 'POST',
        data : {
        	"pageSize":10,//每页展示数
        	"currentPage": $.trim($("#currentPage").val()),//当前页
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
        if(null==name||typeof(name)=="undefined"||""==name){
        	name = "";
        }else{
        	if(name.length>8){
        		name = name.substring(0,8)+"...";
        	}
        }
        var organizationCode = data[i].organizationCode;
        if(null==organizationCode||typeof(organizationCode)=="undefined"||""==organizationCode){
        	organizationCode = "";
        }else{
        	if(organizationCode.length>15){
        		organizationCode = organizationCode.substring(0,15)+"...";
        	}
        }
        var creater = data[i].creater;
        if(null==creater||typeof(creater)=="undefined"||""==creater){
        	creater = "";
        }
        var createtime = data[i].createtimeStr;
        if(null==createtime||typeof(createtime)=="undefined"||""==createtime){
        	createtime = "";
        }
        var state = data[i].state;
        var res = "";
       	var examinestate = data[i].examinestate;
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
        htmlContent += "<td title='"+data[i].organizationCode+"'>" + organizationCode + "</td>";
        htmlContent += "<td>" + creater + "</td>";
        htmlContent += "<td>" + createtime + "</td>";
        htmlContent += "<td>" + res + "</td>";
        htmlContent += "<td class='operate' >";
        var examinestate1 = data[i].examinestate;
        htmlContent += "<shiro:hasPermission name='/custom/insUpdate'>";

       	if(examinestate1==1 || examinestate1==2){
           	htmlContent += "<a href='javaScript:;' onclick=editIns('"+data[i].id+"') >修改</a>";
        }else{
        	htmlContent += "<a href='javaScript:;' style='cursor:default;' >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>";
        }
         htmlContent += "</shiro:hasPermission>";
         htmlContent += "<shiro:hasPermission name='/custom/examineIns'>";
       	if(examinestate1==1){
    		htmlContent += "<a href='javaScript:;' onclick=examineIns('"+data[i].id+"') >审核</a>";
        }else{
        	htmlContent += "<a href='javaScript:;' style='cursor:default;'  >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>";
        }
         htmlContent += "</shiro:hasPermission>";
        htmlContent += "<shiro:hasPermission name='/custom/lookDetailIns'>";
       	htmlContent += "<a href='javaScript:;' onclick=lookDetailIns('"+data[i].id+"') >查看</a>";
        htmlContent += "</shiro:hasPermission>";
            htmlContent += "<shiro:hasPermission name='/custom/csutomExamine'>";
    	if(examinestate1==1 || examinestate1==2){
           	htmlContent += "<a href='javaScript:;' onclick=deleteIns('"+data[i].id+"') >删除</a>";
        }
        htmlContent += "</shiro:hasPermission>";
//         if(examinestate1==2){
//         	htmlContent += "<a href='javaScript:;' onclick=editIns('"+data[i].id+"') >修改</a>";
//         }
//        if(state == 2){
//     	   htmlContent += "<a href='javaScript:;' onclick=startUseIns('"+data[i].id+"') >启用</a>";
//        }else{
// 	    	if(examinestate1==1){
// 	    		htmlContent += "<a href='javaScript:;' onclick=examineIns('"+data[i].id+"') >审核</a>";
// 	        }else if(examinestate1==2){
// 	        	operate += '<button type="button" class="btn btn-sm btn-info examineIns");">审核</button>&nbsp;';
// 	        	htmlContent += "<a href='javaScript:;' onclick=examineIns('"+data[i].id+"') >审核</a>";
// 	        }else if(examinestate1==3){
// 	        	htmlContent += "<a href='javaScript:;' onclick=stopUseIns('"+data[i].id+"') >停用</a>";
// 	        }
// 		}
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
	findInsList();
}

function searchInsList(){
	//查询
	findInsList();
}

//修改机构
function editIns(id) {
    window.location.href = "${ctx}/custom/insUpdate?id=" + id;
}
//查看机构
function lookDetailIns(id){
	window.location.href = "${ctx}/custom/lookDetailIns?id=" + id;
}
//删除该机构
function deleteIns(id){
	var state = 2;
	confirm("确认删除该机构？",function(){
		$.ajax({
    		url: "${ctx}/custom/csutomExamine",
	        data: JSON.stringify(
	        	{
	        		"id":id,
	        		"state":state
	        	}
	        ),
	        dataType : 'json',
			contentType: 'application/json', 
		    type: 'post',
		    async : true,
			success : function(data) {
				var data = eval(data);
				if (data=="1000") {
					alert("机构删除成功！","${ctx}/custom/insManage");
				}else{
					alert("机构删除失败！","${ctx}/custom/insManage");
				}
			}
		});
	});
}
//停用该机构
function stopUseIns(id){
	var state = 2;
	confirm("确认停用该机构？",function(){
		$.ajax({
    		url: "${ctx}/custom/csutomExamine",
	        data: JSON.stringify(
	        	{
	        		"id":id,
	        		"state":state
	        	}
	        ),
	        dataType : 'json',
			contentType: 'application/json', 
		    type: 'post',
		    async : true,
			success : function(data) {
				var data = eval(data);
				if (data=="1000") {
					alert("机构停用成功！","${ctx}/custom/insManage");
				}else{
					alert("机构停用失败！","${ctx}/custom/insManage");
				}
			}
		});
	});
}
//启用该机构
function startUseIns(id){
	var state = 1;
	confirm("确认启用该机构？",function(){
		$.ajax({
    		url: "${ctx}/custom/csutomExamine",
	        data: JSON.stringify(
	        	{
	        		"id":id,
	        		"state":state
	        	}
	        ),
	        dataType : 'json',
			contentType: 'application/json', 
		    type: 'post',
		    async : true,
			success : function(data) {
				var data = eval(data);
				if (data=="1000") {
					alert("机构启用成功！","${ctx}/custom/insManage");
				}else{
					alert("机构启用失败！","${ctx}/custom/insManage");
				}
			}
		});
	});
}
//审核该机构
function examineIns(id){
	window.location.href = "${ctx}/custom/examineIns?id=" + id;
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
                <a href="${ctx}/custom/insManage">机构管理</a>
            </h3>
            <div class="shade main_minHeight">
            	<div class="search_box clear">
                	<div class="fl function_btn">
                        <shiro:hasPermission name='/custom/insAdd'>
                        <a href="${ctx}/custom/insAdd" class="fl">创建机构</a>
                        </shiro:hasPermission>
                        <shiro:hasPermission name='/custom/exportInsListExcel'>
                        <a href="${ctx}/custom/exportInsListExcel" class="fl">导出机构列表</a>
                        </shiro:hasPermission>
                        <!--
                          <a href="javaScript:;" class="fl">导出机构列表</a>
                        -->
                    </div>
                    <shiro:hasPermission name='/custom/insManage'>
                    <div class="search_btn fr">
                    	<a href="javaScript:searchInsList();" class="fr">查询</a>
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
                                    <th>代码标识</th>
                                    <th>创建人</th>
                                    <th>创建时间</th>
                                    <th>状态</th>
                                    <th class="module_width6">操作</th>
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
