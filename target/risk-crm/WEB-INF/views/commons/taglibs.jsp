<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--shiro 标签 --%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>  
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path ;
request.setAttribute("ctx",basePath);
%>
<script src="${ctx}/resources/js/jquery.min.js?v=2.1.4"></script>
<script	src="${ctx}/resources/js/jquery.validate.js"></script>
<script type="text/javascript">
//全局ajax session 过期 跳转登陆	
jQuery(function($){  
	 //首先备份下jquery的ajax方法  
    var _ajax=$.ajax;  
       
    //重写jquery的ajax方法  
    $.ajax=function(opt){  
        //备份opt中error和success方法  
        var fn = {  
            error:function(XMLHttpRequest, textStatus, errorThrown){},  
            success:function(data, textStatus){}  
        }  
        if(opt.error){  
            fn.error=opt.error;  
        }  
        if(opt.success){  
            fn.success=opt.success;  
        }  
           
        //扩展增强处理  
        var_opt = $.extend(opt,{  
            error:function(XMLHttpRequest, textStatus, errorThrown){  
                //错误方法增强处理  
                fn.error(XMLHttpRequest, textStatus, errorThrown);  
            },  
            success:function(data, textStatus){  
                //成功回调方法增强处理  
                if("300" == data.user_status){
//             		alert(data.message,'${ctx }/login');
            		loginAlert(data.message);  
            	}else{
            		fn.success(data, textStatus);  
            	}
            } 
        });  
        return _ajax(opt);  
    }; 
 
});
//退出登录
function onlinePersonLoginOut(){
	$.post('${ctx}/logout', function(result) {
        window.location.href='${ctx }/login';
	}, 'json');
}
var zIndex = 2000;
function loginAlert(msg, callback) {
    var layerFlag = false;
    var alertDiv = "";
    var layer = $("#layer");

    if (!layer.get(0)) {
        alertDiv += "<div class='layer' id='layer' style='display: block;'></div>";
    } else {
        layer.css("display", "block");
    }

    $(".popup").each(function(){
        if ('none' == $(this).css("display")) {return;}
        if (zIndex == $(this).css("z-index")) {
            layerFlag = true;
            $(this).css("z-index",0);
        }
    });

    alertDiv +=
        "<div class='popup popup2' id='myPopupAlert' style='display: block;'>" +
        "<a href='javaScript:;' class='colse'></a>" +
        "<div style='height:100px;'>"+
        "<p style='word-break:break-word; line-height:25px; margin:0 15px;'>"+msg+"</p>" +
        "</div>"+
        "<div class='popup_btn' style='width:75px; margin-top:50px;'>" +
        "<a href='javaScript:onlinePersonLoginOut();' class='a1 fl'>确定</a>" +
        "</div>" +
        "</div>";
    $("body").append(alertDiv);

    $(document).on("click", "#myPopupAlert", function () {
        $("#myPopupAlert").remove();
        if (!layerFlag){
            $("#layer").css("display", "none");
        }
        $(".popup").each(function(){
            $(this).css("z-index",zIndex);
        });
        //
        if (callback) {
            //
            window.location.href = callback;
        }
    });
}
</script>

