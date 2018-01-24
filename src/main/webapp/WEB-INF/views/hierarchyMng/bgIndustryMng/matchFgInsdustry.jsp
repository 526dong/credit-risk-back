<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!doctype html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>
分层管理-相关性管理-匹配前台行业
</title>

<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!-- <script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script> -->
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
<script type="text/javascript">
var checkedMap = new Array();
/*页面初始化*/
$(function(){

})

//删除匹配的行业
function deleteMatchInsdustry(matchId){
	var bgInsdustryId = $("#bgInsdustryId").val();
	if(null!=matchId&&""!=matchId){
		$.ajax({
			url : "${ctx}/insdustryManage/deleteMatchInsdustry",
			type : 'POST',
			data : JSON.stringify(
				{
					"matchId":matchId,
					"bgInsdustryId":bgInsdustryId
				}		
			),
			dataType : 'json',
			contentType: 'application/json',
			success : function(data) {
				window.location.href = "${ctx}/insdustryManage/toMatchFgInsdustryPage?bgInsdustryId="+bgInsdustryId;
			},
			error : function(data) {
				window.location.href = "${ctx}/insdustryManage/toMatchFgInsdustryPage?bgInsdustryId="+bgInsdustryId;
			}
		});
	}
}

//添加行业
function addFgInsdustry(){
	fnPopup('#popup');
	findFgInsdustryList();
}

//查询未匹配的行业列表
function findFgInsdustryList() {
	var bgInsdustryId = $("#bgInsdustryId").val();
	if(null!=bgInsdustryId&&""!=bgInsdustryId){
		$.ajax({
	        url:"${ctx}/insdustryManage/selectCanMatchInsdustry",
	        type:"POST",
		 	async:false,
	        dataType:"json",
	        data:{
	        	"pageSize":10,//每页展示数
	        	"currentPage": $("#currentPage").val(),//当前页
	        	"bgInsdustryId":bgInsdustryId 
	        	},
	        success:function (data) {
	        	var htmlStr = createTable(data.rows);
	            $("#htmlContent").html(htmlStr);
	            var pageStr = creatPage(data.total, data.pageNo,data.totalPage);
	            $("#pageDiv").html(pageStr);
	            fnCheckBoxx('.mateInfo_table a')
	        }
	    });
	}
}

