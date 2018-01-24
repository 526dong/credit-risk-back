<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<title>业务管理-指标管理 </title>
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
<script type="text/javascript">
	var pageSize = 10;
    var typeMap = new Array();

	$(function () {
        findModel(1);
        initType();
    });

    //
    function jumpToPage(pageNo) {
        findModel(pageNo);
    }

    function initType() {
        //报表类型map
        //var typeList = [];
        var typeList = ${typeList};
        for (var i=0; i<typeList.length; i++) {
            var type = typeList[i];
            typeMap[type.id] = type.name;
        }
    }

	//查行业ajax
	function findModel(pageNo) {
        $.ajax({
            url:"${ctx}/businessMng/indexMngModelList",
            type:"POST",
            dataType:"json",
            data:{"pageNo":pageNo, "pageSize":pageSize, "name":$("#name").val()},
            success:function (data) {
				if (200 == data.code) {
					var page = data.page;
					var rows = page.rows;
                    var pageNo = page.pageNo;
                    var pageSize = page.pageSize;
                    var totalPage = page.totalPage;

                    $("#indexConent").html("");
					for (var i=0; i<rows.length; i++) {
						var row = rows[i];

                        var tr = "<tr>"+
                            "<td>"+(i+1+(pageNo-1)*pageSize)+"</td>"+
                            "<td>"+row.idCode+"</td>"+
                            "<td>"+row.name+"</td>";
							if (!typeMap[row.reportTypeId]) {
								tr += "<td>未匹配</td>";
							} else {
								tr += "<td>"+typeMap[row.reportTypeId]+"</td>";
							}
                            tr += "<td>"+row.creator+"</td>"+
                            "<td>"+row.createTimeStr+"</td>";
							tr +="<td class='operate operate1'>"+
								"<shiro:hasPermission name='/businessMng/elementIndex'><a href='${ctx}/businessMng/elementIndex?modelId="+row.id+"'>因素定义</a></shiro:hasPermission>"+
							"</td>"+
                       		"</tr>";
                        $("#indexConent").append(tr);
                    }
                    var pageStr = creatPage(page.total, pageNo, totalPage);
                    $("#pageDiv").html(pageStr);
                    if (0 ==rows.length) {
                        $("#indexConent").append('<tr><td colspan="7">暂无数据</td></tr>');
                    }
				} else {
				    alert("加载失败！")
				}
            }
        });
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
				<a href="javascript:void(0);">业务管理</a>
				<span>></span>
				<a href="javascript:void(0);">指标管理</a>
			</h3>
			<div class="shade main_minHeight">

				<div class="search_box clear">
					<shiro:hasPermission name='/businessMng/indexMngIndex'>
					<div class="search_btn fl">
						<a href="javaScript:;"onclick="findModel(1);" class="fr">查询</a>
						<input id="name" type="text" class="fl" placeholder="输入评分卡名称搜索" />
					</div>
					</shiro:hasPermission>
				</div>
				<div class="module_height">
					<form>
						<input type="hidden" id="currentPage" name="currentPage" value="1"/>
						<table class="module_table">
							<thead>
							<table class="module_table">
								<tr>
									<th class="module_width1">序号</th>
									<th>评分卡编号</th>
									<th>评分卡名称</th>
									<th>适用报表类型</th>
									<th>创建人</th>
									<th>创建时间</th>
									<th class="module_width1">操作</th>
								</tr>
								<tbody id="indexConent"></tbody>
							</table>
						</table>
					</form>
				</div>
				<!-- 分页.html start -->
				<%@include file="../commons/tabPage.jsp" %>
				<!-- 分页.html end -->
			</div>
			<!-- footer.html start -->
			<%@ include file="../commons/foot.jsp"%>
			<!-- footer.html end -->
		</div>
		<!-- 右侧内容.html end -->
	</div>
	<!-- center.html end -->
</div>
</body>
</html>
