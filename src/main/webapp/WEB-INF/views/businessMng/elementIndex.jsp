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
        findElement(1);
    });

    //
    function jumpToPage(pageNo) {
        findElement(pageNo);
    }

	//查行业ajax
	function findElement(pageNo) {
        $.ajax({
            url:"${ctx}/businessMng/elementList",
            type:"POST",
            dataType:"json",
            data:{"pageNo":pageNo, "pageSize":pageSize, "modelId":${modelId}},
            success:function (data) {
				if (200 == data.code) {
                    var page = data.page;
                    var rows = page.rows;
                    var pageNo = page.pageNo;
                    var pageSize = page.pageSize;
                    var totalPage = page.totalPage;

                    $("#elementConent").html("");
					for (var i=0; i<rows.length; i++) {
						var row = rows[i];

						var tr = "<tr>"+
							"<td>"+(i+1+(pageNo-1)*pageSize)+"</td>"+
							"<td>"+row.code+"</td>"+
							"<td>"+row.name+"</td>";
							if (0 == row.state) {
								tr += "<td>禁用</td>";
							} else {
                                tr += "<td>启用</td>";
							}
                        	tr += "<td class='operate'>";

								//禁用状态下才能修改
								/*if (0 == row.state) {
									tr += "<a href='${ctx}/businessMng/elementAddOrEditOrShow?method=edit&id="+row.id+"&modelId=${modelId}'>修改</a>";
								}*/

								if (0 == row.state) {
                                    tr += "<a href='javascript:void(0)' style='display: inline;' class='update' onclick='updateState(\"${ctx}/businessMng/elementUpdateState\", this, " + row.id + ", 1, 0)'>启用</a>";
                                    tr += "<a href='javascript:void(0)' style='display: none;' class='update' onclick='updateState(\"${ctx}/businessMng/elementUpdateState\", this, "+row.id+", 0, 0)'>禁用</a>";
                                    tr += "<a href='${ctx}/businessMng/elementAddOrEditOrShow?method=edit&id="+row.id+"&modelId=${modelId}' style='display: inline;' class='edit'>修改</a>";
                                } else {
                                    tr += "<a href='javascript:void(0)' style='display: none;' class='update' onclick='updateState(\"${ctx}/businessMng/elementUpdateState\", this, " + row.id + ", 1, 0)'>启用</a>";
                                    tr += "<a href='javascript:void(0)' style='display: inline;' class='update' onclick='updateState(\"${ctx}/businessMng/elementUpdateState\", this, "+row.id+", 0, 0)'>禁用</a>";
                                    tr += "<a href='${ctx}/businessMng/elementAddOrEditOrShow?method=edit&id="+row.id+"&modelId=${modelId}' style='display: inline;' class='edit'>修改</a>";
								}
								<%--"<a href='${ctx}/businessMng/elementShow?id="+row.id+"&backId=${backId}'>查看</a>"+--%>
								<%--"<a href='${ctx}/businessMng/elementDel?id="+row.id+"&backId=${backId}'>删除</a>"+--%>
								tr += "<a href='${ctx}/businessMng/indexIndex?eleId="+row.id+"&modelId=${modelId}'>指标定义</a>"+
							"</td>"+
							"</tr>";
                        $("#elementConent").append(tr);
                    }
                    if (0 ==rows.length) {
                        $("#elementConent").append('<tr><td colspan="7">暂无数据</td></tr>');
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
				<a href="javascript:void(0);">因素定义</a>
			</h3>
			<div class="backtrack">
				<a href="javascript:void(0);" onclick="history.go(-1)">返回</a>
			</div>
			<div class="intoPackage_title shade">
				<h3 class="fl intoPackage_h3">
					<span>因素所属评分卡</span>
					<strong>${modelName}</strong>
				</h3>
				<a href="${ctx}/businessMng/elementAddOrEditOrShow?id=-1&method=add&modelId=${modelId}" class="grade_btn fr">新建</a>
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
									<th>因素编号</th>
									<th>因素名称（中文）</th>
									<th>状态</th>
									<th class="module_width5">操作</th>
								</tr>
								<tbody id="elementConent"></tbody>
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
