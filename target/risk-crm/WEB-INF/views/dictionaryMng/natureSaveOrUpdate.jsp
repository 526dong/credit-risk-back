<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加或更新企业性质 </title>
<script src="${ctx}/resources/js/jquery-1.8.3.min.js"></script>
<!--      谷歌记录历史输入选中，输入框黄色修改  -->
<style type="text/css">
  	input:-webkit-autofill {
	-webkit-box-shadow: 0 0 0px 1000px white inset !important;  
 	-webkit-text-fill-color: #333;
	}
</style>
<script type="text/javascript">

	//保存企业性质
    function save(){
    	$.ajax({
			 url:"${ctx}/dictionary/doSaveOrUpdateNature",
			 data : $("#natureForm").serialize(),
			 type : "POST",
			 success:function(data){
				if(data.code == 200){
					alert("企业性质添加或更新成功！");

					window.location.href="${ctx}/dictionary/nature";
				}else{
					alert("企业性质添加或更新失败！");

					window.location.href="${ctx}/dictionary/nature";
				}
			 }
		});
    }
	
	//取消
	function cancel(){
		history.back(-1);
	}
	
</script>
</head>
<body>
<div class="row">
     <div class="col-sm-12">
         <div class="ibox float-e-margins">
             <div class="ibox-title">
                 <h5>字典管理<span>／</span>企业性质</h5>
             </div>
             <form id="natureForm" method="post" action="">
             	 <input id="id" name="id" type="hidden" value="${nature.id}">
	             <div width="100%" class="audit_parent">
	                 <table style="margin-left: 50px;" id="" class="tb">
						 <tr>
						 	 <td>性质名称：</td>
							 <td>
							 	<input id="name" name="name" type="text" value="${nature.name}" />
							 </td>
						 </tr>
					 </table>
	         	 </div>
				 <div style="margin-top: 50px;margin-left: 120px;">
					<a style="margin-right: 20px;" href="javascript:void(0);" onclick="save();">保存</a>
					<a href="javascript:void(0);" onclick="cancel();">取消</a>
				 </div>
			 </form>
         </div>
     </div>
</div>
</body>
</html>
