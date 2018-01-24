<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!doctype html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>客户管理-账号管理-模块分配 </title>

<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!-- <script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script> -->
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
<!-- Data Tables -->
<script	src="${ctx}/resources/h+/js/plugins/layer/laydate/laydate.js"></script>

<script type="text/javascript">
/*页面初始化*/
$(function() {
	$("#currentPage").val(1);
	/*列表数据*/
	getModuleList();
	
	//日期范围限制
	var start = {
	    elem: '#moduleEditStartDate',
	    format: 'YYYY-MM-DD',
	    max: '2099-06-16', //最大日期
	    istime: false,
	    istoday: false,
	    choose: function (datas) {
		        end.min = datas; //开始日选好后，重置结束日的最小日期
		        end.start = datas //将结束日的初始值设定为开始日
	    }
	};
	var end = {
	    elem: '#moduleEditEndDate',
	    format: 'YYYY-MM-DD',
	    max: '2099-06-16',
	    istime: false,
	    istoday: false,
	    choose: function (datas) {
		        start.max = datas; //结束日选好后，重置开始日的最大日期
	    }
	};
	laydate(start);
	laydate(end);
})

/*列表数据*/
function getModuleList() {
    $.ajax({
        url : "${ctx}/custom/getModuleByInsId",
        type : 'POST',
        data : {
        	"pageSize":10,//每页展示数
        	"currentPage": $("#currentPage").val(),//当前页
        	"insId":$("#insId").val()
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

//创建机构列表
function createTable(data) {
    var htmlContent = "";
    for (var i = 0; i < data.length; i++) {
    	var name = data[i].name;
        if(null==name||typeof(name)=="undefined"||""==name){
        	name = "";
        }
        var startDate = data[i].startDate;
        if(null==startDate||typeof(startDate)=="undefined"||""==startDate){
        	startDate = "";
        }
        var endDate = data[i].endDate;
        if(null==endDate||typeof(endDate)=="undefined"||""==endDate){
        	endDate = "";
        }
        var description = data[i].description;
        if(null==description||typeof(description)=="undefined"||""==description){
        	description = "";
        }else{
        	if(description.length>8){
        		description = description.substring(0,8)+"...";
        	}
        }
        var state = data[i].state;
    	var stateStr="正常"; 
        if(state==0){
        	stateStr="正常"; 
        }else if(state==1){
        	stateStr="停用"; 
        }
        var allor = data[i].allor;
        if(null==allor||typeof(allor)=="undefined"||""==allor){
        	allor = "";
        }
        var allorTime = data[i].allorTime;
        if(null==allorTime||typeof(allorTime)=="undefined"||""==allorTime){
        	allorTime = "";
        }
        htmlContent += "<tr>";
        htmlContent += "<td>" + (parseInt(i) + 1)+ "</td>";
        htmlContent += "<td>" + name + "</td>";
        htmlContent += "<td>" + startDate + "</td>";
        htmlContent += "<td>" + endDate + "</td>";
        htmlContent += "<td title='"+data[i].description+"'>" + description + "</td>";
        htmlContent += "<td>" + stateStr + "</td>";
        htmlContent += "<td>" + allor + "</td>";
        htmlContent += "<td>" + allorTime + "</td>";
        htmlContent += "<td class='operate' >";
        htmlContent += "<a href='javaScript:;' onclick=moduleEdit('"+data[i].id+"','"+data[i].name+"','"+data[i].startDate+"','"+data[i].endDate+"','"+data[i].description+"') >修改</a>";
        if(state == 0){
			htmlContent += "<a href='javaScript:;' onclick=stopUseModule('"+data[i].id+"') >停用</a>";
		}else if(state == 1){
			htmlContent += "<a href='javaScript:;' onclick=startUseModule('"+data[i].id+"') >启用</a>";
		}
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
	getModuleList();
}

//新增模块分配
function addModule(){
	var insId = $('#insId').val();
	//校验是否该机构还有未分配的模块，如果有，则进入分配页面；如果没有，则不进入
	$.ajax({
        url : "${ctx}/custom/isHaveWeiFenPeiModule",
        type : 'POST',
        data : {
        	"insId":$("#insId").val()
        },
        async : false,
        success : function(data) {
        	if("0000" == data){
        		alert("该机构已经拥有所有模块，无需新增！");
        	}else{
        		window.location.href = "${ctx}/custom/toAddModuleAllocaPage?id="+insId;
        	}
        }
    });
}

//修改模块
function moduleEdit(id,name,startDate,endDate,description){
	$("#moduleEditId").val(id);
	$("#moduleEditName").html(name);
	$("#moduleEditStartDate").val(startDate);
	$("#moduleEditEndDate").val(endDate);
	$("#moduleEditDescription").val(description);
	fnPopupModule('#popup1');
}

//保存修改
function saveModuleEdit(){
	var moduleId = $('#moduleEditId').val();
	var moduleName = $('#moduleEditName').html();
	var startDate = $('#moduleEditStartDate').val();
	var endDate = $('#moduleEditEndDate').val();
	var description = $('#moduleEditDescription').val();
	var flag = false;
	var startDate1, startDate2,endDate1, endDate2, iDays  
	startDate1 = startDate.split("-") 
    startDate2 = new Date(startDate1[1] + '-' + startDate1[2] + '-' + startDate1[0]) //转换为12-18-2006格式  
    endDate1 = endDate.split("-") 
    endDate2 = new Date(endDate1[1] + '-' + endDate1[2] + '-' + endDate1[0]) //转换为12-18-2006格式  
    iDays = parseInt(Math.abs(endDate2  -  startDate2)  /  1000  /  60  /  60  /24) //把相差的毫秒数转换为天数  
	if(null == startDate || "" == startDate ){
		flag = false;
		$('#popup1').hide();
		fnPopup("#popup2","请填写模块的开始时间!");
		return false;
	}else if(null == endDate || "" == endDate ){
		flag = false;
		$('#popup1').hide();
		fnPopup("#popup2","请填写模块的到期时间!");
		return false;
	}else if(!compareDate(startDate,endDate)){
		flag = false;
		$('#popup1').hide();
		fnPopup("#popup2","模块的到期时间应大于开始时间!");
		return false;
    }else if(iDays<7){
    	flag = false;
    	$('#popup1').hide();
    	fnPopup("#popup2","模块的开始时间与到期时间间隔至少7天!");
    	return false;
    }else{
    	flag = true;
    }
	//进行保存操作
	if(flag && null!=moduleId && ""!=moduleId){
		$.ajax({
			url: "${ctx}/custom/saveModuleEdit",
	        data: JSON.stringify(
	        	{
	        		"id":moduleId,
	        		"name":moduleName,
	        		"startDate":startDate,
	        		"endDate":endDate,
	        		"description":description
	        	}
	        ),
	        dataType : 'json',
			contentType: 'application/json', 
		    type: 'post',
		    async: false,
			success : function(data) {
				if("1000" == data){
					$('#popup1').hide();
					fnPopup("#popup3","修改成功!");
				}else{
					$('#popup1').hide();
					fnPopup("#popup3","修改失败!");
				}
			}
		})
	}else{
		$('#popup1').hide();
		fnPopup("#popup3","修改失败!");
	}
}

//停用模块
function stopUseModule(id){
	$("#insModuleId").val(id);
	$("#stopOrStartFlag").val("stopUse");
	fnPopup("#popup","确认停用？");
}

//启用模块
function startUseModule(id){
	$("#insModuleId").val(id);
	$("#stopOrStartFlag").val("startUse");
	fnPopup("#popup","确认启用？");
}

//确认启用或者停用
function stopOrStartUseInsModule(){
	var id = $("#insModuleId").val();
	var stopOrStartFlag = $("#stopOrStartFlag").val();
	if("stopUse" == stopOrStartFlag){
		var state = 1;
		$.ajax({
    		url: "${ctx}/custom/updateModuleState",
	        data: JSON.stringify(
	        	{
	        		"id":id,
	        		"state":state
	        	}
	        ),
	        dataType : 'json',
			contentType: 'application/json', 
		    type: 'post',
		    async : false,
			success : function(data) {
				var data = eval(data);
				if (data=="1000") {
					window.location.href = "${ctx}/custom/toModuleAllocationPage?id="+$("#insId").val();
				}else{
					window.location.href = "${ctx}/custom/toModuleAllocationPage?id="+$("#insId").val();
				}
			}
		});
	}
	if("startUse" == stopOrStartFlag){
		var state = 0;
		$.ajax({
    		url: "${ctx}/custom/updateModuleState",
	        data: JSON.stringify(
	        	{
	        		"id":id,
	        		"state":state
	        	}
	        ),
	        dataType : 'json',
			contentType: 'application/json', 
		    type: 'post',
		    async : false,
			success : function(data) {
				var data = eval(data);
				if (data=="1000") {
					window.location.href = "${ctx}/custom/toModuleAllocationPage?id="+$("#insId").val();
				}else{
					window.location.href = "${ctx}/custom/toModuleAllocationPage?id="+$("#insId").val();
				}
			}
		});
	}
}

//比较日期大小 
function compareDate(checkStartDate, checkEndDate) {      
    var arys1= new Array();      
    var arys2= new Array();      
	if(checkStartDate != null && checkEndDate != null) {      
	    arys1=checkStartDate.split('-');      
	    var sdate=new Date(arys1[0],parseInt(arys1[1]-1),arys1[2]);      
	    arys2=checkEndDate.split('-');      
	    var edate=new Date(arys2[0],parseInt(arys2[1]-1),arys2[2]);      
		if(sdate > edate) {      
		    return false;         
		}else {
		    return true;      
	    }   
    }      
} 

//打开弹窗
function fnPopupModule(obj,hit){
	$('#layer').show();
	$(obj).show();
	if(hit){
		$(obj).find('p').html(hit);
	}
	$(obj).find('a:eq(0)').click(function(){
		fnShutDownModule(obj);
	})
	$(obj).find('.a2').click(function(){
		fnShutDownModule(obj);
	});
}
//关闭弹窗
function fnShutDownModule(obj){
	$('#layer').hide();
	$(obj).hide();
}

//警示框
function fnPopupChange(){
	$('#popup1').show();
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
                <a href="${ctx}/custom/toModuleAllocationPage?id=${insId }">模块分配</a>
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
                <a href="javaScript:addModule();" class="grade_btn fr">新增模块分配</a>
            </div>
            <div class="shade main_mar main_paddTop">
                <div class="module_height">
                	<form>
                	<input type="hidden" id="currentPage" name="currentPage" value="1"/>
                	<input type="hidden" id="insId" name="insId" value="${insId }"/>
                    	<table class="module_table">
                        	<thead>
                            	<tr>
                                    <th class="module_width1">序号</th>
                                    <th>模块名称</th>
                                    <th>开始使用时间</th>
                                    <th>到期时间</th>
                                    <th>备注</th>
                                    <th>状态</th>
                                    <th>分配人</th>
                                    <th>分配时间</th>
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
<!-- 启用停用 start -->
<div class="popup popup2" id="popup">
	<input type="hidden" id="insModuleId">
	<input type="hidden" id="stopOrStartFlag">
	<a href="javaScript:;" class="close"></a>
    <p></p>
    <div class="popup_btn">
    	<a href="javaScript:stopOrStartUseInsModule();" class="a1 fl">确定</a>
        <a href="javaScript:;" class="a2 fr">取消</a>
    </div>
</div>
<!-- 启用停用 end -->
<!-- 修改 start -->
<div class="popup popup3" id="popup1">
	<a href="javaScript:;" class="close"></a>
    <div class="popup_tab_box">
    	<form>
    		<input type="hidden" id="moduleEditId">
        	<table class="popup_tab">
            	<tbody>
                	<tr>
                    	<td class="td1"><strong>模块名称</strong></td>
                        <td><span id="moduleEditName"></span></td>
                    </tr>
                    <tr>
                    	<td class="td1"><strong>使用时间</strong></td>
                        <td>
                        	<input style="width: 100px;" type="text" id="moduleEditStartDate" class="form-control layer-date" readonly="readonly" style="background-color:#FFFFFF;"/>
                            <em>-</em>
                            <input style="width: 100px;" type="text" id="moduleEditEndDate" class="form-control layer-date" readonly="readonly" style="background-color:#FFFFFF;"/>
                        </td>
                    </tr>
                    <tr>
                    	<td class="td1 td2"><strong>备注</strong></td>
                        <td>
                        	<textarea id="moduleEditDescription"></textarea>
                        </td>
                    </tr>
                    <tr>
                    	<td class="td1"></td>
                        <td>
                        	<div class="popup_btn">
                                <a href="javaScript:saveModuleEdit();" class="a1 fl">确定</a>
                                <a href="javaScript:;" class="a2 fr">取消</a>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </form>
    </div>
</div>
<!-- 修改 end -->
<!-- 成功标识.html start -->
<div class="popup popup2" id="popup2">
	<a href="javaScript:;" class="close"></a>
    <p></p>
    <div class="popup_btn delete_btn">
    	<a href="javaScript:fnPopupChange();" class="a1 fl">确定</a>
    </div>
</div>
<!-- 成功标识.html end -->
<!-- 成功标识.html start -->
<div class="popup popup2" id="popup3">
	<a href="${ctx}/custom/toModuleAllocationPage?id=${insId }" class="close"></a>
    <p></p>
    <div class="popup_btn delete_btn">
    	<a href="${ctx}/custom/toModuleAllocationPage?id=${insId }" class="a1 fl">确定</a>
    </div>
</div>
<!-- 成功标识.html end -->
</body>
</html>
