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
    <script src="${ctx}/resources/js/businessMng/common.js"></script>
<style type="text/css">
    .addCard_table td input{width:100%; text-align:center;}
</style>
<script type="text/javascript">
    $(function () {
        var id = $("#modelId").val();

        //新增标记
        if ("" == id) {
            $("#modelId").val("-1");
        }

        $(document).on("keyup", "input[name='codes']", function () {
            validateNum(this);
        });
        $(document).on("blur", "input[name='degrees']", function () {
            validateDegree(this);
        });

        //下一行赋值
        $(document).on("keyup", "input[name='valueMaxs']", function () {
            //区间结束值
            var end = $(this).val();

            //设置下个区间开始值
            $(this).parent().parent().next().find("input[name='valueMins']").val(end);
        });
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
                <a href="${ctx}/businessMng/modelIndex">评分卡管理</a>
                <span>></span>
                <c:if test="${'add' == method}">
                    <a href="javascript:void(0);">新增评分卡</a>
                </c:if>
                <c:if test="${'edit' == method}">
                    <a href="javascript:void(0);">编辑评分卡</a>
                </c:if>
            </h3>
            <%--<div class="backtrack">
                <a href="javascript:history.go(-1);">返回</a>
            </div>--%>
            <form id="frm">
            <div class="shade">
                <div class="search_box clear">
                    <div class="checkData_box addCard clear">
                        <strong>评分卡名称</strong>
                        <span><input type="text" name="name" id="modelName" value="${model.name}" maxlength="16" placeholder="请输入评分卡名称" /></span>
                    </div>
                </div>
                <div class="module_height addCard_height">
                    <datalist id="itemlist">
                        <option value="C">C</option>
                        <option value="CC">CC</option>
                        <option value="CCC-">CCC-</option>
                        <option value="CCC">CCC</option>
                        <option value="CCC+">CCC+</option>
                        <option value="B">B-</option>
                        <option value="B">B</option>
                        <option value="B+">B+</option>
                        <option value="BB-">BB-</option>
                        <option value="BB">BB</option>
                        <option value="BB+">BB+</option>
                        <option value="BBB-">BBB-</option>
                        <option value="BBB">BBB</option>
                        <option value="BBB+">BBB+</option>
                        <option value="A-">A-</option>
                        <option value="A">A</option>
                        <option value="A+">A+</option>
                        <option value="AA-">AA-</option>
                        <option value="AA">AA</option>
                        <option value="AA+">AA+</option>
                        <option value="AAA">AAA</option>
                    </datalist>
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
                            <th class="module_width0 delBor"></th>
                        </tr>
                        </thead>
                        <tbody id="addCard_tbody">
                            <c:if test="${fn:length(model.ruleList) == 0}">
                                <tr>
                                    <td>
                                        <input type="hidden" name="ruleIds" value="-1" />
                                        <input type="text" name="codes" value="${rule.code}"  maxlength='15' />
                                    </td>
                                    <td><input type="text" name="valueMins" maxlength='10' /></td>
                                    <td><input type="text" name="valueMaxs"  maxlength='15' /></td>
                                    <td style="display:none;"><input type="text" name="scores"  maxlength='15' value="0" /></td>
                                    <td><input type="text" name="degrees"  maxlength='10' /></td>
                                    <td class="delBor">
                                        <a href="javascript:void(0);" class="credit_a credit_a1" onclick="addTr(this);"></a>
                                    </td>
                                </tr>
                            </c:if>
                            <c:if test="${fn:length(model.ruleList) > 0}">
                                <c:forEach items="${model.ruleList}" var="rule" varStatus="idx">
                                    <tr>
                                        <td>
                                            <input type="hidden" name="ruleIds" value="${rule.id}" />
                                            <input type="text" name="codes" value="${rule.code}"  maxlength='10' />
                                        </td>
                                        <td><input type="text" name="valueMins" value="${rule.valueMin}"  maxlength='15' /></td>
                                        <td><input type="text" name="valueMaxs" value="${rule.valueMax}"  maxlength='15' /></td>
                                        <td style="display:none;"><input type="text" name="scores" value="${rule.score}"  maxlength='15' /></td>
                                        <td><input type="text" name="degrees" value="${rule.degree}" maxlength='10' list="itemlist" /></td>
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
                <a href="javaScript:;" class="fl" onclick="saveModel('${ctx}/businessMng/modelSaveOrUpdate', '${ctx}/businessMng/modelIndex');">保存</a>
                <a href="javascript:history.go(-1);" class="fr">取消</a>
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
