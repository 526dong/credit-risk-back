<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!doctype html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>
分层管理-相关性管理-修改后台行业
</title>

<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!-- <script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script> -->
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
<style type="text/css">
.input_table input{width:222px;}

.myError_info {
/*     background: url("${ctx}/resources/image/error.png") 0 3px no-repeat; */
    color: #ed7563;
    font-size: 12px;
    height: 20px;
    line-height: 20px;
    text-indent: 18px;
    position: inherit;
    left: 10px;
    bottom: 0;
    z-index: 1;
}

</style>
<script type="text/javascript">
/*页面初始化*/
$(function() {
	
	//表单验证
	//登录名验证
	$.validator.addMethod("isOnlyBgInsdustryName", function(value,element) {
		var oldBgInsdustryName = "${insdustryBean.insdustryName }";
		var bgInsdustryName = $.trim($("#bgInsdustryName").val());
		var ok = false;
		if(oldBgInsdustryName != bgInsdustryName){
			$.ajax({
	    		url: "${ctx}/insdustryManage/isOnlyBgInsdustryName",
		        data: JSON.stringify(
		        	{
		        		"bgInsdustryName":value
		        	}
		        ),
		        dataType : 'json',
				contentType: 'application/json', 
			    type: 'post',
			    async: false,
				success : function(data) {
					var data = eval(data);
					if("1000" == data){
						ok = true;
					}else{
						ok = false;
					}
				},
	            error: function (data) {
	                ok = false;
	            }
			})
		}else{
			ok = true;
		}
		return ok;
	}, "此行业名称已存在，请尝试其它名称");
	//新增弹窗验证
	$("#bgInsdustryForm").validate({
		rules: {
			bgInsdustryName:{
				required: true,
				isOnlyBgInsdustryName:true
			}
		},
		messages: {
		  bgInsdustryName:{
              required:"请输入用户名"
          }
	    },
	 	errorPlacement: function(error, element) { 
	 		if(element.is("input[name=bgInsdustryName]")){
	 			error.appendTo($("#bgInsdustryName_error")); 
	 		}
	 	},
	});
})

//保存修改
function saveUpdateBgInsdustry(){
	if($("#bgInsdustryForm").valid()){
		$.ajax({
			url : "${ctx}/insdustryManage/updateBgInsdustry",
			type : 'POST',
			data : JSON.stringify(
				{
					"bgInsdustryId":$("#bgInsdustryId").val(),
					"bgInsdustryName":$("#bgInsdustryName").val()
				}		
			),
			dataType : 'json',
			contentType: 'application/json',
			success : function(data) {
				var href = "${ctx}/insdustryManage/toCorrelationSetPage?tabFlag=tabFlag";
				if (data == "1000") {
					alert("行业修改成功!",href);
				}else{
					alert("行业修改失败!",href);
				}
			}
		});
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
                <a href="${ctx}/insdustryManage/toCorrelationSetPage">相关性管理</a>
                <span>></span>
                <a href="${ctx}/insdustryManage/toCorrelationSetPage?tabFlag=tabFlag">后台行业</a>
                <span>></span>
                <a href="${ctx}/insdustryManage/toUpdateBgInsdustryPage?bgInsdustryId=${bgInsdustryId }">修改后台行业</a>
            </h3>
            <div class="shade main_minHeight">
				<div class="input_list_box">
                    <form id="bgInsdustryForm">
                    <input type="hidden" id="bgInsdustryId" value="${bgInsdustryId }">
                        <table class="input_table">
                            <tbody>
                                <tr>
                                    <td><strong>后台行业名称</strong></td>
                                    <td>
                                        <input value="${insdustryBean.insdustryName }" type="text" id="bgInsdustryName" name="bgInsdustryName" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" maxlength="30">
                                    </td>
                                    <td><p id="bgInsdustryName_error" class="myError_info"></p></td>
                                </tr>
                                <tr>
                                    <td><strong></strong></td>
                                    <td>
                                        <div class="input_btn clear">
                                            <a href="javaScript:saveUpdateBgInsdustry();" class="fl makeSure">保存</a>
                                            <a href="${ctx}/insdustryManage/toCorrelationSetPage?tabFlag=tabFlag" class="fr cancel">取消</a>
                                        </div>
                                    </td>
                                </tr>
                                
                            </tbody>
                        </table>
                    </form>	
                </div>
            </div>
            <!-- footer.html start -->
            <%@ include file="../../commons/foot.jsp"%>
            <!-- footer.html end -->
        </div>
        <!-- 右侧内容.html end -->
    </div>
    <!-- center.html end -->
</div>

</body>
</html>
