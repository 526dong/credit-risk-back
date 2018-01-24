<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>企业数据管理-企业评级管理-查看企业历史评级</title>
    <link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet" />
    <link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
    <link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
    <script type="text/javascript" src="${ctx}/resources/js/bootstrap.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/enterpriseMng/enterprise.js"></script>
</head>
<script type="text/javascript">
var pageSize = 10;
$(function(){
    //评级历史列表
    getData(1);
});
/*列表数据*/
function getData(pageNo){
    //企业id
    var enterpriseId = '${enterpriseId}';

    $.ajax({
        url:"${ctx}/enterprise/findHistoryRate?enterpriseId="+enterpriseId,
        type:'POST',
        async: false,
        data:{
            "pageNo":pageNo,
            "pageSize":pageSize
        },
        success:function(data){
            if (200 == data.code) {
                var page = data.data;
                var reData = page.rows;
                //clear
                $("#historyRateContent").html("");
                //tbody
                var htmlStr = createTable(reData);
                $("#historyRateContent").html(htmlStr);
                //page
                var pageStr = creatPage(page.total, page.pageNo, page.totalPage);
                $("#pageDiv").html(pageStr);
            } else {
                alert(data.msg);
                console.error(data.msg);
            }
        }
    });
}
//企业创建列表
function createTable(data){
    var htmlContent = "";
    for(var i=0;i<data.length;i++){
       htmlContent += "<tr>";
           htmlContent += "<td class='audit_td1'>"+(parseInt(i)+1)+"</td>";
           htmlContent += "<td style='display:none'>"+data[i].id+"</td>";
           htmlContent += "<td style='display:none'>"+data[i].entId+"</td>";
            if (data[i].ratingApplyNum != null) {
                htmlContent += "<td title='" + data[i].ratingApplyNum + "' style='overflow: hidden;text-overflow:ellipsis;white-space: nowrap'>" + data[i].ratingApplyNum + "</td>"
            } else {
                htmlContent += "<td></td>";
            }

            if (data[i].entName != null) {
                htmlContent += "<td title='" + data[i].entName + "' style='overflow: hidden;text-overflow:ellipsis;white-space: nowrap'>" + data[i].entName + "</td>"
            } else {
                htmlContent += "<td></td>";
            }

            if(data[i].entType == null){
                htmlContent += "<td></td>";
            }else{
                htmlContent += "<td>"+(data[i].entType == 0 ? '新评级' : '跟踪评级')+"</td>";
            }

            htmlContent += "<td>"+(data[i].initiateTime == null ? '' : data[i].initiateTime)+"</td>";
            htmlContent += "<td>"+(data[i].initiator == null ? '' : data[i].initiator)+"</td>";

            //评级报表
            htmlContent += "<td>"+(data[i].rateReport == null ? '' : data[i].rateReport)+"</td>";

            //审批进度
            if(data[i].approvalStatus == 2){
                htmlContent += "<td>已评级</td>";
            }else if (data[i].approvalStatus == 3) {
                htmlContent += "<td>被退回</td>";
            } else {
                htmlContent += "<td></td>";
            }

            htmlContent += "<td>" + (data[i].approvalTime == null ? '' : data[i].approvalTime) + "</td>";//审批时间
            htmlContent += "<td>"+(data[i].approver == null ? '' : data[i].approver)+"</td>";//审批人
            htmlContent += "<td>"+(data[i].ratingResult == null ? '' : data[i].ratingResult)+"</td>";//评级结果
            htmlContent += "<td>"+(data[i].shadowRatingResult == null ? '' : data[i].shadowRatingResult)+"</td>";//影子评级
            htmlContent += "<td class='audit_td3 audit_td4'>";
                htmlContent += "<shiro:hasPermission name='/enterprise/detail'>";
                htmlContent += "<a onclick='entDetail("+data[i].id+", "+data[i].entId+");' class='detail_btn' href='javascript:;'>查看</a>";
                htmlContent += "</shiro:hasPermission>";
            htmlContent += "</td>";
       htmlContent += "</tr>";
    }

    return htmlContent;
}
/**
 * 查看报表report_detail
 */
function entDetail(id, enterpriseId) {
    window.location.href = "${ctx}/enterprise/rateDetail?enterpriseId="+enterpriseId+"&approvalId="+id;
}
//分页跳转
function jumpToPage(pageNo){
    getData(pageNo);
}
//返回企业列表
function back(){
    window.location.href = "${ctx}/enterprise/list";
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
        <!--页面左侧导航栏 start -->
        <%@ include file="../commons/leftNavigation.jsp"%>
        <!-- 页面左侧导航栏 end -->
        <!-- 右侧内容.html start -->
        <div class="right_content">
            <h3 class="place_title">
                <span>当前位置：</span>
                <a href="${ctx}/enterprise/list">企业数据管理</a>
                <span>></span>
                <a href="${ctx}/enterprise/list">企业评级管理</a>
                <span>></span>
                <a href="javascript:void(0)">查看企业历史评级</a>
            </h3>
            <div class="backtrack">
                <a href="javascript:void(0)" onclick="back();">返回</a>
            </div>
            <!-- 企业主体信息 start -->
            <div class="shade">
                <h2 class="main_title">企业基本信息</h2>
                <div class="container-fluid main_padd">
                    <input id="enterpriseId" value="${enterprise.id}" type="hidden"/>
                    <div class="row">
                        <div class="col-lg-6 col-sm-6 col-md-6">
                            <div class="info_box info_box1 clear">
                                <strong>企业名称</strong>
                                <p>${enterprise.name}</p>
                            </div>
                        </div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                            <div class="info_box info_box1 clear">
                                <strong>代码标识</strong>
                                <p>${enterprise.creditCode}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 企业主体信息 end -->
            <!-- 历史评级列表 start -->
            <div class="shade main_minHeight main_mar">
                <div class="module_height">
                    <table class="module_table module_table1">
                        <thead>
                        <tr>
                          <th class="module_width1">序号</th>
                          <th>评级申请编号</th>
                          <th>企业名称</th>
                          <th>评级类型</th>
                          <th>创建时间</th>
                          <th>创建人</th>
                          <th>评级报表</th>
                          <th>审批进度</th>
                          <th>审批时间</th>
                          <th>审批人</th>
                          <th>评级结果</th>
                          <th>影子评级</th>
                          <th class="module_width1">操作</th>
                        </tr>
                        </thead>
                        <tbody id="historyRateContent"></tbody>
                    </table>
                </div>
            </div>
            <!-- 历史评级列表 end -->
            <!-- 分页.html start -->
            <%@include file="../commons/tabPage.jsp" %>
            <!-- 分页.html end -->
        </div>
        <!-- 右侧内容.html end -->
        <!-- footer.html start -->
        <%@ include file="../commons/foot.jsp"%>
        <!-- footer.html end -->
    </div>
</div>
<!-- center.html end -->
<!-- 遮罩层 start -->
<div class="layer" id="layer"></div>
<!-- 遮罩层 end -->
<!-- 启用停用 start -->
<div class="popup popup2" id="popup">
    <a href="javaScript:;" class="close"></a>
    <p>错误提示</p>
    <div class="popup_btn">
        <a href="javaScript:;" class="a1 fl">确定</a>
        <a href="javaScript:;" class="a2 fr">取消</a>
    </div>
</div>
<!-- 启用停用 end -->
</body>
</html>
