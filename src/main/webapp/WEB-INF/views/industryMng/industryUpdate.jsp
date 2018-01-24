<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>更新行业 </title>
<script src="${ctx}/resources/js/jquery-1.8.3.min.js"></script>
<!--      谷歌记录历史输入选中，输入框黄色修改  -->
<style type="text/css">
  	input:-webkit-autofill {
	-webkit-box-shadow: 0 0 0px 1000px white inset !important;  
 	-webkit-text-fill-color: #333;
	}
</style>
<script type="text/javascript">
	//保存行业
    function save(){
    	var id = $("#id").val();
		var pid = $("#pid").val();
    	var name1 = $("#name1").val();
		var name2 = $("#name2").val();
		
    	$.ajax({
			 url:"${ctx}/industry/doUpdate",
			 data : JSON.stringify(
				 {
					 "id":id,
					 "pid":pid,
					 "name1":name1,
					 "name2":name2,
				 }		
			 ),
			 type : "POST",
			 dataType : 'json',
			 contentType: 'application/json',
			 success:function(data){
				if(data.result != 1){
					alert("行业更新失败！");

					window.location.href="${ctx}/industry/list";
				}else{
					alert("行业更新成功！");

					window.location.href="${ctx}/industry/list";
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
                 <h5>字典管理<span>／</span>更新行业</h5>
             </div>
             <form id="industryForm" method="post" action="">
             	 <input id="id" name="id" type="hidden" value="${industry2.id}" />
     			 <input id="pid" name="pid" type="hidden" value="${industry1.id}" />
	             <div width="100%" class="audit_parent">
	                 <table style="margin-left: 50px;" id="" class="tb">
						 <tr>
							 <td>一级行业名称</td>
							 <td><input id="name1" name="name1" type="text" value="${industry1.name}" /></td>
						 </tr>
						 <tr>
						 	 <td>二级行业名称</td>
							 <td><input id="name2" name="name2" type="text" value="${industry2.name}" /></td>
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
