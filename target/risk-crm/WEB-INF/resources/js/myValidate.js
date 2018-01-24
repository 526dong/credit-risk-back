/*create by zhaotm*/
/*通用验证函数
* 验证通过true，失败返回false
* idArr:要验证id数组如["name", "mobile"]
* ruleArry对应验证的规则二位数组如["require",["require","mobile"]]表示对name做空校验，对mobile做空校核和手机号校验
* 对应提示信息：   require：不能空，
*               mobile：手机号码格式不正确
*               email：邮箱格式不正确
*               weight:只能为小数
*/
var msgMap = new Array();

msgMap["require"] = "不能空";
msgMap["mobile"] = "格式不正确";
msgMap["email"] = "格式不正确";
msgMap["weight"] = "只能为大于0小于等于1的小数";

//方法区
var methodObj = {
    //空验证
    require:function (ele) {
        return !(""  == ele.val() || " " == ele.val())
    },
    //0.01 小于等于1的小数
    weight:function (ele) {
        return /^0\.\d+$|^[1]$|^1\.[0]+$/.test(ele.val());
    },
    //手机号
    mobile:function (ele) {
        return /^1[34578]\d{9}$/.test(ele.val());
    }
}

//(验证失败返回false)
function myValidate(obj) {
    var pass = true;
    var formId = obj.formId;
    var idArr = obj.items;
    var ruleArr = obj.rules;
    var errorClass = obj.errorClass;
    var errorTag = obj.errorTag;
    var passFun = obj.success;
    var errEleFun = obj.errorPlacement;

    if (!errorClass) {errorClass = "error"}
    if (!errorTag) {errorTag = "p"}

    //删除错误信息
    var sel = "#"+formId+" "+errorTag+"[class='"+errorClass+"']";
    $(sel).each(function () {
        $(this).remove();
    });

    //对每个元素校验
    for (var i = 0; i < idArr.length; i++) {
        var id = idArr[i];
        var ele = $("#" + formId+ " #"+id);
        var rule = ruleArr[i];

        //规则为数组
        if (rule instanceof Array) {
            for (var j = 0; j < rule.length; j++) {
                if (!valRule(rule[j], ele)) {
                    pass = false;
                    var errEle = "<"+errorTag+ " class='"+errorClass+"'>"+msgMap[rule[j]]+"</"+errorTag+">";
                    errEleFun($(errEle), ele);
                    break;
                }
            }
        } else {
            //单个规则
            if (!valRule(rule, ele)) {
                pass = false;
                var errEle = "<"+errorTag+ " class='"+errorClass+"'>"+msgMap[rule]+"</"+errorTag+">";
                errEleFun($(errEle), ele);
            }
        }
    }

    //通过回掉成功函数
    if (pass) {
        passFun();
    }
}

//全部规则验证(验证失败返回false)
function valRule(ruleName, ele) {
    for (var key in methodObj) {
        if (key == ruleName) {
            return methodObj[key](ele);
        }

    }
}