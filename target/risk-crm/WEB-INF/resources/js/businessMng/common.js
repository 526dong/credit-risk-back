/* create by zhaotm */
//只能数字
var degreeArr = ["C","CC","CCC-","CCC","CCC+","B-","B","B+","BB-","BB","BB+","BBB-","BBB","BBB+","A-","A","A+","AA-","AA","AA+","AAA"];
function validateNum(obj) {
    obj.value=obj.value.replace(/\D/g,'');
    var len = obj.value.length;
    var value = obj.value;

    for (var i=0; i<3; i++) {
        if (i < 3-len) {
            value = "0" + value;
        }
    }
    obj.value = value;

    if (len > 3) {
        obj.value = obj.value.substring(obj.value.length-3, obj.value.length);
    }

    if (obj.value == "000") {
        obj.value = "";
    }
}

//只能评分等级
function validateDegree(obj)  {
    obj.value = obj.value.toUpperCase().replace(/[^ABC\-\+/]/g,'');
    // alert(obj.value)
    //校验等级：只能输入1-3个A\B\C、可带“+\-”的字串
    if(degreeArr.indexOf(obj.value)==-1){
        alert("输入的等级有误，请重新输入");
        obj.value="";
    }
}

//编号重复判断
function validateCodes() {
    var map = new Array();

    $("input[name='codes']").each(function () {
        var key = this.value;
        var value = map[key];

        if (!value) {
            map[key] = 1;
        } else {
            map[key] = ++value;
        }
    });

    for (var key in map) {
        if (map[key] > 1) {
            alert(key+"标号重复");
            return false;
        }
    }

    return true;
}

function validateScores(obj) {
    //将输入的非数字符号去除
    obj.value=obj.value.replace(/\D/g,'');

    var scores = obj.value*1;
    if(scores>10){
        alert("分值应该为0-10的整数值，请重新输入")
        obj.value = "";
    }
}