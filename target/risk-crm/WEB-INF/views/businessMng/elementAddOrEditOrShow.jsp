<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<title>业务管理-指标管理-因素定义-修改因素定义</title>
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<style type="text/css">
	.input_table input{width:220px;}
</style>
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/myValidate.js"></script>
<script type="text/javascript">
    $(function () {
        var id = $("#eleId").val();

        //新增标记
        if ("" == id) {
            $("#eleId").val("-1");
        }

        //启用状态
		if ($("input[name='state']").val() == 1) {
            $("#state").attr("data-id", "1")
            $("#state").prop("class", "curr factor_w1");
		} else {
            $("#state").attr("data-id", "0")
            $("#state").prop("class", "factor_w1");
		}

    });

    //
	function save() {
	    if ($("#state").attr("data-id") == 1) {
	        $("input[name='state']").val(1);
		} else {
            $("input[name='state']").val(0);
		}

		myValidate(
			{
				formId: 'frm',
				items: ["code", "name"],
				rules: ["require", "require"],
				errorClass: "error_info error_info1",
				success: function () {
                    $.ajax({
                        url:"${ctx}/businessMng/elementSaveOrUpdate",
                        type:"POST",
                        dataType:"json",
                        data:$("#frm").serialize(),
                        success:function (data) {
                            if (200 == data.code) {
                                alert("保存成功", "${ctx}/businessMng/elementIndex?modelId=${model.id}");
                            } else if (400 == data.code){
                                alert(data.msg)
                            } else {
                                alert("保存失败！")
                            }
                        }
                    });
				},
				errorPlacement: function (error, element) {
					var msg = element.parent().prev().children().text().replace(/[\s\t,*:：]/g,"")+""+error.text();
					error.text(msg);
					element.parent().next().append(error);
				}
			}
		);
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
				<a href="javascript:void(0);">业务管理</a>
				<span>></span>
				<a href="${ctx}/businessMng/indexMngIndex">指标管理</a>
				<span>></span>
				<a href="${ctx}/businessMng/elementIndex?modelId=${model.id}">因素定义</a>
				<span>></span>
				<c:if test="${'add' == method}">
					<a href="javascript:void(0);">新建因素</a>
				</c:if>
				<c:if test="${'edit' == method}">
					<a href="javascript:void(0);">修改因素定义</a>
				</c:if>
			</h3>
			<%--<div class="backtrack">
				<a href="javascript:void(0);" onclick="history.go(-1)">返回</a>
			</div>--%>

			<div class="shade">
				 <form id="frm">
					 <input type="hidden" name="id" id="eleId" value="${element.id}" />
					 <input type="hidden" name="modelId" value="${model.id}" />
					 <div class="main_padd">
						 <div class="info_box info_box1 factor clear">
							 <strong>因素所属评分卡</strong>
							 <p>${model.name}</p>
						 </div>
					 </div>
					 <div class="input_list_box factor_box">
						 <table class="input_table">
						 <tbody id="elementConent">
							 <tr>
								 <td><strong>因素编号<font color="red">*</font></strong></td>
								 <td>
									 <input type="text" name="code" id="code" value="${element.code}" maxlength="10" />
								 </td>
								 <td></td>
							 </tr>
							 <tr>
								 <td><strong>因素中文名称<font color="red">*</font></strong></td>
								 <td>
									 <input type="text" name="name" id="name" value="${element.name}" maxlength="16" />
								 </td>
								 <td></td>
							 </tr>
							 <tr>
								 <td><strong>因素英文名称</strong></td>
								 <td>
									 <input type="text" name="enName" id="enName" value="${element.enName}" maxlength="10" />
								 </td>
								 <td></td>
							 </tr>
							 <tr class="mateInfo_table">
								 <td><strong>是否使用</strong></td>
								 <td>
									 <input type="hidden" name="state" value="${element.state}" />
									 <a href="javaScript:;" id="state" data-id="1" class="curr factor_w1">是</a>
								 </td>
							 </tr>
							 <tr>
								 <td><strong></strong></td>
								 <td>
									 <div class="input_btn clear">
										 <a href="javaScript:;" class="fl makeSure" onclick="save();">保存</a>
										 <a href="javascript:history.go(-1);" class="fr cancel">取消</a>
									 </div>
								 </td>
							 </tr>
						 </tbody>
					 </table>
					 </div>
					 <!-- 表格end-->
				 </form>
         	 </div>
			<!-- shade-->
			<!-- footer.html start -->
			<%@ include file="../commons/foot.jsp"%>
			<!-- footer.html end -->
         </div>
     </div>
</div>
</body>
</html>
