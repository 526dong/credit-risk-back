/**
 * Created by zhaotm on 2017/7/13.
 */
var delIds = [];
var tr =  "<tr>"+
    "<td>" +
        "<input type='text' name='codes' maxlength='10' />" +
        "<input type='hidden' name='ruleIds' value='-1' />" +
    "</td>"+
    "<td class='ration'><input type='text' name='valueMins' maxlength='15' placeholder='不填写表示负无穷' /></td>"+
    "<td class='ration'><input type='text' name='valueMaxs' maxlength='15' placeholder='不填写表示正无穷' /></td>"+
    "<td class='nature' colspan='2'><input type='text' name='values'  maxlength='200' /></td>"+
    "<td><input type='text' name='scores'  maxlength='10' /></td>"+
    "<td><input type='text' name='degrees'  maxlength='10' /></td>"+
    "<td class='delBor'><a href='javaScript:;' class='credit_a' onclick='delTr(this);'></a></td>";
"</tr>";

function addTr(obj) {
    var end = $("input[name='valueMaxs']:last").val();
    $(obj).parent().parent().parent().append(tr);
    $("input[name='valueMins']:last").val(end);

    var type = $("#regularIndexFlag").val();
    switchIndexType(type);
}

function switchIndexType(type) {
    if ("0" == type) {
        showRation();
    } else {
        showNature();
    }
}

function delTr(obj, id) {
    $(obj).parent().parent().remove();
    if (id) {
        delIds.push(id);
    }
}
//显示定性
function showNature(){
    $(".nature").css("display", "table-cell");
    $(".ration").css("display", "none");

    $("#avgYears").css("display", "none");
    $("#formule").css("display", "none");

}

//显示定量
function showRation() {
    $(".nature").css("display", "none");
    $(".ration").css("display", "table-cell");

    $("#avgYears").css("display", "block");
    $("#formule").css("display", "block");
}

function addSelect(id, yearLen, obj) {
    var indexLen = $("#aveYears").val();

    if (yearLen < indexLen) {
        alert("公式库定义的年份:"+yearLen+" 小于指标指定的年份:"+indexLen+"！");
        return;
    }

    $("#formulaName").val($(obj).text());
    $("#formulaId").val(id);
    $("#formulaDiv").css("display", "none");
}

//搜索公式
function searchFormula(obj, url, clickFlag, reportTypeId) {
    var div = $("#formulaDiv");
    div.show();

    $.ajax({
        url:url,
        type:"POST",
        dataType:"json",
        async:false,
        data:{"formulaName":1==clickFlag?'':obj.value, "reportTypeId":reportTypeId},
        success:function (data) {
            var divHtml = "";
            if (200 == data.code) {
                $.each(data.list, function (i, formula) {
                    if (formula.formulaName == obj.value) {
                        divHtml += "<p class='active' onclick='addSelect("+formula.id+", "+formula.yearLen+", this)'>"+formula.formulaName+"</p>";
                    } else {
                        divHtml += "<p onclick='addSelect("+formula.id+", "+formula.yearLen+", this)'>"+formula.formulaName+"</p>";
                    }
                });
                if ("" == divHtml) {
                    divHtml += "<p class='active' onclick='addSelect(-1, 3, this)'>请选择</p>";
                }
                div.html(divHtml);
            } else {

            }
        }
    });
}

//对table做规则校验
function checkRule(id) {
    var type = $("#regularIndexFlag").val();
    var trList = $("#"+id).find("tr")
    var tdNameArr = trList.eq(0).find("th");

    for (var i=1;i<trList.length;i++) {
        var tdArr = trList.eq(i).find("td");

        for (var j=0; j<tdArr.length; j++) {
            var td = tdArr.eq(j);
            var ipt = td.find("input[type='text']");

            if ("0" == type) {
                //定量
                if (3==j) continue;
            } else {
                //定性
                if (1==j || 2==j) continue;
            }

            // if ("0" == type //定量
            //     && 1 == i   //仅验证第一行
            //     && 1 == j   //验证最小值开始
            //     && "" == tdArr.eq(1).find("input[type='text']").eq(0).val() //最小值
            //     && "" == tdArr.eq(2).find("input[type='text']").eq(0).val()) {//最大值
            //     alertMsg("第"+i+"行最大值和最小值不能空", tdArr.eq(1).find("input[type='text']").eq(0));
            //     return false;
            // }
            //第一行最小值
            if (1 == i && 1 == j) {
                continue;
            }
            //最后一行最大值
            if (trList.length-1 == i && 2 == j) {
                continue;
            }
            if ("" == ipt.eq(0).val()) {
                alertMsg("第"+i+"行"+tdNameArr.eq(j).text()+"不能空" ,ipt);
                return false;
            }
        }
    }

    if (!validateCodes()) {
        return false;
    }

    return true;
}

function alertMsg(msg, obj) {
    alert(msg);
    obj.focus();
}

//新建指标
function saveIndex(indexSaveUrl, indexIndexUrl) {
    var flag = false;

    if ("" == $("#regularIndexFlag").val()) {$("#regularIndexFlag").val(1);}

    myValidate(
        {
            formId: 'frm',
            items: ["indexCode", "indexName", "indexWeight"],
            rules: ["require", "require",["require","weight"]],
            errorClass: "error_info",
            errorTag: "div",
            success: function () {
                if (!checkRule("index"))return;

                if (0 == $("#regularIndexFlag").val()) {
                    //公式校验
                    if ("" == $("#formulaId").val() || "-1" == $("#formulaId").val()) {
                        alertMsg("财务指标公式必须选择", $("#formulaName"));
                        return;
                    }

                    //同区间开闭判断
                    var min = $("input[name='valueMins']");
                    $("input[name='valueMaxs']").each(function (i, obj) {
                        if (i > 0) {
                            var x1 = parseInt(obj.value);
                            var x2 = parseInt(min.eq(i-1).val());
                            if (x1 < x2) {
                                alertMsg("区间最小不能大于区间的最大值！", $(obj));
                                flag = true;
                                return false;
                            }
                        }
                    });
                    if(flag)return;

                    //上下区间判读
                    var max = $("input[name='valueMaxs']");
                    $("input[name='valueMins']").each(function (i, obj) {
                        if (i > 0) {
                            var x1 = parseInt(obj.value);
                            var x2 = parseInt(max.eq(i-1).val());
                            if (x1 < x2) {
                                alertMsg("下个区间最小不能小于上个区间的最大值！", $(obj));
                                flag = true
                                return false;
                            }
                        }
                    });
                    if(flag)return;
                }

                $.ajax({
                    url:indexSaveUrl,
                    type:"POST",
                    dataType:"json",
                    data:$("#frm").serialize(),
                    success:function (data) {
                        if (200 == data.code) {
                            alert("保存成功", indexIndexUrl)
                        } else if (400 == data.code) {
                            alert(data.msg)
                        } else {
                            alert("保存失败！")
                        }
                    }
                });

            },
            errorPlacement: function (error, element) {
                var msg = element.prev().text().replace(/[\s\t,*:：]/g,"")+""+error.text();
                error.text(msg);
                element.after(error);
            }
        }
    );

}