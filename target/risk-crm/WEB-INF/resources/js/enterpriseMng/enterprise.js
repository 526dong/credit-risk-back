/**
 * Created by xzd on 2017/7/19.
 */

/************************ 一、 enterprise start ******************************/
//所有select下拉框回显
function showSelect() {
    $('.select_parent .select_list').each(function (i, obj) {
        var objVal = $(obj).parents("main_table_td1").find("strong").html();
        if (objVal != "行业" && objVal != "企业区域") {
            //数据库取到的值
            var databaseVal = $(obj).parent().find("input").val();

            $(obj).find("li").each(function (i, liObj) {
                var liVal = $(liObj).attr('data-id');
                if (liVal == databaseVal) {
                    //将当前li选中
                    $(liObj).addClass('active');

                    $(obj).parent().find('span').html($(liObj).html());
                    $(obj).parent().find('span').attr('data-id', $(liObj).attr('data-id'));
                }
            });
        }
    });
}
//主体区域-省市县回显
function showRegion(){
    //初始化变量
    if (!allProvince) {
        getAllProvince();
    }
    if (!allCity) {
        getAllCity();
    }
    if (!allCounty) {
        getAllCounty();
    }

    var provinceId = $("#provinceId").val();
    var cityId = $("#cityId").val();
    var countyId = $("#countyId").val();
    var provinceName = "";
    var cityName = "";
    var countyName = "";
    //循环省
    for (var i = 0;i<allProvince.length;i++) {
        if (allProvince[i].id == provinceId) {
            provinceName = allProvince[i].name;
        }
    }
    //循环市
    for (var i = 0;i<allCity.length;i++) {
        if (allCity[i].id == cityId) {
            cityName = allCity[i].name;
        }
    }
    //循环县
    for (var i = 0;i<allCounty.length;i++) {
        if (allCounty[i].id == countyId) {
            countyName = allCounty[i].name;
        }
    }

    return provinceName + "-" + cityName + "-" + countyName;
}
/************************ 一、 enterprise end ******************************/
/************************ 三、 report start ******************************/
//报表概况列表
function reportList(enterpriseId){
    $.ajax({
        url:reportListUrl,
        type:'POST',
        data:{
            "enterpriseId":enterpriseId
        },
        success:function(data){
            if(data.code == 200){
                var report = data.data;
                var htmlStr = createTable(report);
                $("#report_info").html("");
                $("#report_info").html(htmlStr);
            } else {
                alert(data.msg);
                console.error(data.msg);
            }
        }
    });
}
//企业财务报表创建列表
function createTable(data){
    var htmlContent = "";
    for(var i=0;i<data.length;i++){
        htmlContent += "<tr>";
        htmlContent += "<td>"+(parseInt(i)+1)+"</td>";
        htmlContent += "<td style='display:none'>"+data[i].id+"</td>";
        htmlContent += "<td>"+(data[i].reportTime == null ? '' : data[i].reportTime)+"</td>";
        //口径
        if(data[i].cal == null){
            htmlContent += "<td></td>";
        }else{
            htmlContent += "<td>"+(data[i].cal == 0 ? '母公司' : '合并')+"</td>";
        }
        //类型
        if(data[i].type == null){
            htmlContent += "<td></td>";
        }else{
            htmlContent += "<td data-id='"+data[i].type+"'>"+data[i].reportName+"</td>";
        }
        //周期
        if(data[i].cycle == null){
            htmlContent += "<td></td>";
        }else{
            htmlContent += "<td>"+(data[i].cycle == 1 ? '年报' : '')+"</td>";
        }
        //币种
        if(data[i].currency == null){
            htmlContent += "<td></td>";
        }else{
            htmlContent += "<td>"+(data[i].currency == 1 ? '人民币' : '')+"</td>";
        }
        //是否审计
        if(data[i].audit == null){
            htmlContent += "<td></td>";
        }else{
            htmlContent += "<td>"+(data[i].audit == 0 ? '否' : '是')+"</td>";
        }
        htmlContent += "<td>"+(data[i].createDate == null ? '' : data[i].createDate.substring(0, 10))+"</td>";
        /*//录入状态
        if (data[i].state == null) {
            htmlContent += "<td></td>";
        }else{
            htmlContent += "<td>"+(data[i].state == 0 ? '未完成' : '已完成')+"</td>";
        }*/

        /*//评级状态
        if (0 == data[i].approvalStatus) {
            htmlContent += "<td>未提交</td>";
        } else if (1 == data[i].approvalStatus) {
            htmlContent += "<td>评级中</td>";
        } else if (2 == data[i].approvalStatus) {
            htmlContent += "<td>已评级</td>";
        } else if (3 == data[i].approvalStatus) {
            htmlContent += "<td>被退回</td>";
        } else {
            htmlContent += "<td></td>";
        }*/

        htmlContent = reportHtml(data[i].id, data[i].type, htmlContent, data[i].approvalStatus);

        htmlContent += "</tr>";
    }
    if ("" == htmlContent) {
        htmlContent = "<div style='height:30px;'><p style='margin-left:50px; margin-top:10px; color:#999;'>暂无数据</p></div>";
    }

    return htmlContent;
}
//查看
function checkDetails(url, id, reportType){
    //初始化-加载财务报表模板
    getReportData(reportType);

    //给当前的财务报表input赋值
    $("#reportType").val(reportType+"");

    //按钮 div控制
    $('#btn3').show();
    $('#table_info_box').show();
    $('.table_hide').hide();

    //初始化报表概况
    $('#card_btn').find("li").eq(0).attr("class", "active");
    //报表概况div
    $('#report').show();

    //报表数据
    findReport(url, reportType);

    $("#reportId").val(id);
}
//报表数据加载
function findReport(url, reportType){
    $.ajax({
        url:url,
        type:'POST',
        async:false,
        success:function(data){
            if (data.code == 200) {
                allReportVal(data.data, reportType, true);
            } else {
                alert("查询失败");
            }
        }
    });
}
/************************ 三、 report end ******************************/