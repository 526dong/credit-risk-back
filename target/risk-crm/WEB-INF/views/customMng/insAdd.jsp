<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>客户管理-机构管理 -创建机构</title>
    
<link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!-- <script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script> -->
<script type="text/javascript" src="${ctx}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
<!-- Data Tables -->
<script	src="${ctx}/resources/h+/js/plugins/layer/laydate/laydate.js"></script>

<style type="text/css">
.info_box input{width:222px;}
.info_box .input_w1 {
    width: 67px;
}
.info_box .input_w2 {
    width: 136px;
}
.info_box .input_w {
    width: 460px;
}
</style>

<script type="text/javascript">


$(function() {
var foundtime = {
    elem: '#foundtime',
    format: 'YYYY-MM-DD',
    max: '2099-12-12',
    istime: false,
    istoday: true,
    choose: function (datas) {
        //start.max = datas; //结束日选好后，重置开始日的最大日期
    }
};
	laydate(foundtime);
	
	//查询机构性质下拉框
	findInsNuture();
	//查询机构行业（一级行业）
	findInsFirstIndustry();
	
	
	$("#organizationNature_select_box").on("click","p",function(){
		$("#organizationNature").val($(this).attr("data-id"));
	})
	$("#industryFirst_select_box").on("click","p",function(){
		$("#industryFirstStr").val($(this).attr("data-id"));
		$("#industryFirst").val($(this).html());
		var dataId = $(this).attr("data-id");
		loadSecondIndustry();
		 $('#industrySecondChange').find('span').html("请选择二级行业");
		 $('#industrySecondChange').find('span').attr('data-id',"0000");
		 $('#industrySecondStr').val("0000");
	})
	$("#industrySecond_select_box").on("click","p",function(){
		$("#industrySecondStr").val($(this).attr("data-id"));
		$("#industrySecond").val($(this).html());
	})
	$("#organizationCodeFlag_select_box").on("click","p",function(){
		$("#organizationCodeFlag").val($(this).attr("data-id"));
	})
	$("#organizationScale_select_box").on("click","p",function(){
		$("#organizationScale").val($(this).attr("data-id"));
	})
	
	
	//表单验证
	$.validator.addMethod("checkInsName", function(value,element) {
		var insName = $.trim($("#name").val());
		var flag = false;
		$.ajax({
			type : 'POST',
			url : '${ctx}/custom/checkInsName',
			async:false, 
			data : {
				"insName" : insName
			},
			success : function(data) {
				if (data.result == 0) {
					flag = false;
				}else{
					flag = true;
				}
			}
		})
		return flag;
	}, "机构名称已存在");
	$.validator.addMethod("checkInsNameIsLegal", function(value,element) {
		var insName = $.trim($("#name").val());
		//var reg=/^[a-zA-Z0-9]{8,18}$/; 
// 		var reg=/^[\u4E00-\u9FA5]{1,50}$/
		var reg=/^.{1,50}$/
		if(reg.test(insName)==false){
			return false;
		}
		return true;
	}, "机构名称必须为50字符以内");
	$.validator.addMethod("isSelectorganization", function(value,element) {
		var ok = false;
		var organizationCodeFlag = $("#organizationCodeFlag").val();
		if(null == organizationCodeFlag || "" == organizationCodeFlag || organizationCodeFlag == "0000"){
			ok = false;
		}else{
			ok = true;
		}
		return ok;
	}, "请选择代码标识类型");
	$.validator.addMethod("isorganizationrequired", function(value,element) {
		var ok = false;
		var organizationCodeAdd = $("#organizationCode").val();
		if(null == organizationCodeAdd || "" == organizationCodeAdd){
			ok = false;
		}else{
			ok = true;
		}
		return ok;
	}, "必填");
	$.validator.addMethod("isSelectorganizationNum", function(value,element) {
		var ok = false;
		var organizationCodeFlag = $("#organizationCodeFlag").val();
		var organizationCode = $("#organizationCode").val();
		if(null == organizationCodeFlag || "" == organizationCodeFlag || organizationCodeFlag == "0000"){
			$(element).data('error-msg','请选择代码标识类型');
			ok = false;
		}else if(organizationCodeFlag == 1){
// 			var reg = /^\d{18}$/;
			var reg=/^[a-zA-Z0-9]{18}$/; 
			if(organizationCode.match(reg)){
				ok = true;
			}else{
				$(element).data('error-msg','统一社会信用代码必须18位字母或数字');
				ok = false;
			}
		}else if(organizationCodeFlag == 2){
// 			var reg = /^\d{8}-\d{1}$/;
			 var reg = /^[a-zA-Z\d]{8}\-[a-zA-Z\d]$/;
			if(organizationCode.match(reg)){
				ok = true;
			}else{
				$(element).data('error-msg','组织机构代码必须8位本码+1位校验码，例：00000000-0');
				ok = false;
			}
		}else if(organizationCodeFlag == 3){
			var reg = /^\d{12}$/;
			if(organizationCode.match(reg)){
				ok = true;
			}else{
				$(element).data('error-msg','事证号必须为12位数字');
				ok = false;
			}
		}else if(organizationCodeFlag == 4){
			var reg = /^(-|\+)?\d+$/;
			if(organizationCode.match(reg)){
				ok = true;
			}else{
				$(element).data('error-msg','其它必须数字');
				ok = false;
			}
		}
		return ok;
	}, function(params, element) {
	    return $(element).data('error-msg');
	});
	$.validator.addMethod("isOnlyorganizationCode", function(value,element) {
		var organizationCodeFlag = $("#organizationCodeFlag").val();
		var organizationCode = $("#organizationCode").val();
		var flag = false;
		if(null == organizationCodeFlag || "" == organizationCodeFlag || organizationCodeFlag == "0000"){
			flag = true;
		}else{
			$.ajax({
				type : 'POST',
				url : '${ctx}/custom/checkInsOrganizationCode',
				async:false, 
				data : {
					"organizationCodeFlag" : organizationCodeFlag,
					"organizationCode" : organizationCode
				},
				success : function(data) {
					if (data.result == 0) {
						flag = false;
					}else{
						flag = true;
					}
				}
			})
		}
		return flag;
	}, "组织代码已存在");
	$.validator.addMethod("isindustryFirst", function(value,element) {
		var ok = false;
		var industryFirstStr = $("#industryFirstStr").val();
		if(null == industryFirstStr || "" == industryFirstStr || industryFirstStr == "0000"){
			ok = false;
		}else{
			ok = true;
		}
		return ok;
	}, "请选择一级行业");
	$.validator.addMethod("isindustrySecond", function(value,element) {
		var ok = false;
		var industrySecondStr = $("#industrySecondStr").val();
		if(null == industrySecondStr || "" == industrySecondStr || industrySecondStr == "0000"){
			ok = false;
		}else{
			ok = true;
		}
		return ok;
	}, "请选择二级行业");
	$.validator.addMethod("isEmail", function(value,element) {
		var passport = /^([0-9A-Za-z_\-])+(\.[0-9A-Za-z_\-]+)*@([0-9A-Za-z_\-])+((\.\w+)+)$/;
		return this.optional(element) || (passport.test(value));
	}, "请输入正确的电子邮箱");
	$.validator.addMethod("isMobPhone", function(value,element) {
		var passport = /(^1[34578]{1}[0-9]{9}$)/;
		return this.optional(element) || (passport.test(value));
	}, "请输入正确的手机号码");
	$("#insAddForm").validate({
	    rules: {
	      name: {
	      	required: true,
	      	checkInsName: true,
	      	checkInsNameIsLegal:true
	      },
	      organizationCode:{
			isSelectorganization:true,
			isorganizationrequired: true,
			isSelectorganizationNum:true,
			isOnlyorganizationCode:true
		  },
		  email:{
			isEmail:true
		  },
		  industrySecondStr:{
			isindustryFirst: true,
			isindustrySecond: true
		  },
		  contactName:{
			  required: true
		  },
		  contactDep:{
			  required: true
		  },
		  contactJob:{
			  required: true
		  },
		  contactPhoneNum:{
			  required: true,
			  isMobPhone:true
		  },
		  contactEmail:{
			  isEmail:true
		  }
	    },
	    messages: {
	      name:{
              required:"请输入机构名称"
          },
          contactName:{
              required:"请输入联系人姓名"
          },
          contactDep:{
              required:"请输入联系人所属部门"
          },
          contactJob:{
              required:"请输入联系人职务"
          },
          contactPhoneNum:{
              required:"请输入联系人手机号码"
          }
	    },
	    errorPlacement: function(error, element) { 
	 		if(element.is("input[name=name]")){
	 			error.appendTo($("#name_error")); 
	 		}
	 		if(element.is("input[name=organizationCode]")){
	 			error.appendTo($("#organizationCode_error")); 
	 		}
	 		if(element.is("input[name=industrySecondStr]")){
	 			error.appendTo($("#industry_error")); 
	 		}
	 		if(element.is("input[name=email]")){
	 			error.appendTo($("#email_error")); 
	 		}
	 		if(element.is("input[name=contactName]")){
	 			error.appendTo($("#contactName_error")); 
	 		}
	 		if(element.is("input[name=contactDep]")){
	 			error.appendTo($("#contactDep_error")); 
	 		}
	 		if(element.is("input[name=contactJob]")){
	 			error.appendTo($("#contactJob_error")); 
	 		}
	 		if(element.is("input[name=contactPhoneNum]")){
	 			error.appendTo($("#contactPhoneNum_error")); 
	 		}
	 		if(element.is("input[name=contactEmail]")){
	 			error.appendTo($("#contactEmail_error")); 
	 		}
	 	},
    });
	
})

