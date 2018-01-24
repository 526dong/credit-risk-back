<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<title>业务管理-评分卡管理-批量评分卡匹配 </title>
	<link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<style type="text/css">
	.select_btn input{width:80%; border:none; outline:none; text-align:center; padding:0; border-radius:0;}
</style>
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
<script type="text/javascript">
	var pageSize = 5;

	$(function () {
		findmodel(1);

		$(document).click(function(){
			$(".select_list").hide();
		});

	});

	//
	function jumpToPage(pageNo) {
		findmodel(pageNo);
	}

	//查modelajax
	function findmodel(pageNo) {
		$.ajax({
			url:"${ctx}/businessMng/modelIndustryList",
			type:"POST",
			dataType:"json",
			data:{"pageNo":pageNo, "pageSize":pageSize, "name":$("#name").val(), "modelFlag":true},
			success:function (data) {
				if (200 == data.code) {
					var page = data.page;
					var rows = page.rows;
					var pageNo = page.pageNo;
					var pageSize = page.pageSize;
					var totalPage = page.totalPage;

					$("#industryContent").html("");
					for (var i=0; i<rows.length; i++) {
						var row = rows[i];

						var tr = "<tr>"+
							"<td>"+
							(i+1+(pageNo-1)*pageSize*2)+
							"<input type='hidden' name='industryId' value='"+row.id2+"' />"+
							"<input type='hidden' name='type' value='"+row.type+"' />"+
							"</td>"+
							"<td>"+row.name1+"</td>"+
							"<td>"+row.name2+"</td>";
						if (0 == row.type) {
							tr += "<td>中大型</td>";
						} else if(1 == row.type) {
							tr += "<td>小微型</td>";
						}
						tr += "</td>"+
							"<td class='addAllCard_td'>"+
								"<div class='down_menu'>"+
									"<div class='select_btn'>";
                        				if (0 == row.type) {
                                            tr += "<input type='hidden' name='id' value='"+(row.modelId0==null?'':row.modelId0)+"' />";
										} else {
                                            tr += "<input type='hidden' name='id' value='"+(row.modelId1==null?'':row.modelId1)+"' />";
										}
										tr += "<input type='text' onkeyup='searchModel(this);' value='"+row.modelName+"' placeholder='请输入评分卡名称' />" +
									"</div>"+
									'<div class="select_list"></div>'+
								"</div>"+
							"</td>"+
							"</tr>";
						$("#industryContent").append(tr);
					}
                    bindSeaarch();
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

	//选择下拉菜单
	function bindSeaarch(){
		var divList;

		$('.select_btn').on('click',function(ev){
			divList = $(this).parent().find('.select_list');

            searchModel($(this).find("input[type='text']").get(0), 1);
            $(divList).show();
			$(divList).on("mouseover","p",function(){
				$(divList).find('p').removeClass('active');
				$(this).addClass('active');
			});

			ev.stopPropagation();
		});


	}

	//搜索
	function searchModel(ele, clickFlag) {
        $.ajax({
            url:"${ctx}/businessMng/modelSearch",
            type:"POST",
			async:false,
            dataType:"json",
            data:{"name": 1==clickFlag?'':ele.value},
            success:function (data) {
                if (200 == data.code) {
                    var divHtml = "";
                    $.each(data.list, function (i, model) {
                        if (model.name == ele.value) {
                            divHtml += "<p class='active' onclick='addSelect("+model.id+", this)'>"+model.name+"</p>";
						} else {
                            divHtml += "<p onclick='addSelect("+model.id+", this)'>"+model.name+"</p>";
						}
                    });

                    if ("" == divHtml) {
                        divHtml += "<p class='active' onclick='addSelect(\"\", this)'>请选择</p>";
					}
                    $(ele).parent().next().html(divHtml);
                } else {
                    alert("搜索失败");
                }
            }
        });
	}

	//
	function addSelect(id, ele) {
		$(ele).parent().prev().find("input").eq(0).val(id);
		$(ele).parent().prev().find("input").eq(1).val($(ele).text());
		$(ele).parent().hide();
	}

	//保存匹配
	function saveMatch() {
	    $(".select_btn input[type='text']").each(function () {
			if ("" == $(this).val()) {
                $(this).prev().val("");
			}
        });

		$.ajax({
			url:"${ctx}/businessMng/modelMatchSave",
			type:"POST",
			dataType:"json",
			data:$("#frm").serialize(),
			success:function (data) {
				if (200 == data.code) {
					alert("保存成功");
				} else {
					alert("保存失败");
				}
			}
		});
	}
</script>
<body class="body_bg">
<div class="main">
	<!-- header.html start -->
	<%@ include file="../commons/topHead.jsp"%>
	<!-- header.html end -->
	<!-- center.html start -->
	<div class="main_center">
		<!-- 左侧导航.html start -->
		<%@ include file="../commons/leftNavigation.jsp"%>
		<!-- 左侧导航.html end -->
		<!-- 右侧内容.html start -->
		<div class="right_content">
			<h3 class="place_title">
				<span>当前位置：</span>
				<a href="javascript:void(0)">业务管理</a>
				<span>></span>
				<a href="${ctx}/businessMng/modelIndex">评分卡管理</a>
				<span>></span>
				<a href="">批量评分卡匹配</a>
			</h3>
			<%--<div class="backtrack">
				<a href="javascript:history.go(-1);">返回</a>
			</div>--%>
			<div class="shade main_minHeight">
				<div class="search_box clear">
					<div class="search_btn fl">
						<a href="javaScript:;" class="fr" onclick="findmodel(1);">查询</a>
						<input name="name" id="name" class="fr" type="text" placeholder="一级行业/二级行业查询" />
					</div>
				</div>
				<div class="module_height">
					<form id="frm">
						<input type="hidden" name="batchFlag" value="true" />
						<table class="module_table addAllCard">
							<thead>
							<tr>
								<th class="module_width1">序号</th>
								<th>一级行业</th>
								<th>二级行业</th>
								<th>规模</th>
								<th class="module_width7">评分卡</th>
							</tr>
							</thead>
							<tbody id="industryContent">
							<tr>
								<td>1</td>
								<td>化工</td>
								<td>石油化工</td>
								<td>大中型</td>
								<td class="addAllCard_td"></td>
							</tr>
							</tbody>
						</table>
					</form>
				</div>
				<!-- 分页.html start -->
				<%@include file="../commons/tabPage.jsp" %>
				<!-- 分页.html end -->
			</div>
			<div class="main_btn main_btn1">
				<a href="javaScript:;" class="fl" onclick="saveMatch();">保存当前页匹配</a>
				<a href="javascript:history.go(-1);" class="fr">取消</a>
			</div>
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
<!-- 删除 start -->
<div class="popup popup2" id="popup">
	<a href="javaScript:;" class="close"></a>
	<p>错误提示</p>
	<div class="popup_btn">
		<a href="javaScript:;" class="a1 fl">确定</a>
		<a href="javaScript:;" class="a2 fr">取消</a>
	</div>
</div>
<!-- 删除 end -->
</body>
</html>