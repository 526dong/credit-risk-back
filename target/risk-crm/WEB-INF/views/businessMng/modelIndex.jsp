<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<title>业务管理-评分卡管理 </title>
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
<script type="text/javascript">
    var pageSize = 10;
    var typeMap = new Array();

    $(function () {
        findmodel(1);
        initType();
    });

    //
    function jumpToPage(pageNo) {
        findmodel(pageNo);
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

    //查modelajax
    function findmodel(pageNo) {
        $.ajax({
            url:"${ctx}/businessMng/modelList",
            type:"POST",
            dataType:"json",
            data:{"pageNo":pageNo, "pageSize":pageSize},
            success:function (data) {
                if (200 == data.code) {
                    var page = data.page;
                    var rows = page.rows;
                    var pageNo = page.pageNo;
                    var pageSize = page.pageSize;
                    var totalPage = page.totalPage;

                    $("#modelContent").html("");
                    for (var i=0; i<rows.length; i++) {
                        var row = rows[i];

                        var tr = "<tr>"+
                            "<td>"+(i+1+(pageNo-1)*pageSize)+"</td>"+
                            "<td>"+row.idCode+"</td>"+
                            "<td>"+row.name+"</td>"+
                            "<td>"+row.creator+"</td>"+
                            "<td>"+row.createTimeStr+"</td>";
							if (!typeMap[row.reportTypeId]) {
								tr += "<td>未匹配</td>";
							} else {
								tr += "<td>"+typeMap[row.reportTypeId]+"</td>";
							}
                            tr += "<td title='"+row.industryNameAndType+"'>"+row.industryNameAndType+"</td>";
                        tr += "<td class='operate'>"+
                            "<shiro:hasPermission name='/businessMng/modelShow'>"+
                            "<a href='${ctx}/businessMng/modelShow?method=show&id="+row.id+"'>查看</a>"+
							"</shiro:hasPermission>"+
							"<shiro:hasPermission name='/businessMng/modelAddOrEdit'>"+
                            "<a href='${ctx}/businessMng/modelAddOrEdit?method=edit&id="+row.id+"'>修改</a>"+
							"</shiro:hasPermission>"+
							"<shiro:hasPermission name='/businessMng/modelMatch'>"+
							"<a href='${ctx}/businessMng/modelMatch?id="+row.id+"'>匹配</a>"+
							"</shiro:hasPermission>"+
							"<shiro:hasPermission name='/businessMng/del'>"+
							"<a href='javascript:void(0);' onclick='del("+row.id+")'>删除</a>"
							"</shiro:hasPermission>";
                        tr += "</td>"+
                            "</tr>";
                        $("#modelContent").append(tr);
                    }

                    if (0 ==rows.length) {
                        $("#modelContent").append('<tr><td colspan="7">暂无数据</td></tr>');
					}
                    var pageStr = creatPage(page.total, pageNo, totalPage);
                    $("#pageDiv").html(pageStr);

                } else {
                    alert("加载失败！")
                }
            }
        });
    }

    //删除
    function del(id) {
        confirm("你真的要删除评分卡模型么？", function () {
            $.ajax({
                url: "${ctx}/businessMng/modelDel",
                type: "POST",
                dataType: "json",
                data: {"id": id},
                success: function (data) {
                    if (200 == data.code) {
                        alert("删除成功！", "${ctx}/businessMng/modelIndex")
                    } else {
                        alert("删除失败！")
                    }
                }
            })
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
				<a href="javascript:void(0);">评分卡管理</a>
			</h3>
			<div class="shade main_minHeight">
				<div class="search_box clear">
					<div class="fr function_btn">
						<shiro:hasPermission name='/businessMng/modelAddOrEdit'>
						<a href="${ctx}/businessMng/modelAddOrEdit?method=add&id=-1" class="fl">新增评分卡</a>
						</shiro:hasPermission>
						<%--<shiro:hasPermission name='/businessMng/modelMatchBatch'>
						<a href="${ctx}/businessMng/modelMatchBatch" class="fl">批量评分卡匹配</a>
						</shiro:hasPermission>--%>
					</div>
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
									<th>创建人</th>
									<th>创建时间</th>
									<th>匹配报表类型</th>
									<th>行业匹配</th>
									<th class="module_width6">操作</th>
								</tr>
								<tbody id="modelContent"></tbody>
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
