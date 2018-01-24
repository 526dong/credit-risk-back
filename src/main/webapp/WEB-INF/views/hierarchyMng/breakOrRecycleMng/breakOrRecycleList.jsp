<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!doctype html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>分层管理-违约与回收管理</title>
<link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!-- <script type="text/javascript" src="js/jquery-1.12.4.js"></script> -->
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>

<script type="text/javascript">
/*页面初始化*/
$(function() {
	//样式格式化
	formatSet();
	
	//获取违约率
	getBreakRateList();
	
	//获取回收率list
	getRecoveryRateList();
	
})

//样式格式化
function formatSet(){
	//违约率
	$('.recycle_table_box input').focus(function(){
		$(this).css('background','#eff5f9');
		$(this).attr('data-id',1);
	})
	$('.recycle_table_box input').blur(function(){
		$(this).css('background','#fff');
		$(this).attr('data-id',0);
	})
	$('.recycle_table_box input').hover(function(){
		$(this).css('background','#eff5f9');
	},function(){
		if($(this).attr('data-id')!=1){
			$(this).css('background','#fff');
		}
		
	})
	//回收率
	$('.recycle_table input').focus(function(){
		$(this).css('background','#eff5f9');
		$(this).attr('data-id',1);
	})
	$('.recycle_table input').blur(function(){
		$(this).css('background','#fff');
		$(this).attr('data-id',0);
	})
	$('.recycle_table input').hover(function(){
		$(this).css('background','#eff5f9');
	},function(){
		if($(this).attr('data-id')!=1){
			$(this).css('background','#fff');
		}
	})
}

//获取违约率list
function getBreakRateList() {
    $.ajax({
        url : "${ctx}/hierarchyManage/getBreakRateList",
        type : 'POST',
        data : {},
        async : false,
        success : function(data) { 
        	data = eval("("+data+")");
        	var gradeList = data.gradeList;
        	var htmlContent1 = "";
        	if(null!=gradeList&&""!=gradeList){
        		for (var i = 0; i < gradeList.length; i++) {
        			htmlContent1 += "<tr>";
        			htmlContent1 += "<td>"+gradeList[i]+"</td>";
        			htmlContent1 += "</tr>";
				}
        	}
        	$("#htmlContent1").html(htmlContent1);
        	var layerDefaulRatesList = data.layerDefaulRatesList;
        	var htmlContent2 = "";
        	for (var i = 0; i < 10; i++) {
        		htmlContent2 += "<div class='recycle_table_box'>";
        		htmlContent2 += "<form>";
        		htmlContent2 += "<table class='module_table recycle_module1'>";
        		htmlContent2 += "<thead>";
        		htmlContent2 += "<tr>";
        		htmlContent2 += "<th colspan='12' class='recycle_th recycle_bor'>";
        		if(i == 0){
        			htmlContent2 += "第一年（%）";
        		}else if(i == 1){
        			htmlContent2 += "第二年（%）";
        		}else if(i == 2){
         			htmlContent2 += "第三年（%）";
         		}else if(i == 3){
         			htmlContent2 += "第四年（%）";
         		}else if(i == 4){
         			htmlContent2 += "第五年（%）";
         		}else if(i == 5){
         			htmlContent2 += "第六年（%）";
         		}else if(i == 6){
         			htmlContent2 += "第七年（%）";
         		}else if(i == 7){
         			htmlContent2 += "第八年（%）";
         		}else if(i == 8){
         			htmlContent2 += "第九年（%）";
         		}else{
         			htmlContent2 += "第十年（%）";
         		}
        		htmlContent2 += "</th>";
        		htmlContent2 += "</tr>";
        		htmlContent2 += "<tr>";
        		for (var j = 0; j < 12; j++) {
					if(j == 0){
						htmlContent2 += "<th class='recycle_bor'>1月</th>";
					}else{
						htmlContent2 += "<th>"+(j+1)+"月</th>";
					}
				}
        		htmlContent2 += "</tr>";
        		htmlContent2 += "</thead>";
        		htmlContent2 += "<tbody>";
        		var layerDefaulRatesList = data.layerDefaulRatesList;
            	if(null!=layerDefaulRatesList&&""!=layerDefaulRatesList){
            		for (var m = 0; m < gradeList.length; m++) {
            			htmlContent2 += "<tr>";
            			for (var k = (120*m+12*i); k < (120*m+12*i+12); k++) {
                			if(layerDefaulRatesList[k].month == (i*12+1)){
                				htmlContent2 += "<td class='recycle_bor'><input type='text' value='"+(layerDefaulRatesList[k].breakRate).toFixed(3)+"' onblur=saveBreak('"+layerDefaulRatesList[k].id+"',this.value) onkeyup='num(this)'/></td>";
                			}else{
                				htmlContent2 += "<td><input type='text' value='"+(layerDefaulRatesList[k].breakRate).toFixed(3)+"' onblur=saveBreak('"+layerDefaulRatesList[k].id+"',this.value) onkeyup='num(this)'/></td>";
                			}
        				}
            			htmlContent2 += "</tr>";
    				}
            	}
        		htmlContent2 += "</tbody>";
        		htmlContent2 += "</table>";
        		htmlContent2 += "</form>";
        		htmlContent2 += "</div>";
			}
        	$("#recycle_list").html(htmlContent2);
        	
        	//滚动条设置
        	var iw =$('#card_box').outerWidth()-114;
        	$('#recycle_table_r').width(iw);
        	$('#scroll_box').width(iw);
        	if($('.recycle_table_box').size()<2){
        		$('#recycle_list').width(iw)
        		$('.recycle_table_box').css('width','100%');
        		$('#scroll_box').hide();	
        	}else{
        		$('#recycle_list').width($('.recycle_table_box').outerWidth()*$('.recycle_table_box').size());
        		fnScroll('recycle_table_r','recycle_list','scroll_box','scroll_son',true);	
        	}
        	//样式格式化
        	formatSet();
        }
    });
}

