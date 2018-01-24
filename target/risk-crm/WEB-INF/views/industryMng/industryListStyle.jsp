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
<title>行业管理 </title>

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

<!-- 自动补全 -->
<script src="${ctx}/resources/js/autocomplete/jquery.autocomplete.js"></script>
<script src="${ctx}/resources/js/autocomplete/browser.js"></script>

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
                 <h5>字典管理<span>／</span>行业管理</h5>
             </div>
             <div class="ibox-content">
             	 <a href="javaScript:;" onclick="addIndustry();" class="btn btn-primary" >新增行业</a>
             	 <a style="margin-left: 20px;" href="javaScript:;" onclick="batchMatchIndustry();" class="btn btn-primary" >统一行业匹配</a>
             </div>
             
             <div>
             	<ul id="ul_cla" class="fl clear" style="list-style-type: none;margin-left:55px;padding:0px;">
					<li style="margin:7px;padding:5px;float:left;width:150px;height:25px;">
						<a href="javaScript:;" onclick="industry();">前台行业</a>
					</li>
					<li style="margin:7px;padding:5px;float:left;width:150px;height:25px;">
						<a href="javaScript:;" onclick="backIndustry();">后台行业</a>
					</li>
             	</ul>
             </div>
             
             <div id="industryDiv" class="ibox-content" style="display: none">
                 <table id="industryListTable"></table>
         	 </div>
         	 
         	 <div id="backIndustryDiv" class="ibox-content" style="display: none">
                 <table id="backIndustryListTable"></table>
         	 </div>
         </div>
     </div>
     
     <!-- 新增弹框 -->
     <div class="modal" id="industry" tabindex="-1" role="dialog"  aria-hidden="true">
     	<form class="form-horizontal" id="industryForm">
     		<input id="id" name="id" type="hidden"/>
     		<input id="pid" name="pid" type="hidden"/>
			<div class="modal-dialog modal-lg" >
				<div class="modal-content">
					<div class="modal-header" >
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times;</span>
							<span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title" id="industryTitle"><span style="font-weight:bold;">新增行业</span></h4>
					</div>
					<div class="modal-body">
						<div class="row">
							<div class="col-md-12">
                                <div class="row">
                                    <div class="col-md-6">
						        		<div class="form-group">
						        			<label class="col-sm-4 control-label" ><i style="color: red;">*</i>一级行业名称：</label>
					                        <div class="col-sm-8">
					                           <input type="text" class="form-control" id="name1" name="name1" placeholder="必填" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
					                           <i id="name1_error"></i>
					                        </div>
						        		</div>
						        	</div>
						        </div>
						         <div class="row">
						        	<div class="col-md-6">
						        		<div class="form-group">
						        			<label class="col-sm-4 control-label" ><i style="color: red;">*</i>二级行业名称：</label>
					                        <div class="col-sm-8">
					                           <input type="text" class="form-control" id="name2" name="name2" placeholder="必填" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" />
					                           <i id="name2_error"></i>
					                        </div>
						        		</div>
						        	</div>
						        </div>
							</div>
                        </div>
			        </div>
					<div class="modal-footer" style="display: table; width: auto;margin-left: auto; margin-right: auto;">
						<a href="javaScript:;" class="btn btn-primary" onclick="saveOrUpdateIndustry()">保存</a>
						<button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
					</div>
				</div>
			</div>
		</form>
	</div>
     
    <!-- 行业匹配弹框 -->
    <div class="modal" id="matchIndustry" tabindex="-1" role="dialog" aria-hidden="true">
     	<form class="form-horizontal" id="matchIndustryForm">
     		<input id="id" name="id" type="hidden"/>
     		<input id="pid" name="pid" type="hidden"/>
			<div class="modal-dialog modal-lg" >
				<div class="modal-content">
					<div class="modal-header" >
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times;</span>
							<span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title"><span style="font-weight:bold;">行业匹配</span></h4>
					</div>
					<div class="modal-body">
						<div class="row">
							<div class="col-md-12">
                                <div class="row">
                                    <div class="col-md-6">
						        		<div class="form-group">
						        			<label class="col-sm-4 control-label" ><i style="color: red;">*</i>一级行业名称：</label>
					                        <div class="col-sm-8">
					                           <input type="text" class="form-control" id="name1" name="name1" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
					                           <i id="name1_error"></i>
					                        </div>
						        		</div>
						        	</div>
						        </div>
						         <div class="row">
						        	<div class="col-md-6">
						        		<div class="form-group">
						        			<label class="col-sm-4 control-label" ><i style="color: red;">*</i>二级行业名称：</label>
					                        <div class="col-sm-8">
					                           <input type="text" class="form-control" id="name2" name="name2" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" />
					                           <i id="name2_error"></i>
					                        </div>
						        		</div>
						        	</div>
						        </div>
						        <div><span><strong>匹配行业</strong></span></div>
						        <div>
						        	<div class="col-md-6">
						        		<div class="form-group">
						        			<label class="col-sm-4 control-label" ><i style="color: red;"></i>行业名称：</label>
					                        <div class="col-sm-8">
					                        	<input id="backId" name="backId"/>
					                            <input type="text" class="form-control" id="backName" name="backName" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" />
					                            <!-- <i id="name2_error"></i> -->
					                        </div>
						        		</div>
						        	</div>
						        </div>
							</div>
                        </div>
			        </div>
					<div class="modal-footer" style="display: table; width: auto;margin-left: auto; margin-right: auto;">
						<a href="javaScript:;" class="btn btn-primary" onclick="saveMatchIndustry()">保存</a>
						<button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
					</div>
				</div>
			</div>
		</form>
	</div> 
