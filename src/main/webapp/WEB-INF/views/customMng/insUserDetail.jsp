<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!doctype html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>客户管理-账号管理-用户管理-查看用户信息 </title>

<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!-- <script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script> -->
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
<style type="text/css">
.input_table input{width:222px;}

.myError_info {
/*     background: url("${ctx}/resources/image/error.png") 0 3px no-repeat; */
    color: #ed7563;
    font-size: 12px;
    height: 20px;
    line-height: 20px;
    text-indent: 18px;
    position: inherit;
    left: 10px;
    bottom: 0;
    z-index: 1;
}

</style>
<script type="text/javascript">
/*页面初始化*/
$(function() {
	
})


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
                <a href="${ctx}/custom/toInsUserManagePage?id=${insId }">用户管理</a>
                <span>></span>
                <a href="${ctx}/custom/toInsUserLookDetailPage?userId=${userId }&&insId=${insId }">查看用户信息</a>
            </h3>
            <div class="backtrack">
            	<a href="${ctx}/custom/toInsUserManagePage?id=${insId }">返回</a>
            </div>
            <div class="shade main_minHeight">
            	<div class="input_list_box">
                    <form id="userAddForm">
                    	<input type="hidden" id="insId" value="${insId }" >
                        <table class="input_table">
                            <tbody>
                                <tr>
                                    <td><strong>用户名</strong></td>
                                    <td>
                                        <input value="${userVoFg.loginName }" type="text" id="loginName" name="loginName" readonly="readonly"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td><strong>姓名</strong></td>
                                    <td><input value="${userVoFg.name }" type="text" id="name" name="name" readonly="readonly" /></td>
                                </tr>
                                <tr>
                                    <td><strong>角色</strong></td>
                                    <td><input value="${userVoFg.roleName }" type="text" id="name" name="name" readonly="readonly" /></td>
                                </tr>
                                <tr>
                                    <td><strong>手机号</strong></td>
                                    <td>
                                       <input value="${userVoFg.phone }" type="text" id="phone" readonly="readonly"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td><strong>邮箱</strong></td>
                                    <td>
                                        <input value="${userVoFg.email }" type="text" id="email" readonly="readonly"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td><strong></strong></td>
                                    <td>
                                        <div class="input_btn clear">
                                            <a href="${ctx}/custom/toInsUserManagePage?id=${insId }" class="fl makeSure">确定</a>
                                        </div>
                                    </td>
                                </tr>
                                
                            </tbody>
                        </table>
                    </form>	
                </div>   
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
<!-- 成功标识.html start -->
<div class="popup popup2" id="popup2">
	<a href="javaScript:;" class="close"></a>
    <p></p>
    <div class="popup_btn delete_btn">
    	<a href="javaScript:;" class="a1 fl">确定</a>
    </div>
</div>
<!-- 成功标识.html end -->
</body>
</html>
