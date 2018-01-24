<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>业务管理-定价数据管理</title>
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
<script type="text/javascript">
var pageSize = 10;

$(function () {
	//查询定价数据
	getData(1);
});
//查定价ajax
function getData(pageNo) {
    //查询条件
	var searchContent = $("#searchContent").val();

	$.ajax({
		url:"${ctx}/priceData/findAll",
		type:"POST",
		dataType:"json",
		data:{
			"pageNo":pageNo,
			"pageSize":pageSize
		},
		success:function (data) {
			if (200 == data.code) {
				var page = data.data;
				var dataList = page.rows;
				//clear
				$("#priceDataConent").html("");
                var htmlContent = createTable(dataList);
				$("#priceDataConent").html(htmlContent);
				//page
				var pageStr = creatPage(page.total, page.pageNo, page.totalPage);
				$("#pageDiv").html(pageStr);
			} else {
				alert(data.msg);
				console.error(data.msg);
			}
		}
	});
}
//create html element
function createTable(data){
	var htmlContent = "";

	for (var i=0; i<data.length; i++) {
		htmlContent += "<tr>";
		htmlContent += "<td>"+(parseInt(i)+1)+"</td>";
		htmlContent += "<td>"+(data[i].rateResultName == null ? '' : data[i].rateResultName)+"</td>";
		if (data[i].rateResultName.indexOf("C") < 0) {
			//保证金
			if (data[i].cashDepositMin == null || data[i].cashDepositMin == 0) {
				if (data[i].cashDepositMax == null || data[i].cashDepositMax == 0) {
					htmlContent += "<td>0-0</td>";
				} else {
					htmlContent += "<td>0-"+data[i].cashDepositMax+"%</td>";
				}
			} else {
				if (data[i].cashDepositMax == null || data[i].cashDepositMax == 0) {
					htmlContent += "<td>"+data[i].cashDepositMin+"%-0</td>";
				} else {
					htmlContent += "<td>"+data[i].cashDepositMin+"%-"+data[i].cashDepositMax+"%</td>";
				}
			}
			//手续费
			if (data[i].handlingChargeMin == null || data[i].handlingChargeMin == 0) {
				if (data[i].handlingChargeMax == null || data[i].handlingChargeMax == 0) {
					htmlContent += "<td>0-0</td>";
				} else {
					htmlContent += "<td>0-"+data[i].handlingChargeMax+"%</td>";
				}
			} else {
				if (data[i].cashDepositMax == null || data[i].cashDepositMax == 0) {
					htmlContent += "<td>"+data[i].handlingChargeMin+"%-0</td>";
				} else {
					htmlContent += "<td>"+data[i].handlingChargeMin+"%-"+data[i].handlingChargeMax+"%</td>";
				}
			}
			//利率
			if (data[i].interestRateMin == null || data[i].interestRateMin == 0) {
				if (data[i].interestRateMax == null || data[i].interestRateMax == 0) {
					htmlContent += "<td>0-0</td>";
				} else {
					htmlContent += "<td>0-"+data[i].interestRateMax+"%</td>";
				}
			} else {
				if (data[i].interestRateMax == null || data[i].interestRateMax == 0) {
					htmlContent += "<td>"+data[i].interestRateMin+"%-0</td>";
				} else {
					htmlContent += "<td>"+data[i].interestRateMin+"%-"+data[i].interestRateMax+"%</td>";
				}
			}
			//IRR
			if (data[i].irrMin == null || data[i].irrMin == 0) {
				if (data[i].irrMax == null || data[i].irrMax == 0) {
					htmlContent += "<td>0-0</td>";
				} else {
					htmlContent += "<td>0-"+data[i].interestRateMax+"%</td>";
				}
			} else {
				if (data[i].irrMax == null || data[i].irrMax == 0) {
					htmlContent += "<td>"+data[i].irrMin+"%-0</td>";
				} else {
					htmlContent += "<td>"+data[i].irrMin+"%-"+data[i].irrMax+"%</td>";
				}
			}
			htmlContent += "<td>"+(data[i].creatorName == null ? '' : data[i].creatorName)+"</td>";
			htmlContent += "<td>"+(data[i].createTime == null ? '' : data[i].createTime)+"</td>";
			htmlContent += "<td>";
			htmlContent += "<shiro:hasPermission name='/priceData/update'><a class='update_btn' onclick='update("+data[i].id+");' href='javascript:;'>修改</a></shiro:hasPermission>";
			htmlContent += "</td>";
		} else {
			htmlContent += "<td>"+(data[i].cashDepositMin == null ? '' : data[i].cashDepositMin)+"</td>";
			htmlContent += "<td>"+(data[i].handlingChargeMin == null ? '' : data[i].handlingChargeMin)+"</td>";
			htmlContent += "<td>"+(data[i].interestRateMin == null ? '' : data[i].interestRateMin)+"</td>";
			htmlContent += "<td>"+(data[i].irrMin == null ? '' : data[i].irrMin)+"</td>";

			htmlContent += "<td>"+(data[i].creatorName == null ? '' : data[i].creatorName)+"</td>";
			htmlContent += "<td>"+(data[i].createTime == null ? '' : data[i].createTime)+"</td>";
			htmlContent += "<td></td>";
		}

		htmlContent += "</tr>";
	}

	return htmlContent;
}
//定价数据修改
function update(id) {
	window.location.href = "${ctx}/priceData/update?id="+id;
};
//分页跳转
function jumpToPage(pageNo){
	getData(pageNo);
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
				<a href="javascript:void(0)">业务管理</a>
				<span>></span>
				<a href="${ctx}/priceData/list">定价数据管理</a>
			</h3>
			<div class="shade main_minHeight">
				<div class="search_box clear"></div>
				<div class="module_height">
					<table class="module_table">
						<thead>
							 <tr>
								 <th>序号</th>
								 <th>评级结果</th>
								 <th>保证金</th>
								 <th>手续费</th>
								 <th>利率</th>
								 <th>IRR</th>
								 <th>修改人</th>
								 <th>修改时间</th>
								 <th style="width:150px;">操作</th>
							 </tr>
						</thead>
						<tbody id="priceDataConent"></tbody>
					</table>
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

