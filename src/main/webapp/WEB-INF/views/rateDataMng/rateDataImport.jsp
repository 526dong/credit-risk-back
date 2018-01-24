<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>业务管理-评级数据管理-上传评级管理</title>
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/ajaxfileupload.js"></script>
<script type="text/javascript">
/*导入 start*/
//下载Excel模板
function downloadExcelModel() {
	window.location.href = "${ctx}/rateData/downloadExcel";
}
//下载错误数据
function exportRateData() {
    //err number
    var errNum = $("#errNum").text();

    if (errNum == "" || errNum == "0") {
        alert("错误数据为空！");
	} else {
        window.location.href = "${ctx}/rateData/exportRateData";
    }

}
/*导入 end*/
//取消
function cancel(){
	history.back(-1);
}
//导入弹框初始化
function initFile(obj) {
	$(obj).prev().prev().text("选择文件 +");
	$(obj).prev().val("");
}
//添加文件
function fnFile(obj){
	$(obj).prev().html($(obj).val().substr(12,$(obj).val().length));
	fileUpload(obj);
}
//导入校验后缀
function fileUpload(fileName) {
	var filename = $(fileName).val();
	var fileLen = filename.indexOf('.xls');
	if (fileLen > 0) {
		if (null != filename && "" != filename && typeof (filename) != "undefined") {
			$("#fileNameId").text(filename);
		} else {
			$("#fileNameId").text("");
		}
	} else {
		alert("上传文件格式错误！请上传.xls,.xlsx文件");
		$(fileName).prev().text("选择文件 +");
		$(fileName).val("");
		return;
	}
}
function fnUpdata(){
	$('.updataRate_box').hide();
	$('.updataRate_box1').show();
	setTimeout(function(){
		$('.updataRate_box1').hide();
		$('.updataRate').hide();
		$('.updataRate1').show();
		$('.main_hide').show();
	},500);
}
//提交导入
function submitImport(obj) {
	if ($(obj).prev().prev().text() != "选择文件 +") {
		$.ajaxFileUpload({
			url : "${ctx}/rateData/doImportRateDataExcel",
			secureuri : false,//是否需要安全协议
			fileElementId : 'excelFile',
			contentType : "application/json",
			dataType : "json",
			type : 'POST',
			async : true,
			complete : function() {
			},
			success : function(data) {
				//导入弹框初始化
				initFile(obj);
				if(data){
					var reData = eval('(' + data + ')');
					//成功导入了数据，不代表没有错误数据
					if(reData.code == 200){
						successJump(reData);
					} else {
						//没有导入数据
						if (reData.msg) {
							if (reData.msg == 1) {
								alert("上传文件为空，请重新上传");
							} else if (reData.msg == 2) {
								alert("上传文件没有数据，请重新上传");
							} else if (reData.msg == 3) {
								alert("上传失败，请联系管理员");
							} else if (reData.msg == 4) {
								alert("上传文件已存在，请重新上传");
							}
						}
					}
				}
			},
			error : function() {
				alert("请求失败，请联系管理员");
				return false;
			}
		});
	} else {
		alert("导入文件不能为空！");
	}
}
//成功失败跳转
function successJump(data) {
	//成功条数
	$("#successNum").html(data.successNum);
	//失败条数
	$("#errNum").html(data.errNum);
	//调用上传列表
	fnUpdata();
}
//创建错误表格
function createTable(data){
	var htmlContent = "";
	for (var i = 0; i < data.length; i++) {
		htmlContent += "<tr>";
			//序号
			htmlContent += "<td>"+(parseInt(i)+1)+"</td>";
			//失败行
			htmlContent += "<td>"+data[i].errRow+"</td>";
			//失败列
			htmlContent += "<td>"+data[i].errColumn+"</td>";
			//错误类型：0-必填未填，1-格式不正确，2-数据重复，3-评级结果填写不符合规范
			htmlContent += "<td>"+ (data[i].errType == '0' ? '必填未填' : (data[i].status == '1' ? '格式不正确' : (data[i].status == '2' ?'数据重复':'评级结果不符合填写规范'))) +"</td>";
		htmlContent += "</tr>";
	}
	return htmlContent;
}
//返回列表
function backList() {
	 window.location.href = "${ctx}/rateData/list";
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
				<a href="javascript:void(0)">评级数据管理</a>
			</h3>
			<div class="shade main_minHeight">
				<div class="updataRate">
					<div class="updataRate_box">
						<ul class="updataRate_list">
							<li>
								<span>模板</span>
								<div>评级数据上传资料模板</div>
								<a href="javaScript:;" onclick="downloadExcelModel();">下载</a>
							</li>
							<li>
								<span>选择</span>
								<div>选择文件 +</div>
								<input type="file" id="excelFile" name="excelFile" class="updata_file" onchange="fnFile($(this))" />
								<a href="javaScript:;" onclick="submitImport(this)">上传</a>
							</li>
						</ul>
						<div class="main_btn main_btn1">
							<%--<a href="javaScript:;" class="fl" onClick="fnUpdata();">录入</a>--%>
							<a href="javaScript:;" class="fl" style="margin-left: 60px" onClick="cancel();">取消</a>
						</div>
					</div>
					<div class="updataRate_box updataRate_box1">
						<div><img src="image/updata.png" alt="" /></div>
						<p>正在上传，请稍等...</p>
					</div>
				</div>
                <div class="updataRate1">
                    <div class="updata_success">
                        <h2>上传成功！</h2>
                        <p>成功上传数据<span id="successNum"></span>条，失败数据<span id="errNum"></span>条。<a href="javaScript:;" onclick="exportRateData();">下载失败数据</a></p>
                    </div>
                </div>
            </div>
			<div class="main_btn main_hide">
				<a href="javaScript:;" class="fl" onclick="backList();">确定</a>
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
