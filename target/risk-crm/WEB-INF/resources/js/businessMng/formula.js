/**
 * Created by zhaotm on 2017/7/14.
 */

//第几个文本框
id = 1;
//颜色
color = '#000';


//显示公式div
function showFormula(year) {
    id = year;
    fnPopup("#forDiv");
}

//增加公式选中
function addSelectFormula() {
    var selId = "#formulaContent"+id;
    color = "blue";

   //mouseInsert($(selId).get(0), $("#formulaName").val());
    insertSelect($(selId).get(0), $("#formulaName").val());

}

//显示报表字段
function showReport(year) {
    id = year;
    fnPopup("#reportDiv");
    $("#reportDiv .a1").off("click");
    //年份v字段
    var ddArr = $(".popup_symbol_d2 dd");

    $(".popup_symbol_d2 dd").hide();
    for (var i=0; i<year; i++) {
        ddArr.eq(i).show();
    }
}

//选中字段
function selectCol(obj) {
    $(obj).parent().parent().parent().find("td").each(function () {
        if (obj == this) {
            $(this).addClass("active");
        } else {
            $(this).removeClass("active");
        }
    });
}

//获取选中的年versioin
function getYearVersion() {
    var year = "最新_";

    year = $("#year dd[class='active']").attr("data-att");
    return year;
}

//获取报表名
function getReportName() {
    var name = "未知";
    $("#popup_symbol_dl dd").each(function (i) {
        if ($(this).attr("class") == "active") {
            name = $(this).text();
            return false;
        }
    });

    return name;
}

//
function addSelectColumn() {
    var report;
    color = "black";
    var selId = "#formulaContent"+id;

    $(".popup_statement_son").each(function () {
        if ($(this).css("display") == "block") {
            report = $(this);
            return false;
        }
    });

    var colName = report.find("td[class='active']").text();
    var colRegion = report.find("td[class='active']").attr("data-id");
    if (!colRegion) {alert("请选择数据字段");return}


    var textFinal =getYearVersion() + getReportName() + "_" + colName;
    //var textFinal =getYearVersion() + getReportName() + colRegion + colName;

    // mouseInsert($(selId).get(0), textFinal);
    insertSelect($(selId).get(0), textFinal);
    fnShutDown($("#reportDiv"));
}
/* < 孙建云 */

// 添加column
function insertSelect(dom,text){
    var html = '<span class="fomula_item">' +
                    '<b style="color:'+color+'" class="fomula_text">'+ text+'</b>' +
                    /*'<a contenteditable="false" class="fomula_mark" href="#none;"></a>' +*/
                '</span>';

    var eleArr = $(".fomula_item .edit");
    if(eleArr.length > 0){
        eleArr.text(text);
        eleArr.attr("style", "color:"+color);
        eleArr.removeClass("edit");
        eleArr.parent().removeClass("edit1");
    }else{
        $(dom).append(html);
    }

    $(".fomula_item").off("click");
    bindSelectEle();
}

//绑定添加的元素
function bindSelectEle() {
    $(".fomula_item").on('click',function(e){
        $(".fomula_item").each(function () {
            if (e.target.parentNode == this) {
                $(this).addClass("selected");
            } else {
                $(this).removeClass("selected");
            }
        });

        //$(this).toggleClass('selected');
        //$(this).find('.fomula_mark').toggleClass('fomula_del');
        e.stopPropagation();
    });

    /* $(document).on('click','.fomula_del',function(e){
         $(this).parents('.fomula_item').remove();
         e.stopPropagation()
     });*/
}

// 添加操作符
/*function insertSelect(dom,operator){
    var $dom = $(dom);
   /!* switch (operator){
        case '+': color = 'red'; break;
        case '-': color = 'orange'; break;
        case '*': color = 'yellow'; break;
        case '/': color = 'green'; break;
        case '(': color = 'blue'; break;
        case ')': color = 'indigo'; break;
        case ',': color = 'purple'; break;
        case 'MATH_RAN(': color = 'black'; break;
        case 'MATH_POW_2(': color = 'cyan'; break;
    }*!/
   /!* var operators = $dom.find('.fomula_operators');
    if(operators.length > 0){
        operators.find('br').remove();
        operators.append('<em class="fomula_operator" style="color: '+color+'" data-type="'+operator+'">'+operator+'</em>');
    }else{
        $dom.append('<span class="fomula_operators" contenteditable="true" spellcheck="false"><em class="fomula_operator" style="color: '+color+'" data-type="'+operator+'">'+operator+'</em></span>')
    }*!/
    var html = '<span contenteditable="false" class="fomula_operators" spellcheck="false">' +
                    '<em class="fomula_operator" style="color: '+color+'" data-type="'+operator+'">'+operator+'</em>' +
             '</span>';
    $dom.append(html)
}*/
/* 孙建云 > */