//机构性质
function findInsNuture(){
	$.ajax({
		url:"${ctx }/custom/findInsNuture",
		data : JSON.stringify(
				{
					
				}		
		),
		type : "POST",
		dataType : 'json',
		async: true,
		contentType: 'application/json',
	    success: function(data) {//回调函数，result，返回值
	    	var scenesHtml = '<p data-id="0000">请选择机构性质</p>';
			for (var i = 0; i < data.length; i++) {
				var id = data[i].id;
				var name = data[i].name;
			    scenesHtml += '<p data-id="'+name+'">'+name+'</p>';
			}
			$('#organizationNature_select_box').html(scenesHtml);
	    }
	});
}

//机构行业
function findInsFirstIndustry(){
	$.ajax({
		url:"${ctx }/custom/findInsFirstIndustry",
		data : JSON.stringify(
				{
					
				}		
		),
		type : "POST",
		dataType : 'json',
		async: true,
		contentType: 'application/json',
	    success: function(data) {//回调函数，result，返回值
	    	var scenesHtml = '<p data-id="0000">请选择一级行业</p>';
			for (var i = 0; i < data.length; i++) {
				var id = data[i].id;
				var name = data[i].name;
			    scenesHtml += '<p data-id="'+id+'">'+name+'</p>';
			}
			$('#industryFirst_select_box').html(scenesHtml);
	    }
	});
}
//通过一级行业加载二级行业
function loadSecondIndustry(){
	var industryId= $("#industryFirstStr").val();
	if(null != industryId && industryId != "" && "0000" != industryId){
		$.ajax({
			 url:"${ctx}/custom/getIndustry",
			 type:'POST',
			 async: false,
			 data: {"pid":industryId},
			 success:function(data){
				 if(data){
					 var industry = data.industry2;
					 //清空option
					 $("#industrySecond_select_box").html(""); 
					 var scenesHtml = '<p data-id="0000">请选择二级行业</p>';
					 if(industry != null && industry != ""){
						for (var i = 0; i < industry.length; i++) {
							var id = industry[i].id;
							var name = industry[i].name;
						    scenesHtml += '<p data-id="'+id+'">'+name+'</p>';
						}
					 }
					 $('#industrySecond_select_box').html(scenesHtml);
				 }
			 }
		});
	}else{
		$("#industrySecond_select_box").html(""); 
		var scenesHtml = '<p data-id="0000">请选择二级行业</p>';
		$('#industrySecond_select_box').html(scenesHtml);
	}
}

