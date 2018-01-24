<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>业务管理-评级数据管理</title>
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/my97datepicker/WdatePicker.js"></script>
<style type="text/css">
	.input_table input {width: 222px;}
	.input_table tr{width: 600px;}
	.error_info{background:url(../resources/image/error.png) 0 3px no-repeat;color: #ed7563;font-size:12px;height:20px;line-height:20px;text-indent:18px; position:absolute; top:10px; left:10px; bottom:-10px; z-index:1;}
</style>
<script type="text/javascript">
$(function(){
	//校验评级数据表单
	validateRate();

	$(".input_table").find("tr").find("td").eq(2).css("width", "300px");
});
//select框默认选择-请选择
$.validator.addMethod("isBlank",function(value,element){
	var flag = true;
	if(value == "-1"){
		flag = false;
	}else{
		flag = true;
	}
	return flag;
},"请选择有效值");
//统一社会信用代码校验
function isCreditCodeRight(){
	var flag = false;
	var code = $("#creditCode").val();

	var reg=/^[a-zA-Z0-9]{18}$/;
	if(!code.match(reg)){
		alert('统一社会信用代码18位数字或字母');
		flag = false;
	} else {
		flag = true;
	}
	return flag;
}
//事证号校验
function isCertificateCodeRight(){
	var flag = false;
	var code = $("#certificateCode").val();

	var reg = /^\d{12}$/;
	if (!code.match(reg)) {
		alert('事证号12位数字');
		flag = false;
	} else {
		flag = true;
	}
	return flag;
}
//组织机构代码
function isOrganizationCodeRight(){
	var flag = false;
	var code = $("#organizationCode").val();

	var reg = /^[a-zA-Z\d]{8}\-[a-zA-Z\d]$/;
	if(!code.match(reg)){
		alert('组织机构代码8位数字(或大写拉丁字母)和1位数字(或大写拉丁字母)');
		flag = false;
	} else {
		flag = true;
	}
	return flag;
}
//校验评级数据表单
function validateRate(){
	$("#rateDataForm").validate({
		rules: {
			name:{
				required:true
			},
			rateResult:{
				required:true,
				isBlank:true
			},
			rateInstitution:{
				required:true,
				isBlank:true
			},
			rateTime:{
				required:true
			}
		},
		messages:{
			name:{
				required:"请输入企业名称"
			},
			rateResult:{
				required:"请选择评级结果"
			},
			rateInstitution:{
				required:"请选择评级机构"
			},
			rateTime:{
				required:"请输入评级时间"
			}
		},
		errorPlacement: function(error, element) {
			if (element.is("input[name='rateResult']")) {
				error.appendTo($("#rateResultError"));
			} else if (element.is("input[name='rateInstitution']")) {
				error.appendTo($("#rateInstitutionError"));
			} else {
				element.parent().next().find("p").append(error);
			}
		}
	});
}
//rate_result clear error
$(document).on('click','.rate_result p',function(){
	setTimeout(function () {
		//add clear logo
		var initCount = setInterval(function(){
			//评级结果
			var rateResult = $("#rateResult").val();
			var flag = isValueNull(rateResult);
			if (flag) {
				$("#rateResultError").children("errtry").hide();
				//clear interval
				clearInterval(initCount);
			}
		},100);
	},1000);
});
//rate_institution clear error
$(document).on('click','.rate_institution p',function(){
	setTimeout(function () {
		//add clear logo
		var initCount = setInterval(function(){
			//评级结果
			var rateInstitution = $("#rateInstitution").val();
			var flag = isValueNull(rateInstitution);
			if (flag) {
				$("#rateInstitutionError").children("errtry").hide();
				//clear interval
				clearInterval(initCount);
			}
		},100);
	},1000);
});
//统一信用代码、事证号、组织机构代码三者必须填写一个
function validateCreditCodeVal(){
	var flag = false;

	var creditCode = $("#creditCode").val();
	var organizationCode = $("#organizationCode").val();
	var certificateCode = $("#certificateCode").val();

	var creditCodeFlag = isValueNull(creditCode);
	var organizationCodeFlag = isValueNull(organizationCode);
	var certificateCodeFlag = isValueNull(certificateCode);

	//三者有一个不为空就行
	if (creditCodeFlag || organizationCodeFlag || certificateCodeFlag) {
		if (creditCodeFlag) {
			flag = isCreditCodeRight();
		} else if (organizationCodeFlag) {
			flag = isOrganizationCodeRight();
		} else if (certificateCodeFlag) {
			flag = isCertificateCodeRight();
		}
	}

	return flag;
}
//判断null
function isValueNull(obj) {
	var flag = true;
	if (obj == null || obj == "" || obj == undefined) {
		flag = false;
	}
	return flag;
}
//验证主体名称name的唯一性
function validateName(){
	var flag = true;
	$.ajax({
		url:"${ctx}/rateData/validateName",
		type:'POST',
		data: {
			"rateDataId":$("#rateDataId").val(),
			"name":$("#name").val()
		},
		async: false,
		success:function(data){
			if(data.code == 200){
				flag = true;
			}else{
				//clear
				$("#name").val("");
				flag = false;
				alert(data.msg);
			}
		}
	});
	return flag;
}
//验证代码标识的唯一性
function validateCode(){
	var flag = true;
	$.ajax({
		url:"${ctx}/rateData/validateCode",
		type:'POST',
		data: {
			"rateDataId":$("#rateDataId").val(),
			"creditCode":$("#creditCode").val(),
			"organizationCode":$("#organizationCode").val(),
			"certificateCode":$("#certificateCode").val()
		},
		async: false,
		success:function(data){
			if(data.code == 200){
				flag = true;
			}else{
				flag = false;
				alert(data.msg);
			}
		}
	});
	return flag;
}
//保存评级
function save(){
	if(!$("#rateDataForm").valid()){
		return;
	}else{
		//校验统一信用代码、事证号、组织机构代码三者必须填写一个
		var flag = validateCreditCodeVal();
		if (flag == true) {
			//校验企业名称标识唯一性
			var nameFlag = validateName();
			if (nameFlag == true) {
				//校验企业代码标识唯一性
				var codeFlag = validateCode();
				if (codeFlag == true) {
					$.ajax({
						url:"${ctx}/rateData/doAdd",
						data : $("#rateDataForm").serialize(),
						type : "POST",
						success:function(data){
							if(data.code == 200){
								alert("添加成功！");
								setTimeout(function() {
									window.location.href="${ctx}/rateData/list";
								},1500);
							}else{
								alert("添加失败！");
								setTimeout(function() {
									window.location.href="${ctx}/rateData/list";
								},1500);
								console.error(data.msg);
							}
						}
					});
				}
			}
		} else {
			alert("统一信用代码、事证号、组织机构代码三者必须填写一个");
		}
	}
}
//取消
function cancel(){
	window.location.href="${ctx}/rateData/list";
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
				<a href="${ctx}/rateData/list">评级数据管理</a>
				<span>></span>
				<a href="javascript:void(0)">新增评级数据</a>
			</h3>
			<div class="shade main_minHeight">
				<div class="input_list_box">
					<form id="rateDataForm">
						<input id="rateDataId" name="id" type="hidden" value="0"/>
						<table class="input_table input_table1">
							<tbody>
							<tr>
								<td><strong>企业名称<i>*</i></strong></td>
								<td>
									<input id="name" name="name" type="text"/>
								</td>
								<td>
									<p class="error_info" id="nameError"></p>
								</td>
							</tr>
							<tr>
								<td><strong>评级结果<i>*</i></strong></td>
								<td>
									<div class="down_menu">
										<div class="select_btn">
											<span data-id="">请选择</span>
											<input id="rateResult" name="rateResult" type="hidden"/>
										</div>
										<div class="select_list rate_result">
											<p data-id="">请选择</p>
											<c:forEach items="${resultList}" var="result" varStatus="status">
												<p data-id="${result.id}">${result.name}</p>
											</c:forEach>
										</div>
									</div>

								</td>
								<td><p class="error_info" id="rateResultError"></p></td>
							</tr>
							<tr>
								<td><strong>统一社会信用代码</strong></td>
								<td>
									<input id="creditCode" name="creditCode" type="text" onchange="isCreditCodeRight();"/>
								</td>
								<td>
									<p class="error_info" id="creditCodeError"></p>
								</td>
							</tr>
							<tr>
								<td><strong>事证号</strong></td>
								<td>
									<input id="certificateCode" name="certificateCode" type="text" class=""/>
								</td>
								<td>
									<p class="error_info" id="certificateCodeError"></p>
								</td>
							</tr>
							<tr>
								<td><strong>组织机构代码</strong></td>
								<td>
									<input id="organizationCode" name="organizationCode" type="text" class=""/>
								</td>
								<td>
									<p class="error_info" id="organizationCodeError"></p>
								</td>
							</tr>
							<tr>
								<td><strong>评级机构<i>*</i></strong></td>
								<td>
									<div class="down_menu">
										<div class="select_btn">
											<span data-id="">请选择</span>
											<input id="rateInstitution" name="rateInstitution" type="hidden"/>
										</div>
										<div class="select_list rate_institution">
											<p data-id="">请选择</p>
											<c:forEach items="${institutionList}" var="ins" varStatus="status">
												<p data-id="${ins.id}">${ins.name}</p>
											</c:forEach>
										</div>
									</div>
								</td>
								<td>
									<p class="error_info" id="rateInstitutionError"></p>
								</td>
							</tr>
							<tr>
								<td><strong>评级时间<i>*</i></strong></td>
								<td>
									<input type="text" id="rateTime" name="rateTime" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})">
								</td>
								<td>
									<p class="error_info" id="rateTimeError"></p>
								</td>
							</tr>
							<tr>
								<td><strong></strong></td>
								<td>
									<div class="input_btn clear">
										<a href="javaScript:;" class="fl makeSure" onclick="save();">保存</a>
										<a href="javaScript:;" class="fr cancel" onclick="cancel();">取消</a>
									</div>
								</td>
							</tr>
							</tbody>
						</table>
					</form>
				</div>
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
