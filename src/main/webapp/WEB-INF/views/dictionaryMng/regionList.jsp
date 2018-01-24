<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>地区管理 </title>

<!-- Jquery组件引用 -->
<script src="${ctx}/resources/h+/js/jquery.min.js"></script>
<link href="${ctx}/resources/h+/css/style.css?v=4.1.0" rel="stylesheet">
<!-- bootstrap组件引用 -->
<script src="${ctx}/resources/h+/js/bootstrap.min.js?v=3.3.6"></script>
<link href="${ctx}/resources/h+/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
<!-- bootstrap table组件以及中文包的引用 -->
<script src="${ctx}/resources/h+/js/plugins/bootstrap-table/bootstrap-table.min.js"></script>
<link href="${ctx}/resources/h+/css/plugins/bootstrap-table/bootstrap-table.min.css" rel="stylesheet">
<script src="${ctx}/resources/h+/js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>

<link href="${ctx}/resources/h+/css/animate.css" rel="stylesheet">
<link href="${ctx}/resources/h+/css/font-awesome.css?v=4.4.0" rel="stylesheet">

<!-- Data Tables -->
<script src="${ctx}/resources/h+/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${ctx}/resources/h+/js/plugins/dataTables/dataTables.bootstrap.js"></script>
<link href="${ctx}/resources/h+/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
<script	src="${ctx}/resources/h+/js/plugins/layer/laydate/laydate.js"></script>

<!-- Data Tables -->
<script src="${ctx}/resources/h+/js/plugins/validate/jquery.validate.min.js"></script>
<script src="${ctx}/resources/h+/js/plugins/validate/messages_zh.min.js"></script>

<script src="${ctx}/resources/h+/js/plugins/sweetalert/sweetalert.min.js"></script>
<link href="${ctx}/resources/h+/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">


<style type="text/css">
.popup_table1{ width:100%;}
.popup_table1 td{ height:40px; line-height:40px; border:1px solid #f0f0f6;}
.popup_table1 .popup_td1{ background:#f9f9fb; width:98px; text-indent:16px;}
.popup_table1 .popup_td2{text-indent:16px;}

</style>
<!--      谷歌记录历史输入选中，输入框黄色修改  -->
<style type="text/css">
  	input:-webkit-autofill {
	-webkit-box-shadow: 0 0 0px 1000px white inset !important;  
 	-webkit-text-fill-color: #333;
	}
</style>
<script type="text/javascript">

</script>
</head>
<body >
<div class="row">
    <div class="col-sm-12">
        <div class="ibox float-e-margins">
            <div class="ibox-title">
                <h5>字典管理<span>／</span>地区管理</h5>
            </div>
            <div class="ibox-content">
             	<a href="javaScript:;" onclick="addRegion();" class="btn btn-primary" >新增地区</a>
            </div>
            <div class="ibox-content">
                <table id="regionListTable"></table>
        	</div>
        </div>
    </div>
</div>
<script type="text/javascript">
$(function() {
	//1.初始化Table
	var oTable = new TableInit();
	oTable.Init();
});


//初始化Table
var TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
    	$('#regionListTable').bootstrapTable({
	    	url:"${ctx}/dictionary/findAllRegion",   //请求后台的URL（*）
			method : 'post', //请求方式（*）
			toolbar : '#toolbar', //工具按钮用哪个容器
			striped : true, //是否显示行间隔色
			cache : false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination : true, //是否显示分页（*）
	 		//singleSelect  : true,  // 单选checkbox 
			queryParams : oTableInit.queryParams,//传递参数（*）
			sidePagination : "server", //分页方式：client客户端分页，server服务端分页（*）
			pageNumber : 1, //初始化加载第一页，默认第一页
			pageSize : 10, //每页的记录行数（*）
			pageList : [ 10, 25, 50, 100 ], //可供选择的每页的行数（*）
			clickToSelect : true, //是否启用点击选中行
			uniqueId : "name", //每一行的唯一标识，一般为主键列
			columns : [{
				field : 'province',
				title : '省',
				align : 'center',
	            formatter: function (value, row, index) {
	               var result = "";
	        	   if(null == value || "" == value || typeof(value)=="undefined" ){
	        		   result=""; 
	               }else{
	            	   result =  value;
	               }
	        	   return result;
	        	   
	           }
			},{
				field : 'city',
				title : '市',
				align : 'center',
	            formatter: function (value, row, index) {
	               var result = "";
	        	   if(null == value || "" == value || typeof(value)=="undefined" ){
	        		   result=""; 
	               }else{
	            	   result =  value;
	               }
	        	   return result;
	           }
			},{
				field : 'county',
				title : '区',
				align : 'center',
	            formatter: function (value, row, index) {
	               var result = "";
	        	   if(null == value || "" == value || typeof(value)=="undefined" ){
	        		   result=""; 
	               }else{
	            	   result =  value;
	               }
	        	   return result;
	           }
			},/* {
				field : 'address',
				title : '详细地址',
				align : 'center',
	            formatter: function (value, row, index) {
	               var result = "";
	        	   if(null == value || "" == value || typeof(value)=="undefined" ){
	        		   result=""; 
	               }else{
	            	   result =  value;
	               }
	        	   return result;
	           }
			}, */{
				field : 'center',
				title : '操作',
				align : 'center',
				events : operateEvents,
				formatter : operateFormatter
			}] 
		});
	};

	//得到查询的参数
	oTableInit.queryParams = function(params) {
		
		var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			limit : params.limit, //页面大小
			offset : params.offset, //页码
			/* startDate : $("#startDate").val(),//-开始时间
			endDate : $("#endDate").val(),//-结束时间
			searchContent : $("#searchContent").val()// */
		};
		return temp;
	};
	
	//点击按钮
	window.operateEvents = {
		//点击编辑
		'click .editIns' : function(e, value, row, index) {
			/* window.location.href = "${ctx}/dictionary/updateRegion?id="+row.id; */
		},
		//点击删除
		'click .deleteIns' : function(e, value, row, index) {
			var flag = confirm("是否删除已选地区");
			if(flag == true){
				$.ajax({
					 url:"${ctx}/dictionary/deleteRegion?id="+row.id,
					 type:'POST',
					 success:function(data){
						 if(data){
							if(data.result != 1){
								swal({
				                    title: "地区删除失败",
				                    text: "",
				                    type: "error",
				                    confirmButtonText: "确定"
				                }, function(){
				                	$('#regionListTable').bootstrapTable('refresh');
				                });
							}else{
								swal({
				                    title: "地区删除成功",
				                    text: "",
				                    type: "success",
				                    confirmButtonText: "确定"
				                }, function(){
				                	$('#regionListTable').bootstrapTable('refresh');
				                });
							}
						 }
					 }
				});
			}
			
		}
	};
	
	//操作
	function operateFormatter(value, row, index) {
		var operate = '';
		operate = '<button type="button" class="btn btn-info editIns">修改</button>&nbsp<button type="button" class="btn btn-sm btn-info deleteIns">删除</button>';
		return operate;
	}
	
	return oTableInit;
	};

/* //查询数据
function searchInsList() {
	$('#regionListTable').bootstrapTable('refresh');
} */

</script>
</body>
</html>
