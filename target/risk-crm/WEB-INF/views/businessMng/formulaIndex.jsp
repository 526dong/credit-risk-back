<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<title>业务管理-指标公式库 </title>
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
<script type="text/javascript">
	var pageSize = 10;
	var typeMap = new Array();

	$(function () {
        findIndex(1);

        //报表类型map
        var typeList = ${typeList};
        for (var i=0; i<typeList.length; i++) {
            var type = typeList[i];
			typeMap[type.id] = type.name;
		}

		//
		$(".select_list p").on("click", function () {
		    setTimeout('findIndex(1)', 100);
        });
    });
    
    //
    function jumpToPage(pageNo) {
        findIndex(pageNo);
    }

	//查公式ajax
	function findIndex(pageNo) {
        $.ajax({
            url:"${ctx}/businessMng/formulaList",
            type:"POST",
            dataType:"json",
            data:{"pageNo":pageNo, "pageSize":pageSize, "name":$("#name").val(), "reportTypeId":$("#reportTypeId").val()},
            success:function (data) {
				if (200 == data.code) {
                    var page = data.page;
                    var rows = page.rows;
                    var pageNo = page.pageNo;
                    var pageSize = page.pageSize;
                    var totalPage = page.totalPage;

                    $("#formulaConent").html("");
					for (var i=0; i<rows.length; i++) {
						var row = rows[i];

						var tr = "<tr>"+
                            "<td>"+(i+1+(pageNo-1)*pageSize)+"</td>";
                        	tr += "<td>"+row.formulaName+"</td>";
							if (!typeMap[row.reportTypeId]) {
                                tr += "<td>未匹配</td>";
							} else {
                                tr += "<td>"+typeMap[row.reportTypeId]+"</td>";
							}
                        	tr += "<td>"+row.creator+"</td>"+
							"<td>"+row.createTimeStr+"</td>"+
                        	"<td class='operate'>"+
								"<shiro:hasPermission name='/businessMng/formulaAddOrEdit'><a href='${ctx}/businessMng/formulaAddOrEdit?id="+row.id+"&method=edit'>修改</a></shiro:hasPermission>"+
								"<shiro:hasPermission name='/businessMng/formulaShow'><a href='${ctx}/businessMng/formulaShow?id="+row.id+"&method=show'>查看</a></shiro:hasPermission>"+
								"<shiro:hasPermission name='/businessMng/formulaDel'><a href='javascript:void(0);' onclick='del("+row.id+");'>删除</a></shiro:hasPermission>"+
						    /*if (0 == row.showFlag) {
								tr += "<a href='' onclick='updateState(this, "+row.id+", 1)'>启用</a>";
							} else {
                                tr += "<a href='' onclick='updateState(this, "+row.id+", 0)'>禁用</a>";
							}*/
							"</td>"+
							"</tr>";
                        $("#formulaConent").append(tr);
                    }

                    if (0 ==rows.length) {
                        $("#formulaConent").append('<tr><td colspan="7">暂无数据</td></tr>');
                    }
                    var pageStr = creatPage(page.total, pageNo, totalPage);
                    $("#pageDiv").html(pageStr);

				} else {
				    alert("加载失败！")
				}
            }
        });
    }
   
	function del(id) {
		confirm("你真的要删除么？", function () {
            $.ajax({
                url:"${ctx}/businessMng/formulaFindUsage",
                type:"POST",
                dataType:"json",
                data:{"id":id},
                success:function (data) {
                    if (200 == data.code) {
                        if ("extis" == data.extis) {
                            alert(data.name+"使用了公式，不能删除");
                        } else {
                            delFormula(id);
                        }
                    }
                }
            });
        });
   }
   
   function delFormula(id) {
       $.ajax({
           url:"${ctx}/businessMng/formulaDel",
           type:"GET",
           dataType:"json",
           data:{"id":id},
           success:function (data) {
               if (200 == data.code) {
                   alert("删除成功", "${ctx}/businessMng/formulaIndex");
               } else {
                   alert("删除失败");
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
				<a href="javascript:void(0);">指标公式库</a>
			</h3>
			<div class="shade main_minHeight">
				<div class="search_box clear">
					<div class="fr function_btn">
						<shiro:hasPermission name='/businessMng/formulaAddOrEdit'>
						<a href="${ctx}/businessMng/formulaAddOrEdit?id=-1&method=add" class="fl">新建公式</a>
						</shiro:hasPermission>
					</div>
					<shiro:hasPermission name='/businessMng/formulaIndex'>
					<div class="checkData_box checkData_box1 addCard clear">
						<strong>适用报表类型<font color="red">*</font></strong>
						<div class="down_menu">
							<c:if test="${!empty reportTypeList}">
								<div class="select_btn">
									<span data-id="">全部</span>
									<input type="hidden" name="reportTypeId" id="reportTypeId" value="-1" />
								</div>
								<div class="select_list">
									<p data-id="-1" title="全部">全部</p>
									<c:forEach items="${reportTypeList}" var="type" varStatus="idx">
										<p data-id="${type.id}" title="${type.name}">${type.name}</p>
									</c:forEach>
								</div>
							</c:if>
							<c:if test="${empty reportTypeList}">
								<div class="select_btn">
									<span data-id="">全部</span>
									<input type="hidden" name="reportTypeId" id="reportTypeId" value="-1" />
								</div>
								<div class="select_list">
									<p data-id="-1" title="全部">全部</p>
								</div>
							</c:if>
						</div>
					</div>
					<div class="search_btn fl" style="margin:0 auto;">
						<a href="javaScript:;"onclick="findIndex(1);" class="fr">查询</a>
						<input id="name" type="text" class="fr" placeholder="输入公式名称搜索" />
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
									<th>公式名称</th>
									<th>适用报表类型</th>
									<th>创建人</th>
									<th>创建时间</th>
									<th class="module_width9">操作</th>
								</tr>
								<tbody id="formulaConent"></tbody>
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
