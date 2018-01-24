<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>中诚信信用风险管理平台</title>
<link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!-- <script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script> -->
<script type="text/javascript" src="${ctx}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
</head>
<script type="text/javascript">
$(function(){
	
})
</script>
<body class="body_bg">
<div class="main">
<input type="hidden" id="isNeedWarnFlag" value="${isNeedWarnFlag }">
	<!--页面头部 start -->
	<%@ include file="../commons/topHead.jsp"%>
	<!--页面头部 end -->
    <!-- center.html start -->
    <div class="main_center">
    	<!--页面左侧导航栏 start -->
		<%@ include file="../commons/leftNavigation.jsp"%>
		<!-- 页面左侧导航栏 end -->
		
        <!-- 右侧内容.html start -->
        
        <!-- 右侧内容.html end -->
    </div>
    <!-- center.html end -->
</div>

<!-- 页面内容 end -->
<script type="text/javascript" >
$(document).ready(function(){
	   var menuIndex1;//左侧导航栏下表
	   var menuIndex2;//头部导航栏下表
	   var herf = $(".leftsideBar_list a").eq(0).attr("data-url");
	   if(""!=herf && typeof(herf) != "undefined"){
		   menuIndex1 = 0;
		   $(".leftsideBar_list a").each(function(){
			   if($(this).attr("data-url") == herf){
				   menuIndex1 = $(".leftsideBar_list a").index($(this));
				   return false;
			   }
		   });
		   //window.location.href = herf+"?menuIndex="+menuIndex1;
	  }
	  var menuIndex2 = 0;
	  var id = $(".header_nav a").eq(menuIndex2).attr("id");
	  if(menuIndex1 >= 0 && menuIndex2 >= 0){
		  var fenGeFu = encodeURI("|");
		  menuIndex2 = menuIndex2+fenGeFu+id;
		  var url = herf+"?menuIndex1="+menuIndex1+"&&menuIndex2="+menuIndex2;
		  var isNeedWarnFlag = $("#isNeedWarnFlag").val();
		  if(null==isNeedWarnFlag||typeof(isNeedWarnFlag)=="undefined"||""==isNeedWarnFlag||"noNeed"==isNeedWarnFlag){
			  window.location.href = url;
		  }else{
			  $.post('${ctx}/getWarnMsg', function(result) {
				  	console.log(result.warnMsgList);
				  	var warnMsgList = result.warnMsgList;
				  	if(null !=warnMsgList && warnMsgList.length>0){
				  		var warnMsghtml = "";
				  		for(var i = 0 ;i<warnMsgList.length;i++){
				  			var warnMsg = warnMsgList[i];
				  	        if(null==warnMsg||typeof(warnMsg)=="undefined"||""==warnMsg){
				  	        	warnMsg = "";
				  	        }else{
				  	        	if(warnMsg.length>25){
				  	        		warnMsg = warnMsg.substring(0,25)+"...";
				  	        	}
				  	        }
				  			warnMsghtml += "<p title='"+warnMsgList[i]+"'>"+(i+1)+".&nbsp;&nbsp;&nbsp;&nbsp;"+warnMsg+"；</p>";
				  		}
				  		$("#popup_list").html(warnMsghtml);
				  		$("#urlIndexWarnMsg").val(url);
				  		myFnPopupIndexx('#popupIndexWarnMsg');
				  	}else{
				  		window.location.href = url;
				  	}
				}, 'json');
		  }
	  }
	})
	
	

	
	//打开弹窗
function myFnPopupIndexx(obj){
	$('#layerIndexWarnMsg').show();
	$(obj).show();
	$(obj).find('a:eq(0)').click(function(){
		myFnShutDownIndexx(obj);
	})
// 	$(obj).find('.a1').click(function(){
// 		fnShutDown(obj);
// 	});
	$(obj).find('.a2').click(function(){
		myFnShutDownIndexx(obj);
	});
}
//关闭弹窗
function myFnShutDownIndexx(obj){
	$('#layerIndexWarnMsg').hide();
	$(obj).hide();
}

function confirmWarnMsgIndexx(){
	var urlIndexWarnMsg = $("#urlIndexWarnMsg").val();
	window.location.href = urlIndexWarnMsg;
}


$(function(){
	fnPopup('#popup1');
	if($('#popup_list p').size()>5){
		$('#popup_scroll').show();
		$('#popup_scroll_son').show();
		fnPopupScroll('popup_par','popup_list','popup_scroll','popup_scroll_son',true)
	}else{
		$('#popup_scroll').hide();
		$('#popup_scroll_son').hide();
	}

})
</script>


<div class="layer" id="layerIndexWarnMsg"></div>
<div class="popup popup3 popup7" id="popupIndexWarnMsg">
	<a href="javaScript:confirmWarnMsgIndexx();" class="close"></a>
    <h2>到期提醒</h2>
    <h3>以下机构有功能模块即将到期：</h3>
    <div class="popup_par" id="popup_par">
    	<div class="popup_list" id="popup_list">
        	
        </div>
        <div class="popup_scroll" id="popup_scroll">
        	<div class="popup_scroll_son" id="popup_scroll_son"></div>	
        </div>
    </div>
    <div class="popup_btn">
    	<input type="hidden" id="urlIndexWarnMsg">
    	<a href="javaScript:confirmWarnMsgIndexx();" class="a1 fl">确定</a>
    </div>
</div>
</body>
</html>