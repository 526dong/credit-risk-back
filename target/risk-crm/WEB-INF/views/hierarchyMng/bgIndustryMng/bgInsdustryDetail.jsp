<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!doctype html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>
分层管理-相关性管理-查看后台行业
</title>

<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!-- <script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script> -->
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>

<script type="text/javascript">

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
                <span>></span>
                <a href="${ctx}/insdustryManage/toCorrelationSetPage?tabFlag=tabFlag">后台行业</a>
                <span>></span>
                <a href="${ctx}/insdustryManage/toLookBgInsdustryPage?bgInsdustryId=${bgInsdustryId }">查看后台行业</a>
            </h3>
            <div class="backtrack">
            	<a href="${ctx}/insdustryManage/toCorrelationSetPage?tabFlag=tabFlag">返回</a>
            </div>
            <div class="shade main_minHeight">
            	<div class="search_box clear">
            		<h3 class="checkInfo_h3">基本信息</h3>
                	<div class="checkData_box addCard clear">
                    	<strong>后台行业名称</strong>
                        <span>${insdustryBean.insdustryName }</span>
                    </div>  
                </div>
                <div class="search_box clear">
            		<h3 class="checkInfo_h3">匹配前台行业</h3>
            	</div>
                <div class="mateInfo">
                	<c:choose>
					    <c:when test="${empty insdustryBeanList }">
					       <div class="tip_box">本评分卡暂未匹配相应的前台行业，请返回相关性管理页面进行匹配！</div>
					    </c:when>
					    <c:otherwise>
					        <c:forEach var="itemm" items="${insdustryBeanList }">
					        	<div class="clear">
			                        <div class="info_box info_box1 info_box2 fl">
			                            <strong>行业</strong>
			                            <p>${itemm.matchFirstInsdustryName }-${itemm.matchSecondInsdustryName }</p>
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

</body>
</html>
