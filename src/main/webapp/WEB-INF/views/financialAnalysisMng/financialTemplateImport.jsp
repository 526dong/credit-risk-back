<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>业务管理-财务分析管理-新增财务分析模板</title>
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/ajaxfileupload.js"></script>
<script type="text/javascript">
$(function(){
	$("#reportType_select_box").on("click","li",function(){
		$("#reportTypeId").val($(this).attr("data-id"));
	})
})
  
  	//导入弹框初始化
    function initFile(obj) {
		$(obj).prev().html("选择文件<strong>+</strong>");
	    $(obj).val("");
	    $("#templateName").html("");
    }
    //添加文件
    function fnFile(obj){
        checkFile(obj);
    }
	//导入校验后缀
	function checkFile(obj) {
	    var filename = $(obj).val();
	    var fileLen = filename.indexOf('.xls');
	    var isXlsx = filename.substring(filename.indexOf('.') + 1,filename.length);
	    if (fileLen > 0 && (isXlsx == "xls"||isXlsx == "xlsx")) {
	    	var urlArr = $(obj).val().split("\\");        //截取路径
		 	var getName = urlArr[urlArr.length - 1]; //获取文件的名字
		 	$(obj).prev().html(getName);
	    	var fileNameStr = getName.substring(0,getName.indexOf('.xls'));
	    	$("#templateName").html(fileNameStr);
	    } else {
	        $(obj).prev().html("选择文件<strong>+</strong>");
	        $(obj).val("");
	        $("#templateName").html("");
	        alert("上传文件格式错误！请上传.xls或者.xlsx文件");
	        return;
	    }
	}
    
	//提交导入
	function submitImport(obj) {
		var reportTypeId = $("#reportTypeId").val();
		if(null == reportTypeId || "" == reportTypeId || "0000" == reportTypeId){
			alert("请选择报表类型！");
		}else{
			var fileFlag = $("#fileFlag").text();
			if ( null == fileFlag || "" == fileFlag || typeof(fileFlag)=="undefined" || "选择文件+" == fileFlag ) {
				alert("请上传模板！");
			}else{
				$.ajaxFileUpload({
					url : "${ctx}/financialAnaly/importFinancialTemplate",
                    secureuri : false,//是否需要安全协议
                    fileElementId : 'excelFile',
                    contentType : "application/json",
                    dataType : "json",
                    type : 'POST',
                    data:{
                        reportTypeId:reportTypeId
                    },
					async : true,
					success : function(data) {
	                    //导入弹框初始化
	                    initFile(obj);
						if(data){
							var reData = eval('(' + data + ')');
	                        //成功导入了数据，不代表没有错误数据
							if(reData.msg == 0){
								fnUpdata();
							} else {
								//没有导入数据
							    $("#fileFlag").html("选择文件<strong>+</strong>");
						        $("#excelFile").val("");
						        $("#templateName").html("");
	 							if (reData.msg) {
	 								 if (reData.msg == 1) {
	                                     alert("上传文件为空，请重新上传！");
	                                 } else if (reData.msg == 2) {
	                                     alert("上传文件中只有标题没有数据，请重新上传！");
		 							 } else if (reData.msg == 3) {
	                                     alert("上传文件扩展名是不允许的扩展名，请重新上传");
	 								 } else if (reData.msg == 4) {
	                                     alert("上传失败，请联系管理员！");
	                                 } else if (reData.msg == 5) {
	                                     alert("上传的文件数据在数据库中已经存在！");
	                                 } else if (reData.msg == 6) {
	                                     alert("上传的文件数据解析校验报错！");
	                                 }else if (reData.msg == 7) {
	                                     alert("上传模板与报表模板比对校验报错！");
	                                 } else if (reData.msg == 8) {
	                                     alert("上传模板与报表模板比对校验不匹配！");
	                                 } else if (reData.msg == 9) {
	                                     alert("传参错误！");
	                                 } else if (reData.msg == 10) {
	                                     alert("模板中第1列第"+reData.falseNum+"行，字段为：<"+reData.falseKey+">不符合要求！");
	                                 } else{
	                                	 alert("上传失败，请联系管理员！");
	                                 }
	 							}
							}
						}
					}
				});
			}
		}
	}
	
	//上传成功
	function fnUpdata(){
		$('.updataRate_box').hide();
		$('.updataRate_box1').show();
		setTimeout(function(){
			$('.updataRate_box1').hide();
			$('.updataRate').hide();
			$('.updataRate1').show();
			$('.main_hide').show();	
		},200);
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
				<a href="${ctx }/financialAnaly/financialTemplateList">财务分析管理</a>
				<span>></span>
				<a href="${ctx }/financialAnaly/toImportFinancialTemplate">新增财务分析模板</a>
			</h3>
			<div class="backtrack">
            	<a href="${ctx }/financialAnaly/financialTemplateList">返回</a>
            </div>
            <div class="shade main_minHeight">
            	<div class="updataRate">
                    <div class="updataRate_box">
                    	<ul class="updataRate_list">
                            <li>
                                <span>报表类型</span>
                                <div class="report_select">
                                	<input type="hidden" id="reportTypeId">
                                    <em class="select_value">请选择</em>
                                    <i class="report_select_icon"><i></i></i>
                                    <ul class="report_options" id="reportType_select_box">
                                        <li data-id="0000">请选择</li>
                                        <c:forEach var="item" items="${reportTypeList }">
											<li data-id="${item.reportTypeId }">${item.reportTypeName }</li>
										</c:forEach>
                                    </ul>
                                </div>
                            </li>
                        	<li>
                            	<span>模板名称</span>
                                <div id="templateName"></div>
                            </li>
                            <li>
                            	<span>上传模板</span>
                                <div id="fileFlag">选择文件<strong>+</strong></div>
                                <input type="file" class="updata_file" id="excelFile" name="excelFile" onchange="fnFile($(this))"/>
<!--                                 <a href="javaScript:;">上传</a> -->
                            </li>
                        </ul>
<!--                         <a class="download_example"><span>下载模板示例</span><b></b></a> -->
                        <div class="main_btn main_btn1">
                            <a href="javaScript:;" class="fl" onclick="submitImport(this)">上传</a>
                            <a href="${ctx }/financialAnaly/financialTemplateList" class="fr">取消</a>
                        </div>
                    </div>
                    <div class="updataRate_box updataRate_box1">
                    	<div><img src="${ctx}/resources/image/updata.png" alt="" /></div>
                        <p>正在上传，请稍等...</p>	
                    </div>
                </div>
                <div class="updataRate1 clear">
                	<div class="updata_success">
                    	<h2>上传成功！</h2>
<!--                         <p>成功上传数据<span>1000</span>条，失败数据<span>20</span>条。<a href="javaScript:;">下载失败数据</a></p> -->
                    </div>
                	<div class="main_btn main_hide">
                        <a href="${ctx }/financialAnaly/financialTemplateList" class="fl">确定</a>
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
<script>
    $('.report_select').click(function(e){
        var _this = $(this);
        var options =  _this.find('.report_options');
        _this.find('.report_select_icon i').toggleClass('act');
        options.slideToggle();
        $(document).one('click',function(e){
             options.slideUp();
            _this.find('.report_select_icon i').removeClass('act');
        })
        options.on('click','li',function(e){
            _this.find('.select_value').text($(this).text());
            options.slideUp();
            _this.find('.report_select_icon i').removeClass('act');
            e.stopPropagation();
        })
        e.stopPropagation();
    })
</script>
</body>
</html>
