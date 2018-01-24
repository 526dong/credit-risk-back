/**
 * 更新状态公共方法
 * Created by zhaotm on 2017/7/16.
 */
//更新状态
function updateState(url, obj, id, state,type) {

    if(type == 0){
        $.ajax({
            url: url,
            type: "POST",
            dataType: "json",
            data: {"id": id, "state": state},
            success: function (data) {
                if (200 == data.code) {
                    //操作按钮切换
                    $(obj).parent().find(".update").each(function () {
                        if (this == obj) {
                            $(this).css("display", "none");
                            if (this.innerText == "禁用") {
                                //显示修改btn
                                $(this).parent().find(".edit").show();
                            } else {
                                $(this).parent().find(".edit").hide();
                            }
                        }
                        else {
                            $(this).css("display", "inline");
                        }
                    });

                    //文本提示切换
                    if (1 == state) {
                        $(obj).parent().prev().text("启用");
                        alert("启用成功！")
                    } else {
                        $(obj).parent().prev().text("禁用");
                        alert("禁用成功！")
                    }
                } else if (400 == data.code) {
                    alert(data.msg)
                } else {
                    if (1 == state) {
                        alert("启用失败！")
                    } else {
                        alert("禁用失败！")
                    }
                }
            }
        });
    }else {
        var total = 0.0;

        var flag = true;

        //该指标状态
        var thisState = $(obj).parent().prev().text();
        var thisDegree = $(obj).parent().prev().prev().text()*1;

        //计算已启用指标的权重和
        $("#indexConent").find("tr").each(function () {
            var tdsLabel = $(this).find("td");
            var state = tdsLabel.eq(6).text();
            var degree = tdsLabel.eq(5).text()*1;
            if ("启用" == state){
                total +=degree;
                if (total > 1) {
                    flag = false;
                    return;
                }
            }
        })
        //如果初始狀態是啟用或者是因素的話 ，可以直接修改狀態
        if("启用"==thisState ){
            $.ajax({
                url: url,
                type: "POST",
                dataType: "json",
                data: {"id": id, "state": state},
                success: function (data) {
                    if (200 == data.code) {
                        //操作按钮切换
                        $(obj).parent().find(".update").each(function () {
                            if (this == obj) {
                                $(this).css("display", "none");
                                if (this.innerText == "禁用") {
                                    //显示修改btn
                                    $(this).parent().find(".edit").show();
                                } else {
                                    $(this).parent().find(".edit").hide();
                                }
                            }
                            else {
                                $(this).css("display", "inline");
                            }
                        });

                        //文本提示切换
                        if (1 == state) {
                            $(obj).parent().prev().text("启用");
                            alert("启用成功！")
                        } else {
                            $(obj).parent().prev().text("禁用");
                            alert("禁用成功！")
                        }
                    } else if (400 == data.code) {
                        alert(data.msg)
                    } else {
                        if (1 == state) {
                            alert("启用失败！")
                        } else {
                            alert("禁用失败！")
                        }
                    }
                }
            });
        }
        //如果启用指标权重和大于1的话  不允许更改
        else if("禁用"==thisState&&(total+thisDegree>1)){
            alert("启用指标的权重和不应大于1")
        }
        //如果权重之和小于1，且状态为禁用，允许更改状态
        else if("禁用"==thisState&&(total+thisDegree<=1)){
            $.ajax({
                url: url,
                type: "POST",
                dataType: "json",
                data: {"id": id, "state": state},
                success: function (data) {
                    if (200 == data.code) {
                        //操作按钮切换
                        $(obj).parent().find(".update").each(function () {
                            if (this == obj) {
                                $(this).css("display", "none");
                                if (this.innerText == "禁用") {
                                    //显示修改btn
                                    $(this).parent().find(".edit").show();
                                } else {
                                    $(this).parent().find(".edit").hide();
                                }
                            }
                            else {
                                $(this).css("display", "inline");
                            }
                        });

                        //文本提示切换
                        if (1 == state) {
                            $(obj).parent().prev().text("启用");
                            alert("启用成功！")
                        } else {
                            $(obj).parent().prev().text("禁用");
                            alert("禁用成功！")
                        }
                    } else if (400 == data.code) {
                        alert(data.msg)
                    } else {
                        if (1 == state) {
                            alert("启用失败！")
                        } else {
                            alert("禁用失败！")
                        }
                    }
                }
            });
        }
    }
}