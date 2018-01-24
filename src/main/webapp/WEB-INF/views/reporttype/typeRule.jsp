<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>业务管理-报表类型管理-规则设置</title>
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
        height: 90px;
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
    .operating{
        display: inline-block;
        height: 14px;
        width: 14px;
        cursor: pointer;
    }
    .operating_minu{ background: url(${ctx}/resources/image/del.png) no-repeat; }
    .operating_minu:hover{ background: url(${ctx}/resources/image/del_hover.png) no-repeat; }
    .operating_plus{ background: url(${ctx}/resources/image/+.png) no-repeat; }
    .addData_table .tips{margin: 10px 0 5px;font-size: 12px;color: #7b8da0;}
</style>
    <script type="text/javascript" src="${ctx}/resources/js/bootstrap.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/move.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
    <script src="${ctx}/resources/js/businessMng/formula.js"></script>
    <script src="${ctx}/resources/js/businessMng/index.js"></script>
<script type="text/javascript">
    var Id = 1;

    $(function(){
        //切换运算符选择
        fnRadio(".popup_symbol_d2 dd");
        //显示规则
        showRule();
        //显示表
        showAllReport("${ctx}/businessMng/prepareReportAllDiv", '${typeId}', 1);
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
        //删除按键屏蔽
        stopE();
        //增加和删除
        $(document).on('click','.operating',function(e){
            var _this = $(this);
            var parent = _this.parents('tr');

            if(_this.hasClass('operating_minu')){
                parent.prev().remove();
                parent.remove();
            }else{
                $(".operating").each(function (i) {
                    if (this == e.target) {
                        Id = i;
                    }
                });
                parent.before(getDataTr());
                parent.before(getOptTr());
            }
        });
        //最后一个按钮变成加号
        $(".operating").last().attr("class", "operating operating_plus");
        //元素选择变成acttive
        bindSelectEle();
        //div点击处插入span(动态调用)
        $(document).on("click", ".fomula_collection", function (e) {
            posInsert();
        });

    });
    
    function showRule() {
        var ruleList = ${ruleList};
        var len = ruleList.length;

        if (len <= 3) {
            //不够3个也显示3个
            for (var i=0; i<3; i++) {
                if (len > i) {
                    //数据
                    $("#ruleBody").append(getOptTr());
                    $("#ruleBody").append(getDataTr(ruleList[i]));
                } else {
                    //空
                    $("#ruleBody").append(getOptTr());
                    $("#ruleBody").append(getDataTr());
                }
            }
        } else {
            //大于3个
            for (var i=0; i<len; i++) {
                //数据
                $("#ruleBody").append(getOptTr());
                $("#ruleBody").append(getDataTr(ruleList[i]));
            }
        }
    }

    function getOptTr() {
        var optTr = $("#optTr").clone();
        var html = "<tr>"+optTr.html()+"</tr>";
        return html;
    }

    function getDataTr(obj) {
        var dataTr = $("#dataTr").clone();
        var td = dataTr.find("td").eq(1);

        if (obj) {
            td.find("div").html(obj.formulaChinese);
            td.find("input").eq(0).val(obj.formulaChinese);
            td.find("input").eq(1).val(obj.formula);
        }
        var html = "<tr>"+dataTr.html()+"</tr>";
        return html;
    }

    var color;
    //显示报表字段
    function showReport(obj) {
        $(".reportBtn").each(function (i) {
            if (this == obj) {
                Id = i;
            }
            console.log(Id)
        });
        fnPopup("#reportDiv");
    }

    //显示运算符
    function showOpt(obj) {
        fnPopup("#optDiv");
        $(".optBtn").each(function (i) {
            if (this == obj) {
                Id = i;
            }
        });
    }

    function addSelectColumn() {
        var report;
        color = "black";

        $(".popup_statement_son").each(function () {
            if ($(this).css("display") == "block") {
                report = $(this);
                return false;
            }
        });

        var colName = report.find("td[class='active']").text();
        var formula = report.find("td[class='active']").attr("data-id");
        if (!formula) {alert("请选择数据字段");return}


        var textFinal = getReportName() + "_" + colName;
        //var textFinal =getYearVersion() + getReportName() + colRegion + colName;

        // mouseInsert($(selId).get(0), textFinal);
        insertSelect($(".fomula_collection").eq(Id).get(0), textFinal, formula);
        fnShutDown($("#reportDiv"));
    }

    //选中运算符号和函数
    function addSelectOpt() {
        var name = $("#optDiv li[class='active']").attr("data-att");
        color = $("#optDiv li[class='active']").attr("data-color");
        //mouseInsert($(selId).get(0), name);
        insertSelect($(".fomula_collection").eq(Id).get(0), name, name);
        fnShutDown($("#optDiv"));
    }

    // 添加column
    function insertSelect(dom, text, formula){
        var html = '<span data-id="'+formula+'" class="fomula_item">' +
            '<b style="color:'+color+'" class="fomula_text">'+ text+'</b>' +
            /*'<a contenteditable="false" class="fomula_mark" href="#none;"></a>' +*/
            '</span>';

        var eleArr = $(".fomula_item .edit");
        if(eleArr.length > 0){
            //插入运算符
            eleArr.text(text);
            eleArr.attr("style", "color:"+color);
            eleArr.parent().attr("data-id", formula);
            eleArr.removeClass("edit");
            eleArr.parent().removeClass("edit1");
        }else{
            //插入字段
            $(dom).append(html);
        }

        $(".fomula_item").off("click");
        bindSelectEle();
    }

    function saveFormula() {
        if ($(".fomula_item .edit").length > 0) {
            alert("请先完成输入");
            return;
        }
        var flag = false;

        var formulaArr = $(".fomula_collection");

        for (var i=1; i<formulaArr.length; i++) {
            var htmlArr = $(".htmlInpt");
            var textArr = $(".textIpt");
            var html = formulaArr.eq(i).html();
            var textSpanArr = formulaArr.eq(i).find("span");
            var textTmp = "";

            for (var j=0; j<textSpanArr.length; j++) {
                textTmp += textSpanArr.eq(j).attr("data-id");
            }

            htmlArr.eq(i).val(html);
            textArr.eq(i).val(textTmp);
            if(html != '' && html != undefined && html != null){
                flag = true;
            }
        }
        if(!flag){
            alert("规则设置不能为空，请设置。");
            return;
        }
        $.ajax({
            url:'${ctx}/rtype/setRuleSave',
            type:"POST",
            dataType:"json",
            data:$("#frm").serialize(),
            success:function (data) {
                if (200 == data.code) {
                    alert("保存成功", '${ctx}/rtype/index');
                } else if (400 == data.code) {
                    alert(data.msg)
                } else {
                    alert("保存失败！")
                }
            }
        });
    }

    //打开弹窗
    function fnPopup(obj,hit){
        $('#layer').show();
        $(obj).show();
        if(hit){
            $(obj).find('p').html(hit);
        }

        $(obj).find('.a2').click(function(){
            fnShutDown(obj);
        });
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
                <a href="${ctx}/rtype/index">报表类型管理</a>
                <span>></span>
                <a href="javascript:void(0);">设置规则</a>
            </h3>
            <%--<div class="backtrack">
                <a href="javascript:history.go(-1);">返回</a>
            </div>--%>
            <form id="frm">
                <input type="hidden" id="id" name="typeId" value="${typeId}" />
                <div class="shade">
                    <div class="formula">
                        <table class="input_table addData_table">
                            <tbody id="ruleBody">
                                <tr id="optTr" style="display:none;">
                                    <td class="addData_td1"></td>
                                    <td>
                                        <div class="fl function_btn">
                                            <a href="javaScript:;" class="fl reportBtn" onClick="showReport(this);">选择财报数据</a>
                                            <a href="javaScript:;" class="fl optBtn" onClick="showOpt(this);">选择运算符号</a>
                                        </div>
                                    </td>
                                </tr>
                                <tr id="dataTr" style="display:none;">
                                    <td class="addData_td1"><strong>校验规则</strong></td>
                                    <td>
                                        <div class="fomula_collection"></div>
                                        <input type="hidden" class="htmlInpt" name="formulaHtmlContent">
                                        <input type="hidden" class="textIpt" name="formulaContent">
                                    </td>
                                    <td class="addData_td1">
                                        <span class="operating operating_minu"></span>
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
<!-- 选择运算符号 start -->
<div class="popup popup3" id="optDiv">
    <a href="javaScript:fnShutDown($('#optDiv'));" class="close"></a>
    <div class="popup_symbol">
        <ul class="popup_symbol_list clear">
            <li data-att="+" data-color="red" class="active">+</li>
            <li data-att="-" data-color="red">-</li>
            <li data-att="*" data-color="red">*</li>
            <li data-att="/" data-color="red">/</li>
            <li data-att="(" data-color="green">(</li>
            <li data-att=")" data-color="green">)</li>
            <li data-att="=" data-color="deepPink">=</li>
           <%-- <li data-att="," data-color="black">,</li>
            <li data-att="方差(" data-color="green">方差</li>
            <li data-att="根号(" data-color="green">根号</li>
            <li data-att="1" data-color="deepPink">1</li>
            <li data-att="2" data-color="deepPink">2</li>
            <li data-att="3" data-color="deepPink">3</li>--%>
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
    <a href="javaScript:fnShutDown($('#reportDiv'));" class="close"></a>
    <div class="popup_symbol_box">
        <%--sheet--%>
        <dl class="popup_symbol_dl clear" id="popup_symbol_dl"></dl>
    </div>
    <div class="popup_statement" id="col"></div>
    <div class="popup_btn">
        <a href="javaScript:;" class="a1 fl" onclick="addSelectColumn('reportType');">确定</a>
        <a href="javaScript:;" class="a2 fr">取消</a>
    </div>
</div>
<!-- 选择财报数据 end -->

</body>
</html>