//创建机构-提交申请
function insAddSave(){
	if($("#insAddForm").valid()){
		$.ajax({
      		url :"${ctx}/custom/saveCsutomAdd",
			data :JSON.stringify(
					{
						"name":$("#name").val(),
						"organizationNature":$("#organizationNature").val(),
						"organizationCodeFlag":$("#organizationCodeFlag").val(),
						"organizationCode":$("#organizationCode").val(),
						"organizationScale":$("#organizationScale").val(),
						"industryFirst":$("#industryFirst").val(),
						"industrySecond":$("#industrySecond").val(),
						"address":$("#address").val(),
						"officeAddress":$("#officeAddress").val(),
						"foundtime":$("#foundtime").val(),
						"legalName":$("#legalName").val(),
						"officePhoneAreacode":$("#officePhoneAreacode").val(),
						"officePhone":$("#officePhone").val(),
						"email":$("#email").val(),
						"fax":$("#fax").val(),
						"webSite":$("#webSite").val(),
						"contactName":$("#contactName").val(),
						"contactDep":$("#contactDep").val(),
						"contactJob":$("#contactJob").val(),
						"contactPhoneAreacode":$("#contactPhoneAreacode").val(),
						"contactPhone":$("#contactPhone").val(),
						"contactPhoneNum":$("#contactPhoneNum").val(),
						"contactEmail":$("#contactEmail").val(),
						"contactWechat":$("#contactWechat").val(),
						"contactQQ":$("#contactQQ").val()
					}		
			),
			type : "POST",
			dataType : 'json',
			contentType: 'application/json',
			success : function(data) {
				if (data == "1000") {
					alert("机构创建成功！","${ctx}/custom/insManage");
				}else{
					alert("机构创建失败！","${ctx}/custom/insManage");
				}
			}
		});
	}
}
</script>
  </head>
  
