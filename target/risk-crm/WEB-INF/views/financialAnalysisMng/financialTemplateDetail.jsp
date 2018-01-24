<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!doctype html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>业务管理-财务分析管理-查看</title>
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!-- <script type="text/javascript" src="js/jquery-1.12.4.js"></script> -->
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>


<script type="text/javascript">




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
                <a href="javascript:void(0);">业务管理</a>
                <span>></span>
                <a href="${ctx}/financialAnaly/financialTemplateList">财务分析管理</a>
                <span>></span>
                <a href="${ctx}/financialAnaly/toFinancialTemplateDetail?financialId=${financialAnalyBean.id}">查看</a>
            </h3>
            <div class="backtrack">
            	<a href="${ctx}/financialAnaly/financialTemplateList">返回</a>
            </div>
            <div class="shade main_minHeight">
               <dl class="finance_analysis">
                   <dt>基本信息</dt>
                   <dd><b>报表类型</b><span>${financialAnalyBean.reportTypeName}</span></dd>
                   <dd><b>模板名称</b><span>${financialAnalyBean.name}</span></dd>
                   <dt>模板内容</dt>
                   <dd><b>模板名称</b><a href="${ctx}/financialAnaly/exportFinancialTemplate?financialId=${financialAnalyBean.id}">${financialAnalyBean.name}</a></dd>
               </dl>
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