//在光标位置插入
/*function mouseInsert(obj, html) {
    if (obj.prev().hasClass(".fomula_item, .fomula_operators")) {

    }
}*/

//显示运算符
function showOpt(year) {
    id = year;
    fnPopup("#optDiv");
}

//选中运算符号和函数
function addSelectOpt() {
    var selId = "#formulaContent"+id;
    var name = $("#optDiv li[class='active']").attr("data-att");
    color = $("#optDiv li[class='active']").attr("data-color");
    //mouseInsert($(selId).get(0), name);
    insertSelect($(selId).get(0),name);
}

//光标处插入文字
function mouseInsert($t, myValue) {
    /*if (document.selection) {
        $t.focus();
        sel = document.selection.createRange();
        sel.text = myValue;
        $t.focus();
    }else if($t.selectionStart || $t.selectionStart == '0') {
        var startPos = $t.selectionStart;
        var endPos = $t.selectionEnd;
        var scrollTop = $t.scrollTop;
        $t.value = $t.value.substring(0, startPos) + myValue + $t.value.substring(endPos, $t.value.length);
        this.focus();
        $t.selectionStart = startPos + myValue.length;
        $t.selectionEnd = startPos + myValue.length;
        $t.scrollTop = scrollTop;
    }else{
        $t.value += myValue;
        $t.focus();
    }*/

}

//鼠标点击处注入元素
function posInsert() {
    var range = window.getSelection().getRangeAt(0);//找到焦点位置
    var span = document.createElement('span');
    span.className = "fomula_item";
    span.innerHTML = "b"
    span.id ="new";
    range.insertNode(span);//在焦点插入节点

    //调整节点
    var eleArr = $(".fomula_item");
    if(eleArr.length == 0){
        $("#new").remove();
        return;
    }
    eleArr = $(".fomula_item .edit");
    if(eleArr.length > 0){
        $("#new").remove();
        return;
    }

    //插入调整
    var parent = $(".fomula_item #new").parent().parent();
    var preArr = parent.prev();
    var nextArr = parent.prev();
//        if (preArr.length == 0 && preArr.length == 0) {
    parent.after("<span class='fomula_item'><b style='color:black;' class='fomula_text edit'>请选择</b></span>");
//        } else {
//            parent.before("<span class='fomula_item selected'><b style='color:black;' class='fomula_text edit'>请选择</b></span>");
//        }
    $("#new").remove();

}

//显示报表字段
function showAllReport(url, typeId, ruleOrFormula) {
    $.ajax({
        url:url,
        type:"POST",
        async:false,
        dataType:"json",
        data:{"reportTypeId":typeId},
        success:function (data) {

            if (200 == data.code) {
                var sheetArr = data.data;
                var sheetNameHtml = "";
                $("#col").html("");

                for (var i=0; i<sheetArr.length; i++) {
                    var sheet = sheetArr[i];
                    var sheetName = sheet.sheetName;
                    var title = sheet.fristClumn;
                    var columnArr = sheet.fields;

                    //填充sheet
                    if (0 == i) {
                        sheetNameHtml += "<dt>选择子表</dt><dd class='active'>"+sheetName+"</dd>";
                    } else {
                        sheetNameHtml += "<dd>"+sheetName+"</dd>";
                    }
                    //填充表格
                    fillReport(i, title, typeId, sheet.sheetId, columnArr, ruleOrFormula);
                }

                $("#popup_symbol_dl").html(sheetNameHtml);
                fnTabControl('#popup_symbol_dl dd','.popup_statement_son',false,true);
            } else {
                alert("报表模板加载失败");
            }
        }
    });
}

