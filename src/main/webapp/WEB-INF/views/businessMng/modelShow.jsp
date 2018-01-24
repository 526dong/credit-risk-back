<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>业务管理-评分卡管理-查看评分卡 </title>
    <link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet" />
    <link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
    <link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
    <script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
    <script src="${ctx}/resources/js/businessMng/model.js"></script>
<style type="text/css">
</style>
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
                <a href="${ctx}/businessMng/modelIndex">评分卡管理</a>
                <span>></span>
                <a href="javascript:void(0);">查看评分卡</a>
            </h3>
            <div class="backtrack">
                <a href="javascript:history.go(-1);">返回</a>
            </div>
            <div class="shade">
                <div class="search_box clear">
                    <div class="checkData_box addCard clear">
                        <strong>评分卡名称</strong>
                        <c:if test="${'show' == method}">
                            <span>${model.name}</span>
                        </c:if>
                        <c:if test="${'add' == method or 'edit' == method}">
                            <span><input type="text" name="name" id="modelName" value="${model.name}" maxlength="16" placeholder="请输入评分卡名称" /></span>
                        </c:if>
                    </div>
                </div>
                <div class="module_height addCard_height">
                    <table id="model" class="module_table addCard_table">
                        <input type="hidden" name="id" id="modelId" value="${model.id}" />
                        <input type="hidden" name="delIds" id="delIds" />
                        <thead>
                        <tr>
                            <th>分值编号</th>
                            <th>最小值</th>
                            <th>最大值</th>
                            <%--<th>分值</th>--%>
                            <th>对应等级</th>
                            <c:if test="${'add' == method or 'edit' == method}">
                                <th class="module_width0 delBor"></th>
                            </c:if>
                        </tr>
                        </thead>
                        <!-- 详情 -->
                        <tbody>
                            <c:if test="${fn:length(model.ruleList) > 0}">
                                <c:forEach items="${model.ruleList}" var="rule">
                                    <tr>
                                        <td>${rule.code}</td>
                                        <td>${rule.valueMin}</td>
                                        <td>${rule.valueMax}</td>
                                        <%--<td>${rule.score}</td>--%>
                                        <td>${rule.degree}</td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            <c:if test="${fn:length(model.ruleList) == 0}">
                                <tr><td colspan="5">暂无数据</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
            <!--模型匹配-->
            <c:if test="${'show' == method}">
                <h2 class="credit_title">匹配行业与规模</h2>
                <div class="shade_bor">
                    <div class="container-fluid main_padd">
                        <div class="row">
                            <!--第一种状态-->
                            <c:if test="${fn:length(list) > 0}">
                                <c:forEach items="${list}" var="industry">
                                    <div class="col-lg-6 col-sm-6 col-md-6">
                                        <div class="info_box info_box1 info_box2 clear">
                                            <strong>行业</strong>
                                            <p>${industry.name1} - ${industry.name2}</p>
                                        </div>
                                    </div>
                                    <div class="col-lg-6 col-sm-6 col-md-6">
                                        <div class="info_box info_box1 info_box2 clear">
                                            <strong>规模</strong>
                                            <p><c:if test="${industry.type == 0}">大中型</c:if><c:if test="${industry.type == 1}">小微型</c:if></p>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:if>
                            <!--第二种状态-->
                            <c:if test="${fn:length(list) == 0}">
                                <div class="col-lg-6 col-sm-6 col-md-6">
                                    <div class="info_box info_box1 info_box2 clear">
                                        <strong>本评分卡暂未匹配使用的行业与规模，请返回评分卡管理页面进行匹配！</strong>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${'add' == method or 'edit' == method}">
                <div class="main_btn main_btn1">
                    <a href="javaScript:;" class="fl" onclick="saveModel('${ctx}/businessMng/modelSaveOrUpdate', '${ctx}/businessMng/modelIndex');">保存</a>
                    <a href="javaScript:;" class="fr">取消</a>
                </div>
            </c:if>
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
