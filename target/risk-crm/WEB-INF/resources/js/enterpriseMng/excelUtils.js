/**
 * Created by xzd on 2017/7/4.
 */
/************************ 三、 export start ******************************/

/**
 * 导出报表
 * @param reportSonType 报表子表类型
 * @param reportIndex 第几张子表
 */
function exportReportDataExcel(reportSonType, reportIndex){
    //报表id
    var reportId = $("#reportId").val();
    //报表类型
    var reportType = $("#reportType").val();

    //null和undifined校验
    var reportSonType = checkUndifined(reportSonType);
    var reportIndex = checkUndifined(reportIndex);

    window.location.href =
        exportReportDataExcelUrl + "?reportId="+reportId+"&reportType="+reportType+"&reportSonType="+reportSonType+"&reportIndex="+reportIndex;
}
/*校验null和undifined*/
function checkUndifined(obj){
    if (obj == null || obj == undefined) {
        obj = "";
    }
    return obj;
}

/************************ 三、 export end ******************************/