<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>业务管理-指标公式库-编辑指标公式</title>
    <link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet" />
    <link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
    <link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<style type="text/css">
    .addData_table textarea{height:200px;}
    .statement_list td.active{background-color:#38abf8}
    .checkData_box1{margin-top:10px;}
    .fomula_collection{
        width: 96%;
        margin: 0 0 10px 0;
        height: 200px;
        line-height: 18px;
        border: 1px solid #ccc;
        overflow: hidden;
        padding: 5px 1%;
        color: #666;
        overflow-y: auto;
    }
    .fomula_item{
        /*float: left;*/
        position: relative;
        display: inline-block;
        margin: 0 7px;
        /*padding: 10px 5px 10px 5px;*/
        border: 1px solid #fff;
        line-height: 1;
    }
    .fomula_collection .selected{
        /*border: 1px solid #ccc;*/
        background-color:#c6dbe8;
    }
    .fomula_item b{
        font-weight: normal;
    }
    .fomula_item .fomula_mark{
        position: absolute;
        right: -5px;
        top: 0;
        display: inline-block;
        width: 15px;
        height: 15px;
        z-index: 3;
    }
    .fomula_item .fomula_del{
        background: url(${ctx}/resources/image/del_mark.png) no-repeat;
        background-position: center;
    }
    .fomula_collection .edit1{
        background-color: #dcaf49;
    }
   /* .fomula_operators{
        display: inline-block;
        padding: 10px 5px;
        line-height: 1;
    }*/
</style>
    <script type="text/javascript" src="${ctx}/resources/js/bootstrap.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/move.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
    <script src="${ctx}/resources/js/businessMng/formula.js"></script>
    <script src="${ctx}/resources/js/businessMng/index.js"></script>
<script type="text/javascript">
    $(function(){
        //指标公式库-创建指标公式-选择财报数据滚动
        /*fnPopupScroll('statement_par1','statement_list1','scrollPar1','scroll1');
        fnPopupScroll('statement_par2','statement_list2','scrollPar2','scroll2');
        fnPopupScroll('statement_par3','statement_list3','scrollPar3','scroll3');
        fnPopupScroll('statement_par4','statement_list4','scrollPar4','scroll4');
        fnPopupScroll('statement_par5','statement_list5','scrollPar5','scroll5');
        fnPopupScroll('statement_par6','statement_list6','scrollPar6','scroll6');*/
        fnRadio(".popup_symbol_d2 dd");

        var id = $("#formulaId").val();

        //新增标记
        if ("" == id) {
            $("#formulaId").val("-1");
        }

        //初始化下拉
        initSelect();
        //禁止div切换下拉
        $(".select_btn").has("input[type='text']").off("click");
        //绑定选择
        bindSelectEle();
        //div点击处插入span
        $(".fomula_collection").on("click", function (e) {
            posInsert();
        });

        //公式编辑闪烁
        var cnt = 0;
        window.setInterval(function () {
            if (2 == cnt) {
                cnt = 0;
            }

           $(".fomula_item .edit").each(function () {
                if (0 == cnt) {
                    $(this).parent().addClass("edit1")
                } else {
                    $(this).parent().removeClass("edit1");
                }
            });
            cnt++;
        }, 700);

        //删除按键
        stopE();
    });

    //初始化下拉
    function initSelect() {
        var selectItem = $(".select_list p[class='active']");

        //报表类型
        selectItem.eq(0).parent().prev().find("input").val(selectItem.eq(0).attr("data-id"));
        selectItem.eq(0).parent().prev().find("span").attr("data-id", selectItem.eq(0).attr("data-id"));
        selectItem.eq(0).parent().prev().find("span").text(selectItem.eq(0).text());
        selectItem.eq(0).parent().prev().find("span").attr("title", selectItem.eq(0).text());
        showAllReport("${ctx}/businessMng/prepareReportAllDiv", selectItem.eq(0).attr("data-id"));

        //初始化年份下拉和公式年份
        showFormulaTr(selectItem.eq(1));
    }

    //初始化公式年份
    function showFormulaTr(ele) {
        var len = ele.attr("data-id");

        //公式输入框
        ele.parent().prev().find("input").val(len);
        ele.parent().prev().find("span").attr("data-id", ele.attr("data-id"));
        ele.parent().prev().find("span").text(ele.text());
        $(".formulaTr").each(function (i, ele) {
            if (i< 2*len) {
                $(ele).css("display", "table-row");
            } else {
                $(ele).css("display", "none");
            }
        });
    }

    //增加公式选中
    function addSelect(id, yearLen, obj) {
        var indexLen = $("#yearLen").val();

        if (yearLen < indexLen) {
            alert("公式库定义的年份:"+yearLen+" 小于当前指定的年份:"+indexLen+"！");
            return;
        }

        $("#formulaName").val($(obj).text());
        $("#formulaDiv").css("display", "none");
    }
</script>
<body class="body_bg">
<div class="main">
    <!-- header.html start -->
    <%@ include file="../commons/topHead.jsp"%>
    <!-- header.html end -->
    <!-- center.html start -->
    <div class="main_center">
        <!-- 左侧导航.html start -->
        <%@ include file="../commons/leftNavigation.jsp"%>
        <!-- 左侧导航.html end -->
        <!-- 右侧内容.html start -->
        <div class="right_content">
            <h3 class="place_title">
                <span>当前位置：</span>
                <a href="javascript:void(0);">业务管理</a>
                <span>></span>
                <a href="${ctx}/businessMng/formulaIndex">指标公式库</a>
                <span>></span>
                <c:if test="${'add' == method}" >
                    <a href="javascript:void(0);">创建指标公式</a>
                </c:if>
                <c:if test="${'edit' == method}" >
                    <a href="javascript:void(0);">编辑指标公式</a>
                </c:if>
            </h3>
            <%--<div class="backtrack">
                <a href="javascript:history.go(-1);">返回</a>
            </div>--%>
            <form id="frm">
                <input type="hidden" id="formulaId" name="id" value="${formula.id}" />
                <div class="shade">
                    <div class="container-fluid">
                        <div class="row addData">
                            <div class="col-lg-12 col-sm-12 col-md-12">
                                <div class="checkData_box checkData_box1 addCard clear">
                                    <strong>适用报表类型<font color="red">*</font></strong>
                                    <div class="down_menu">
                                        <div class="select_btn">
                                            <span data-id=""></span>
                                            <input type="hidden" name="reportTypeId" id="reportTypeId" value="${formula.reportTypeId}" />
                                        </div>
                                        <div class="select_list">
                                            <c:forEach items="${reportTypeList}" var="type" varStatus="idx">
                                                <p data-id="${type.id}" title="${type.name}" onclick="showAllReport('${ctx}/businessMng/prepareReportAllDiv', ${type.id});" <c:if test="${(null==formula.reportTypeId and idx.first) or type.id == formula.reportTypeId}">class="active"</c:if>>${type.name}</p>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-6 col-sm-6 col-md-6">
                                <div class="checkData_box checkData_box1 addCard clear">
                                    <strong>公式名称<font color="red">*</font></strong>
                                    <input placeholder="请输入公式名称" type="text" name="formulaName" value="${formula.formulaName}" maxlength="16" />
                                </div>
                            </div>
                            <div class="col-lg-6 col-sm-6 col-md-6">
                                <div class="checkData_box checkData_box1 clear">
                                    <strong>平均年数<font color="red">*</font></strong>
                                    <div class="down_menu">
                                        <div class="select_btn">
                                            <span data-id=""></span>
                                            <input type="hidden" name="yearLen" id="yearLen" value="${formula.yearLen}" />
                                        </div>
                                        <div class="select_list">
                                            <p onclick="showFormulaTr($(this));" data-id="1" <c:if test="${null == formula.yearLen or 1 == formula.yearLen}">class="active"</c:if>>1年</p>
                                            <p onclick="showFormulaTr($(this));" data-id="2" <c:if test="${2 == formula.yearLen}">class="active"</c:if>>2年</p>
                                            <p onclick="showFormulaTr($(this));" data-id="3" <c:if test="${3 == formula.yearLen}">class="active"</c:if>>3年</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <h2 class="credit_title">计算逻辑</h2>
                <div class="shade">
                    <div class="formula">
                        <table class="input_table addData_table">
                            <tbody>
                            <tr class="formulaTr">
                                <td class="addData_td1"></td>
                                <td>
                                    <div class="fl function_btn">
                                        <a href="javaScript:;" class="fl" onclick="showFormula(1);">选择已存公式</a>
                                        <a href="javaScript:;" class="fl" onClick="showReport(1);">选择财报数据</a>
                                        <a href="javaScript:;" class="fl" onClick="showOpt(1);">选择运算符号</a>
                                    </div>
                                </td>
                            </tr>
                            <tr class="formulaTr">
                                <td class="addData_td1"><strong>单表公式<font color="red">*</font></strong></td>
                                <td>
                                    <%--<textarea id="formulaContent1" name="formulaContent1">${formula.formulaContent1}</textarea>--%>
                                    <div class="fomula_collection" id="formulaContent1">${formula.formulaHtmlContent1}</div>
                                    <input type="hidden" class="htmlInpt" name="formulaHtmlContent1">
                                    <input type="hidden" class="textIpt" name="formulaContent1">
                                </td>
                            </tr>
                            <tr class="formulaTr">
                                <td class="addData_td1"></td>
                                <td>
                                    <div class="fl function_btn">
                                        <a href="javaScript:;" class="fl" onclick="showFormula(2);">选择已存公式</a>
                                        <a href="javaScript:;" class="fl" onClick="showReport(2);">选择财报数据</a>
                                        <a href="javaScript:;" class="fl" onClick="showOpt(2);">选择运算符号</a>
                                    </div>
                                </td>
                            </tr>
                            <tr class="formulaTr">
                                <td class="addData_td1"><strong>双表公式</strong></td>
                                <td>
                                    <%--<textarea id="formulaContent2" name="formulaContent2">${formula.formulaContent2}</textarea>--%>
                                    <div class="fomula_collection" id="formulaContent2">${formula.formulaHtmlContent2}</div>
                                    <input type="hidden" class="htmlInpt" name="formulaHtmlContent2">
                                    <input type="hidden"  class="textIpt" name="formulaContent2">
                                </td>
                            </tr>
                            <tr class="formulaTr">
                                <td class="addData_td1"></td>
                                <td>
                                    <div class="fl function_btn">
                                        <a href="javaScript:;" class="fl" onclick="showFormula(3);">选择已存公式</a>
                                        <a href="javaScript:;" class="fl" onClick="showReport(3);">选择财报数据</a>
                                        <a href="javaScript:;" class="fl" onClick="showOpt(3);">选择运算符号</a>
                                    </div>
                                </td>
                            </tr>
                            <tr class="formulaTr">
                                <td class="addData_td1"><strong>三表公式</strong></td>
                                <td>
                                   <%-- <textarea id="formulaContent3" name="formulaContent3">${formula.formulaContent3}</textarea>--%>
                                   <div class="fomula_collection" id="formulaContent3">${formula.formulaHtmlContent3}</div>
                                   <input type="hidden" class="htmlInpt" name="formulaHtmlContent3">
                                   <input type="hidden" class="textIpt" name="formulaContent3">
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </form>
            <div class="main_btn main_btn1">
                <a href="javaScript:;" class="fl" onclick="saveFormula('${ctx}/businessMng/formulaSaveOrUpdate', '${ctx}/businessMng/formulaIndex');">保存</a>
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
<!-- 遮罩层 start -->
<div class="layer" id="layer"></div>
<!-- 遮罩层 end -->
<!-- 选择已存公式 start -->
<div class="popup popup3" id="forDiv">
    <a href="javaScript:;" class="close"></a>
    <div class="popup_ip popup_ip1">
        <span>选择公式</span>
        <div class="down_menu">
            <div class="select_btn">
                <input id="formulaName"  type="text" onclick="searchFormula(this, '${ctx}/businessMng/indexSearchFormula', 1, $('#reportTypeId').val())" onkeyup="searchFormula(this, '${ctx}/businessMng/indexSearchFormula', 0, $('#reportTypeId').val())" placeholder="请输入公式名搜索" autocomplete="off" />
            </div>
            <div class="select_list" id="formulaDiv"></div>
        </div>
    </div>
    <div class="popup_btn">
        <a href="javaScript:;" class="a1 fl" onclick="addSelectFormula();">确定</a>
        <a href="javaScript:;" class="a2 fr">取消</a>
    </div>
</div>
<!-- 选择已存公式 end -->
<!-- 选择运算符号 start -->
<div class="popup popup3" id="optDiv">
    <a href="javaScript:;" class="close"></a>
    <div class="popup_symbol">
        <ul class="popup_symbol_list clear">
            <li data-att="+" data-color="red" class="active">+</li>
            <li data-att="-" data-color="red">-</li>
            <li data-att="*" data-color="red">*</li>
            <li data-att="/" data-color="red">/</li>
            <li data-att="(" data-color="green">(</li>
            <li data-att=")" data-color="green">)</li>
            <li data-att="," data-color="black">,</li>
            <li data-att="方差(" data-color="green">方差</li>
            <li data-att="根号(" data-color="green">根号</li>
            <li data-att="1" data-color="deepPink">1</li>
            <li data-att="2" data-color="deepPink">2</li>
            <li data-att="3" data-color="deepPink">3</li>
        </ul>
    </div>
    <div class="popup_btn">
        <a href="javaScript:;" class="a1 fl" onclick="addSelectOpt();">确定</a>
        <a href="javaScript:;" class="a2 fr">取消</a>
    </div>
</div>
<!-- 选择运算符号 end -->
<!-- 选择财报数据 start -->
<div class="popup popup5" id="reportDiv">
    <a href="javaScript:;" class="close"></a>
    <div class="popup_symbol_box">
        <dl class="popup_symbol_dl popup_symbol_d2 clear" id="year">
            <dt>选择报表年份</dt>
            <dd data-att="最新_"class="active">最新年份报表</dd>
            <dd data-att="次最新_">次最新年份报表</dd>
            <dd data-att="次新_">次新年份报表</dd>
        </dl>
        <%--sheet--%>
        <dl class="popup_symbol_dl clear" id="popup_symbol_dl">
            <%--<dt>选择子表</dt>
            <dd class="active">资产类数据</dd>
            <dd>负债及所有者权益类数据</dd>
            <dd>损益类数据</dd>
            <dd>现金流类数据</dd>
            <dd>现金流补充信息</dd>
            <dd>财务报表附注补充信息</dd>--%>
        </dl>
    </div>
    <div class="popup_statement" id="col">
        <!--资产类数据-->
        <%--<div class="popup_statement_son" style="display:block;">
            <div class="statement_hegiht">
                <div class="popup_statement_tab">
                    <form>
                        <table class="statement_list">
                            <thead>
                            <tr>
                                <th class="statement_w1">财务科目（单位元）</th>
                                <th>选择上期数据</th>
                                <th>选择本期数据</th>
                            </tr>
                            </thead>
                        </table>
                    </form>
                </div>
                <div class="popup_statement_tab popup_statement_tab1" id="statement_par1">
                    <form>
                        <table class="statement_list statement_list1" id="statement_list1">
                            <tbody>
                            <c:forEach items="${assetList}" var="report">
                                <tr>
                                    <td>${report.name}</td>
                                    <td data-id="begin_" onclick="selectCol(this);">上期_${report.name}</td>
                                    <td data-id="end_" onclick="selectCol(this);">本期_${report.name}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </form>
                </div>
            </div>
            <div class="scrollPar" id="scrollPar1">
                <div class="scroll" id="scroll1"></div>
            </div>
        </div>
        <!--负债及所有者权益类数据-->
        <div class="popup_statement_son">
            <div class="statement_hegiht">
                <div class="popup_statement_tab">
                    <form>
                        <table class="statement_list">
                            <thead>
                            <tr>
                                <th class="statement_w1">财务科目（单位元）</th>
                                <th>选择上期数据</th>
                                <th>选择本期数据</th>
                            </tr>
                            </thead>
                        </table>
                    </form>
                </div>
                <div class="popup_statement_tab popup_statement_tab1" id="statement_par2">
                    <form>
                        <table class="statement_list statement_list1" id="statement_list2">
                            <tbody>
                            <c:forEach items="${LeList}" var="report">
                                <tr>
                                    <td>${report.name}</td>
                                    <td data-id="begin_" onclick="selectCol(this);">上期_${report.name}</td>
                                    <td data-id="end_" onclick="selectCol(this);">本期_${report.name}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </form>
                </div>
            </div>
            <div class="scrollPar" id="scrollPar2">
                <div class="scroll" id="scroll2"></div>
            </div>
        </div>
        <!--损益类数据-->
        <div class="popup_statement_son">
            <div class="statement_hegiht">
                <div class="popup_statement_tab">
                    <form>
                        <table class="statement_list">
                            <thead>
                            <tr>
                                <th class="statement_w1">财务科目（单位元）</th>
                                <th>上期数据</th>
                                <th>本期数据</th>
                            </tr>
                            </thead>
                        </table>
                    </form>
                </div>
                <div class="popup_statement_tab popup_statement_tab1" id="statement_par3">
                    <table class="statement_list statement_list1" id="statement_list3">
                        <tbody>
                        <c:forEach items="${profitLossList}" var="report">
                            <tr>
                                <td>${report.name}</td>
                                <td data-id="begin_" onclick="selectCol(this);">上期_${report.name}</td>
                                <td data-id="end_" onclick="selectCol(this);">本期_${report.name}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="scrollPar" id="scrollPar3">
                <div class="scroll" id="scroll3"></div>
            </div>
        </div>
        <!--现金流类数据-->
        <div class="popup_statement_son">
            <div class="statement_hegiht">
                <div class="popup_statement_tab">
                    <form>
                        <table class="statement_list">
                            <thead>
                            <tr>
                                <th class="statement_w1">财务科目（单位元）</th>
                                <th>上期数据</th>
                                <th>本期数据</th>
                            </tr>
                            </thead>
                        </table>
                    </form>
                </div>
                <div class="popup_statement_tab popup_statement_tab1" id="statement_par4">
                    <table class="statement_list statement_list1" id="statement_list4">
                        <tbody>
                        <c:forEach items="${cashFlowList}" var="report">
                            <tr>
                                <td>${report.name}</td>
                                <td data-id="begin_" onclick="selectCol(this);">上期_${report.name}</td>
                                <td data-id="end_" onclick="selectCol(this);">本期_${report.name}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="scrollPar" id="scrollPar4">
                <div class="scroll" id="scroll4"></div>
            </div>
        </div>
        <!--现金流类补充资料数据-->
        <div class="popup_statement_son">
            <div class="statement_hegiht">
                <div class="popup_statement_tab">
                    <form>
                        <table class="statement_list">
                            <thead>
                            <tr>
                                <th class="statement_w1">财务科目（单位元）</th>
                                <th>上期数据</th>
                                <th>本期数据</th>
                            </tr>
                            </thead>
                        </table>
                    </form>
                </div>
                <div class="popup_statement_tab popup_statement_tab1" id="statement_par5">
                    <table class="statement_list statement_list1" id="statement_list5">
                        <tbody>
                        <c:forEach items="${ACFList}" var="report">
                            <tr>
                                <td>${report.name}</td>
                                <td data-id="begin_" onclick="selectCol(this);">上期_${report.name}</td>
                                <td data-id="end_" onclick="selectCol(this);">本期_${report.name}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="scrollPar" id="scrollPar5">
                <div class="scroll" id="scroll5"></div>
            </div>
        </div>
        <!--财务报表类数据-->
        <div class="popup_statement_son">
            <div class="statement_hegiht">
                <div class="popup_statement_tab">
                    <form>
                        <table class="statement_list">
                            <thead>
                            <tr>
                                <th class="statement_w1">财务科目（单位元）</th>
                                <th>上期数据</th>
                                <th>本期数据</th>
                            </tr>
                            </thead>
                        </table>
                    </form>
                </div>
                <div class="popup_statement_tab popup_statement_tab1" id="statement_par6">
                    <table class="statement_list statement_list1" id="statement_list6">
                        <tbody>
                        <c:forEach items="${NFSList}" var="report">
                            <tr>
                                <td>${report.name}</td>
                                <td data-id="begin_" onclick="selectCol(this);">上期_${report.name}</td>
                                <td data-id="end_" onclick="selectCol(this);">本期_${report.name}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="scrollPar" id="scrollPar6">
                <div class="scroll" id="scroll6"></div>
            </div>
        </div>--%>
    </div>
    <div class="popup_btn">
        <a href="javaScript:;" class="a1 fl" onclick="addSelectColumn();">确定</a>
        <a href="javaScript:;" class="a2 fr">取消</a>
    </div>
</div>
<!-- 选择财报数据 end -->

</body>
</html>