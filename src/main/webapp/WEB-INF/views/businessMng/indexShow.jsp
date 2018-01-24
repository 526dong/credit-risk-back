<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>业务管理-指标管理-因素定义-指标定义-查看指标信息</title>
    <link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet" />
    <link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
    <link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<style type="text/css">
    .ration {
        display: none;
    }
    .nature{
        display: table-cell;
    }
</style>
    <script type="text/javascript" src="${ctx}/resources/js/bootstrap.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
    <script src="${ctx}/resources/js/businessMng/index.js"></script>
<script type="text/javascript">
	$(function () {
		var id = $("#indexId").val();

		//新增标记
		if ("" == id) {
            $("#indexId").val("-1");
		}

		var type = "${index.regularIndexFlag}";
		if ("0" == type) {
            showRation();
        } else {
            showNature();
        }

    });



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
                <a href="${ctx}/businessMng/indexMngIndex">指标管理</a>
                <span>></span>
                <a href="${ctx}/businessMng/elementIndex?modelId=${model.id}">因素定义</a>
                <span>></span>
                <a href="${ctx}/businessMng/indexIndex?eleId=${element.id}&modelId=${model.id}">指标定义</a>
                <span>></span>
                <a href="javascript:void(0);">查看指标信息</a>
            </h3>
            <div class="backtrack">
                <a href="javascript:history.go(-1);">返回</a>
            </div>
            <div class="shade main_minHeight">
                <div class="container-fluid newDefinition">
                    <div class="row">
                        <div class="col-lg-6 col-sm-6 col-md-6">
                            <div class="info_box info_box1 factor factor1 clear">
                                <strong>指标所属评分卡</strong>
                                <p>${model.name}</p>
                            </div>
                        </div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                            <div class="info_box info_box1 factor factor1 clear">
                                <strong>指标所属因素</strong>
                                <p>${element.name}</p>
                            </div>
                        </div>

                        <div class="col-lg-6 col-sm-6 col-md-6">
                            <div class="info_box info_box1 factor factor1 clear">
                                <strong>指标编号</strong>
                                <p>${index.indexCode}</p>
                            </div>
                        </div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                            <div class="info_box info_box1 factor factor1 clear">
                                <strong>指标中文名称</strong>
                                <p>${index.indexName}</p>
                            </div>
                        </div>

                        <div class="col-lg-6 col-sm-6 col-md-6">
                            <div class="info_box info_box1 factor factor1 clear">
                                <strong>权重</strong>
                                <p>${index.indexWeight}</p>
                            </div>
                        </div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                            <div class="info_box info_box1 factor factor1 clear">
                                <strong>指标英文名称</strong>
                                <p>${index.indexEnName}</p>
                            </div>
                        </div>

                        <div class="col-lg-6 col-sm-6 col-md-6">
                            <div class="info_box info_box1 factor factor1 clear">
                                <strong>平均年数</strong>
                                <p>${index.aveYears}</p>
                            </div>
                        </div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                            <div class="info_box info_box1 factor factor1 clear">
                                <strong>公式定义</strong>
                                <p>${index.formulaName}</p>
                            </div>
                        </div>

                        <div class="col-lg-12 col-sm-12 col-md-12">
                            <div class="info_box info_box1 factor factor1 clear">
                                <strong>指标描述</strong>
                                <p>${index.indexDescribe}</p>
                            </div>
                        </div>

                        <div class="col-lg-6 col-sm-6 col-md-6">
                            <div class="info_box info_box1 factor factor1 clear">
                                <strong>是否定性指标</strong>
                                <p>
                                    <c:if test="${index.regularIndexFlag ==1}">是</c:if>
                                    <c:if test="${index.regularIndexFlag ==0}">否</c:if>
                                </p>
                            </div>
                        </div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                            <div class="info_box info_box1 factor factor1 clear">
                                <strong>是否使用</strong>
                                <p>
                                    <c:if test="${index.state == 1}">是</c:if>
                                    <c:if test="${index.state == 0}">否</c:if>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="module_height addCard_height">
                    <table id="index" class="module_table addCard_table">
                        <thead>
                        <th>分值编号</th>
                        <th class="ration">最小值</th>
                        <th class="ration">最大值</th>
                        <th class="nature" colspan="2">定性指标打分标准</th>
                        <th>分值</th>
                        <th>对应等级</th>
                        </thead>
                        <tbody id="addCard_tbody">
                        <c:forEach items="${index.ruleList}" var="rule">
                            <tr>
                                <td>${rule.code}</td>
                                <td class="ration">${rule.valueMinStr}</td>
                                <td class="ration">${rule.valueMaxStr}</td>
                                <td colspan="2" class="nature">${rule.value}</td>
                                <td>${rule.score}</td>
                                <td>${rule.degree}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <!-- 右侧内容.html end -->
    </div>
    <!-- center.html end -->
</div>
</body>
</html>
