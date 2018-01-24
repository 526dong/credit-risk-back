<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!doctype html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>客户管理-账号管理-模块分配-新增模块分配 </title>

<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!-- <script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script> -->
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
<!-- Data Tables -->
<script	src="${ctx}/resources/h+/js/plugins/layer/laydate/laydate.js"></script>

<script type="text/javascript">
/*页面初始化*/
$(function() {
	/*列表数据*/
	getWeiFenPeiModuleList();
})

/*列表数据*/
function getWeiFenPeiModuleList() {
	var insId = "${insId}";
    $.ajax({
        url : "${ctx}/custom/getWeiFenPeiModuleList",
        type : 'POST',
        data : JSON.stringify(
   			{
   				"insId":insId
   			}		
   		),
        dataType : 'json',
		contentType: 'application/json',
        success : function(data) { 
        	var moduleList = data.allModuleList;
			var htmlContent = createTable(moduleList);
			$("#moduleContent").html(htmlContent);
			if(null!=moduleList&&moduleList.length>0){
				//复选框
				fnCheckBoxxx('.newModeling_tab a')
				for (var i = 0; i < moduleList.length; i++) {
					var startDateid = "startDate"+moduleList[i].myselfId;
					var endDateid = "endDate"+moduleList[i].myselfId;
					//日期范围限制
					var start = {
					    elem: '#'+startDateid+'',
					    format: 'YYYY-MM-DD',
					    max: '2099-06-16', //最大日期
					    istime: false,
					    istoday: false,
					    choose: function (datas) {
// 					        end.min = datas; //开始日选好后，重置结束日的最小日期
// 					        end.start = datas //将结束日的初始值设定为开始日
					    }
					};
					var end = {
					    elem: '#'+endDateid+'',
					    format: 'YYYY-MM-DD',
					    max: '2099-06-16',
					    istime: false,
					    istoday: false,
					    choose: function (datas) {
// 					        start.max = datas; //结束日选好后，重置开始日的最大日期
					    }
					};
					laydate(start);
					laydate(end);
				}
			}
			
        }
    });
}

//创建机构列表
function createTable(data) {
    var htmlContent = '';
    for (var i = 0; i < data.length; i++) {
    	var name = data[i].name;
    	if(null==name||typeof(name)=="undefined"||""==name){
    		name = "";
        }
		var myselfId = data[i].myselfId;
		if(null==myselfId||typeof(myselfId)=="undefined"||""==myselfId){
			myselfId = "";
        }
		var startDateid = "startDate"+myselfId;
		var endDateid = "endDate"+myselfId;
        htmlContent += '<tr>';
        if(i==0){
        	htmlContent += '<td class="td1"><strong>选择模块</strong></td>';
        }else{
        	htmlContent += '<td class="td1"><strong></strong></td>';
        }
        if("系统管理" == name){
        	htmlContent += '<td class="td2"><a href="javaScript:;"  class="curr" data-id="1" data-content="'+myselfId+'">'+name+'</a></td>';
        }else{
        	htmlContent += '<td class="td2"><a href="javaScript:;"  data-id="0" data-content="'+myselfId+'">'+name+'</a></td>';
        }
        htmlContent += '<td class="td3">';
        htmlContent += '<span>使用时间</span>';
        htmlContent += '<input style="width:100px;" type="text" class="form-control layer-date" id="'+startDateid+'" name="'+startDateid+'" readonly="readonly" style="background-color:#FFFFFF;"/>';
        htmlContent += '<em>-</em>';
        htmlContent += '<input style="width:100px;" type="text" class="form-control layer-date" id="'+endDateid+'" name="'+endDateid+'" readonly="readonly" style="background-color:#FFFFFF;"/>';
        htmlContent += '</td>';
        htmlContent += '</tr>';
    }
    htmlContent += '<tr style="vertical-align:top">';
    htmlContent += '<td class="td1 td4"><strong>备注</strong></td>';
    htmlContent += '<td colspan="3"><textarea id="description"></textarea></td>';
    htmlContent += '</tr>';
    return htmlContent;
}