//创建列表
function createTable(data) {
	var htmlContent = "";
	if(null==data||0 ==data.length){
		htmlContent = '<tr><td colspan="6">暂无数据</td></tr>';
    }else{
    	for (var i = 0; i < data.length; i++) {
        	var matchFirstInsdustryName = data[i].matchFirstInsdustryName;
            if(null==matchFirstInsdustryName||typeof(matchFirstInsdustryName)=="undefined"||""==matchFirstInsdustryName){
            	matchFirstInsdustryName = "";
            }else{
            	if(matchFirstInsdustryName.length>10){
            		matchFirstInsdustryName = matchFirstInsdustryName.substring(0,10)+"...";
            	}
            }
            var matchSecondInsdustryName = data[i].matchSecondInsdustryName;
            if(null==matchSecondInsdustryName||typeof(matchSecondInsdustryName)=="undefined"||""==matchSecondInsdustryName){
            	matchSecondInsdustryName = "";
            }else{
            	if(matchSecondInsdustryName.length>10){
            		matchSecondInsdustryName = matchSecondInsdustryName.substring(0,10)+"...";
            	}
            }
            htmlContent += "<tr>";
            htmlContent += "<td>";
            if(1 == data[i].checkedFlag){
            	htmlContent += "<a data-id='1' class='curr' onclick='$(this).off(\"click\")'></a>";
            }else{
            	var flag = contains(checkedMap, data[i].matchSecondInsdustryId);
            	if(!flag){
            		htmlContent += "<a data-id='0'></a>";
            	}else{
            		htmlContent += "<a data-id='1' class='curr' ></a>";
            	}
            }
            htmlContent += "<span>"+(parseInt(i) + 1)+"</span>";
            htmlContent += "<input type='hidden' value='"+data[i].matchSecondInsdustryId+"' />";
            htmlContent += "</td>";
            htmlContent += "<td title='"+data[i].matchFirstInsdustryName+"'>" + matchFirstInsdustryName + "</td>";
            htmlContent += "<td title='"+data[i].matchSecondInsdustryName+"'>" + matchSecondInsdustryName + "</td>";
            htmlContent += "</tr>";
        }
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
	findFgInsdustryList();
}


//复选框
function fnCheckBoxx(obj){
	$(obj).click(function(){
		if($(this).attr('data-id')==0){
			$(this).addClass('curr');
			$(this).attr('data-id',1);
			checkedMap.push($(this).parent().find("input").val());
		}else{
			$(this).removeClass('curr');
			$(this).attr('data-id',0);	
			var id = $(this).parent().find("input").val();
			removeByValue(checkedMap, id);
		}
	})
}

//删除数组
function removeByValue(arr, val) {
	for(var i=0; i<arr.length; i++) {
	 	if(arr[i] == val) {
		    arr.splice(i, 1);
		    break;
		}
	}
}

//判断数组中的某个元素是否存在
function contains(arr, val) {
	for(var i=0; i<arr.length; i++) {
	 	if(arr[i] == val) {
	 		return true;
		}
	}
	return false;
}
//添加匹配的行业
function addSelectInd(){
	var matchSecondInsdustryIdStr = "";
	for(var i=0; i<checkedMap.length; i++) {
		matchSecondInsdustryIdStr += checkedMap[i]+",";
	}
	var bgInsdustryId = $("#bgInsdustryId").val();
	var href = "${ctx}/insdustryManage/toMatchFgInsdustryPage?bgInsdustryId="+bgInsdustryId;
	if(""!=bgInsdustryId&&""!=bgInsdustryId){
		if(null!=matchSecondInsdustryIdStr&&""!=matchSecondInsdustryIdStr){
			$.ajax({
				url : "${ctx}/insdustryManage/addMatchInsdustry",
				type : 'POST',
				data : JSON.stringify(
					{
						"matchSecondInsdustryIdStr":matchSecondInsdustryIdStr,
						"bgInsdustryId":bgInsdustryId
					}		
				),
				dataType : 'json',
				contentType: 'application/json',
				success : function(data) {
					window.location.href = href;
				}
			});
		}else{
			window.location.href = href;
		}
	}
}
</script>
</head>
<body class="body_bg">
<div class="main">
	<!--页面头部 start -->
	<%@ include file="../../commons/topHead.jsp"%>
	<!--页面头部 end -->
    <!-- center.html start -->
    <div class="main_center">
    	<!--页面左侧导航栏 start -->
		<%@ include file="../../commons/leftNavigation.jsp"%>
		<!-- 页面左侧导航栏 end -->
		
        <!-- 右侧内容.html start -->
        <div class="right_content">
            <h3 class="place_title">
                <span>当前位置：</span>
                <a href="javascript:void(0);">
                <span>></span>
                <a href="${ctx}/insdustryManage/toCorrelationSetPage">相关性管理</a>
                <span>></span>
                <a href="${ctx}/insdustryManage/toCorrelationSetPage?tabFlag=tabFlag">后台行业</a>
                <span>></span>
                <a href="${ctx}/insdustryManage/toMatchFgInsdustryPage?bgInsdustryId=${bgInsdustryId }">匹配前台行业</a>
            </h3>
            <div class="backtrack">
            	<a href="${ctx}/insdustryManage/toCorrelationSetPage?tabFlag=tabFlag">返回</a>
            </div>
            <div class="shade main_minHeight">
            	<input type="hidden" id="bgInsdustryId" value="${bgInsdustryId }">
            	<div class="search_box clear">
                	<div class="checkData_box addCard clear">
                    	<strong>后台行业名称</strong>
                        <span>${insdustryBean.insdustryName }</span>
                    </div>  
                </div>
				<p class="clear mateInfo_btn">
                	<a href="javaScript:;" class="grade_btn fl" onclick="addFgInsdustry()">添加行业</a>
                </p>
                <div class="mateInfo">
                	<c:choose>
					    <c:when test="${empty insdustryBeanList }">
					      
					    </c:when>
					    <c:otherwise>
					        <c:forEach var="itemm" items="${insdustryBeanList }">
					        	<div class="clear">
			                        <div class="info_box info_box1 info_box2 fl">
			                            <strong>行业</strong>
			                            <p>${itemm.matchFirstInsdustryName }-${itemm.matchSecondInsdustryName }</p>
			                        </div>
			                        <div class="info_box info_box1 info_box2 fl">
			                            <a href="javaScript:;" class="grade_btn fl" onclick="deleteMatchInsdustry('${itemm.id }')">删除</a>
			                        </div>
			                    </div>
					        </c:forEach>
					    </c:otherwise>
					</c:choose>
                </div>
            </div>
            <!-- footer.html start -->
            <%@ include file="../../commons/foot.jsp"%>
            <!-- footer.html end -->
        </div>
        <!-- 右侧内容.html end -->
    </div>
    <!-- center.html end -->
</div>


<!-- 遮罩层 start -->
<div class="layer" id="layer"></div>
<!-- 遮罩层 end -->
<!-- 添加行业 start -->
<div class="popup popup4" id="popup" style="display: none;">
	<a href="javaScript:;" class="close"></a>
	<div class="clear popup_btn_m">
		<a href="javaScript:;" class="grade_btn fl" onclick="addSelectInd();">添加</a>
		<a href="javaScript:fnShutDown('#popup');" class="grade_btn fl">取消</a>
	</div>
	<div class="module_height">
		<form>
			<input type="hidden" id="currentPage" name="currentPage" value="1"/>
			<table class="module_table mateInfo_table">
				<thead>
				<tr>
					<th class="module_width8">序号</th>
					<th>一级行业</th>
					<th>二级行业</th>
				</tr>
				</thead>
				<tbody  id="htmlContent"></tbody>
			</table>
		</form>
	</div>
	<!-- 分页.html start -->
	<%@include file="../../commons/tabPage.jsp" %>
	<!-- 分页.html end -->
</div>
<!-- 添加行业 end -->
</body>
</html>
