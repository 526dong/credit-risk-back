<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!doctype html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>分层管理-分层层级管理</title>
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!-- <script type="text/javascript" src="js/jquery-1.12.4.js"></script> -->
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>


<script type="text/javascript">
/*页面初始化*/
$(function() {
	
	$("#currentPage").val(1);
	/*列表数据*/
	findLevelList();
	
})


/*列表数据*/
function findLevelList() {
    $.ajax({
        url : "${ctx}/hierarchyManage/findLevelList",
        type : 'POST',
        data : {
        	"pageSize":10,//每页展示数
        	"currentPage": $("#currentPage").val()//当前页
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
    	var layerName = data[i].layerName;
        if(null==layerName||typeof(layerName)=="undefined"||""==layerName){
        	layerName = "";
        }
        var securityType = data[i].securityType;
        var securityTypeStr = "优先级证券";
        if(null==securityType||typeof(securityType)=="undefined"||""==securityType){
        	securityTypeStr = "优先级证券";
        }else{
        	if(securityType == 0){
        		securityTypeStr = "优先级证券";
        	} else if(securityType == 1){
        		securityTypeStr = "次级证券";
        	}
        }
        var capitalRate = data[i].capitalRate;
        if(null==capitalRate||typeof(capitalRate)=="undefined"||""==capitalRate){
        	capitalRate = "";
        }
        var expectEarningsRate = data[i].expectEarningsRate;
        if(null==expectEarningsRate||typeof(expectEarningsRate)=="undefined"||""==expectEarningsRate){
        	expectEarningsRate = "";
        }
        var isFloat = data[i].isFloat;
        var isFloatStr = "";
        if(0 == isFloat){
    		isFloatStr = "是";
    	} else if(1 == isFloat){
    		isFloatStr = "否";
    	}else{
        	isFloatStr = "";
        }
        htmlContent += "<tr>";
        htmlContent += "<td>" + (parseInt(i) + 1)+ "</td>";
        htmlContent += "<td title='"+layerName+"'>" + layerName + "</td>";
        htmlContent += "<td title='"+securityTypeStr+"'>" + securityTypeStr + "</td>";
        htmlContent += "<td>" + capitalRate + "</td>";
        htmlContent += "<td>" + expectEarningsRate + "</td>";
        htmlContent += "<td>" + isFloatStr + "</td>";
        htmlContent += "<td class='operate' >";
        htmlContent += "<shiro:hasPermission name='/hierarchyManage/toUpdateHierarchyLevelPage'><a href='javaScript:;' onclick=levelEdit('"+data[i].id+"') >修改</a></shiro:hasPermission>";
        htmlContent += "<shiro:hasPermission name='/hierarchyManage/updateHierarchyLevel'><a href='javaScript:;' onclick=levelDelete('"+data[i].id+"') >删除</a></shiro:hasPermission>";
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
	findLevelList();
}

//修改分层层级
function levelEdit(levelId){
	window.location.href = "${ctx }/hierarchyManage/toUpdateHierarchyLevelPage?levelId="+levelId;
}

function levelDelete(levelId){
	$("#levelId").val(levelId);
	fnPopup('#popup','确定删除该层级？');
}


//删除分层层级
function deleteConfirm(){
	var levelId = $("#levelId").val();
	var href = "${ctx}/hierarchyManage/toHierarchyManagerPage";
	if(null!=levelId&&""!=levelId){
		$.ajax({
			url :"${ctx}/hierarchyManage/updateHierarchyLevel",
		    data:{"levelId":levelId},
		    datatype: 'json',
		    type: 'post',
		    async : true,
			success : function(data) {
				if (data=="1000") {
					findLevelList();
//	 				alert("删除成功！",href);
				}else{
					alert("删除失败！",href);
				}
			}
		});
	}else{
		alert("删除失败！",href);
	}
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
                <a href="javascript:void(0);">分层管理</a>
                <span>></span>
                <a href="${ctx}/hierarchyManage/toHierarchyManagerPage">分层层级管理</a>
            </h3>
            <div class="shade main_minHeight">
				<div class="search_box clear"></div>
                <div class="module_height" id="tableContent">
                	<form>
                		<input type="hidden" id="currentPage" name="currentPage" value="1"/>
                    	<table class="module_table">
                        	<thead>
                            	<tr>
                                    <th class="module_width1">序号</th>
                                    <th>层级名称</th>
                                    <th>证券类型</th>
                                    <th>本金占比</th>
                                    <th>预期收益率</th>
                                    <th>利率是否可浮动</th>
                                    <th class="module_width2">操作</th>
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
<!-- 启用停用 start -->
<div class="popup popup2" id="popup">
	<input type="hidden" id="levelId">
	<a href="javaScript:;" class="close"></a>
    <p></p>
    <div class="popup_btn">
    	<a href="javaScript:deleteConfirm();" class="a1 fl">确定</a>
        <a href="javaScript:;" class="a2 fr">取消</a>
    </div>
</div>
<!-- 启用停用 end -->
</body>
</html>