//保存选中的模块
function saveModuleAdd(){
	var myselfIdStr='';
	var moduleNameStr='';
	var startDateStr='';
	var endDateStr='';
	//获取选择的模块数组——————循环复选框数组判断是否选中————复选框数组长度=模块名称数组长度=开始日期数组长度=结束日期数组长度
	$(".newModeling_tab a").each(function(){
	   if($(this).attr("data-id") == "1"){
		   moduleNameStr += $(this).html() + ",";
		   myselfIdStr += $(this).attr("data-content") + ",";
		   var startDateid = "startDate"+$(this).attr("data-content");
		   var endDateid = "endDate"+$(this).attr("data-content");
		   startDateStr += $("#"+startDateid).val() + ",";
		   endDateStr += $("#"+endDateid).val() + ",";
	   }
   });
	var myselfId = myselfIdStr.substring(0,myselfIdStr.length-1);
	var moduleName = moduleNameStr.substring(0,moduleNameStr.length-1);
	var startDate = startDateStr.substring(0,startDateStr.length-1);
	var endDate = endDateStr.substring(0,endDateStr.length-1);
	var moduleName = moduleName.split(",");
	var startDateList = startDate.split(",");
	var endDateList = endDate.split(",");
	var flag = false;
	for(var i=0; i<moduleName.length; i++){
		var startDateq = startDateList[i];//验证时间是否为空
		if(null == startDateq || "" == startDateq ){
			flag = false;
			alert("请填写‘"+moduleName[i]+"’模块的开始时间");
			break;
		}else{
			flag = true;
		}
		var endDateq = endDateList[i];
		if(null == endDateq || "" == endDateq ){
			flag = false;
			alert("请填写‘"+moduleName[i]+"’模块的到期时间");
			break;
		}else{
			flag = true;
			var startDate1, startDate2,endDate1, endDate2, iDays  
			startDate1 = startDateq.split("-") 
		    startDate2 = new Date(startDate1[1] + '-' + startDate1[2] + '-' + startDate1[0]) //转换为12-18-2006格式  
		    endDate1 = endDateq.split("-") 
		    endDate2 = new Date(endDate1[1] + '-' + endDate1[2] + '-' + endDate1[0]) //转换为12-18-2006格式  
		    iDays = parseInt(Math.abs(endDate2  -  startDate2)  /  1000  /  60  /  60  /24) //把相差的毫秒数转换为天数  
		    if(!compareDate(startDateq,endDateq)){
		    	flag = false;
		    	alert("‘"+moduleName[i]+"’模块的到期时间应大于开始时间");
		    	break;
		    }else{
		    	flag = true;
		    	if(iDays<7){
			    	flag = false;
			    	alert("‘"+moduleName[i]+"’模块的开始时间与到期时间间隔至少7天");
			    	break;
			    }else{
			    	flag = true;
			    }
		    }
		}
	}
	if(flag){
		$.ajax({
			url : "${ctx}/custom/saveModuleAdd",
			type : 'POST',
			data : JSON.stringify(
				{
					"insId":$("#insId").val(),
					"description":$("#description").val(),
					"myselfId":myselfIdStr,
					"moduleName":moduleNameStr,
					"startDate":startDateStr,
					"endDate":endDateStr
				}		
			),
			dataType : 'json',
			contentType: 'application/json',
			success : function(data) {
				if("1000" == data){
					var url = "${ctx}/custom/toModuleAllocationPage?id="+$("#insId").val();
					alert("新增成功",url);
				}
			}
		});
	}else{
		return false;
	}
}

//复选框
function fnCheckBoxxx(obj){
	$(obj).click(function(){
		if("系统管理" != $(this).html()){
			if($(this).attr('data-id')==0){
				$(this).addClass('curr');
				$(this).attr('data-id',1);	
			}else{
				$(this).removeClass('curr');
				$(this).attr('data-id',0);	
			}
		}
	})
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
            <h3 class="place_title" >
                <span>当前位置：</span>
                <a href="javascript:void(0);">客户管理</a>
                <span>></span>
                <a href="${ctx}/custom/insAccountManager">账号管理</a>
                <span>></span>
                <a href="${ctx}/custom/toModuleAllocationPage?id=${insId }">模块分配</a>
                <span>></span>
                <a href="${ctx}/custom/toAddModuleAllocaPage?id=${insId }">新增模块分配</a>
            </h3>
            <div class="backtrack">
            	<a href="${ctx}/custom/toModuleAllocationPage?id=${insId }">返回</a>
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
                <h3 class="fl">
                    <span>
                    	<c:choose>  
						  <c:when test="${institution.organizationCodeFlag == '1' }">
						  	统一社会信用代码
						  </c:when> 
						  <c:when test="${institution.organizationCodeFlag == '2' }">
						  	组织机构代码
						  </c:when> 
						  <c:when test="${institution.organizationCodeFlag == '3' }">
						  	事证号
						  </c:when>  
						  <c:when test="${institution.organizationCodeFlag == '4' }">
						  	其他
						  </c:when> 
						  <c:otherwise>
						  	其他
						  </c:otherwise>  
						</c:choose> 
                    </span>
                    <strong>${institution.organizationCode }</strong>
                </h3>
            </div>
            <div class="shade main_mar main_paddTop1 main_minHeight1">
            	<div class="newModeling">
                	<form>
                		<input type="hidden" id="insId" value="${insId }">
                    	<table class="newModeling_tab">
                        	<tbody id="moduleContent">
                            	
                            </tbody>
                        </table>
                    </form>
                </div>  
            </div>
            <div class="main_btn main_btn1">
            	<a href="javaScript:saveModuleAdd();" class="fl">保存</a>
                <a href="${ctx}/custom/toModuleAllocationPage?id=${insId }" class="fr">取消</a>
            </div>
            <!-- footer.html start -->
            <%@ include file="../commons/foot.jsp"%>
            <!-- footer.html end -->
        </div>
        <!-- 右侧内容.html end -->
    </div>
    <!-- center.html end -->
</div>
</body>
</html>