<body class="body_bg">
<div class="main">
	<!-- header.html start -->
    <%@ include file="../commons/topHead.jsp"%>
    <!-- header.html end -->
    <!-- center.html start -->
    <div class="main_center">
    	<!-- 左侧导航.html start -->
    	<%@ include file="../commons/leftNavigation.jsp"%>
        <!-- 左侧导航.html end -->
        <!-- 右侧内容.html start -->
        <div class="right_content">
            <form id="insAddForm">
            <h3 class="place_title">
                <span>当前位置：</span>
                <a href="javascript:void(0);">客户管理</a>
                <span>></span>
                <a href="${ctx}/custom/insManage">机构管理</a>
                <span>></span>
                <a href="${ctx}/custom/insAdd">创建机构</a>
            </h3>
            <div class="shade_bor">
            	<h2 class="main_title">机构基本信息</h2>
                <div class="container-fluid main_padd">
                	<div class="row">
                    	<div class="col-lg-7 col-sm-7 col-md-7">
                        	<div class="info_box clear">
                            	<strong>机构名称<i>*</i></strong>
                                <input type="text"  id="name" name="name" placeholder="必填" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" />
                                <p class="error_info" id="name_error"></p>
                            </div>
                        </div>
                        <div class="col-lg-5 col-sm-5 col-md-5">
                        	<div class="info_box clear">
                            	<strong>机构性质</strong>
                                <div class="down_menu">
                                	<input value=""  type="hidden" id="organizationNature"  name="organizationNature" >
                                    <div class="select_btn">
                                        <span data-id="0000">请选择机构性质</span>
                                    </div>
                                    <div class="select_list" id="organizationNature_select_box" >
                                        
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-7 col-sm-7 col-md-7">
                            <div class="info_box clear">
                                <strong>代码标识<i>*</i></strong>
                                <div class="down_menu">
                                	<input value="" type="hidden" id="organizationCodeFlag"  name="organizationCodeFlag" >
                                    <div class="select_btn">
                                        <span data-id="0000">请选择代码标识类型</span>
                                    </div>
                                    <div class="select_list" id="organizationCodeFlag_select_box">
                                        <p data-id="0000">请选择代码标识类型</p>
                                        <p data-id="1">统一社会信用代码</p>
                                        <p data-id="2">组织机构代码</p>
                                        <p data-id="3">事证号</p>
