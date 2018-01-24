<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>企业数据管理-企业评级管理-查看企业历史评级-查看评级详情</title>
<link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<script type="text/javascript" src="${ctx}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/cityselect.js"></script>
<!-- 企业主体信息 -->
<script type="text/javascript">
    //报表列表
	reportListUrl = "${ctx}/enterprise/reportList";
</script>
<script type="text/javascript" src="${ctx}/resources/js/enterpriseMng/enterprise.js"></script>
<!-- 报表详情-->
<script type="text/javascript">
	//加载报表模板
	reportModelUrl = "${ctx}/enterprise/getReportModel";
	//加载报表联表校验公式
	reportCheckFormulaUrl = "${ctx}/enterprise/getReportCheckFormula";
</script>
<script type="text/javascript" src="${ctx}/resources/js/enterpriseMng/report.js"></script>
<!-- 导入、导出 -->
<script type="text/javascript">
    //导出财务报表
    exportReportDataExcelUrl = "${ctx}/enterprise/exportReportDataExcel";
</script>
<script type="text/javascript" src="${ctx}/resources/js/enterpriseMng/excelUtils.js"></script>
<!-- 字典 -->
<script type="text/javascript">
	//指标
	indexUrl = "${ctx}/enterprise/getIndex";
	entType = '${enterprise.scale}';

	var indexIds = [];
	var ruleIds = [];
</script>
<script type="text/javascript" src="${ctx}/resources/js/enterpriseMng/dictionary.js"></script>
<script type="text/javascript">
$(function(){
	//评级详情
	findRateInfo();
	fnTabControl('#card_btn li','#card_box1 .table_content_son');
});
//点击评级详情tab
$(document).on("click",'#rate_info',function(){
	//切换tab
	switchTab(true);
	findRateInfo();
});
//点击评级信息tab
$(document).on("click",'#rate_detail',function(){
	//切换tab
	switchTab(false);
	rateInfoDetail();

	//地区-省市县回显
	var regionVal = showRegion();
	//主体区域回显
	$("#regionSpan").html(regionVal);
});
/**
 * switch tab
 * @param flag true-评级详情，false-评级信息
 */
function switchTab(flag) {
	if (flag) {
		$(".ele").show();
		$(".rate").hide();
		$(this).addClass("active");
		$(this).next().removeClass("active");
	} else {
		$(".rate").show();
		$(".ele").hide();
		$(this).addClass("active");
		$(this).prev().removeClass("active");
	}
}
//查询评级详情
function findRateInfo(){
	//评级申请编号
	var ratingApplyNum = '${approval.ratingApplyNum}';
	$.ajax({
		 url:"${ctx}/enterprise/rateInfo",
		 type:'POST',
		 data:{
			 "ratingApplyNum":ratingApplyNum
		 },
		 async: false,
		 success:function(data){
			 if (200 == data.code) {
				var data = data.data;
				//clear
				$("#elementContent").html("");
				var htmlStr = createRateInfoTable(data);
				$("#elementContent").html(htmlStr);
			 } else {
                 alert(data.msg);
                 console.error(data.msg);
             }
		 }
	});
}
//评级详情列表
function createRateInfoTable(data){
	var htmlContent = "";
	for(var i=0;i<data.length;i++){
		if (data[i].finalFlag == 1) {
			htmlContent += "<tr>";

			htmlContent += "<td style='display:none'>"+data[i].id+"</td>";
			htmlContent += "<td>评分卡打分结果</td>";//重要因素

			htmlContent += "<td>-</td>";//序号
			htmlContent += "<td>-</td>";//指标名称
			htmlContent += "<td>-</td>";//指标数据
			htmlContent += "<td>-</td>";//权重
			htmlContent += "<td>"+(data[i].doubleValue == null ? '' : data[i].doubleValue)+"</td>";//评级得分
			htmlContent += "<td>"+(data[i].degree == null ? '' : data[i].degree)+"</td>";//对应等级
			htmlContent += "</tr>";
		} else if(data[i].finalFlag == 0){
		   htmlContent += "<tr>";

		   htmlContent += "<td style='display:none'>"+data[i].id+"</td>";
		   if (data[i].count > 1) {
			   htmlContent += "<td rowspan="+data[i].count+">"+(data[i].elementName == null ? '' : data[i].elementName)+"</td>";//重要因素
		   } else if (data[i].count == 1) {
			   htmlContent += "<td>"+(data[i].elementName == null ? '' : data[i].elementName)+"</td>";//重要因素
		   }

		   htmlContent += "<td class='audit_td1'>"+(parseInt(i)+1)+"</td>";

		   htmlContent += "<td>"+(data[i].indexName == null ? '' : data[i].indexName)+"</td>";//指标名称
		   htmlContent += "<td>"+(data[i].indexData == null ? '' : data[i].indexData)+"</td>";//指标数据
		   htmlContent += "<td>"+(data[i].weight == null ? '' : data[i].weight)+"</td>";//权重
		   htmlContent += "<td>"+(data[i].value == null ? '' : data[i].value)+"</td>";//评级得分
		   htmlContent += "<td>"+(data[i].degree == null ? '' : data[i].degree)+"</td>";//对应等级
		   htmlContent += "</tr>";
		}
	}
	return htmlContent;
}
//评级详情查看
function rateInfoDetail(){
    //报表列表
    reportList('${enterpriseId}');
    //查指标
    //loadIndexById("${enterprise.industry2}", "${enterprise.scale}", "${indexIds}", "${ruleIds}", 1);
    showIndex(${approval.approvalIndexNameAndValueJson}, 4, "${approval.adjustContent}", "${approval.adjustChange}");
    //指标样式
    indexStyle();
}
/**
 * 指标样式
 */
