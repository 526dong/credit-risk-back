<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>业务管理-定价数据管理-修改定价数据</title>
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
<script type="text/javascript">
//保存定价
function save(){
	$.ajax({
		 url:"${ctx}/priceData/doUpdate",
		 data : $("#priceDataForm").serialize(),
		 type : "POST",
		 success:function(data){
			if(data.code == 200){
				alert("更新成功！");

				setTimeout(function () {
					window.location.href="${ctx}/priceData/list";
				},1500);
			}else{
				alert("更新失败！");

				setTimeout(function () {
					window.location.href="${ctx}/priceData/list";
				},1500);
			}
		 }
	});
}
//取消
function cancel(){
	window.location.href="${ctx}/priceData/list";
}
//校验小数
function isRightNumber(obj) {
	//校验小数：/^-?\d+\.\d+$/
	//var reg = /^-?\d+\.?\d{0,2}$/;//保留四位小数
	//var reg = /^\d{1,3}(\.\d{1,2})?$/;//最多三位整数，两位小数

	if (obj == "0.0" || obj == "0.00") {
		return "";
	} else {
		var reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^[0](\.[0-9]{1,2})?$)|(^(1){1}(0){2}$)/;

		if(!reg.test(obj)){
			return "";
		}else{
			return obj;
		}
	}
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
				<span>></span>
				<a href="javascript:void(0)">修改定价数据</a>
			</h3>
			<div class="backtrack">
				<a href="javascript:history.back(-1)">返回</a>
			</div>
			<form id="priceDataForm">
				<input id="id" name="id" type="hidden" value="${priceData.id}">
				<div class="shade">
					<div class="main_padd">
						<div class="info_box info_box1 clear">
							<strong>评级结果</strong>
							<c:forEach items="${resultList}" var="result" varStatus="status">
								<c:if test='${result.id == priceData.rateResult}'>
									<span>${result.name}</span>
								</c:if>
							</c:forEach>
							<%--<select id="rateResult" name="rateResult">--%>
								<%--<option value="">请选择评级结果</option>--%>
								<%--<c:forEach items="${resultList}" var="result" varStatus="status">
									<option value="${result.id}" <c:if test='${result.id == priceData.rateResult}'> selected="selected" </c:if>>${result.name}</option>
								</c:forEach>--%>
							<%--</select>--%>
						</div>
					</div>
					<ul class="pricing_list clear">
						<li>
							<strong>保证金</strong>
							<input style="width:50px;" id="cashDepositMin" name="cashDepositMin" type="text" value="${priceData.cashDepositMin}" onchange="value=isRightNumber(this.value);" />
							<span>%</span>
							<i>-</i>
							<input id="cashDepositMax" name="cashDepositMax" type="text" value="${priceData.cashDepositMax}" onchange="value=isRightNumber(this.value);" />
							<span>%</span>
						</li>
						<li>
							<strong>手续费</strong>
							<input id="handlingChargeMin" name="handlingChargeMin" type="text" value="${priceData.handlingChargeMin}" onchange="value=isRightNumber(this.value);" />
							<span>%</span>
							<i>-</i>
							<input id="handlingChargeMax" name="handlingChargeMax" type="text" value="${priceData.handlingChargeMax}" onchange="value=isRightNumber(this.value);" />
							<span>%</span>
						</li>
						<li>
							<strong>利率</strong>
							<input id="interestRateMin" name="interestRateMin" type="text" value="${priceData.interestRateMin}" onchange="value=isRightNumber(this.value);" />
							<span>%</span>
							<i>-</i>
							<input id="interestRateMax" name="interestRateMax" type="text" value="${priceData.interestRateMax }" onchange="value=isRightNumber(this.value);" />
							<span>%</span>
						</li>
						<li>
							<strong>IRR</strong>
							<input id="irrMin" name="irrMin" type="text" value="${priceData.irrMin}" onchange="value=isRightNumber(this.value);" />
							<span>%</span>
							<i>-</i>
							<input id="irrMax" name="irrMax" type="text" value="${priceData.irrMax}" onchange="value=isRightNumber(this.value);" />
							<span>%</span>
						</li>
					</ul>
				</div>
			</form>
			<div class="main_btn main_btn1">
				<a href="javaScript:;" class="fl" onclick="save();">保存</a>
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

