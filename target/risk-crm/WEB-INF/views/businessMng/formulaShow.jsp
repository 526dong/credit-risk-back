<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>业务管理-指标公式库-查看指标公式</title>
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
    <script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
<script type="text/javascript">
    var typeMap = new Array();

    $(function () {
        showFormulaTr("${formula.yearLen}");
        initType();
    });

    function showFormulaTr(len) {
        $(".formulaTr").each(function (i, ele) {
            if (i< len) {
                $(ele).css("display", "table-row");
            } else {
                $(ele).css("display", "none");
            }
        });
    }

    function initType() {
        //报表类型map
        //var typeList = [];
        var typeList = ${typeList};
        for (var i=0; i<typeList.length; i++) {
            var type = typeList[i];
            typeMap[type.id] = type.name;
        }

        if (!${formula.reportTypeId} || !typeMap[${formula.reportTypeId}]) {
            $("#type").text("未匹配");
        } else {
            $("#type").text(typeMap[${formula.reportTypeId}]);
        }
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
                <a href="javascript:void(0);">查看指标公式</a>
            </h3>
            <div class="backtrack">
                <a href="javascript:history.go(-1);">返回</a>
            </div>
            <div class="shade">
                <div class="container-fluid">
                    <div class="row addData">
                        <div class="col-lg-4 col-sm-4 col-md-4">
                            <div class="info_box info_box1 info_box3 clear">
                                <strong>公式名称</strong>
                                <p>${formula.formulaName}</p>
                            </div>
                        </div>
                        <div class="col-lg-4 col-sm-4 col-md-4">
                            <div class="info_box info_box1 info_box3 clear">
                                <strong>适用报表类型</strong>
                                <p id="type"></p>
                            </div>
                        </div>
                        <div class="col-lg-4 col-sm-4 col-md-4">
                            <div class="info_box info_box1 info_box3 clear">
                                <strong>平均年数</strong>
                                <p>${formula.yearLen}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <h2 class="credit_title">计算逻辑</h2>
            <div class="shade">
                <div class="formula">
                    <form>
                        <table class="input_table addData_table">
                            <tbody>
                            <tr class="formulaTr">
                                <td class="addData_td1"><strong>单表公式</strong></td>
                                <td>
                                    <%--<textarea readonly="readonly">${formula.formulaContent1}</textarea>--%>
                                    <div class="fomula_collection" id="formulaContent1">${formula.formulaHtmlContent1}</div>
                                </td>
                            </tr>
                            <tr class="formulaTr">
                                <td class="addData_td1"><strong>双表公式</strong></td>
                                <td>
                                    <%--<textarea readonly="readonly">${formula.formulaContent2}</textarea>--%>
                                    <div class="fomula_collection" id="formulaContent1">${formula.formulaHtmlContent2}</div>
                                </td>
                            </tr>
                            <tr class="formulaTr">
                                <td class="addData_td1"><strong>三表公式</strong></td>
                                <td>
                                    <%--<textarea readonly="readonly">${formula.formulaContent3}</textarea>--%>
                                    <div class="fomula_collection" id="formulaContent1">${formula.formulaHtmlContent3}</div>
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
</body>
</html>
