<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<title>业务管理-评分卡管理-查看评分卡 </title>
	<link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
<script type="text/javascript">
    var pageSize = 10;
	var checkedMap = new Array();
	var reportTypeIdBak;

	$(function () {
        initSelect();
        $(".select_list p").on("click", function () {
            setTimeout(function () {
                var reportTypeId = $("#reportTypeId").val();

                if (reportTypeIdBak != reportTypeId) {
                    $("#modelContent .list").remove();
                } else {
                    window.location.reload(true);
                }
            }, 500);
        });
        reportTypeIdBak = $("#reportTypeId").val();
    });

    //添加行业guan关联
    function addIndustry() {
        findmodel(1);
        fnPopup('#popup');
    }

    //
	function search() {
        findmodel(1);
    }

    //
    function jumpToPage(pageNo) {
        $("#industryContent").html("");
        findmodel(pageNo);
    }

    //查modelajax
    function findmodel(pageNo) {
        $.ajax({
            url:"${ctx}/businessMng/modelIndustryList",
            type:"POST",
			async:false,
            dataType:"json",
            data:{"pageNo":pageNo, "pageSize":pageSize, "name":$("#name").val(), "reportTypeId":$("#reportTypeId").val()},
            success:function (data) {
                if (200 == data.code) {
                    var page = data.page;
                    var rows = page.rows;
                    var pageNo = page.pageNo;
                    var pageSize = page.pageSize;
                    var totalPage = page.totalPage;

                    initCheckedMap();
                    $("#industryContent").html("");
                    for (var i=0; i<rows.length; i++) {
                        var row = rows[i];
                        var id2 = row.id2;
                        var type = row.type;
                        var key = id2+":"+type;

						var tr = "<tr>"+
							"<td>";
						if (checkedMap[key]) {
                            tr += '<a href="javaScript:;" data-id="1" class="curr" onclick="$(this).off(\'click\')"></a>';
						} else {
                            tr += '<a href="javaScript:;" data-id="0"></a>';
						}

                        tr += "<span>"+(i+1+(pageNo-1)*pageSize*2)+"</span>"+
								"<input type='hidden' value='"+id2+"' />"+
								"<input type='hidden' value='"+type+"' />"+
							"</td>"+
							"<td>"+row.name1+"</td>"+
							"<td>"+row.name2+"</td>";
						if (0 == row.type) {
							tr += "<td>中大型</td>";
						} else {
							tr += "<td>小微型</td>";
						}
						tr += "</td>"+
							"</tr>";
						$("#industryContent").append(tr);
                    }

                    if (0 ==rows.length) {
                        $("#industryContent").append('<tr><td colspan="7">暂无数据</td></tr>');
                    }
                    var pageStr = creatPage(page.total, pageNo, totalPage);
                    $("#pageDiv").html(pageStr);
                    fnCheckBox('.mateInfo_table a')
                } else {
                    alert("加载失败！")
                }
            }
        });
    }
    
    //增加选中
	function addSelectInd() {
        var div = "";

        $(".mateInfo_table a[class='curr']").each(function (i, ele) {
            var ind1Name = $(ele).parent().next().text();
            var ind2Name = $(ele).parent().next().next().text();
            var ind2Id = $(ele).next().next().val();
            var type = $(ele).next().next().next().val();
            var flag = false;

            $(".mateInfo .clear").each(function () {
				var ind2IdAdded = $(this).find("input[name='industryId']").val();
				var typeAdded = $(this).find("input[name='type']").val();

				if (ind2IdAdded==ind2Id && typeAdded==type) {
					flag = true;
					return false;
				}
            });
            if (flag) {return true}

			div = "<div class='clear list'>" +
                "<div class='info_box info_box1 info_box2 fl'>" +
					"<strong>行业</strong>" +
					"<p>"+ind1Name+" - "+ind2Name+"</p>" +
					"<input type='hidden' name='industryId' value='"+ind2Id+"' />" +
                "</div>" +
                "<div class='info_box info_box1 info_box2 fl'>" +
					"<strong>规模</strong>" +
					"<p>"+$(ele).parent().next().next().next().text()+
					"<input type='hidden' name='type' value='"+type+"' />" +
					"</p>" +
                "<a href='javaScript:;' class='grade_btn fr' onClick='del(this);'>删除</a>" +
                "</div>" +
                "</div>";
            $("#modelContent").append(div);
            initCheckedMap();
            $(".curr").off("click");
        });
        if ("" != div) {
			alert("添加成功");
		}
    }

    function fnDelete(obj){
        $(obj).parent().parent().remove();
    }

    var delId = [];
    var delType = [];
    //删除选中
	function del(ele, delIndId, type) {
        fnDelete($(ele));
		delId.push(delIndId);
		delType.push(type);
    }

    var confirmFlag = false;
    //保存匹配
	function saveMatch() {
        if ($("#modelContent .list").length == 0) {
            alert("您还没有选择行业，请选择行业后再保存");
            return;
        }

	    if (confirmFlag){return;}
	    confirmFlag = true;

		$("#delId").val(delId.join(","));
		$("#delType").val(delType.join(","));

		$.ajax({
			url:"${ctx}/businessMng/modelMatchSave",
			type:"POST",
			dataType:"json",
			data:$("#frm").serialize(),
			success:function (data) {
                if (200 == data.code) {
                    alert("保存成功", "${ctx}/businessMng/modelIndex");

				} else if (400 == data.code) {
                    alert(data.msg);
                    confirmFlag = false;
                    delId.splice(0, delId.length);
                    delType.splice(0, delType.length);
                } else {
                    alert("保存失败");
                    confirmFlag = false;
                    delId.splice(0, delId.length);
                    delType.splice(0, delType.length);
				}
			}
		});
	}

	//
	function initCheckedMap() {
		var indArr = $("input[name='industryId']");
		var typeArr = $("input[name='type']");

		for (var i=0; i<indArr.length; i++) {
		    var id = indArr.eq(i).val();
		    var type = typeArr.eq(i).val();
		    var key = id+":"+type;

		    checkedMap[key] = true;
		}
    }

    //下拉回显
    function initSelect() {
        var selectItem = $(".select_list p[class='active']");

        //报表类型
        selectItem.eq(0).parent().prev().find("input").val(selectItem.eq(0).attr("data-id"));
        selectItem.eq(0).parent().prev().find("span").attr("data-id", selectItem.eq(0).attr("data-id"));
        selectItem.eq(0).parent().prev().find("span").text(selectItem.eq(0).text());
        selectItem.eq(0).parent().prev().find("span").attr("title", selectItem.eq(0).text());
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
				<a href="${ctx}/businessMng/modelIndex">评分卡管理</a>
				<span>></span>
				<a href="javascript:void(0);">单个评分卡匹配</a>
			</h3>
			<%--<div class="backtrack">
				<a href="javascript:history.go(-1);">返回</a>
			</div>--%>
			<div class="search_box clear">
				<div class="checkData_box addCard clear">
					<strong>评分卡名称</strong>
					<span>${model.name}</span>
				</div>
			</div>
            <form id="frm">
                <div class="checkData_box checkData_box1 addCard clear">
                    <strong>适用报表类型<font color="red">*</font></strong>
                    <div class="down_menu">
                        <div class="select_btn">
                            <span data-id=""></span>
                            <input type="hidden" name="reportTypeId" id="reportTypeId" value="${model.reportTypeId}" />
                        </div>
                        <div class="select_list">
                            <c:forEach items="${reportTypeList}" var="type" varStatus="idx">
                                <p data-id="${type.id}" title="${type.name}" <c:if test="${(null==model.reportTypeId and idx.first) or type.id == model.reportTypeId}">class="active"</c:if>>${type.name}</p>
                            </c:forEach>
                        </div>
                    </div>
                </div>
                <p class="clear mateInfo_btn">
                    <a href="javaScript:;" class="grade_btn fl" onClick="addIndustry();">添加行业与企业规模</a>
                </p>
				<div class="mateInfo" id="modelContent">
					<input type="hidden" name="id" value="${model.id}" />
					<input type="hidden" name="delId" id="delId" />
					<input type="hidden" name="delType" id="delType" />
					<input type="hidden" name="batchFlag" value="false" />
					<c:forEach items="${list}" var="industry">
						<div class="clear list">
							<div class="info_box info_box1 info_box2 fl">
								<strong>行业</strong>
								<p>${industry.name1} - ${industry.name2}</p>
								<input type='hidden' name='industryId' value='${industry.id2}' />
							</div>
							<div class="info_box info_box1 info_box2 fl">
								<strong>规模</strong>
								<p>
									<c:if test="${industry.type == 0}">大中型</c:if><c:if test="${industry.type == 1}">小微型</c:if>
									<input type='hidden' name='type' value='${industry.type}' />
								</p>
								<a href="javaScript:;" class="grade_btn fr" onClick="del(this, ${industry.id2}, ${industry.type});">删除</a>
							</div>
						</div>
					</c:forEach>
				</div>
			</form>
			<div class="main_btn main_btn1">
				<a href="javaScript:;" class="fl" onclick="saveMatch();">保存</a>
				<a href="javascript:history.go(-1);" class="fr">取消</a>
			</div>

			 <%--弹出div--%>
			 <%--<div id="pop" class="pop">
				 一级或二级行业名：<input type="text" name="name" id="name" /><input type="button" value="查询" onclick="search();" />
				 <table id="" class="tb">
					 <tr>
						 <th>序号</th>
						 <th>一级行业</th>
						 <th>二级行业</th>
						 <th>规模</th>
					 </tr>
					 <tbody id="industryContent"></tbody>
				 </table>
				 <input type="button" value="确定" onclick="$('#pop').css('display', 'none');" />
			 </div>--%>
			<!-- footer.html start -->
			<%@ include file="../commons/foot.jsp"%>
			<!-- footer.html end -->
		</div>
		<!-- 右侧内容.html end -->
	</div>
	<!-- center.html end -->
</div>
<!-- 遮罩层 start -->
<div class="layer" id="layer"></div>
<!-- 遮罩层 end -->
<!-- 添加单个评分卡匹配 start -->
<div class="popup popup4" id="popup">
	<a href="javaScript:;" class="close"></a>
	<div class="clear popup_btn_m">
		<a href="javaScript:;" class="grade_btn fl" onclick="addSelectInd();">添加当前页</a>
		<a href="javaScript:;" class="grade_btn fl" onclick="fnShutDown($('#popup').get(0))">取消</a>
	</div>
	<div class="module_height">
		<form>
			<table class="module_table mateInfo_table">
				<thead>
				<tr>
					<th class="module_width8">序号</th>
					<th>一级行业</th>
					<th>二级行业</th>
					<th>规模</th>
				</tr>
				</thead>
				<tbody  id="industryContent"></tbody>
			</table>
		</form>
	</div>
	<!-- 分页.html start -->
	<%@include file="../commons/tabPage.jsp" %>
	<!-- 分页.html end -->
</div>
<!-- 添加单个评分卡匹配 end -->
</body>
</html>
