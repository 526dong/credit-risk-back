<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!doctype html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>分层管理-相关性管理</title>
<link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!-- <script type="text/javascript" src="js/jquery-1.12.4.js"></script> -->
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>

<script type="text/javascript">
/*页面初始化*/
$(function(){
	
	//资产相关性系数公式设置
	$('.correlation_formula input').focus(function(){
		$(this).addClass('active');
	})
	$('.correlation_formula input').blur(function(){
		$(this).removeClass('active');
	})

	//获取资产相关性系数公式设置
	getAssetCorrelationFormula();
	
	//获取行业以及相关系数
	getBgInsdustryCorrelation();
	
	$("#currentPage").val(1);
	/*列表数据*/
	findBgInsdustryList();
	
	//如果非空，则将选项卡定位到行业
	var tabFlag = "${tabFlag }";
	if(tabFlag){
		$("#card_list li").eq(1).click();
	}
	
})

//获取资产相关性系数公式设置
function getAssetCorrelationFormula() {
    $.ajax({
        url : "${ctx}/insdustryManage/getAssetCorrelationFormula",
        type : 'POST',
        data : {},
        async : false,
        success : function(data) { 
        	data =  eval("("+data+")");
        	var assetCorrelationFormula = '';
        	if(null!=data&&""!=data){
        		assetCorrelationFormula += '<strong>资产相关性系数</strong>&nbsp;';
        		assetCorrelationFormula += '<span>=</span>&nbsp;';
        		for (var i = 0; i < data.length; i++) {
					if(data[i].paramName == "α"){
						assetCorrelationFormula += '<input type="text" value="'+(data[i].paramValue).toFixed(2)+'" onblur=saveCorrelationFormula("'+data[i].id+'",this.value)  onkeyup="num(this)"/>&nbsp;';
					}
				}
        		assetCorrelationFormula += '<span>*</span>&nbsp;';
        		assetCorrelationFormula += '<i>行业相关性+</i>&nbsp;';
        		for (var i = 0; i < data.length; i++) {
					if(data[i].paramName == "β"){
						assetCorrelationFormula += '<input type="text" value="'+(data[i].paramValue).toFixed(2)+'" onblur=saveCorrelationFormula("'+data[i].id+'",this.value)  onkeyup="num(this)"/>&nbsp;';
					}
				}
        		assetCorrelationFormula += '<span>*</span>&nbsp;';
        		assetCorrelationFormula += '<i>区域相关性+</i>&nbsp;';
        		for (var i = 0; i < data.length; i++) {
					if(data[i].paramName == "γ"){
						assetCorrelationFormula += '<input type="text" value="'+(data[i].paramValue).toFixed(2)+'" onblur=saveCorrelationFormula("'+data[i].id+'",this.value)  onkeyup="num(this)"/>&nbsp;';
					}
				}
        		assetCorrelationFormula += '<span>*</span>&nbsp;';
        		assetCorrelationFormula += '<i>主体相关性</i>&nbsp;';
        	}else{
        		assetCorrelationFormula += '<strong>资产相关性系数</strong>&nbsp;';
        		assetCorrelationFormula += '<span>=</span>&nbsp;';
        		assetCorrelationFormula += '<input type="text" value="0.2" onkeyup="num(this)"/>&nbsp;';
        		assetCorrelationFormula += '<span>*</span>&nbsp;';
        		assetCorrelationFormula += '<i>行业相关性+</i>&nbsp;';
        		assetCorrelationFormula += '<input type="text" value="0.2" onkeyup="num(this)"/>&nbsp;';
        		assetCorrelationFormula += '<span>*</span>&nbsp;';
        		assetCorrelationFormula += '<i>区域相关性+</i>&nbsp;';
        		assetCorrelationFormula += '<input type="text" value="0.2" onkeyup="num(this)"/>&nbsp;';
        		assetCorrelationFormula += '<span>*</span>&nbsp;';
        		assetCorrelationFormula += '<i>主体相关性</i>&nbsp;';
        	}
        	$("#assetCorrelationFormula").html(assetCorrelationFormula);
        }
    });
}

