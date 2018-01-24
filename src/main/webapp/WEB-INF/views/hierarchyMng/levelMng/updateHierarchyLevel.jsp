<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!doctype html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>分层管理-分层层级管理-修改分层</title>
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!-- <script type="text/javascript" src="js/jquery-1.12.4.js"></script> -->
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
	//利差提示
	$('#layer_icon').hover(function(){
		$(this).parent().next('.layer_content').show();	
	},function(){
		$(this).parent().next('.layer_content').hide();		
	})	
	
	//利差正负数
	var val =Number("${layerLevelBean.floatValue }");
	if(val>0){
		$('.layer_spread').eq(0).show();
		$('.layer_spread').eq(1).hide();
	}else if(val<0){
		$('.layer_spread').eq(1).show();
		$('.layer_spread').eq(0).hide();
	}
	
	//表单验证
	hierarchyLevelFormvalidator();
	
	
	
})

//表单验证
function hierarchyLevelFormvalidator(){
	$.validator.addMethod("isOnlyLayerName", function(value,element) {
		var oldLayerName = "${layerLevelBean.layerName }";
		var layerName = $.trim($("#layerName").val());
		var ok = false;
		if(oldLayerName != layerName){
			$.ajax({
	    		url: "${ctx}/hierarchyManage/isOnlyLayerName",
		        data: JSON.stringify(
		        	{
		        		"layerName":value
		        	}
		        ),
		        dataType : 'json',
				contentType: 'application/json', 
			    type: 'post',
			    async: false,
				success : function(data) {
					var data = eval(data);
					if("1000" == data ){
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
	}, "此名称已存在，请尝试其它名称");
	$.validator.addMethod("checkNumber", function(value,element) {
		var reg=/^(\-?)\d+(\.\d{1,2})?$/;
		if(reg.test(value)==false ){
			return false;
		}
		return true;
	}, "必须为数字,并且最多为两位小数");
	$.validator.addMethod("checkFloatValueLimit", function(value,element) {
		var val =Number(value);
		if(val==0 || val>10 || val<-10){
			return false;
		}
		return true;
	}, "利差范围在-10~10之间,但不能为0");
	//新增弹窗验证
	$("#hierarchyLevelForm").validate({
		rules: {
			layerName:{
				required: true,
				isOnlyLayerName:true
			},floatValue:{
				required: true,
				checkNumber:true,
				checkFloatValueLimit:true
			},floatUp:{
				checkNumber: true
			},floatDown:{
				checkNumber: true
			}
		},
		messages: {
		  layerName:{
              required:"请输入层级名称"
          },
          floatValue:{
              required:"请输入利差（%）"
          },
          floatUp:{
              required:"利差上限（%）"
          },
          floatDown:{
              required:"利差下限（%）"
          }
	    },
	 	errorPlacement: function(error, element) { 
	 		if(element.is("input[name=layerName]")){
	 			error.appendTo($("#layerName_error")); 
	 		}
	 		if(element.is("input[name=floatValue]")){
	 			error.appendTo($("#floatValue_error")); 
	 		}
	 		if(element.is("input[name=floatUp]")){
	 			error.appendTo($("#floatUp_error")); 
	 		}
	 		if(element.is("input[name=floatDown]")){
	 			error.appendTo($("#floatDown_error")); 
	 		}
	 	},
	});
}


//利差正负数
function fnNumber(obj){
	var val =Number($(obj).val());
	if(val>0){
		$('.layer_spread').eq(0).show();
		$('.layer_spread').eq(1).hide();
	}else if(val<0){
		$('.layer_spread').eq(1).show();
		$('.layer_spread').eq(0).hide();
	}
}

/**
 * 保存
 */
function save(){
	if($("#hierarchyLevelForm").valid()){
		var levelId = $("#levelId").val();
		var href = "${ctx}/hierarchyManage/toHierarchyManagerPage";
		if(null != levelId && "" != levelId){
			$.ajax({
				url : "${ctx}/hierarchyManage/saveUpdateHierarchyLevel",
				type : 'POST',
				data : JSON.stringify(
					{
						"levelId":levelId,
						"layerName":$("#layerName").val(),
						"floatValue":$("#floatValue").val(),
						"floatUp":$("#floatUp").val(),
						"floatDown":$("#floatDown").val()
					}		
				),
				dataType : 'json',
				contentType: 'application/json',
				async: false,
				success : function(data) {
					if (data == "1000") {
						alert("修改成功!",href);
					}else{
						alert("修改失败!",href);
					}
				}
			});
		}else{
			alert("修改失败！",href);
		}
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
                <a href="${ctx}/hierarchyManage/toHierarchyManagerPage">分层层级管理</a>
                <span>></span>
                <a href="${ctx }/hierarchyManage/toUpdateHierarchyLevelPage?levelId=${levelId }">修改分层</a>
            </h3>
            <div class="shade main_minHeight">
                <div class="input_list_box">
                    <form id="hierarchyLevelForm">
                    	<input type="hidden" id="levelId" value="${levelId }">
                        <table class="input_table layer_table">
                            <tbody>
                                <tr>
                                    <td><strong>层级名称<i style="margin-left:4px;">*</i></strong></td>
                                    <td> 
                                        <input id="layerName" name="layerName" value="${layerLevelBean.layerName }" type="text" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" maxlength="30"/>
                                    </td>
                                    <td><p id="layerName_error" class="myError_info"></p></td>
                                </tr>
                                <tr>
                                    <td>
                                    	<strong>
                                    		利率可浮动时利差（%）
                                    		<i>*</i>
                                    		<img src="${ctx}/resources/image/layer_icon.png" alt="" class="layer_icon" id="layer_icon" />
                                    	</strong>
                                    	<div class="layer_content" >
                                    		<p class="icon_tip">利差填写说明</p>
                                    		<p>利差范围在-10~10之间,但不能为0</p>
                                    	</div>
                                    </td>
                                    <td>
                                        <input id="floatValue" name="floatValue" value="${layerLevelBean.floatValue }" type="text" onblur="fnNumber($(this))" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                                    </td>
                                    <td><p id="floatValue_error" class="myError_info"></p></td>
                                </tr>
                                <tr class="layer_spread">
                                    <td><strong>利差上限（%）<i>*</i></strong></td>
                                    <td>
                                        <input id="floatUp" name="floatUp" value="${layerLevelBean.floatUp }" type="text" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                                    </td>
                                    <td><p id="floatUp_error" class=" myError_info"></p></td>
                                </tr>
                                <tr class="layer_spread">
                                    <td><strong>利差下限（%）<i>*</i></strong></td>
                                    <td>
                                        <input id="floatDown" name="floatDown" value="${layerLevelBean.floatDown }" type="text" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                                    </td>
                                    <td><p id="floatDown_error" class=" myError_info"></p></td>
                                </tr>
                                <tr>
                                    <td><strong></strong></td>
                                    <td>
                                        <div class="input_btn clear">
                                            <a href="javaScript:save();" class="fl makeSure">保存</a>
                                            <a href="${ctx}/hierarchyManage/toHierarchyManagerPage" class="fr cancel">取消</a>
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
