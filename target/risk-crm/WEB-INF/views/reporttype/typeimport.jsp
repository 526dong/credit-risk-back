<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>业务管理-报表类型管理-上传报表类型</title>
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/ajaxfileupload.js"></script>
	<style>
		.updataRate_box{
			width: 460px;
			height: auto;
		}
		.updataRate_list li span{
			width: 100px;
		}
		.updataRate_list li div, .updata_file{
			width: 290px;
		}
		.updataRate_box .main_btn1{
			margin-top: 20px;
		}
		.download_example{
			display: block;
			line-height: 14px;
			height: 14px;
			margin-left: 100px;
			color: #38abf8;
			font-size: 0;
			cursor: pointer;
		}
		.download_example span{
			font-size: 12px;
			vertical-align: middle;
		}
		.download_example b{
			margin-left: 5px;
			display: inline-block;
			width: 10px;
			height: 10px;
			background: url(${ctx}/resources/image/download_example.png) no-repeat;
			vertical-align: middle;
		}
	</style>
<script type="text/javascript">
	/*导入 start*/
	//下载Excel模板
	function downloadExcelModel() {
		window.location.href = "${ctx}/rtype/download";
	}
	//下载原模板
	function downloadOldModel() {
	    var typeId='${typeId}';
	    if (typeId==null){
	        alert("无法下载");
	        return;
		}
		window.location.href = "${ctx}/rtype/downloadType?id="+typeId;
	}
    //下载错误数据
    function exportRateData() {
        window.location.href = "${ctx}/rateData/exportRateData";
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
        var isXlsx = filename.substring(filename.indexOf('.') + 1,filename.length);
        if (fileLen > 0) {
            /*if (isXlsx == "xlsx") {
             alert("请上传xls格式模板！");
             $(fileName).prev().text("+ 添加文件");
             $(fileName).val("");
             return;
             } else {*/
            if (null != filename && "" != filename && typeof (filename) != "undefined") {
                $("#fileNameId").text(filename);
            } else {
                $("#fileNameId").text("");
            }
            /*}*/
        } else {
            alert("上传文件格式错误！请上传.xls文件");
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
	function submitImport() {
        var filename=$("#filename").val();
        if (filename==null||filename==''){
            alert("类型不能为空");
            return;
		}
        if ($("#excelFile").prev().text() != "选择文件 +") {
            $('.updataRate_box').hide();
            $('.updataRate_box1').show();
            var typeId='${typeId}';
            var url=typeId ==null?'import':'update';
			$.ajaxFileUpload({
				url : "${ctx}/rtype/"+url,
				secureuri : false,//是否需要安全协议
				fileElementId : 'excelFile',
                contentType : "application/json",
				dataType : "json",
				type : 'POST',
                data :{
				  name:filename,
				  oldTypeId:typeId
				},
				async : true,
				complete : function() {
				},
				success : function(data) {
                    $('.updataRate_box1').hide();
                    //导入弹框初始化
                   // initFile(obj);
					if(data){
                        var reData=eval('(' +  checkJson(data) + ')');
                        //成功导入了数据，不代表没有错误数据
						if(reData.code == 100){
                            successJump(reData);
						} else {
						    //没有导入数据
							if (reData.msg) {
                                    alert(reData.msg);
							}
                            $('.updataRate_box').show();
                            $('.updataRate_box1').hide();
						}
					}
				}
			});
		} else {
            alert("导入文件不能为空！");
		}
	}
	//成功失败跳转
	function successJump(data) {
        //成功条数
       // $("#successNum").html(data.successNum);
        //失败条数
        //$("#errNum").html(data.errNum);
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
                //错误类型：0-必填未填，1-格式不正确
                htmlContent += "<td>"+(data[i].errType == '0' ? '必填未填' : '格式不正确')+"</td>";
            htmlContent += "</tr>";
        }
        return htmlContent;
    }
    //返回列表
    function backList() {
         window.location.href = "${ctx}/rtype/index";
    }
    function  checkJson(json) {
		if (json.indexOf("<pre")>-1){
            json = json.substring(json.indexOf("{"),json.length);
		}
		return json;
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
								<span>报表类型名称</span>
								<div><input type="text" id="filename" value="${typeName}"></div>
							</li>

							<li>
								<span>上传报表模板</span>
								<div>选择文件 +</div>
								<input type="file" id="excelFile" name="excelFile" class="updata_file" onchange="fnFile($(this))" />
<%--
								<a href="javaScript:;" onclick="submitImport(this)">上传</a>
--%>
							</li>
						</ul>
						<a class="download_example"  href="javaScript:;" onclick="downloadExcelModel();"><span>下载模板示例</span><b></b></a>
						<c:if test="${typeId!=null}">
							<a class="download_example"  href="javaScript:;" onclick="downloadOldModel();"><span>下载原模板</span><b></b></a>
						</c:if>
						<div class="main_btn main_btn1">
							<a href="javaScript:;" class="fl" onClick="submitImport();">录入</a>
							<a href="javaScript:;" class="fl"  onClick="cancel();">取消</a>
						</div>
					</div>
					<div class="updataRate_box updataRate_box1">
						<div><img src="${ctx}/resources/image/updata.png" alt="" /></div>
						<p>正在上传，请稍等...</p>
					</div>
				</div>
                <div class="updataRate1">
                    <div class="updata_success">
                        <h2>上传成功！</h2>
<%--
                        <p>成功上传数据<span id="successNum"></span>条，失败数据<span id="errNum"></span>条。<a href="javaScript:;" onclick="exportRateData();">下载失败数据</a></p>
--%>
                    </div>
                    <%--<div class="module_height">
                        <form>
                            <table class="module_table">
                                <thead>
                                <tr>
                                    <th class="module_width1">序号</th>
                                    <th>失败行</th>
                                    <th>失败列</th>
                                    <th>失败类型</th>
                                </tr>
                                </thead>
                                <tbody id="errContent"></tbody>
                            </table>
                        </form>
                    </div>--%>
                    <%--<!-- 分页.html start -->
                    <%@include file="../commons/tabPage.jsp" %>
                    <!-- 分页.html end -->--%>
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