<!--                                         <p data-id="4">其他</p> -->
                                    </div>
                                </div>
                                <input id="organizationCode" name="organizationCode" type="text" style="margin-left:16px;" placeholder="必填" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                                <p class="error_info" id="organizationCode_error"></p>
                            </div>
                    	</div>
                        <div class="col-lg-5 col-sm-5 col-md-5">
                        	<div class="info_box clear">
                            	<strong>机构规模</strong>
                                <div class="down_menu">
                                	<input value=""  type="hidden" id="organizationScale"  name="organizationScale" >
                                    <div class="select_btn">
                                        <span data-id="0000">请选择机构规模</span>
                                    </div>
                                    <div class="select_list" id="organizationScale_select_box">
                                        <p data-id="0000">请选择机构规模</p>
                                        <p data-id="大中型">大中型</p>
                                        <p data-id="小微型">小微型</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-7 col-sm-7 col-md-7">
                        	<div class="info_box clear">
                            	<strong>成立时间</strong>
                                <input placeholder="点击选择成立时间" class="form-control layer-date" id="foundtime" name="foundtime" readonly="readonly" style="background-color:#FFFFFF;"/>
                            </div>
                        </div>
                        <div class="col-lg-5 col-sm-5 col-md-5">
                        	<div class="info_box clear">
                            	<strong>法人名称</strong>
                                <input type="text" id="legalName" name="legalName" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                            </div>
                        </div>
                        <div class="col-lg-7 col-sm-7 col-md-7">
                        	<div class="info_box clear">
                            	<strong>公司电话</strong>
                                <input type="text" class="input_w1" id="officePhoneAreacode" name="officePhoneAreacode" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" />
                                <span class="fl xian">-</span>
                                <input type="text" class="input_w2" id="officePhone" name="officePhone" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                            </div>
                        </div>
                        <div class="col-lg-5 col-sm-5 col-md-5">
                        	<div class="info_box clear">
                            	<strong>公司邮箱</strong>
                                <input type="text" id="email" name="email" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" />
                            	<p class="error_info" id="email_error"></p>
                            </div>
                        </div>
                        <div class="col-lg-7 col-sm-7 col-md-7">
                        	<div class="info_box clear">
                            	<strong>公司传真</strong>
                                <input type="text" id="fax" name="fax" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                            </div>
                        </div>
                        <div class="col-lg-5 col-sm-5 col-md-5">
                        	<div class="info_box clear">
                            	<strong>公司官网</strong>
                                <input type="text" id="webSite" name="webSite" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                            </div>
                        </div>
                        <div class="col-lg-7 col-sm-7 col-md-7">
                        	<div class="info_box clear">
                            	<strong>行业<i>*</i></strong>
                                <input value=""  type="hidden" id="industryFirstStr"  name="industryFirstStr" >
                                <input value=""  type="hidden" id="industryFirst"  name="industryFirst" >
                                <div class="down_menu">
                                    <div class="select_btn">
                                        <span data-id="0000">请选择一级行业</span>
                                    </div>
                                    <div class="select_list" id="industryFirst_select_box">
                                        
                                    </div>
                                </div>
                                <div class="down_menu" style="margin-left:16px;">
                                	<input value=""  type="hidden" id="industrySecondStr"  name="industrySecondStr" >
                                	<input value=""  type="hidden" id="industrySecond"  name="industrySecond" >
                                    <div class="select_btn" id="industrySecondChange">
                                        <span data-id="0000">请选择二级行业</span>
                                    </div>
                                    <div class="select_list" id="industrySecond_select_box">
                                       
                                    </div>
                                </div>
                                <p class="error_info" id="industry_error"></p>
                            </div>
                        </div>
                        <div class="col-lg-12 col-sm-12 col-md-12">
                        	<div class="info_box clear">
                            	<strong>注册地址</strong>
                                <input type="text" class="input_w" id="address" name="address" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                            </div>
                        </div>
                        <div class="col-lg-12 col-sm-12 col-md-12">
                        	<div class="info_box clear">
                            	<strong>办公地址</strong>
                                <input type="text" class="input_w"  id="officeAddress" name="officeAddress" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                            </div>
                        </div>                     
                    </div>
                </div>
            </div>
            <div class="shade_bor main_mar">
            	<h2 class="main_title">联系人信息</h2>
                <div class="container-fluid main_padd">
                	<div class="row">
                    	<div class="col-lg-7 col-sm-7 col-md-7">
                        	<div class="info_box clear">
                            	<strong>姓名<i>*</i></strong>
                                <input type="text" id="contactName" name="contactName"  placeholder="必填" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                                <p class="error_info" id="contactName_error"></p>
                            </div>
                        </div>
                        <div class="col-lg-5 col-sm-5 col-md-5">
                        	<div class="info_box clear">
                            	<strong>所属部门<i>*</i></strong>
                                <input type="text" id="contactDep" name="contactDep" placeholder="必填" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                                <p class="error_info" id="contactDep_error"></p>
                            </div>
                        </div>
                        <div class="col-lg-7 col-sm-7 col-md-7">
                        	<div class="info_box clear">
                            	<strong>职务<i>*</i></strong>
                                <input type="text" id="contactJob" name="contactJob" placeholder="必填" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                                <p class="error_info" id="contactJob_error"></p>
                            </div>
                        </div>
                        <div class="col-lg-5 col-sm-5 col-md-5">
                        	<div class="info_box clear">
                            	<strong>办公电话</strong>
                                <input type="text" class="input_w1" id="contactPhoneAreacode" name="contactPhoneAreacode" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                                <span class="fl xian">-</span>
                                <input type="text" class="input_w2" id="contactPhone" name="contactPhone" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                            </div>
                        </div>
                        <div class="col-lg-7 col-sm-7 col-md-7">
                        	<div class="info_box clear">
                            	<strong>手机号码<i>*</i></strong>
                                <input type="text" id="contactPhoneNum" name="contactPhoneNum"  placeholder="必填" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                                <p class="error_info" id="contactPhoneNum_error"></p>
                            </div>
                        </div>
                        <div class="col-lg-5 col-sm-5 col-md-5">
                        	<div class="info_box clear">
                            	<strong>个人邮箱</strong>
                                <input type="text" id="contactEmail" name="contactEmail" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                            	<p class="error_info" id="contactEmail_error"></p>
                            </div>
                        </div>
                        <div class="col-lg-7 col-sm-7 col-md-7">
                        	<div class="info_box clear">
                            	<strong>微信号</strong>
                                <input type="text" id="contactWechat" name="contactWechat" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                            </div>
                        </div>
                        <div class="col-lg-5 col-sm-5 col-md-5">
                        	<div class="info_box clear">
                            	<strong>QQ号</strong>
                                <input type="text" id="contactQQ" name="contactQQ" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
                            </div>
                        </div>
            		</div>
            	</div>
            </div>
            <div class="main_btn">
            	<a href="javaScript:;" onclick="insAddSave()" class="fl">提交申请</a>
            </div>
            <!-- footer.html start -->
             <%@ include file="../commons/foot.jsp"%>
            <!-- footer.html end -->
            </form>
        </div>
        <!-- 右侧内容.html end -->
    </div>
    <!-- center.html end -->
</div>
</body>
</html>