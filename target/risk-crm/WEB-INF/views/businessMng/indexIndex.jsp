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
	<script src="${ctx}/resources/js/businessMng/updateState.js"></script>
<script type="text/javascript">
	var pageSize = 10;

	$(function () {
        findIndex(1);
    });

    //
    function jumpToPage(pageNo) {
        findIndex(pageNo);
    }

	//查指标ajax
	function findIndex(pageNo) {
        $.ajax({
            url:"${ctx}/businessMng/indexList",
            type:"POST",
            dataType:"json",
            data:{"pageNo":pageNo, "pageSize":pageSize, "eleId":${eleId}},
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
							"<td>"+row.indexCode+"</td>"+
							"<td>"+row.indexName+"</td>"+
							/*"<td>"+row.equalPick+"</td>"+*/
							"<td>"+row.aveYears+"</td>";
							if (1 == row.regularIndexFlag) {
							    tr += "<td>定性</td>";
							} else {
                                tr += "<td>定量</td>";
							}
							tr += "<td>"+row.indexWeight+"</td>";
							if (0 == row.state) {
                                tr += "<td>禁用</td>";
							} else {
                                tr += "<td>启用</td>";
							}
                        	tr += "<td class='operate'>"+
								"<a href='${ctx}/businessMng/indexShow?method=show&id="+row.id+"&modelId=${model.id}&eleId=${eleId}'>查看</a>";
								<%--"<a href='${ctx}/businessMng/indexDel?id="+row.id+"&modelId=${modelId}'>删除</a>"+--%>
							if (0 == row.state) {
                                tr += "<a style='display: inline;' class='edit' href='${ctx}/businessMng/indexAddOrEdit?method=edit&id="+row.id+"&modelId=${model.id}&eleId=${eleId}'>修改</a>";
								tr += "<a href='javascript:void(0)' style='display: inline;' class='update' onclick='updateState(\"${ctx}/businessMng/indexUpdateState\", this, " + row.id + ", 1, 1)'>启用</a>";
								tr += "<a href='javascript:void(0)' style='display: none;' class='update' onclick='updateState(\"${ctx}/businessMng/indexUpdateState\", this, "+row.id+", 0, 1)'>禁用</a>";
							} else {
                                tr += "<a style='display: none;' class='edit' href='${ctx}/businessMng/indexAddOrEdit?method=edit&id="+row.id+"&modelId=${model.id}&eleId=${eleId}'>修改</a>";
								tr += "<a href='javascript:void(0)' style='display: none;' class='update' onclick='updateState(\"${ctx}/businessMng/indexUpdateState\", this, " + row.id + ", 1, 1)'>启用</a>";
								tr += "<a href='javascript:void(0)' style='display: inline;' class='update' onclick='updateState(\"${ctx}/businessMng/indexUpdateState\", this, "+row.id+", 0, 1)'>禁用</a>";
							}
							tr += "</td>"+
							"</tr>";
                        $("#indexConent").append(tr);
                    }
                    if (0 ==rows.length) {
                        $("#indexConent").append('<tr><td colspan="8">暂无数据</td></tr>');
                    }
                    var pageStr = creatPage(page.total, pageNo, totalPage);
                    $("#pageDiv").html(pageStr);

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
				<a href="${ctx}/businessMng/indexMngIndex">指标管理</a>
				<span>></span>
				<a href="elementIndex?modelId=${model.id}">因素定义</a>
				<span>></span>
				<a href="javascript:void(0);">指标定义</a>
			</h3>
			<div class="backtrack">
				<a href="javascript:void(0);" onclick="history.go(-1)">返回</a>
			</div>
			<div class="intoPackage_title shade">
				<h3 class="fl intoPackage_h3">
					<span>指标所属评分卡</span>
					<strong>${model.name}</strong>
				</h3>
				<h3 class="fl intoPackage_h3">
					<span>指标所属因素</span>
					<strong>${element.name}</strong>
				</h3>
				<a href="${ctx}/businessMng/indexAddOrEdit?method=add&id=-1&modelId=${model.id}&eleId=${eleId}" class="grade_btn fr">新建</a>
			</div>
			<div class="shade main_mar">
				<div class="module_height main_paddTop">
					<form>
						<input type="hidden" id="currentPage" name="currentPage" value="1"/>
						<table class="module_table">
							<thead>
							<table class="module_table">
								<tr>
									<th class="module_width1">序号</th>
									<th>指标编号</th>
									<th>指标名称（中文）</th>
									<th>平均年数</th>
									<th>是否定性指标</th>
									<th>权重</th>
									<th>状态</th>
									<th class="module_width9">操作</th>
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