/*行业相关性设置*/
function getBgInsdustryCorrelation() {
    $.ajax({
        url : "${ctx}/insdustryManage/getInsdustryCorrelationList",
        type : 'POST',
        data : {},
        async : false,
        success : function(data) { 
        	data =  eval("("+data+")");
        	var html1 = "";
        	var html2 = "";
        	var html3 = "";
        	var htmlmsg = "";
        	if(null == data || "" == data){
        		htmlmsg += "暂无数据";
        		$("#htmlmsg").html(htmlmsg);
        	}else{
        		htmlmsg += "<tr>";
    			htmlmsg += "<th>行业</th>";
    			htmlmsg += "</tr>";
        		for (var i = 0; i < data.length; i++) {
        			html1 += "<tr>";
        			html1 += "<td>"+data[i].insdustryFirstName+"</td>";
        			html1 += "</tr>";
        			
        			html2 += "<th>"+data[i].insdustryFirstName+"</th>";
        			
        			var indexValue = data[i].indexValue;
        			var indexValueList = indexValue.split(",");
        			var insdustrySecond = data[i].insdustrySecond;
        			var insdustrySecondList = insdustrySecond.split(",");
        			html3 += "<tr>";
        			if(null!=data&&""!=data){
        				for (var j = 0; j < indexValueList.length; j++) {
        					html3 += "<td class='recycle_bor'>";
        					html3 += "<input type='text' value="+indexValueList[j]+" onblur=saveBgInsdustryCorrelation('"+data[i].insdustryFirst+"','"+insdustrySecondList[j]+"',this.value)  onkeyup='num(this)'/>";
        					html3 += "</td>";
        				}
        			}
        			html3 += "</tr>";
        		}
        		$("#htmlmsg").html(htmlmsg);
        		$("#html1").html(html1);
        		$("#html2").html(html2);
        		$("#html3").html(html3);
        	}
        	formatSet();
        }
    });
}

/**
 * 行业相关性设置
 */
function formatSet(){
	//行业相关性设置-横向滚动条
	var iw =parseInt($('#table_top_title th:eq(0)').css('min-width'))*$('#table_top_title th').size();
	var iwPar =$('.correlation_box').width()-126;
	$('#scroll_box').width(iwPar);//滚动条
	$('#table_top').width(iwPar); //表头标题
	$('#table_bottom_box').width(iwPar);//表内容
	if(iw>iwPar){
		$('#table_top_title').css('width',iw);
		$('#table_bottom_title').css('width',iw);
		fnScrollSeek('table_top','table_top_title','table_bottom_box','table_bottom_title','scroll_box','scroll_son');
		
	}else{
		$('#table_top_title').css('width','100%');
		$('#table_bottom_title').css('width','100%');
		$('#scroll_box').hide();
	}
	//行业相关性设置-纵向滚动条
	var ih =parseInt($('#table_left_title tr').height())*$('#table_left_title tr').size();
	var ihPar =$('#table_left_box').height();
	if(ih>ihPar){
		fnScrollSeek1('table_left_box','table_left_title','table_bottom_box','table_bottom_title','scroll_box1','scroll_son1');
	}else{
		$('#scroll_box1').hide();	
	}
	//行业相关性设置-input
	$('#table_bottom_title input').focus(function(){
		$(this).css('background','#eff5f9');
		$(this).attr('data-id',1);
	})
	$('#table_bottom_title input').blur(function(){
		$(this).css('background','#fff');
		$(this).attr('data-id',0);
	})
	$('#table_bottom_title input').hover(function(){
		$(this).css('background','#eff5f9');
	},function(){
		if($(this).attr('data-id')!=1){
			$(this).css('background','#fff');
		}
	})
}