function indexStyle() {
    //完善样式
    $("#indexListDiv").find("Strong").each(function (i, obj) {
        $(obj).eq(0).css("margin-left", "25px")
        $(obj).css("margin-right", "5px")
        $(obj).find("i").hide();
    });
}
//修改、查看：按钮控制
function reportHtml(id, reportType, htmlContent){
	htmlContent += "<td>";
		htmlContent += "<a href='#none;' onclick='checkDetails("+"\"${ctx}/enterprise/mainReport?id="+id+"\", "+id+", "+reportType+")'>查看</a>";
   htmlContent += "</td>";
   return htmlContent;
}
//返回上一级
function fnShow(){
	$('.table_hide').show();
	$('.table_info_box').hide();
}
//返回
function back(){
	var id = '${enterpriseId}';
	window.location.href = "${ctx}/enterprise/detail?id="+id;
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
				<a href="${ctx}/enterprise/list">企业数据管理</a>
				<span>></span>
				<a href="${ctx}/enterprise/list">企业评级管理</a>
				<span>></span>
				<a href="javascript:void(0)" onclick="back();">查看企业历史评级</a>
				<span>></span>
				<a href="javascript:void(0)">查看评级详情</a>
			</h3>
			<div class="backtrack">
				<a href="javascript:history.back(-1);">返回</a>
			</div>
			<div class="shade">
				<div class="container-fluid main_padd">
					<div class="row">
						<div class="col-lg-2 col-sm-2 col-md-2" style="width: 250px">
							<div class="info_box info_box1 info_box2 clear">
								<strong>企业评级结果</strong>
								<p>${approval.ratingResult}</p>
							</div>
						</div>
						<div class="col-lg-2 col-sm-2 col-md-2" style="width: 250px">
							<div class="info_box info_box1 info_box2 clear">
								<strong>评级申请编号</strong>
								<p>${approval.ratingApplyNum}</p>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-2 col-sm-2 col-md-2" style="width: 250px">
							<div class="info_box info_box1 info_box2 clear">
								<strong>评级时间</strong>
								<p>${approval.approvalTime}</p>
							</div>
						</div>
						<div class="col-lg-2 col-sm-2 col-md-2" style="width: 250px">
							<div class="info_box info_box1 info_box2 clear">
								<strong>审批人</strong>
								<p>${approval.approver}</p>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="shade main_mar">
				<ul class="card_list" id="card_list">
					<li id="rate_info" class="active">评级详情</li>
					<li id="rate_detail">评级信息查看</li>
				</ul>
			</div>
			<div class="main_mar" id="card_box">
				<div class="module_box shade ele" style="display:block;">
					<div class="module_height">
						<form>
							<table class="module_table">
								<thead>
								<tr>
									<th>重要因素</th>
									<th class="module_width1">序号</th>
									<th>指标名称</th>
									<th>指标数据</th>
									<th>权重</th>
									<th>评级得分</th>
									<th>对应等级</th>
								</tr>
								</thead>
								<tbody id="elementContent"></tbody>
							</table>
						</form>
					</div>
					<%--分页--%>
				</div>
				<div class="module_box module_box1 rate">
					<%--引入主体基本信息--%>
					<%@ include file="../enterpriseDataMng/enterpriseCommon.jsp"%>
					<div class="shade main_mar">
						<h2 class="main_title">定性指标</h2>
						<div class="container-fluid main_padd">
							<div class="row" id="indexListDiv"></div>
						</div>
					</div>
					<%--引入主体报告列表--%>
					<div class="shade main_mar clear report_info">
						<%@ include file="../commons/enterpriseReporList.jsp" %>
						<div class="table_info_box" id="table_info_box">
							<div class="function_btn statement_btn clear">
								<a href="javaScript:;" class="fl" onclick="fnShow();">返回上一级</a>
								<a href="javaScript:;" class="fl" onclick="exportReportDataExcel();" >导出报表信息</a>
							</div>
							<%--导航菜单--%>
							<%@ include file="../commons/enterpriseReportMenu.jsp"%>
							<div class="main_minHeight1" id="card_box1">
								<!--报告概览-->
								<%@ include file="./report/reportDetail.jsp"%>
								<%--财务报表--%>
								<div id="reportVal"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
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
