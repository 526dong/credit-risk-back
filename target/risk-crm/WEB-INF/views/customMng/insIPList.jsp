<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!doctype html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>客户管理-账号管理-IP管理 </title>

<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!-- <script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script> -->
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>

<script type="text/javascript">
/*页面初始化*/
$(function() {
	$("#currentPage").val(1);
	/*列表数据*/
	findAllInsIPList();
	
	//武向楠IP验证
	$.validator.addMethod("isOnlyIPAdress", function(value,element) {
		var IPId= $.trim($("#IPId").val());
		var ipAdressFnOld= $.trim($("#ipAdressFnOld").val());
		var ipAdressFn = $.trim($("#ipAdressFn").val());
		var flag = false;
		if(ipAdressFnOld == ipAdressFn){
			flag = true;
		}else{
			$.ajax({
				type : 'POST',
				url : '${ctx}/custom/checkIPAdress',
				async:false, 
				data : {
					"ipAdress" : ipAdressFn,
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
		}
		return flag;
	}, "IP已存在");
	$.validator.addMethod("isIPAdress", function(value,element) {
		var IPAdress = value;
		var regexp = /^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/;
		var valid = regexp.test(IPAdress);
		if(!valid){//首先必须是 xxx.xxx.xxx.xxx 类型的数字，如果不是，返回false
            return false;
        }
		return IPAdress.split('.').every(function(num){
            //切割开来，每个都做对比，可以为0，可以小于等于255，但是不可以0开头的俩位数
            //只要有一个不符合就返回false
            if(num.length > 1 && num.charAt(0) === '0'){
                //大于1位的，开头都不可以是‘0’
                return false;
            }else if(parseInt(num , 10) > 255){
                //大于255的不能通过
                return false;
            }
            return true;
        });
	}, "请输入正确的IP地址");
	$("#ipAdressFnForm").validate({
	    rules: {
	    	ipAdressFn:{
				required: true,
		      	isIPAdress:true,
		      	isOnlyIPAdress:true
			}
	    },
	    messages: {
	    	ipAdressFn:{
	            required:"请输入IP地址"
	        }
	    },
	    errorPlacement: function(error, element) { 
	 		if(element.is("input[name=ipAdressFn]")){
	 			error.appendTo($("#ipAdressFn_error")); 
	 		}
	 	},
    });
	
})

/*列表数据*/
function findAllInsIPList() {
    $.ajax({
        url : "${ctx}/custom/findAllInsIPList",
        type : 'POST',
        data : {
        	"pageSize":10,//每页展示数
        	"currentPage": $("#currentPage").val(),//当前页
            "insId": $("#insId").val()
        },
        async : false,
        success : function(data) { 
        	var htmlStr = createTable(data.rows);
            $("#htmlContent").html(htmlStr);
            var pageStr = creatPage(data.total, data.pageNo,data.totalPage);
            $("#pageDiv").html(pageStr);
        }
    });
}

//创建机构用户列表
function createTable(data) {
    var htmlContent = "";
    for (var i = 0; i < data.length; i++) {
    	var ipAddress = data[i].ipAddress;
        if(null==ipAddress||typeof(ipAddress)=="undefined"||""==ipAddress){
        	ipAddress = "";
        }
        var createTime = data[i].createTimeStr;
        if(null==createTime||typeof(createTime)=="undefined"||""==createTime){
        	createTime = "";
        }
        var creator = data[i].creator;
        if(null==creator||typeof(creator)=="undefined"||""==creator){
        	creator = "";
        }
        htmlContent += "<tr>";
        htmlContent += "<td>" + (parseInt(i) + 1)+ "</td>";
        htmlContent += "<td>" + ipAddress + "</td>";
        htmlContent += "<td>" + createTime + "</td>";
        htmlContent += "<td>" + creator + "</td>";
        htmlContent += "<td class='operate' >";
        htmlContent += "<shiro:hasPermission name='/custom/update'><a href='javaScript:;' onclick=insIPEdit('"+data[i].id+"','"+data[i].ipAddress+"') >修改</a></shiro:hasPermission>";
        htmlContent += "<shiro:hasPermission name='/custom/deleteInsIP'><a href='javaScript:;' onclick=insIPDelete('"+data[i].id+"') >删除</a></shiro:hasPermission>";
        htmlContent += "</td>";
        htmlContent += "</tr>";
    }
    return htmlContent;
}

//分页跳转
function jumpToPage(curPage){
	if(typeof(curPage) != "undefined"){
    	$("#currentPage").val(curPage);
	}else{
		$("#currentPage").val(1);
	}
	//查询
	findAllInsIPList();
}

//新增IP
function addInsIP(){
	$("#ipAdressFn_error").html("");
	$("#IPId").val("");
	$("#ipAdressFn").val("");
	$("#ipAdressFnOld").val("");
	myFnPopup('#popup1');
}

//修改IP
function insIPEdit(IPId,ipAddress){
	$("#ipAdressFn_error").html("");
	$("#IPId").val("");
	$("#ipAdressFn").val("");
	$("#IPId").val(IPId);
	$("#ipAdressFn").val(ipAddress);
	$("#ipAdressFnOld").val("");
	$("#ipAdressFnOld").val(ipAddress);
	
	myFnPopup('#popup1');
}

//保存新增IP
function saveInsIP(){
	var insId = $("#insId").val();
	var IPId = $("#IPId").val();
	var ipAdressFn = $("#ipAdressFn").val();
	var href = "${ctx}/custom/toInsIPManagePage?id="+insId;
	//如果IPId为null，则为新增操作；否则为更新操作
	if("" == IPId || null == IPId){
		if($("#ipAdressFnForm").valid()){
			$.ajax({
				url : "${ctx}/custom/saveInsIPAdd",
				type : 'POST',
				data : {
						"insId":insId,
						"IPId":IPId,
						"ipAdressFn":ipAdressFn
					},
				success : function(data) {
					myFnShutDown('#popup1');
					if (data == "1000" || 1000 == data) {
						alert("添加成功!",href);
					} else {
						alert("添加失败!",href);
					}
				}
			});
		}
	}else{
		if($("#ipAdressFnForm").valid()){
			$.ajax({
				url : "${ctx}/custom/saveInsIPEdit",
				type : 'POST',
				data : {
						"insId":insId,
						"IPId":IPId,
						"ipAdressFn":ipAdressFn
					},
				success : function(data) {
					myFnShutDown('#popup1');
					if (data == "1000" || 1000 == data) {
						alert("修改成功!",href);
					} else {
						alert("修改失败!",href);
					}
				}
			});
		}
	}
}

//删除IP
function insIPDelete(IPId){
	var insId = $("#insId").val();
	var href = "${ctx}/custom/toInsIPManagePage?id="+insId;
	confirm("确认删除ip地址？",function(){
		$.ajax({
			url : "${ctx}/custom/deleteInsIP",
			type : 'POST',
			data : {
					"IPId":IPId
				},
			success : function(data) {
				if (data == "1000" || 1000 == data) {
					alert("删除成功!",href);
				} else {
					alert("删除失败!",href);
				}
			}
		});
	})
}

//打开弹窗
function myFnPopup(obj,hit){
	$('#layer').show();
	$(obj).show();
	if(hit){
		$(obj).find('p').html(hit);
	}
	$(obj).find('a:eq(0)').click(function(){
		fnShutDown(obj);
	})
// 	$(obj).find('.a1').click(function(){
// 		fnShutDown(obj);
// 	});
	$(obj).find('.a2').click(function(){
		fnShutDown(obj);
	});
}
//关闭弹窗
function myFnShutDown(obj){
	$('#layer').hide();
	$(obj).hide();
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
                <a href="${ctx}/custom/toInsIPManagePage?id=${insId }">IP管理</a>
            </h3>
            <div class="backtrack">
            	<a href="${ctx}/custom/insAccountManager">返回</a>
            </div>
            <div class="intoPackage_title shade">
                <h3 class="fl intoPackage_h3">
                    <span>机构名称</span>
                    <strong>${institution.name }</strong>
                </h3>
                <h3 class="fl">
                    <span>办公电话</span>
                    <strong>${institution.officePhoneAreacode }-${institution.officePhone }</strong>
                </h3>
                <shiro:hasPermission name='/custom/toInsIPManagePage'>
                <a href="javaScript:addInsIP();" class="grade_btn fr">添加IP</a>
                </shiro:hasPermission>
            </div>
            <div class="shade main_mar">
                <div class="module_height main_paddTop">
                	<form>
                		<input type="hidden" id="insId"  value="${insId }"/>
                		<input type="hidden" id="currentPage" name="currentPage" value="1"/>
                    	<table class="module_table">
                        	<thead>
                            	<tr>
                                    <th class="module_width1">序号</th>
                                    <th>IP地址</th>
                                    <th>创建时间</th>
                                    <th>创建人</th>
                                    <th class="module_width2">操作</th>
                                </tr>
                            </thead>
                            <tbody id="htmlContent">
                            	
                            	
                            </tbody>
                        </table>
                    </form>
                </div>
                <!-- 分页.html start -->
                <%@include file="../commons/tabPage.jsp" %>
                <!-- 分页.html end -->
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
<!-- 启用停用删除 start -->
<div class="popup popup2" id="popup">
	<a href="javaScript:;" class="close"></a>
    <p>错误提示</p>
    <div class="popup_btn">
    	<a href="javaScript:;" class="a1 fl">确定</a>
        <a href="javaScript:;" class="a2 fr">取消</a>
    </div>
</div>
<!-- 启用停用删除 end -->
<!-- 添加修改 start -->
<div class="popup popup3" id="popup1">
	<a href="javaScript:;" class="close"></a>
	<input type="hidden" id="IPId">
	<input type="hidden" id="ipAdressFnOld">
    <form id="ipAdressFnForm">
	    <div class="popup_ip">
	    	<span>请输入IP</span>
	        <input type="text" id="ipAdressFn" name="ipAdressFn" placeholder="请确认密码" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" onkeydown="if(event.keyCode==13) return false;"/>
	        <div class="error_info" id="ipAdressFn_error"></div>
	    </div>
    </form>
    <div class="popup_btn">
    	<a href="javaScript:saveInsIP();" class="a1 fl">提交</a>
        <a href="javaScript:;" class="a2 fr">取消</a>
    </div>
</div>
<!-- 添加修改 end -->
</body>
</html>