/*列表数据*/
function findBgInsdustryList() {
    $.ajax({
        url : "${ctx}/insdustryManage/findBgInsdustryList",
        type : 'POST',
        data : {
        	"pageSize":10,//每页展示数
        	"currentPage": $("#currentPage").val()//当前页
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

//创建列表
function createTable(data) {
    var htmlContent = "";
    for (var i = 0; i < data.length; i++) {
    	var insdustryCode = data[i].insdustryCode;
        if(null==insdustryCode||typeof(insdustryCode)=="undefined"||""==insdustryCode){
        	insdustryCode = "";
        }else{
        	if(insdustryCode.length>6){
        		insdustryCode = insdustryCode.substring(0,6)+"...";
        	}
        }
        var insdustryName = data[i].insdustryName;
        if(null==insdustryName||typeof(insdustryName)=="undefined"||""==insdustryName){
        	insdustryName = "";
        }else{
        	if(insdustryName.length>8){
        		insdustryName = insdustryName.substring(0,8)+"...";
        	}
        }
        var creatorName = data[i].creatorName;
        if(null==creatorName||typeof(creatorName)=="undefined"||""==creatorName){
        	creatorName = "";
        }
        var createTime = data[i].createTimeStr;
        if(null==createTime||typeof(createTime)=="undefined"||""==createTime){
        	createTime = "";
        }
        var matchSecondInsdustryName = data[i].matchSecondInsdustryName;
        if(null==matchSecondInsdustryName||typeof(matchSecondInsdustryName)=="undefined"||""==matchSecondInsdustryName){
        	matchSecondInsdustryName = "未匹配";
        }
        htmlContent += "<tr>";
        htmlContent += "<td>" + (parseInt(i) + 1)+ "</td>";
        htmlContent += "<td title='"+data[i].insdustryCode+"'>" + insdustryCode + "</td>";
        htmlContent += "<td title='"+data[i].insdustryName+"'>" + insdustryName + "</td>";
        htmlContent += "<td>" + creatorName + "</td>";
        htmlContent += "<td>" + createTime + "</td>";
        htmlContent += "<td title='"+matchSecondInsdustryName+"'>" + matchSecondInsdustryName + "</td>";
        htmlContent += "<td class='operate' >";
        htmlContent += "<shiro:hasPermission name='/insdustryManage/toLookBgInsdustryPage'><a href='javaScript:;' onclick=lookDetail('"+data[i].id+"') >查看</a></shiro:hasPermission>";
        htmlContent += "<shiro:hasPermission name='/insdustryManage/toUpdateBgInsdustryPage'><a href='javaScript:;' onclick=updateBgInsdustry('"+data[i].id+"') >修改</a></shiro:hasPermission>";
        htmlContent += "<shiro:hasPermission name='/insdustryManage/toMatchFgInsdustryPage'><a href='javaScript:;' onclick=matchFgInsdustry('"+data[i].id+"') >匹配</a></shiro:hasPermission>";
        htmlContent += "<shiro:hasPermission name='/insdustryManage/deleteBgInsdustry'><a href='javaScript:;' onclick=deleteBgInsdustry('"+data[i].id+"') >删除</a></shiro:hasPermission>";
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
	findBgInsdustryList();
}


//查看详情
function lookDetail(bgInsdustryId){
	window.location.href = "${ctx}/insdustryManage/toLookBgInsdustryPage?bgInsdustryId="+bgInsdustryId;
}

//修改后台行业名称
function updateBgInsdustry(bgInsdustryId){
	window.location.href = "${ctx}/insdustryManage/toUpdateBgInsdustryPage?bgInsdustryId="+bgInsdustryId;
}

//匹配前台行业
function matchFgInsdustry(bgInsdustryId){
	window.location.href = "${ctx}/insdustryManage/toMatchFgInsdustryPage?bgInsdustryId="+bgInsdustryId;
}

//弹出删除框
function deleteBgInsdustry(bgInsdustryId){
	$("#bgInsdustryIdDelete").val(bgInsdustryId);
	fnPopup('#popup','确认删除？');
}
//确认删除
function confirmDelete(){
	var bgInsdustryId = $("#bgInsdustryIdDelete").val();
	if(null!=bgInsdustryId&&""!=bgInsdustryId){
		$.ajax({
			url : "${ctx}/insdustryManage/deleteBgInsdustry",
			type : 'POST',
			data : JSON.stringify(
				{
					"bgInsdustryId":bgInsdustryId
				}		
			),
			dataType : 'json',
			contentType: 'application/json',
			success : function(data) {
				fnShutDown('#popup');
				var href = "${ctx}/insdustryManage/toCorrelationSetPage?tabFlag=tabFlag";
				if (data == "1000") {
					alert("删除成功!",href);
				}else{
					alert("删除失败!",href);
				}
			}
		});
	}else{
		fnShutDown('#popup');
	}
}

//限制输入
function num(obj){
	obj.value = obj.value.replace(/[^\d.]/g,""); //清除"数字"和"."以外的字符
	obj.value = obj.value.replace(/^\./g,""); //验证第一个字符是数字
	obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个, 清除多余的
	obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
	obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //只能输入两个小数
}


	//保存资产相关性系数公式设置
	function saveCorrelationFormula(correlationSetupId,paramValue){
		if(null!=correlationSetupId&&""!=correlationSetupId&&null!=paramValue&&""!=paramValue){
	 		$.ajax({
	 			url : "${ctx}/insdustryManage/updateCorrelationFormula",
	 			type : 'POST',
	 			data : JSON.stringify(
	 				{
	 					"correlationSetupId":correlationSetupId,
	 					"paramValue":paramValue
	 				}		
	 			),
	 			dataType : 'json',
	 			contentType: 'application/json',
	 			success : function(data) {
	 				//获取资产相关性系数公式设置
	 				getAssetCorrelationFormula();
	 			}
	 		});
	 	}
	}
	
	//保存行业相关性数据
	function saveBgInsdustryCorrelation(correlationId1,correlationId2,correlationValue){
		if(null!=correlationId1&&""!=correlationId1&&null!=correlationId2&&""!=correlationId2&&null!=correlationValue&&""!=correlationValue){
	 		$.ajax({
	 			url : "${ctx}/insdustryManage/saveBgInsdustryCorrelation",
	 			type : 'POST',
	 			data : JSON.stringify(
	 				{
	 					"correlationId1":correlationId1,
	 					"correlationId2":correlationId2,
	 					"correlationValue":correlationValue
	 				}		
	 			),
	 			dataType : 'json',
	 			contentType: 'application/json',
	 			success : function(data) {
	 				/*行业相关性设置*/
	 				getBgInsdustryCorrelation();
	 			}
	 		});
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
                <a href="javascript:void(0);">分层管理</a>
                <span>></span>
                <a href="${ctx}/insdustryManage/toCorrelationSetPage">相关性管理</a>
            </h3>
            <div class="shade">
            	<ul class="card_list" id="card_list">
                	<li class="active" id="xiangguanxing_card">相关性设置</li>
                    <li id="houtaihangye_card">后台行业</li>		
               </ul>
               <div id="card_box">
	               <div class="module_box field" style="display:block;" id="xiangguanxing_module">
	               		<div class="correlation">
	               			<h3 class="correlation_title">资产相关性系数公式设置</h3>
	               			<div class="correlation_formula clear" id="assetCorrelationFormula">
	               				
	               			</div>
	               		</div>
	               		<div class="main_minHeight correlation_box">
	               			<h3 class="correlation_title">行业相关性设置</h3>
	               			<div class="correlation_content clear">
	               				<div class="correlation_l">
	               					<from>
	               						<table class="module_table correlation_module correlation_pos1">
	               							<thead id="htmlmsg">
	               								
	               							</thead>
	               						</table>
	               					</from>
	               					<div class="table_pos table_left_box" id="table_left_box">
		               					<from>
		               						<table class="module_table correlation_pos correlation_module" id="table_left_title">
		               							<tbody id="html1">
		               								
		               							</tbody>
		               						</table>
		               					</from>
	               					</div>
	               				</div>
	               				<div class="correlation_r">
	               					<div class="table_pos table_title" id="table_top">
		               					<from>
		               						<table class="module_table correlation_module correlation_pos2" id="table_top_title">
		               							<thead>
		               								<tr id="html2">
		               									
		               								</tr>
		               							</thead>
		               						</table>
		               					</from>		
	               					</div>
	               					<div class="table_bottom_box" id="table_bottom_box">
	               						<from>
		               						<table class="module_table correlation_pos3" id="table_bottom_title">
		               							<tbody id="html3">
		               								
		               							</tbody>
		               						</table>
		               					</from>					
	               					</div>
	               				</div>
	               			</div>
	               			<!--横向滚动条-->
	               			<div style="padding:12px 22px 0px 126px; ">
	                			<div class="scroll_box" id="scroll_box">
		                        	<div class="scroll_son" id="scroll_son" style="left: 0px;"></div>
		                    	</div>
	                		</div>
	                		<!--纵向滚动条-->
	                		<div class="scroll_box1" id="scroll_box1">
	                        	<div class="scroll_son1" id="scroll_son1" style="left: 0px;"></div>
	                    	</div>
	               		</div>
	               </div>
	               
	               <div class="module_box field" id="houtaihangye_module">
	               		<div class="shade main_minHeight">
	               			<div class="search_box clear">
			                	<div class="fr function_btn">
			                		<shiro:hasPermission name='/insdustryManage/toAddBgInsdustryPage'>
			                        <a href="${ctx}/insdustryManage/toAddBgInsdustryPage" class="fl database_btn">新增后台行业</a>
			                        </shiro:hasPermission>
			                    </div>
			                </div>
			                <div class="module_height">
			                	<form>
			                		<input type="hidden" id="currentPage" name="currentPage" value="1"/>
			                    	<table class="module_table">
			                        	<thead>
			                            	<tr>
			                                    <th class="module_width1">序号</th>
			                                    <th>行业编号</th>
			                                    <th>后台行业名称</th>
			                                    <th>创建人</th>
			                                    <th>创建时间</th>
			                                    <th>匹配前台行业</th>
			                                    <th class="module_width6">操作</th>
			                                </tr>
			                            </thead>
			                            <tbody id="htmlContent">
			                            	
			                            </tbody>
			                        </table>
			                    </form>
			                </div>
			                <!-- 分页.html start -->
			                <%@include file="../../commons/tabPage.jsp" %>
			                <!-- 分页.html end -->
	               		</div>
	               </div>
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
<!-- 启用停用 start -->
<div class="popup popup2" id="popup">
	<input type="hidden" id="bgInsdustryIdDelete">
	<a href="javaScript:;" class="close"></a>
    <p>错误提示</p>
    <div class="popup_btn">
    	<a href="javaScript:confirmDelete();" class="a1 fl">确定</a>
        <a href="javaScript:;" class="a2 fr">取消</a>
    </div>
</div>
<!-- 启用停用 end -->
</body>
</html>
