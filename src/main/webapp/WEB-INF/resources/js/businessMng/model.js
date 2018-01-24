/**
 * Created by zhaotm on 2017/7/13.
 */
var delIds = [];
var tr =  "<tr>"+
    "<td>" +
        "<input type='text' name='codes'  maxlength='10' />" +
        "<input type='hidden' name='ruleIds' value='-1' />" +
    "</td>"+
    "<td><input type='text' name='valueMins'  maxlength='15' /></td>"+
    "<td><input type='text' name='valueMaxs'  maxlength='15' /></td>"+
    "<td style='display:none;'><input type='text' name='scores'  maxlength='10' value='0' /></td>"+
    "<td><input type='text' name='degrees'  maxlength='10' /></td>"+
    "<td class='delBor'><a href='javaScript:;' class='credit_a' onclick='delTr(this);'></a></td>";
"</tr>";

function addTr(obj) {
    var end = $("input[name='valueMaxs']:last").val();
    $(obj).parent().parent().parent().append(tr);
    $("input[name='valueMins']:last").val(end);
    $("input[name='degrees']:last").attr("list","itemlist");
}

function delTr(obj, id) {
    $(obj).parent().parent().remove();
    if (id) {
        delIds.push(id);
    }
}

//对table做规则校验
function checkRule(id) {
    var trList = $("#"+id).find("tr")
    var tdNameArr = trList.eq(0).find("th");

    for (var i=1;i<trList.length;i++) {
        var tdArr = trList.eq(i).find("td");

        for (var j=0; j<tdArr.length; j++) {
            var td = tdArr.eq(j);
            var ipt = td.find("input[type='text']");

            if ("" == ipt.eq(0).val()) {
                alertMsg("第"+i+"行"+tdNameArr.eq(j).text()+"不能空" ,td);
                return false;
            }
        }
    }

    if ($("input[name='valueMins']:first").val() != 0) {
        alert("最小值必须是0");
        return false;
    }
    if ($("input[name='valueMaxs']:last").val() != 10) {
        alert("最大值必须是10");
        return false;
    }

    if (!validateCodes()) {
        return false;
    }

    return true;
}

//
function alertMsg(msg, obj) {
    flag = true;
    alert(msg);
    obj.focus();
}

//新建模型
function saveModel(saveUrl, indexUrl) {
    if ("" == $("#modelName").val()) {
        alertMsg("评分卡名称不能空！", $("#modelName"));
        return;
    }
    if (!checkRule("model"))return;


    $("#delIds").val(delIds.join(","));
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