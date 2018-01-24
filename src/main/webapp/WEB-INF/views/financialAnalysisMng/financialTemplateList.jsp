<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!doctype html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>业务管理-财务分析管理</title>
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!-- <script type="text/javascript" src="js/jquery-1.12.4.js"></script> -->
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>


<script type="text/javascript">
/*页面初始化*/
$(function() {
	
	$("#currentPage").val(1);
	/*列表数据*/
	getFinancialTemplateList();
	
	var historyTypeTemplateList = ${historyTypeTemplateList};
	if(null != historyTypeTemplateList && historyTypeTemplateList.length>0){
		var warnMsghtml = "";
		for (var i = 0; i < historyTypeTemplateList.length; i++) {
			var reportTypeName = historyTypeTemplateList[i].reportTypeName;
			if(null==reportTypeName||typeof(reportTypeName)=="undefined"||""==reportTypeName){
				reportTypeName = "";
  	        }else{
  	        	if(reportTypeName.length>25){
  	        		reportTypeName = reportTypeName.substring(0,25)+"...";
  	        	}
  	        }
  			warnMsghtml += "<p title='"+historyTypeTemplateList[i].reportTypeName+"'>"+(i+1)+".&nbsp;&nbsp;&nbsp;&nbsp;"+reportTypeName+"；</p>";
		}
		$("#popup_list").html(warnMsghtml);
		myFnPopup('#popupIndexWarnMsg');
	}
})




