<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>业务管理-评级数据管理</title>
<link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<script type="text/javascript" src="${ctx}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/my97datepicker/WdatePicker.js"></script>
<script type="text/javascript">
var pageSize = 10;
$(function () {
	//评级数据列表
	getData(1);
});
//查评级ajax
function getData(pageNo) {
	$.ajax({
		url:"${ctx}/rateData/findAll",
		type:"POST",
		dataType:"json",
		data:{
			"pageNo": pageNo,
			"pageSize": pageSize,
			"rateResult": $("#result").val(),
			"startDate": $("#startDate").val(),
			"endDate": $("#endDate").val(),
			"searchContent": $("#keyWord").val()
		},
		success:function (data) {
			if (200 == data.code) {
				var page = data.data;
				var dataList = page.rows;
				//clear
				$("#rateDataConent").html("");
				var htmlContent = createTable(dataList)
				$("#rateDataConent").html(htmlContent);
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
function createTable(data){
	var htmlContent = "";
	for (var i=0; i<data.length; i++) {
		htmlContent += "<tr>";
		htmlContent += "<td>"+(parseInt(i)+1)+"</td>";
		if (data[i].name ==  null) {
			htmlContent += "<td></td>";
		} else {
			htmlContent += "<td title='"+data[i].name+"' style='overflow: hidden;text-overflow:ellipsis;white-space: nowrap'>"+data[i].name+"</td>";
		}
		htmlContent += "<td>"+ (data[i].creditCode == null ? '' : data[i].creditCode)+"</td>";//统一社会信用代码
		htmlContent += "<td>"+ (data[i].organizationCode == null ? '' : data[i].organizationCode) +"</td>";//组织机构代码
		htmlContent += "<td>"+ (data[i].certificateCode == null ? '' : data[i].certificateCode) +"</td>";//事证号
		htmlContent += "<td>"+ (data[i].rateResultName == null ? '' : data[i].rateResultName) +"</td>";
		//评级时间
		if (data[i].rateTime ==  null) {
			htmlContent += "<td></td>";
		} else {
			htmlContent += "<td title='"+data[i].rateTime+"' style='overflow: hidden;text-overflow:ellipsis;white-space: nowrap'>"+data[i].rateTime.substring(0, 10)+"</td>";
		}
		//评级机构
		if (data[i].rateInstitutionName == null) {
			htmlContent +="<td>"+(data[i].rateInsNameOut == null ? '' : data[i].rateInsNameOut)+"</td>";
		} else {
			htmlContent += "<td title='"+data[i].rateInstitutionName+"' style='overflow: hidden;text-overflow:ellipsis;white-space: nowrap'>"+ data[i].rateInstitutionName+"</td>";
		}
		htmlContent += "<td>"+ (data[i].creatorName == null ? '' : data[i].creatorName) +"</td>";
		if (data[i].createTime == null) {
			htmlContent += "<td></td>";
		} else {
			htmlContent += "<td title='"+data[i].createTime+"' style='overflow: hidden;text-overflow:ellipsis;white-space: nowrap'>"+ data[i].createTime.substring(0, 10) +"</td>";
		}
		htmlContent += "<td class='module_width2'>";
		htmlContent += " <shiro:hasPermission name='/rateData/update'><a class='update_btn' style='margin-right: 10px;' onclick='update("+data[i].id+");' href='javascript:;'>修改</a></shiro:hasPermission>";
		htmlContent += " <shiro:hasPermission name='/rateData/del'><a class='delete_btn' onclick='del("+data[i].id+", "+data[i].useShadow+");' href='javascript:;'>删除</a></shiro:hasPermission>";
		htmlContent += "</td>";
		htmlContent += "</tr>";
	}

	return htmlContent;
}
$(document).on('click', '.select_btn', function(ev){
	debugger
	var This =$(this);
	oDiv =$(this).parent().find('.select_list');
	dis =$(oDiv).css('display');
	$('.select_list').hide();
	if(dis=='block'){
		$(oDiv).hide();
	}else{
		$(oDiv).show();
	}
	$(oDiv).on("mouseover","p",function(){
		$(oDiv).find('p').removeClass('active');
		$(this).addClass('active');
	});
	$(oDiv).on("click","p",function(){
		$(This).find('span').html($(this).html());
		$(This).find('span').attr('data-id',$(this).attr('data-id'));
		$(This).find('input').val($(this).attr('data-id'));
		$(oDiv).hide();
	});

	ev.stopPropagation();
})
//评级数据修改
function update(id) {
	window.location.href = "${ctx}/rateData/update?id="+id;
}
/**
 * //评级数据删除
 * @param id
 * @param shadowFlag 是否使用影子评级
 */
function del(id, shadow) {
	if (shadow == "0") {
        confirmDelete(id);
	} else {
	    alert("该数据已被影子评级所使用，无法删除！");
	}
}
//确认删除
function confirmDelete(id) {
    confirm("确定删除该评级信息吗?", function(){
        $.ajax({
            url:"${ctx}/rateData/delete?id="+id,
            type:'POST',
            success:function(data){
                if(data.code == 200){
                    alert("删除成功！");
                    setTimeout(function() {
                        window.location.href="${ctx}/rateData/list";
                    },1500);
                }else{
                    alert("删除失败！");
                    setTimeout(function() {
                        window.location.href="${ctx}/rateData/list";
                    },1500);
                    console.error(data.msg);
                }
            }
        });
    });
}
//查询
function searchRateData(){
	getData(1);
}
//分页跳转
function jumpToPage(pageNo){
	getData(pageNo);
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
				<a href="javascript:void(0)">业务管理</a>
				<span>></span>
				<a href="javascript:void(0)">评级数据管理</a>
			</h3>
			<div class="shade main_padd">
				<div class="container-fluid">
					<div class="row">
						<shiro:hasPermission name='/rateData/list'>
						<div class="col-lg-4 col-sm-4 col-md-4">
							<div class="search_son search_son2">
								<strong>评级结果</strong>
								<div class="down_menu down_menu1">
									<div class="select_btn">
										<span data-id="">全部</span>
										<input id="result" value="" type="hidden"/>
									</div>
									<div class="select_list">
										<p data-id="">全部</p>
										<c:forEach items="${resultList}" var="res" varStatus="status">
											<p data-id="${res.id}">${res.name}</p>
										</c:forEach>
									</div>
								</div>
							</div>
						</div>
						<div class="col-lg-4 col-sm-4 col-md-4">
							<div class="search_son search_son3">
								<strong>创建时间</strong>
								<%--class="select_time fl Wdate"--%>
								<input id="startDate" readonly="readonly" class="fl" placeholder="开始时间" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="">
								<i class="select_xian">-</i>
								<input id="endDate" readonly="readonly" class="fr" placeholder="结束时间" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="">
							</div>
						</div>
						<div class="col-lg-4 col-sm-4 col-md-4">
							<div class="search_btn search_btn2 fr">
								<a class="fr" href="javaScript:;" onclick="searchRateData();" >查询</a>
								<input id="keyWord" class="fr" type="text" placeholder="公司名称/创建人" value="">
							</div>
						</div>
						</shiro:hasPermission>
					</div>
				</div>
			</div>
			<div class="shade main_minHeight main_mar">
				<div class="search_box clear" style="padding-top:12px;">
					<div class="fr function_btn">
						<shiro:hasPermission name='/rateData/add'>
						<a href="${ctx}/rateData/add" class="fl">新增评级</a>
						</shiro:hasPermission>
						<shiro:hasPermission name='/rateData/importRateDataExcel'>
						<a href="${ctx}/rateData/importRateDataExcel" class="fl database_btn">上传</a>
						</shiro:hasPermission>
					</div>
				</div>
				<div class="module_height">
					<form>
						<table class="module_table module_table1">
							<thead>
								<tr>
									<th class="module_width1">序号</th>
									<th>公司名称</th>
									<th>统一社会信用代码</th>
									<th>组织机构代码</th>
									<th>事证号</th>
									<th>最新评级结果</th>
									<th>最新评级日期</th>
									<th>评级机构</th>
									<th>创建人</th>
									<th>创建时间</th>
									<th class="module_width2">操作</th>
								</tr>
							</thead>
							<tbody id="rateDataConent"></tbody>
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