</div>
<script type="text/javascript">
$(function() {
	//1.初始化Table
	var oTable = new TableInit();
	oTable.Init();
	
	$('#industryDiv').show();
});


//初始化Table
var TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
    	$('#industryListTable').bootstrapTable({
	    	url:"${ctx}/dictionary/findAllIndustry",   //请求后台的URL（*）
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
				field : 'code1',
				title : '一级行业编号',
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
				field : 'name1',
				title : '一级行业名称',
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
			},
			{
				field : 'code2',
				title : '二级行业编号',
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
				field : 'name2',
				title : '二级行业名称',
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
				field : 'creatorName',
				title : '创建人',
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
				field : 'createDate',
				title : '创建时间',
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
			//弹出弹窗之前先清除内容
			$("#id").val("");
			$("#pid").val("");
			$("#name1").val("");
			$("#name2").val("");
			$("#name1_error").html("");
			$("#name2_error").html("");
			
			//赋值
        	$("#id").val(row.id);
        	$("#pid").val(row.pid);
        	$("#name1").val(row.name1);
        	$("#name2").val(row.name2);
        	$("#industryTitle").html("修改行业");
 			$('#industry').modal();
		},
		//点击行业匹配
		'click .matchIns' : function(e, value, row, index) {
			//自动补全后台行业初始化
			var backIndustryList = "${backIndustryList}";
			$('#backName').autocomplete(backIndustryList, {
		    	minChars: 1,
		    	max: 999999999,    //列表里的条目数
		        minChars: 0,    //自动完成激活之前填入的最小字符
		        width: 300,     //提示的宽度，溢出隐藏
		        scrollHeight: 400,   //提示的高度，溢出显示滚动条
		        matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
		        autoFill: false,    //自动填充
		        showNoSuggestionNotice: true,
		        noSuggestionNotice: 'Sorry, no matching results',
		        formatItem: function(row) {
		            return  row.name ;
		        },
		    	formatResult : function(row) { //定义最终返回的数据
		        	return row.name;
		    	} 
		    }).result(function(event, row, formatted) {
		       $("#backId").val(row.id);
		       
		    });
			
			//弹出弹窗之前先清除内容
			$("#id").val("");
			$("#pid").val("");
			$("#name1").val("");
			$("#name2").val("");
			$("#name1_error").html("");
			$("#name2_error").html("");
			
			//赋值
        	$("#id").val(row.id);
        	$("#pid").val(row.pid);
        	$("#name1").val(row.name1);
        	$("#name2").val(row.name2);
        	
        	$('#matchIndustry').modal();
			/* window.location.href = "${ctx}/dictionary/matchIndustry?id="+row.id; */
		},
		//点击删除
		'click .deleteIns' : function(e, value, row, index) {
			var flag = confirm("是否删除已选行业");
			if(flag == true){
				$.ajax({
					 url:"${ctx}/dictionary/deleteIndustry?id="+row.id,
					 type:'POST',
					 success:function(data){
						 if(data){
							if(data.result != 1){
								swal({
				                    title: "行业删除失败",
				                    text: "",
				                    type: "error",
				                    confirmButtonText: "确定"
				                }, function(){
				                	$('#industryListTable').bootstrapTable('refresh');
				                });
							}else{
								swal({
				                    title: "行业删除成功",
				                    text: "",
				                    type: "success",
				                    confirmButtonText: "确定"
				                }, function(){
				                	$('#industryListTable').bootstrapTable('refresh');
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
		operate = '<button type="button" class="btn btn-info editIns">修改</button>&nbsp<button type="button" class="btn btn-sm btn-info matchIns">行业匹配</button>&nbsp<button type="button" class="btn btn-sm btn-info deleteIns">删除</button>';
		return operate;
	}

	return oTableInit;
};

/* //查询数据
function searchInsList() {
	$('#industryListTable').bootstrapTable('refresh');
} */

//新增行业
function addIndustry() {
	//弹出弹窗之前先清除内容
	$("#id").val("");
	$("#pid").val("");
	$("#name1").val("");
	$("#name2").val("");
	
	$("#name1_error").html("");
	$("#name2_error").html("");
	
	$('#industry').modal();
}

//新增行业
function saveOrUpdateIndustry(){
	if($("#industryForm").valid()){
		var id = $("#id").val();
		var pid = $("#pid").val();
		var name1 = $("#name1").val();
		var name2 = $("#name2").val();
		
    	$.ajax({
      		url :"<%=path%>/dictionary/saveOrUpdateIndustry",
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
				success : function(data) {
					$('#industry').modal('hide');
					if (data.result == 1) {
						swal({
		                    title: "行业新增或更新成功",
		                    text: "",
		                    type: "success",
		                    confirmButtonText: "确定"
		                }, function(){
		                	$('#industryListTable').bootstrapTable('refresh');
		                });
					}else{
						swal({
		                    title: "行业新增或更新失败",
		                    text: "",
		                    type: "error",
		                    confirmButtonText: "确定"
		                }, function(){
		                	$('#industryListTable').bootstrapTable('refresh');
		                });
				}
			}
		});
	}
}

</script>
</body>
</html>
