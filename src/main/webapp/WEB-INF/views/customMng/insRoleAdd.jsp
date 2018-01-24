<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!doctype html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>客户管理-账号管理-角色管理-创建角色 </title>

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
	$.validator.addMethod("checkRoleName", function(value,element) {
		var roleName = $.trim($("#name").val());
		var flag = false;
		$.ajax({
			type : 'POST',
			url : '${ctx}/custom/checkRoleName',
			async:false, 
			data : {
				"roleName" : roleName,
				"insId" : $("#insId").val()
			},
			success : function(data) {
				if(null !=data && typeof(data) != "undefined" && "" != data && "0000"==data){
					flag = false;
				}else{
					flag = true;
				}
			}
		})
		return flag;
	}, "角色已存在");
	$.validator.addMethod("checkRoleName2", function(value,element) {
		var roleName = $.trim($("#name").val());
		//var reg=/^[a-zA-Z0-9]{8,18}$/; 
		var reg=/^[\u4e00-\u9fa5a-zA-Z]{1,30}$/
		if(reg.test(roleName)){
			return true;
		}else{
			return false;
		}
		return true;
	}, "仅支持30字以内中英文");
	$("#insRoleAddForm").validate({
		rules: {
			name:{
				required: true,
				checkRoleName: true,
		      	checkRoleName2: true
			}
		},
		messages: {
	      name:{
              required:"请输入角色名称"
          }
		},
	 	errorPlacement: function(error, element) { 
	 		if(element.is("input[name=name]")){
	 			error.appendTo($("#name_error")); 
	 		}
	 	},
	});
})

//保存新增角色
function saveAddRole(){
	if($("#insRoleAddForm").valid()){
		$.ajax({
			url : "${ctx}/custom/saveInsRoleAdd",
			type : 'POST',
			data : {
					"companyId":$("#insId").val(),
					"name":$("#name").val(),
					"description":$("#description").val()
				},
			dataType : 'json',
			success : function(data) {
				var href = "${ctx}/custom/toInsRoleManagePage?id="+$("#insId").val();
				if (data == "1000") {
					alert("角色创建成功",href);
				}else{
					alert("角色创建失败",href);
				}
			}
		});
	}
}

//多行文本输入框剩余字数计算  
function checkMaxInput(obj, maxLen) {  
    if (obj == null || obj == undefined || obj == "") {  
        return;  
    }  
    if (maxLen == null || maxLen == undefined || maxLen == "") {  
        maxLen = 50;  
    }  
    var strResult;  
    var $obj = $(obj);   

    if (obj.value.length > maxLen) { //如果输入的字数超过了限制  
        obj.value = obj.value.substring(0, maxLen); //就去掉多余的字  
        $("#addRole_word span").html(0);//计算并显示剩余字数  
    }else {  
    	$("#addRole_word span").html(maxLen - obj.value.length);//计算并显示剩余字数  
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
                <a href="javascript:void(0);">客户管理</a>
                <span>></span>
                <a href="${ctx}/custom/insAccountManager">账号管理</a>
                <span>></span>
                <a href="${ctx}/custom/toInsRoleManagePage?id=${insId }">角色管理</a>
                <span>></span>
                <a href="${ctx}/custom/toInsRoleAddPage?insId=${insId }">创建角色</a>
            </h3>
            <div class="backtrack">
            	<a href="${ctx}/custom/toInsRoleManagePage?id=${insId }">返回</a>
            </div>
            <div class="shade main_minHeight">
            	<div class="input_list_box">
                    <form id="insRoleAddForm">
                    	<input type="hidden" id="insId" value="${insId }" >
                        <table class="input_table">
                            <tbody>
                                <tr>
                                    <td><strong>角色名称<i>*</i></strong></td>
                                    <td style="width:222px;">
                                        <input value="" type="text" id="name" name="name" placeholder="请输入角色名称" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" onkeydown="if(event.keyCode==13) return false;"/>
                                    </td>
                                    <td><p class="myError_info" id="name_error"></p></td>
                                </tr>
                                <tr style="vertical-align:top">
                                    <td><strong class="st1">角色简介</strong></td>
                                    <td colspan="2">
                                        <textarea id="description" name="description" placeholder="请输入角色描述"
                                        onkeydown="checkMaxInput(this,50)" onkeyup="checkMaxInput(this,50)" onfocus="checkMaxInput(this,50)" onblur="checkMaxInput(this,50);"></textarea>
                                        <div class="addRole_word" id="addRole_word"><span>50</span>字以内</div>
                                    </td>
                                </tr>
                                <tr>
                                    <td><strong></strong></td>
                                    <td>
                                        <div class="input_btn clear">
                                            <a href="javaScript:saveAddRole();" class="fl makeSure">保存</a>
                                            <a href="${ctx}/custom/toInsRoleManagePage?id=${insId }" class="fr cancel">取消</a>
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
<!-- 成功标识.html start -->
<div class="popup popup2" id="popup2">
	<a href="javaScript:;" class="close"></a>
    <p></p>
    <div class="popup_btn delete_btn">
    	<a href="javaScript:;" class="a1 fl">确定</a>
    </div>
</div>
<!-- 成功标识.html end -->
</body>
</html>
