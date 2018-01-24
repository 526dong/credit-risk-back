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
<title>机构管理 </title>

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
                        <h5>企业数据管理<span>／</span>企业管理<span>／</span>历史审批</h5>
                        <a href="javaScript:;" onclick="history.go(-1)" class="btn btn-primary" >返回</a>
                    </div>
							<div class="ibox-content">
                                <div class="row">
                                    <div class="col-md-6">
						        		<div class="form-group">
						        			<label class="col-sm-2 control-label" >企业名称：</label>
					                        <div class="col-sm-4">
					                           <input type="text" class="form-control" id="name" name="name" value="${enterpriseBean.name}" />
					                           <i id="nameEdit_error"></i>
					                        </div>
						        		</div>
						        	</div>
						        	<div class="col-md-6">
						        		<div class="form-group">
						        			<label class="col-sm-2 control-label" >统一信用代码：</label>
					                        <div class="col-sm-4">
					                           <input type="text" class="form-control" id="creditCode" name="creditCode" value="${enterpriseBean.creditCode}" />
					                           <i id="shortnameEdit_error"></i>
					                        </div>
						        		</div>
						        	</div>
						        </div>
						         <div class="row">
						        	<div class="col-md-6">
						        		<div class="form-group">
						        			<label class="col-sm-2 control-label" >组织机构代码：</label>
					                        <div class="col-sm-4">
					                           <input type="text" class="form-control" id="organizationCodeEdit" name="organizationCodeEdit" />
					                           <i id="organizationCodeEdit_error"></i>
					                        </div>
						        		</div>
						        	</div>
						        	<div class="col-md-6">
						        		<div class="form-group">
						        			<label class="col-sm-2 control-label" >注册资本：</label>
					                        <div class="col-sm-4">
					                           <input type="text" class="form-control" id="registeredCapitalEdit" name="registeredCapitalEdit" />
					                        </div>
						        		</div>
						        	</div>
						        </div>
						         <div class="row">
						        	<div class="col-md-6">
						        		<div class="form-group">
						        			<label class="col-sm-2 control-label" >成立时间：</label>
					                        <div class="col-sm-4">
					                          <input class="form-control" id="foundtimeEdit" name="foundtimeEdit" >
					                        </div>
						        		</div>
						        	</div>
				                    <div class="col-md-6">
						        		<div class="form-group">
						        			<label class="col-sm-2 control-label" >法人代表姓名：</label>
					                        <div class="col-sm-4">
					                          <input type="text" class="form-control" id="legalRepresentativeEdit" name="legalRepresentativeEdit" />
					                        </div>
						        		</div>
						        	</div>
						        </div>
						         <div class="row">
						        	<div class="col-md-6">
						        		<div class="form-group">
						        			<label class="col-sm-2 control-label" >工商登记号：</label>
					                        <div class="col-sm-4">
					                          <input type="text" class="form-control" id="registrationNoEdit" name="registrationNoEdit" />
					                        </div>
						        		</div>
						        	</div>
						        	<div class="col-md-6">
						        		<div class="form-group">
						        			<label class="col-sm-2 control-label" >税务登记号：</label>
					                        <div class="col-sm-4">
					                          <input type="text" class="form-control" id="taxRegisterNoEdit" name="taxRegisterNoEdit" />
					                        </div>
						        		</div>
						        	</div>
						        </div>
						   </div>
                    
                    <div class="ibox-content">
                        <form role="form" class="form-inline">
                            <div class="form-group">
                             <input type="hidden"  id="enterpriseId" name="enterpriseId" value="${enterpriseId}">
                                <strong>审批进度&nbsp;</strong>
                                <select class="form-control m-b" id="approvalStatus" name="approvalStatus">
                                        <option value="">全部</option>
                                        <option value="2">已评级</option>
                                        <option value="3">被退回</option>
                                </select>
                                <strong>评级类型&nbsp;</strong>
                                <select class="form-control m-b" id="type" name="type">
                                        <option value="">全部</option>
                                        <option value="0">新评级</option>
                                        <option value="1">跟踪评级</option>
                                </select>
                                <strong>评级结果&nbsp;</strong>
                                <select class="form-control m-b" id="ratingResult" name="ratingResult">
                                        <option value="">全部</option>
                                        <option value="AAA">AAA</option>
                                        <option value="AA">AA</option>
                                        <option value="A">A</option>
                                        <option value="BBB">BBB</option>
                                        <option value="BB">BB</option>
                                        <option value="B">B</option>
                                        <option value="CCC">CCC</option>
                                        <option value="CC">CC</option>
                                        <option value="C">C</option>
                                </select>
                            </div>
                        </form>
                    </div>
                    		        
                    <div class="ibox-content">
                        <table id="insListTable">
                        
                        </table>
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
    $('#insListTable').bootstrapTable({
    	
    	url:"${ctx}/enterprise/historyApproval",   //请求后台的URL（*）
		method : 'post', //请求方式（*）
		toolbar : '#toolbar', //工具按钮用哪个容器
		striped : true, //是否显示行间隔色
		cache : false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		pagination : true, //是否显示分页（*）
// 		singleSelect  : true,  // 单选checkbox 
		queryParams : oTableInit.queryParams,//传递参数（*）
		sidePagination : "server", //分页方式：client客户端分页，server服务端分页（*）
		pageNumber : 1, //初始化加载第一页，默认第一页
		pageSize : 10, //每页的记录行数（*）
		pageList : [ 10, 25, 50, 100 ], //可供选择的每页的行数（*）
		clickToSelect : true, //是否启用点击选中行
		uniqueId : "name", //每一行的唯一标识，一般为主键列
		columns : [{
			field : 'ratingApplyNum',
			title : '评级申请编号',
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
			field : 'name',
			title : '企业名称',
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
			field : 'ratingResult',
			title : '评级结果',
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
		enterpriseId : $("#enterpriseId").val(),//
		approvalStatus : $("#approvalStatus").val(),//
		type : $("#type").val(),//
		ratingResult : $("#ratingResult").val()//
	};
	return temp;
};

//申请类型映射
function statusFormatter(value, row, index) {
	var res = "";
	if(value==2){
		res="停用"; 
    }else{
    	var examinestate = row.examinestate;
    	if(examinestate==1){
    		res="待审核"; 
        }else if(examinestate==2){
    		res="拒绝"; 
        }else if(examinestate==3){
    		res="使用中"; 
        }
    }
	return res;
}
	   	  
	
//点击按钮
window.operateEvents = {
		//点击编辑
		'click .editIns' : function(e, value, row, index) {
			window.location.href = "${ctx}/enterprise/goHistoryApproval?id="+row.id;
		},
		
	};
	
	
//操作
function operateFormatter(value, row, index) {
	var operate = '';
	operate = '<button type="button" class="btn btn-sm btn-info editIns"); ">查看</button>&nbsp;';
	return operate;
}

return oTableInit;
};

//查询数据
// function searchInsList() {
// 	$('#insListTable').bootstrapTable('refresh');
// }

$("#approvalStatus").change(function(){
	$('#insListTable').bootstrapTable('refresh');
});
$("#type").change(function(){
	$('#insListTable').bootstrapTable('refresh');
});
$("#ratingResult").change(function(){
	$('#insListTable').bootstrapTable('refresh');
});

//删除左右两端的空格
function trim(str){ 
    return str.replace(/(^\s*)|(\s*$)/g, "");
}
</script>
</body>
</html>