//获取回收率list
function getRecoveryRateList() {
    $.ajax({
        url : "${ctx}/hierarchyManage/getRecoveryRateList",
        type : 'POST',
        data : {},
        async : false,
        success : function(data) { 
        	data =  eval("("+data+")");
        	var layerRecoveryRatesAList = data.layerRecoveryRatesAList;
        	var html1 = "";
        	if(null!=layerRecoveryRatesAList&&""!=layerRecoveryRatesAList){
        		for (var i = 0; i < layerRecoveryRatesAList.length; i++) {
        			html1 += "<tr>";
        			html1 += "<td class='recycle_td'>"+layerRecoveryRatesAList[i].ratingLevel+"</td>";
        			html1 += "<td>";
        			html1 += "<input type='text' value="+(layerRecoveryRatesAList[i].recycleRate).toFixed(3)+" onblur=saveRecovery('"+layerRecoveryRatesAList[i].id+"',this.value) onkeyup='num(this)'/>";
        			html1 += "</td>";
        			html1 += "</tr>";
				}
        	}
        	$("#html1").html(html1);
        	var layerRecoveryRatesBList = data.layerRecoveryRatesBList;
        	var html2 = "";
        	if(null!=layerRecoveryRatesBList&&""!=layerRecoveryRatesBList){
        		for (var i = 0; i < layerRecoveryRatesBList.length; i++) {
        			html2 += "<tr>";
        			html2 += "<td class='recycle_td'>"+layerRecoveryRatesBList[i].ratingLevel+"</td>";
        			html2 += "<td>";
        			html2 += "<input type='text' value="+(layerRecoveryRatesBList[i].recycleRate).toFixed(3)+" onblur=saveRecovery('"+layerRecoveryRatesBList[i].id+"',this.value) onkeyup='num(this)'/>";
        			html2 += "</td>";
        			html2 += "</tr>";
				}
        	}
        	$("#html2").html(html2);
        	var layerRecoveryRatesCList = data.layerRecoveryRatesCList;
        	var html3 = "";
        	if(null!=layerRecoveryRatesCList&&""!=layerRecoveryRatesCList){
        		for (var i = 0; i < layerRecoveryRatesCList.length; i++) {
        			html3 += "<tr>";
        			html3 += "<td class='recycle_td'>"+layerRecoveryRatesCList[i].ratingLevel+"</td>";
        			html3 += "<td>";
        			html3 += "<input type='text' value="+(layerRecoveryRatesCList[i].recycleRate).toFixed(3)+" onblur=saveRecovery('"+layerRecoveryRatesCList[i].id+"',this.value) onkeyup='num(this)'/>";
        			html3 += "</td>";
        			html3 += "</tr>";
				}
        	}
        	$("#html3").html(html3);
        	//样式格式化
        	formatSet();
        }
    });
}


