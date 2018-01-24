<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>业务管理-指标管理-因素定义-指标定义-编辑指标信息</title>
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
    .info_box input{width:220px;}
    .addCard_table td input{width:100%; text-align:center;}
</style>
    <script type="text/javascript" src="${ctx}/resources/js/bootstrap.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
    <script src="${ctx}/resources/js/businessMng/index.js"></script>
    <script src="${ctx}/resources/js/businessMng/common.js"></script>
    <script src="${ctx}/resources/js/myValidate.js"></script>
<script type="text/javascript">
    $(function () {
        var id = $("#indexId").val();

        //新增标记
        if ("" == id) {
            $("#indexId").val("-1");
        }

        //分值大小验证
        $(document).on("blur", "input[name='scores']", function () {
            validateScores(this);
        });
        //分值标号
        $(document).on("keyup", "input[name='codes']", function () {
            validateNum(this);
        });
        $(document).on("blur", "input[name='degrees']", function () {
            validateDegree(this);
        });

        var type = "${index.regularIndexFlag}";
        switchIndexType(type);

        //绑定指标类型
        $(".popup_symbol_list li").on("click", function () {
            var type = $(this).attr("data-att");
            $("#regularIndexFlag").val(type);
            switchIndexType(type);
        });

        //初始化年份下拉
        initSelect();

        searchFormula($("#formulaName").get(0), '${ctx}/businessMng/indexSearchFormula', 0, ${model.reportTypeId});
        $("#formulaDiv").hide();

        //禁止div切换下拉
        $(".select_btn").has("input[type='text']").off("click");
        //隐藏下拉
        $(document).on("click", function () {
            $("#formulaDiv").hide();
            var checkedFlag = false;

            $("#formulaName").parent().next().find("p").each(function () {
                if ($(this).text() ==  $("#formulaName").val()) {
                    checkedFlag = true;
                    return false;
                }
            });
            if (!checkedFlag) {
                $("#formulaName").val("请选择");
                $("#formulaName").prev().val("-1");
            }
        });
        //停止公式冒泡
        $("#formulaName").on("click", function (event) {
            event.stopPropagation();
        });

        //分值区间最小值和最大值赋值设计
        validateMinAndMax();
    });

    //初始化下拉
    function initSelect() {
        var selectItem = $(".select_list p[class='active']");

        showYear(selectItem);
    }


    //初始化公式年份
    function showYear(ele) {
        var len = ele.attr("data-id");

        ele.parent().prev().find("input").val(len);
        ele.parent().prev().find("span").attr("data-id", ele.attr("data-id"));
        ele.parent().prev().find("span").text(ele.text());
    }

    //分值区间最小值和最大值赋值设计
    function validateMinAndMax(){
        //最大值传到下一行的最小值
        $(document).on("keyup", "input[name='valueMaxs']", function () {
            //区间结束值
            var end = $(this).val();

            //设置下个区间开始值
            $(this).parent().parent().next().find("input[name='valueMins']").val(end);
        });

        //验证下一行最大值大于上一行的最大值
        $(document).on("blur","input[name='valueMaxs']",function () {
            var max = $(this).val();
            var min = $(this).parent().parent().find("input[name='valueMins']").val();
            if(max<=min){
                alert("最大值应该大于等于该行最小值，请重新输入最大值");
                $(this).val("");
            }
        })

        //最小值传到上一行的最大值
        /*$(document).on("blur", "input[name*='Mins']", function () {
            //当前区间开始值
            var start = $(this).val();

            //上一个区间结束值
            $(this).parent().parent().prev().find("input[name='valueMaxs']").val(start);
        });*/
    }
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
                <c:if test="${'add' == method}">
                    <a href="javascript:void(0);">新增指标</a>
                </c:if>
                <c:if test="${'edit' == method}">
                    <a href="javascript:void(0);">修改指标信息</a>
                </c:if>
            </h3>
            <%--<div class="backtrack">
                <a href="javascript:history.go(-1);">返回</a>
            </div>--%>
            <form id="frm">
                <input type="hidden" name="delIds" id="delIds" />
                <input type="hidden" name="eleId"  value="${element.id}"/>
                <input type="hidden" name="modelId"  value="${model.id}"/>
                <input type="hidden" name="id" id="indexId" value="${index.id}"/>
                <input type="hidden" name="method" value="${method}"/>
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
                                <strong>指标编号<i>*</i></strong>
                                <input type="text" id="indexCode" name="indexCode" value="${index.indexCode}" maxlength="12" />
                            </div>
                        </div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                            <div class="info_box info_box1 factor factor1 clear">
                                <strong>指标中文名称<i>*</i></strong>
                                <input type="text" id="indexName" name="indexName" value="${index.indexName}" maxlength="16" />
                            </div>
                        </div>

                        <div class="col-lg-6 col-sm-6 col-md-6">
                            <div class="info_box info_box1 factor factor1 clear">
                                <strong>权重<i>*</i></strong>
                                <input type="text" id="indexWeight" name="indexWeight" value="${index.indexWeight}" maxlength="8" />
                            </div>
                        </div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                            <div class="info_box info_box1 factor factor1 clear">
                                <strong>指标英文名称</strong>
                                <input type="text" name="indexEnName" value="${index.indexEnName}" maxlength="10" />
                            </div>
                        </div>

                        <div class="col-lg-6 col-sm-6 col-md-6" id="avgYears">
                            <div class="info_box info_box1 factor factor1 clear">
                                <strong>平均年数<i>*</i></strong>
                                <div class="down_menu">
                                    <div class="select_btn">
                                        <span data-id=""></span>
                                        <input type="hidden" name="aveYears" id="aveYears" value="${index.aveYears}" />
                                    </div>
                                    <div class="select_list">
                                        <p onclick="showYear($(this));" data-id="1" <c:if test="${null == index.aveYears or 1 == index.aveYears}">class="active"</c:if>>1年</p>
                                        <p onclick="showYear($(this));" data-id="2" <c:if test="${2 == index.aveYears}">class="active"</c:if>>2年</p>
                                        <p onclick="showYear($(this));" data-id="3" <c:if test="${3 == index.aveYears}">class="active"</c:if>>3年</p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-lg-6 col-sm-6 col-md-6">
                            <div class="info_box info_box1 factor factor1 clear">
                                <strong>指标类型</strong>
                                <ul class="popup_symbol_list popup_symbol_list1 fl">
                                    <input type="hidden" id="regularIndexFlag" name="regularIndexFlag" value="${index.regularIndexFlag}">
                                    <li style="width: 106px" <c:if test="${index.regularIndexFlag ==1 or index.regularIndexFlag==null}">class="active"</c:if> data-att="1">非财务指标</li>
                                    <li style="width: 85px" <c:if test="${index.regularIndexFlag ==0}">class="active" </c:if> data-att="0">财务指标</li>
                                </ul>
                            </div>
                        </div>

                        <div class="col-lg-12 col-sm-12 col-md-12">
                            <div class="info_box info_box1 factor factor1 clear">
                                <strong>指标描述</strong>
                                <div class="factor2">
                                    <textarea name="indexDescribe" >${index.indexDescribe}</textarea>
                                </div>
                            </div>
                        </div>

                        <div class="col-lg-6 col-sm-6 col-md-6" id="formule">
                            <div class="info_box info_box1 factor factor1 clear">
                                <strong>公式定义</strong>
                                <div class="down_menu">
                                    <div class="select_btn">
                                        <input type="hidden" name="formulaId" id="formulaId" value="${index.formulaId}" />
                                        <input type="text" autocomplete="off" name="formulaName" id="formulaName" onclick="searchFormula(this, '${ctx}/businessMng/indexSearchFormula', 1, ${model.reportTypeId})" onkeyup="searchFormula(this, '${ctx}/businessMng/indexSearchFormula', 0, ${model.reportTypeId})"  placeholder="请输入公式名搜索" value="${index.formulaName}" maxlength="16" />
                                    </div>
                                    <div id="formulaDiv" class="select_list"></div>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                            <div class="info_box info_box1 factor factor1 mateInfo_table clear">
                                <strong>是否使用</strong>
                                <c:if test="${1 == index.state }">
                                    <input type="hidden" name="state" value="1" />
                                    <a id="state" href="javaScript:;" data-id="1" class="curr factor_w1">是</a>
                                </c:if>
                                <c:if test="${0 == index.state or empty index.state}">
                                    <input type="hidden" name="state" value="0" />
                                    <a id="state" href="javaScript:;" data-id="0" class="curr factor_w1">是</a>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="module_height addCard_height">
                    <table id="index" class="module_table addCard_table">
                        <thead>
                        <tr>
                            <th>分值编号</th>
                            <th class="ration">最小值</th>
                            <th class="ration">最大值</th>
                            <th class="nature" colspan="2">定性指标打分标准</th>
                            <th>分值</th>
                            <th>对应等级</th>
                            <th class="module_width0 delBor"></th>
                        </tr>
                        </thead>
                        <tbody id="addCard_tbody">
                        <c:if test="${fn:length(index.ruleList) == 0}">
                            <tr>
                                <td>
                                    <input type="hidden" name="ruleIds" value="-1" />
                                    <input type="text" name="codes" value="${rule.code}" maxlength="10" />
                                </td>
                                <td class="ration"><input type="text" name="valueMins" maxlength="15" placeholder="不填写表示负无穷" /></td>
                                <td class="ration"><input type="text" name="valueMaxs" maxlength="15" placeholder="不填写表示正无穷" /></td>
                                <td colspan="2" class="nature"><input type="text" name="values" maxlength="200" /></td>
                                <td><input type="text" name="scores" maxlength="10" /></td>
                                <td><input type="text" name="degrees" maxlength="10" /></td>
                                <td class="delBor">
                                    <a href="javascript:void(0);" class="credit_a credit_a1" onclick="addTr(this);"></a>
                                </td>
                            </tr>
                        </c:if>
                        <c:if test="${fn:length(index.ruleList) > 0}">
                            <c:forEach items="${index.ruleList}" var="rule" varStatus="idx">
                                <tr>
                                    <td>
                                        <input type="hidden" name="ruleIds" value="${rule.id}" />
                                        <input type="text" name="codes" value="${rule.code}" maxlength="10"  />
                                    </td>
                                    <td class="ration"><input type="text" name="valueMins" value="${rule.valueMinStr}" maxlength="15" placeholder="不填写表示负无穷" /></td>
                                    <td class="ration"><input type="text" name="valueMaxs" value="${rule.valueMaxStr}" maxlength="15" placeholder="不填写表示正无穷" /></td>
                                    <td colspan="2" class="nature"><input type="text" name="values" value="${rule.value}" maxlength="200" /></td>
                                    <td><input type="text" name="scores" value="${rule.score}" maxlength="10" /></td>
                                    <td><input type="text" name="degrees" value="${rule.degree}" maxlength="10" /></td>
                                    <td class="delBor">
                                        <c:if test="${idx.index == 0}">
                                            <a href="javascript:void(0);" class="credit_a credit_a1" onclick="addTr(this);"></a>
                                        </c:if>
                                        <c:if test="${idx.index > 0}">
                                            <a href="javascript:void(0);" class="credit_a" onclick="delTr(this, ${rule.id});"></a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
            </form>
            <div class="main_btn main_btn1">
                <a href="javaScript:;" class="fl" onclick="saveIndex('${ctx}/businessMng/indexSaveOrUpdate', '${ctx}/businessMng/indexIndex?eleId=${element.id}&modelId=${model.id}');">保存</a>
                <a href="javascript:history.go(-1);" class="fr cancel">取消</a>
            </div>
        </div>
        <!-- 右侧内容.html end -->
    </div>
    <!-- center.html end -->
</div>
</body>
</html>