/*列表数据*/
function getFinancialTemplateList() {
    $.ajax({
        url : "${ctx}/financialAnaly/getFinancialTemplateList",
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

//创建财务模板列表
function createTable(data) {
    var htmlContent = "";
    for (var i = 0; i < data.length; i++) {
    	var name = data[i].name;
        if(null==name||typeof(name)=="undefined"||""==name){
        	name = "";
        }else{
        	if(name.length>18){
        		name = name.substring(0,18)+"...";
        	}
        }
        
        var reportTypeName = data[i].reportTypeName;
        var reportTypeNameStr = data[i].reportTypeName;
        if(null==reportTypeName||typeof(reportTypeName)=="undefined"||""==reportTypeName){
        	reportTypeName = "未配置";
        	reportTypeNameStr = "未配置";
        }else{
        	if(reportTypeName.length>18){
        		reportTypeName = reportTypeName.substring(0,18)+"...";
        	}
        }
        
        var state = data[i].state;
        var res = "";
        if(state == 0){
        	res="禁用";
        }else if(state == 1){
        	res="启用";
        }
        
        var creater = data[i].creater;
        if(null==creater||typeof(creater)=="undefined"||""==creater){
        	creater = "";
        }
        
        var createTime = data[i].createTimeStr;
        if(null==createTime||typeof(createTime)=="undefined"||""==createTime){
        	createTime = "";
        }
        htmlContent += "<tr>";
        htmlContent += "<td>" + (parseInt(i) + 1)+ "</td>";
        htmlContent += "<td title='"+data[i].name+"'>" + name + "</td>";
        htmlContent += "<td title='"+reportTypeNameStr+"'>" + reportTypeName + "</td>";
        htmlContent += "<td>" + res + "</td>";
        htmlContent += "<td>" + creater + "</td>";
        htmlContent += "<td>" + createTime + "</td>";
        htmlContent += "<td class='operate' >";
        htmlContent += "<shiro:hasPermission name='/financialAnaly/toFinancialTemplateDetail'><a href='javaScript:;' onclick=lookFinancial('"+data[i].id+"') >查看</a></shiro:hasPermission>";
        htmlContent += "<shiro:hasPermission name='/financialAnaly/toUpdateFinancialTemplate'><a href='javaScript:;' onclick=updateFinancial('"+data[i].id+"') >修改</a></shiro:hasPermission>";
        if(state == 0){
        	htmlContent += "<shiro:hasPermission name='/financialAnaly/updateFinancialStates'><a href='javaScript:;' onclick=startUseFinancial('"+data[i].id+"') >启用</a></shiro:hasPermission>";
        	htmlContent += "<shiro:hasPermission name='/financialAnaly/updateFinancialStates'><a href='javaScript:;' onclick=deleteFinancial('"+data[i].id+"') >删除</a></shiro:hasPermission>";
        }else if(state == 1){
        	htmlContent += "<shiro:hasPermission name='/financialAnaly/updateFinancialStates'><a href='javaScript:;' onclick=stopUseFinancial('"+data[i].id+"') >禁用</a></shiro:hasPermission>";
        }
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
	getFinancialTemplateList();
}

//跳转新增页面
function addFinancialTemplate(){
	window.location.href = "${ctx }/financialAnaly/toImportFinancialTemplate";
}
//跳转修改页面
function updateFinancial(financialId){
	window.location.href = "${ctx }/financialAnaly/toUpdateFinancialTemplate?financialId="+financialId;
}

//查看 进入查看页面  这里直接下载
function lookFinancial(financialId){
	window.location.href = "${ctx}/financialAnaly/toFinancialTemplateDetail?financialId="+financialId;
// 	window.location.href = "${ctx}/financialAnaly/exportFinancialTemplate?financialId="+financialId;
}
//启用
function startUseFinancial(financialId){
	$("#financialId").val(financialId);
	$("#financialFlag").val("startUse");
	var msg = "启用？"
	myFnPopup('#popup',msg);
}
//禁用
function stopUseFinancial(financialId){
	$("#financialId").val(financialId);
	$("#financialFlag").val("stopUse");
	var msg = "禁用？"
	myFnPopup('#popup',msg);
}
//删除
function deleteFinancial(financialId){
	$("#financialId").val(financialId);
	$("#financialFlag").val("delete");
	var msg = "删除？"
	myFnPopup('#popup',msg);
}
//启用/删除/禁用操作
function updateSatate(){
	var financialId = $("#financialId").val();
	var financialFlag = $("#financialFlag").val();
	var msg = "";
	if("startUse" == financialFlag){
		msg = "启用"
	}else if("stopUse" == financialFlag){
		msg = "禁用"
	}else if("delete" == financialFlag){
		msg = "删除"
	}
	var href = "${ctx}/financialAnaly/financialTemplateList";
	$.ajax({
		url : "${ctx}/financialAnaly/updateFinancialStates",
		type : 'POST',
		data : {
				"financialId":financialId,
				"financialFlag":financialFlag
			},
		success : function(data) {
			myFnShutDown('#popup');
			if (data == "1000" || 1000 == data) {
				alert(msg+"成功!",href);
			} else {
				alert(msg+"失败!",href);
			}
		}
	});
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
                <a href="javascript:void(0);">业务管理</a>
                <span>></span>
                <a href="${ctx}/financialAnaly/financialTemplateList">财务分析管理</a>
            </h3>
            <div class="shade main_minHeight">
                <div class="search_box clear">
                    <div class="fr function_btn">
                    	<shiro:hasPermission name='/financialAnaly/toImportFinancialTemplate'>
                        <a href="javaScript:addFinancialTemplate();" class="fl database_btn" style="width: 120px;">新增财务分析模板</a>
                        </shiro:hasPermission>
                    </div>
                </div>
                <div class="module_height" id="tableContent">
                	<form>
                		<input type="hidden" id="currentPage" name="currentPage" value="1"/>
                    	<table class="module_table">
                        	<thead>
                            	<tr>
                                    <th class="module_width1">序号</th>
                                    <th>模板名称</th>
                                    <th>匹配报表类型</th>
                                    <th class="module_width1">状态</th>
                                    <th>创建人</th>
                                    <th>创建时间</th>
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
<!-- 启用停用删除 start -->
<div class="popup popup2" id="popup">
	<a href="javaScript:;" class="close"></a>
	<input type="hidden" id="financialId">
	<input type="hidden" id="financialFlag">
    <p></p>
    <div class="popup_btn">
    	<a href="javaScript:updateSatate();" class="a1 fl">确定</a>
        <a href="javaScript:;" class="a2 fr">取消</a>
    </div>
</div>
<!-- 启用停用删除 end -->

<div class="layer" id="layerIndexWarnMsg"></div>
<div class="popup popup3 popup7" id="popupIndexWarnMsg">
	<a href="javaScript:;" class="close"></a>
    <h2>变更提醒</h2>
    <h3>以下报表类型已改变，请重新创建财务分析模板：</h3>
    <div class="popup_par" id="popup_par">
    	<div class="popup_list" id="popup_list">
        	
        </div>
        <div class="popup_scroll" id="popup_scroll">
        	<div class="popup_scroll_son" id="popup_scroll_son"></div>	
        </div>
    </div>
    <div class="popup_btn">
    	<input type="hidden" id="urlIndexWarnMsg">
    	<a href="javaScript:;" class="a2 fl">确定</a>
    </div>
</div>

</body>
</html>