//保存违约率
function saveBreak(defaulratesId,value){
	if(null != defaulratesId && "" != defaulratesId){
		$.ajax({
			url : "${ctx}/hierarchyManage/saveUpdateDefaulrates",
			type : 'POST',
			data : JSON.stringify(
				{
					"defaulratesId":defaulratesId,
					"breakRate":value
				}
			),
			dataType : 'json',
			contentType: 'application/json',
			async: false,
			success : function(data) {
				//获取违约率
				getBreakRateList();
			},
			error: function(data) {
				//获取违约率
				getBreakRateList();
			}
		});
	}
}

//保存回收率
function saveRecovery(recoveryId,value){
	if(null != recoveryId && "" != recoveryId){
		$.ajax({
			url : "${ctx}/hierarchyManage/saveUpdateRecoveryrates",
			type : 'POST',
			data : JSON.stringify(
				{
					"recoveryId":recoveryId,
					"recycleRate":value
				}
			),
			dataType : 'json',
			contentType: 'application/json',
			async: false,
			success : function(data) {
				getRecoveryRateList();
			},
			error: function(data) {
				getRecoveryRateList();
			}
		});
	}
}

//限制输入
function num(obj){
	obj.value = obj.value.replace(/[^\d.]/g,""); //清除"数字"和"."以外的字符
	obj.value = obj.value.replace(/^\./g,""); //验证第一个字符是数字
	obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个, 清除多余的
	obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
	obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d\d).*$/,'$1$2.$3'); //只能输入两个小数
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
                <a href="${ctx}/hierarchyManage/toBreakOrRecycleManagerPage">违约与回收管理</a>
            </h3>
            <div class="shade main_minHeight">
                <ul class="card_list" id="card_list">
                	<li class="active">违约率</li>
                    <li>回收率</li>		
                </ul>
                <div id="card_box">
                	<div class="module_box field clear" style="display:block;">
                		<div class="recycle_box">
	                		<div class="recycle_table_l fl">
	                            <form>
	                                <table class="module_table recycle_module">
	                                	<thead>
	                                		<tr>
	                                			<th>评级级别</th>
	                                		</tr>
	                                	</thead>
	                                    <tbody id="htmlContent1">
	                                    	
	                                    </tbody>
	                                </table>
	                            </form>
	                		</div>
	                		<div class="recycle_table_r" id="recycle_table_r">
	                			<div class="clear recycle_list" id="recycle_list">
		                			
	                			</div>
	                		</div>	
                		</div>
                		<div style="padding:0 22px 26px 92px; ">
                			<div class="scroll_box" id="scroll_box">
	                        	<div class="scroll_son" id="scroll_son"></div>
	                    	</div>
                		</div>
                	</div>
                	<div class="module_box field">
                		<div class="recycle_box recycle_box1">
                			<div class="container-fluid">
                				<div class="row">
                					<div class="col-lg-4 col-sm-4 col-md-4" style="padding-right:3px;">
                						<form>
					                    	<table class="module_table recycle_table">
					                        	<thead>
					                            	<tr>
					                                    <th>评级级别</th>
					                                    <th>回收率（%）</th>
					                                </tr>
					                            </thead>
					                            <tbody id="html1">
					                            	
					                            </tbody>
					                        </table>
					                    </form>
                					</div>
                					<div class="col-lg-4 col-sm-4 col-md-4" style="padding:0 3px;">
                						<form>
					                    	<table class="module_table recycle_table">
					                        	<thead>
					                            	<tr>
					                                    <th>评级级别</th>
					                                    <th>回收率（%）</th>
					                                </tr>
					                            </thead>
					                            <tbody id="html2">
					                            	
					                            </tbody>
					                        </table>
					                    </form>
                					</div>
                					<div class="col-lg-4 col-sm-4 col-md-4" style="padding-left:3px;">
                						<form>
					                    	<table class="module_table recycle_table">
					                        	<thead>
					                            	<tr>
					                                    <th>评级级别</th>
					                                    <th>回收率（%）</th>
					                                </tr>
					                            </thead>
					                            <tbody id="html3">
					                            	
					                            </tbody>
					                        </table>
					                    </form>
                					</div>
                				</div>
                			</div>	
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

</body>
</html>