//填充字段
function fillReport(i, title, typeId, sheetId, columnArr, ruleOrFormula) {
    console.log(columnArr);
    var columnHtml = "";

    if (!columnArr) {

        return;
    }

    for (var j=0; j<columnArr.length; j++) {
        var column = columnArr[j];

        if (0 == j) {
            //表格头
            var titleArr = title;//[0].split(";;");

            columnHtml +=
                "<div class='popup_statement_son' style='display:"+(i==0?"block;":"none;")+"'>"+
                '<div class="statement_hegiht">'+
                /*表头*/
                '<div class="popup_statement_tab">'+
                '<table class="statement_list">'+
                '<thead>'+
                '<tr>'+
                '<th class="statement_w1">'+titleArr[0]+'</th>'+
                '<th>'+titleArr[1]+'</th>'+
                '<th>'+titleArr[2]+'</th>'+
                '</tr>'+
                '</thead>'+
                '</table>'+
                '</div>'+
                /*表头*/
                '<div class="popup_statement_tab popup_statement_tab1" id="statement_par'+(i+1)+'">'+
                '<table class="statement_list statement_list1" id="statement_list'+(i+1)+'">'+
                '<tbody>'+
                '<tr>'+
                '<td>'+column.name+'</td>';
                if (1 == ruleOrFormula) {
                    columnHtml +='<td data-id="report_'+typeId+'_'+sheetId+'_'+column.modleId+'_begin"'+'onclick="selectCol(this);">上期_'+column.name+'</td>';
                    columnHtml +='<td data-id="report_'+typeId+'_'+sheetId+'_'+column.modleId+'_end"'+'onclick="selectCol(this);">本期_'+column.name+'</td>';
                } else {
                    columnHtml +='<td data-id="'+sheetId+'_begin_'+column.modleId+'"'+'onclick="selectCol(this);">上期_'+column.name+'</td>';
                    columnHtml +='<td data-id="'+sheetId+'_end_'+column.modleId+'"'+'onclick="selectCol(this);">本期_'+column.name+'</td>';
                }

            columnHtml += '</tr>';
        } else {
            //表格内容
            columnHtml += '<tr>';
                if (1 == column.required) {
                    columnHtml += '<td><i class="i_active">*</i>'+column.name+'</td>';
                } else {
                    columnHtml += '<td><i>*</i>'+column.name+'</td>';
                }
                if (1 == ruleOrFormula) {
                    columnHtml +='<td data-id="report_'+typeId+'_'+sheetId+'_'+column.modleId+'_begin"'+'onclick="selectCol(this);">上期_'+column.name+'</td>';
                    columnHtml +='<td data-id="report_'+typeId+'_'+sheetId+'_'+column.modleId+'_end"'+'onclick="selectCol(this);">本期_'+column.name+'</td>';
                } else {
                    columnHtml +='<td data-id="'+sheetId+'_begin_'+column.modleId+'"'+'onclick="selectCol(this);">上期_'+column.name+'</td>';
                    columnHtml +='<td data-id="'+sheetId+'_end_'+column.modleId+'"'+'onclick="selectCol(this);">本期_'+column.name+'</td>';
                }
            columnHtml += '</tr>';

            if (columnArr.length-1 == j) {
                //表格尾
                columnHtml +=
                    '</tbody>'+
                    '</table>'+
                    '</div>'+
                    '</div>'+
                    '<div class="scrollPar" id="scrollPar'+(i+1)+'">'+
                    '<div class="scroll" id="scroll'+(i+1)+'"></div>'+
                    '</div>';
            }
        }
    }
    $("#col").append(columnHtml);
    fnPopupScroll('statement_par'+(i+1),'statement_list'+(i+1),'scrollPar'+(i+1),'scroll'+(i+1));
}

//保存公式
function saveFormula(saveUrl, indexUrl) {
    if ($("input[name='formulaName']").val() == "") {
        alert("公式名不能空！");
        return;
    }
    if ($(".fomula_item .edit").length > 0) {
        alert("请先完成输入");
        return;
    }

    var flag = false;
    var len = $("#yearLen").val();
    var formulaArr = $(".fomula_collection");

    for (var i=0; i<len; i++) {
        var html = formulaArr.eq(i).html();
        var text = formulaArr.eq(i).text();
        var htmlArr = $(".htmlInpt");
        var arr = $(".textIpt");

        htmlArr.eq(i).val(html);
        arr.eq(i).val(text);
        if ("" == html) {
            alert((i+1)+"年公式不能空！")
            flag = true;
            break;
        }
    }
    if (flag) {return;}


    $.ajax({
        url:saveUrl,
        type:"POST",
        dataType:"json",
        data:$("#frm").serialize(),
        success:function (data) {
            if (200 == data.code) {
                alert("保存成功", indexUrl);
            } else if (400 == data.code) {
                alert(data.msg)
            } else {
                alert("保存失败！")
            }
        }
    });
}

function _stopIt(e){
    if(e.returnValue){
        e.returnValue = false ;
    }
    if(e.preventDefault ){
        e.preventDefault();
    }

    deleteSelect();
    return false;
}
function stopE() {
    document.getElementsByTagName("body")[0].onkeydown =function(){

        //获取事件对象
        var elem = event.relatedTarget || event.srcElement || event.target ||event.currentTarget;

        if(event.keyCode==8){//判断按键为backSpace键

            //获取按键按下时光标做指向的element
            var elem = event.srcElement || event.currentTarget;

            //判断是否需要阻止按下键盘的事件默认传递
            var name = elem.nodeName;

            if(name!='INPUT' && name!='TEXTAREA'){
                return _stopIt(event);
            }
            var type_e = elem.type.toUpperCase();
            if(name=='INPUT' && (type_e!='TEXT' && type_e!='TEXTAREA' && type_e!='PASSWORD' && type_e!='FILE')){
                return _stopIt(event);
            }
            if(name=='INPUT' && (elem.readOnly==true || elem.disabled ==true)){
                return _stopIt(event);
            }
        } else if (event.keyCode == 46 ) {
            deleteSelect();
        }
    }
}

function deleteSelect() {
    $(".fomula_item.selected").remove();
    $(".fomula_item .edit").parent().remove();
}