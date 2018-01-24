<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>业务管理-字段维护</title>
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
<style type="text/css">
	.error_info_dic{color: #ed7563;font-size:12px;height:20px;line-height:20px;text-indent:18px; position:absolute; top:10px; left:10px; bottom:-10px; z-index:1;}
</style>
<script type="text/javascript">
var pageSize = 10;
$(function () {
	//tab导航栏切换
	tabMenu('#card_list li','#card_box .module_box');

	//绑定选项卡
	bindTab();
});
//绑定选项卡
function bindTab(){
	//切换选项卡
	var i;
	var cookies = document.cookie.split(";");

	for (i=0; i<cookies.length; i++) {
		var c = cookies[i];
		if (c.indexOf("dictionaryList") != -1) {
			var n = c.substr("dictionaryList=".length);
			$("#card_list li").eq(n).click();
			break;
		}
	}
	if (i == cookies.length){
		$("#card_list li").eq(0).click();
	}
}
//tab导航栏切换
function tabMenu(btn, box){
	$(btn).click(function(){
		if ($(this).attr("data-id") == 0) {
			//企业性质
			findNature(1);
			//设置cookie的失效时间
			setNatureTime();
		} else if ($(this).attr("data-id") == 1) {
			//评级机构
			findRateInstitution(1);
			//设置cookie的失效时间
			setInsTime();
		}
		$(btn).attr('class','');
		$(this).attr('class','active');
		$(box).css('display','none');
		$(box).eq($(this).index()).css('display','block');
	})
}
//设置cookie的失效时间
function setNatureTime(){
	//设置选项卡位置
	var exp = new Date();
	exp.setTime(exp.getTime() +60*1000);
	document.cookie = "dictionaryList="+0+";expires="+exp.toGMTString();
}
//设置cookie的失效时间
function setInsTime(){
	//设置选项卡位置
	var exp = new Date();
	exp.setTime(exp.getTime() +60*1000);
	document.cookie = "dictionaryList="+1+";expires="+exp.toGMTString();
}
/**********--nature start--***********/
//查企业性质ajax
function findNature(pageNo) {
	$.ajax({
		url:"${ctx}/dictionary/findAllNature",
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
				$("#natureConent").html("");
				var htmlContent = createNatureTable(dataList);
				$("#natureConent").html(htmlContent);
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
//create nature table
function createNatureTable(data) {
	var htmlContent = "";

	for (var i=0; i<data.length; i++) {
		htmlContent += "<tr>";
		htmlContent += "<td class='audit_td1'>"+(parseInt(i)+1)+"</td>";
		htmlContent += "<td style='display:none'>"+data[i].id+"</td>";
		htmlContent += "<td>"+(data[i].name == null ? '' : data[i].name)+"</td>";
		htmlContent += "<td>"+(data[i].creatorName == null ? '' : data[i].creatorName)+"</td>";
		htmlContent += "<td>"+(data[i].createDate == null ? '': data[i].createDate)+"</td>";
		htmlContent += "<td class='audit_td3 audit_td4'>";
		htmlContent += "<a style='margin-right:10px;' class='update_btn' onclick='funPop(\"#popup1\", "+data[i].id+", \""+data[i].name+"\");' href='javascript:;'>修改</a>";
		htmlContent += "<a class='delete_btn' onclick='delNature("+data[i].id+")' href='javascript:;'>删除</a>";
		htmlContent += "</td>";
		htmlContent += "</tr>";
	}

	return htmlContent;
}
//企业性质数据删除
function delNature(id) {
	confirm("确定删除该企业性质信息吗?", function(){
		$.ajax({
			url:"${ctx}/dictionary/deleteNature?id="+id,
			type:'POST',
			success:function(data){
				if(data.code == 200){
					alert("删除成功！");
					setTimeout(function() {
						window.location.href = "${ctx}/dictionary/list";
					},1500);
				}else{
					alert("删除失败！");
					setTimeout(function() {
						window.location.href = "${ctx}/dictionary/list";
					},1500);
					console.error(data.msg);
				}
			}
		});
	});
}
//查询1
function searchNature(){
	findNature(1);
}
//分页跳转1
function jumpToPage(pageNo){
	findNature(pageNo);
}
/**********--nature end--***********/

/**********--rateIns start--***********/
//查评级机构ajax
function findRateInstitution(pageNo) {
	$.ajax({
		url:"${ctx}/dictionary/findAllRateInstitution",
		type:"POST",
		dataType:"json",
		data:{
			"pageNo":pageNo,
			"pageSize":pageSize
		},
		success:function (data) {
			if (200 == data.code) {
				var page = data.data;
				var totalPage = page.totalPage;
				var dataList = page.rows;
				//clear
				$("#rateInstitutionConent").html("");
				var htmlContent = createInsTable(dataList);
				$("#rateInstitutionConent").html(htmlContent);
				//page
				var pageStr = createPage2(page.total, page.pageNo, page.totalPage);
				$("#pageDiv2").html(pageStr);
			} else {
				alert(data.msg);
				console.error(data.msg);
			}
		}
	});
}
//create ins table
function createInsTable(data) {
	var htmlContent = "";

	for (var i=0; i<data.length; i++) {
		htmlContent += "<tr>";
		htmlContent += "<td class='audit_td1'>"+(parseInt(i)+1)+"</td>";
		htmlContent += "<td style='display:none'>"+data[i].id+"</td>";
		htmlContent += "<td>"+(data[i].name == null ? '' : data[i].name)+"</td>";
		htmlContent += "<td>"+(data[i].code == null ? '' : data[i].code)+"</td>";
		htmlContent += "<td>"+(data[i].creatorName == null ? '' : data[i].creatorName)+"</td>";
		htmlContent += "<td>"+(data[i].createTime == null ? '' : data[i].createTime)+"</td>";
		htmlContent += "<td class='audit_td3 audit_td4'>";
		htmlContent += "<a style='margin-right:10px;' class='update_btn' onclick='funPop(\"#popup2\", "+data[i].id+", \""+data[i].name+"\", \""+data[i].code+"\");' href='javascript:;'>修改</a>";
		htmlContent += "<a class='delete_btn' onclick='delRateIns("+data[i].id+")' href='javascript:;'>删除</a>";
		htmlContent += "</td>";
		htmlContent += "</tr>";
	}

	return htmlContent;
}
//评级机构数据删除
function delRateIns(id){
	confirm("确定删除该评级机构信息吗?", function () {
		$.ajax({
			url:"${ctx}/dictionary/deleteRateInstitution?id="+id,
			type:'POST',
			success:function(data){
				if(data.code == 200){
					alert("删除成功！");
					setTimeout(function() {
						window.location.href = "${ctx}/dictionary/list";
					},1500);
				}else{
					alert("删除失败！");
					setTimeout(function() {
						window.location.href = "${ctx}/dictionary/list";
					},1500);
					console.error(data.msg);
				}
			}
		});
	});
}
//查询1
function searchRateIns(){
	findRateInstitution(1);
}
//分页跳转2
function jumpToPage2(pageNo){
	findRateInstitution(pageNo);
}
/**********--rateIns end--***********/
//判断null
function isValueNull(obj) {
	var flag = true;
	if (obj == null || obj == "" || obj == undefined) {
		flag = false;
	}
	return flag;
}
//nature clear error
$(document).on('click','.nature_change',function(){
	setTimeout(function () {
		//add clear logo
		var initCount = setInterval(function(){
			//评级结果
			var rateResult = $("#natureName").val();
			var flag = isValueNull(rateResult);
			if (flag) {
				$("#natureNameError").hide();
				//clear interval
				clearInterval(initCount);
			}
		},100);
	},1000);
});
$(document).on("click",'#popup1 .a1',function(){
	var name = $("#natureName").val();
	var flag = isValueNull(name);
	if (flag == false) {
		$("#natureNameError").html("");
		var errHtml = '<errtry>请输入企业性质名称!</errtry>';
		$("#natureNameError").append(errHtml);
		$("#natureNameError").show();
		return;
	}
});
//验证企业性质名称的唯一性
function validateNatureName(){
	var flag = true;
	$.ajax({
		url:"${ctx}/dictionary/validateNatureName",
		type:'POST',
		data: {
			"natureId":$("#natureId").val(),
			"name":$("#natureName").val()
		},
		async: false,
		success:function(data){
			if(data.code == 200){
				flag = true;
			}else{
				//clear
				$("#natureName").val("");
				flag = false;
				alert(data.msg);
			}
		}
	});
	return flag;
}
//保存企业性质信息nature_save
$(document).on("click",'.nature_save',function(){
	var name = $("#natureName").val();
	var flag = isValueNull(name);
	if (flag == true) {
		//校验企业性质名称标识唯一性
		var nameFlag = validateNatureName();
		if (nameFlag == true) {
			$.ajax({
				url:"${ctx}/dictionary/doSaveOrUpdateNature",
				data : {
					"id":$("#natureId").val(),
					"name":$("#natureName").val()
				},
				type : "POST",
				success:function(data){
					if(data.code == 200){
                        $("#popup1").hide();
						alert("企业性质添加或更新成功！", "${ctx}/dictionary/list");
						/*setTimeout(function() {
							window.location.href = "${ctx}/dictionary/list";
						},1500);*/
					}else{
                        $("#popup1").hide();
						alert("企业性质添加或更新失败！", "${ctx}/dictionary/list");
						/*setTimeout(function() {
							window.location.href = "${ctx}/dictionary/list";
						},1500);*/
						console.error(data.msg);
					}
				}
			});
		}
	}
});
//ins_name clear error
function ins_name_change(){
	setTimeout(function () {
		//add clear logo
		var initCount = setInterval(function(){
			var insName = $("#insName").val();
			var nameFlag = isValueNull(insName);
			if (nameFlag) {
				$("#insNameError").hide();
				//clear interval
				clearInterval(initCount);
			}
		},100);
	},500);
}
//ins_code clear error
function ins_code_change(){
	setTimeout(function () {
		//add clear logo
		var initCount = setInterval(function(){
			var creditCode = $("#creditCode").val();
			var codeFlag = isValueNull(creditCode);
			if (codeFlag) {
				$("#creditCodeError").hide();
				//clear interval
				clearInterval(initCount);
			}
		},100);
	},500);
}
$(document).on("click",'#popup2 .a1',function(){
	var insName = $("#insName").val();
	var creditCode = $("#creditCode").val();
	var nameFlag = isValueNull(insName);
	var codeFlag = isValueNull(creditCode);
	if (!nameFlag && !codeFlag ) {
		$("#insNameError").html("");
		var errHtml = '<errtry>请输入评级机构名称!</errtry>';
		$("#insNameError").append(errHtml);
		$("#insNameError").show();
		$("#creditCodeError").html("");
		var errHtml2 = '<errtry>请输入企业统一信用代码!</errtry>';
		$("#creditCodeError").append(errHtml2);
		$("#creditCodeError").show();
		return;
	} else if (!nameFlag) {
		$("#insNameError").html("");
		var errHtml = '<errtry>请输入评级机构名称!</errtry>';
		$("#insNameError").append(errHtml);
		$("#insNameError").show();
		return;
	}  else if (!codeFlag) {
		$("#creditCodeError").html("");
		var errHtml = '<errtry>请输入企业统一信用代码!</errtry>';
		$("#creditCodeError").append(errHtml);
		$("#creditCodeError").show();
		return;
	}
});
//验证机构名称的唯一性
function validateInsName(){
	var flag = true;
	$.ajax({
		url:"${ctx}/dictionary/validateInsName",
		type:'POST',
		data: {
			"insId":$("#insId").val(),
			"name":$("#insName").val()
		},
		async: false,
		success:function(data){
			if(data.code == 200){
				flag = true;
			}else{
				//clear
				$("#insName").val("");
				flag = false;
				alert(data.msg);
			}
		}
	});
	return flag;
}
//验证代码标识的唯一性
function validateInsCode(){
	var flag = true;
	$.ajax({
		url:"${ctx}/dictionary/validateInsCode",
		type:'POST',
		data: {
			"insId":$("#insId").val(),
			"code":$("#creditCode").val()
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
//保存评级机构信息ins_save
$(document).on("click",'.ins_save',function(){
	var insName = $("#insName").val();
	var creditCode = $("#creditCode").val();
	var nameFlag = isValueNull(insName);
	var codeFlag = isValueNull(creditCode);
	if (nameFlag == true && codeFlag == true) {
		//验证统一信用代码格式
		var creditCodeFlag = isCreditCodeRight();
		if (creditCodeFlag == true) {
			//校验机构名称标识唯一性
			var insNameFlag = validateInsName();
			if (insNameFlag == true) {
				//校验企业代码标识唯一性
				var insCodeFlag = validateInsCode();
				if (insCodeFlag == true) {
					$.ajax({
						url:"${ctx}/dictionary/doSaveOrUpdateRateInstitution",
						data : {
							"id":$("#insId").val(),
							"name":$("#insName").val(),
							"code":$("#creditCode").val()
						},
						type : "POST",
						success:function(data){
							if(data.code == 200){
                                $("#popup2").hide();
								alert("评级结果添加或更新成功！", "${ctx}/dictionary/list");
								/*setTimeout(function() {
									window.location.href = "${ctx}/dictionary/list";
								},1500);*/
							}else{
                                $("#popup2").hide();
								alert("评级结果添加或更新失败！", "${ctx}/dictionary/list");
								/*setTimeout(function() {
									window.location.href = "${ctx}/dictionary/list";
								},1500);*/
								console.error(data.msg);
							}
						}
					});
				}
			}
		}
	}
});
//新增/修改企业性质
function funPop(obj, id, name, code){
	$("#natureNameError").hide();
	$("#insNameError").hide();
	$("#creditCodeError").hide();
	$('#layer').show();
	$(obj).show();
	if (id) {
		$(obj).find("input").eq(0).val(id);
		$(obj).find("input").eq(1).val(name);
		if (code) {
			$(obj).find("input").eq(2).val(code);
		}
	} else {
		$(obj).find("input").eq(0).val("0");
		$(obj).find("input").eq(1).val("");
		if (obj == "#popup2") {
			$(obj).find("input").eq(2).val("");
		}
	}
	$(obj).find('.a2').click(function(){
		fnShutDown(obj);
	});
}
//关闭弹框
function closeAlertDiv(obj) {
    //close alert div
	$(obj).parent().hide();
	//layer hide
    $('#layer').hide();
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
				<ul class="card_list" id="card_list">
					<li data-id="0" class="active">企业性质</li>
					<li data-id="1">评级机构</li>
				</ul>
				<div id="card_box">
					<div class="module_box field" style="display:block;">
						<div class="search_box clear" style="padding-top:12px;">
							<div class="fr function_btn">
								<a href="javaScript:;" class="fr database_btn" onClick="funPop('#popup1')">新增企业性质</a>
							</div>
						</div>
						<div class="module_height">
							<form>
								<table class="module_table">
									<thead>
									<tr>
										<th class="module_width1">序号</th>
										<th>性质名称</th>
										<th>创建人</th>
										<th>创建时间</th>
										<th class="module_width2">操作</th>
									</tr>
									</thead>
									<tbody id="natureConent"></tbody>
								</table>
							</form>
						</div>
						<!-- 分页.html start -->
						<%@include file="../commons/tabPage.jsp" %>
						<!-- 分页.html end -->
					</div>
					<div class="module_box field">
						<div class="search_box clear" style="padding-top:12px;">
							<div class="fr function_btn">
								<a href="javaScript:;" class="fr database_btn" onClick="funPop('#popup2')">新增评级机构</a>
							</div>
						</div>
						<div class="module_height">
							<form>
								<table class="module_table">
									<thead>
									<tr>
										<th class="module_width1">序号</th>
										<th>机构名称</th>
										<th>统一信用代码</th>
										<th>创建人</th>
										<th>创建时间</th>
										<th class="module_width2">操作</th>
									</tr>
									</thead>
									<tbody id="rateInstitutionConent"></tbody>
								</table>
							</form>
						</div>
						<!-- 分页.html start -->
						<%@include file="../commons/tabPage2.jsp" %>
						<!-- 分页.html end -->
					</div>
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
<!-- 删除 start -->
<div class="popup popup2" id="popup">
	<a href="javaScript:;" class="close"></a>
	<p>错误提示</p>
	<div class="popup_btn">
		<a href="javaScript:;" class="a1 fl">确定</a>
		<a href="javaScript:;" class="a2 fr">取消</a>
	</div>
</div>
<!-- 删除 end -->
<!-- 新增和修改企业性质 start -->
<div class="popup popup3" id="popup1">
	<a href="javaScript:;" class="close" onclick="closeAlertDiv(this);"></a>
	<div class="popup_ip">
		<table class="popup_tab popup_tab1">
			<input id="natureId" name="id" type="hidden" value="0">
			<tbody>
			<tr>
				<td><span>性质名称</span></td>
				<td>
					<input type="text" id="natureName" class="nature_change" name="name" type="text">
					<div id="natureNameError" class="error_info_dic" style="left:-15px;top:40px;" type="hidden"></div>
				</td>
			</tr>
			</tbody>
		</table>
	</div>
	<div class="popup_btn">
		<a href="javaScript:;" class="a1 fl nature_save">确认</a>
		<a href="javaScript:;" class="a2 fr">取消</a>
	</div>
</div>
<!-- 新增和修改企业性质 end -->
<!-- 新增和修改评级机构 start -->
<div class="popup popup3" id="popup2">
	<a href="javaScript:;" class="close" onclick="closeAlertDiv(this);"></a>
	<div class="popup_tab_box popup_tab_box1">
		<form>
			<input id="insId" name="id" type="hidden" value="0">
			<table class="popup_tab popup_tab1">
				<tbody>
				<tr>
					<td class="td1"><strong>评级机构名称</strong></td>
					<td>
						<input class="popup_tab_w" type="text" id="insName" onchange="ins_name_change();" name="name">
						<div id="insNameError" class="error_info_dic" style="left:-15px;top:40px;" type="hidden"></div>
					</td>
				</tr>
				<tr>
					<td class="td1"><strong>机构统一信用代码</strong></td>
					<td>
						<input class="popup_tab_w" type="text" id="creditCode" onchange="ins_code_change();" name="creditCode">
						<div id="creditCodeError" class="error_info_dic" style="left:-15px;top:40px;" type="hidden"></div>
					</td>
				</tr>
				<tr>
					<td class="td1"></td>
					<td>
						<div class="popup_btn">
							<a href="javaScript:;" class="a1 fl ins_save">确定</a>
							<a href="javaScript:;" class="a2 fr">取消</a>
						</div>
					</td>
				</tr>
				</tbody>
			</table>
		</form>
	</div>
</div>
<!-- 新增和修改评级机构 end -->
</body>
</html>
