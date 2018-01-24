<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>业务管理-报表类型管理</title>
	<link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
	<script type="text/javascript" src="${ctx}/resources/js/bootstrap.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/my97datepicker/WdatePicker.js"></script>
	<style>
		.operate a{
			margin-left: 10px;
		}
	</style>
	<script type="text/javascript">
        var pageSize = 10;
        $(function () {
            //评级数据列表
        });
        //查评级ajax
        function getData(pageNo) {
            $.ajax({
                url:"${ctx}/rtype/getDetail",
                type:"POST",
                dataType:"json",
                data:{
                    typeId: typeId
                },
                success:function (data) {
                    if (data!=null) {
                        alert("len"+data.length);

                    } else {
                        alert("加载失败！");
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
				<a href="client.html">业务管理</a>
				<span>></span>
				<a href="account.html">报表类型管理</a>
				<span>></span>
				<a href="javascript:;">查看</a>
			</h3>
			<div class="backtrack">
				<a href="javascript:history.go(-1);">返回</a>
			</div>
			<div class="shade main_minHeight">
				<div class="permissions">
					<h2 class="permissions_title">基本信息</h2>
					<div class="permissions_title1 clear">
						<h3 class="fl">
							<b>报表类型名称</b>
							<strong>${name}</strong>
						</h3>
					</div>
					<h2 class="permissions_title">模板内容</h2>
					<div class="model_type_nav_wrap">
						<div class="model_type_nav">
							<c:forEach  items="${sheets}" var="str" varStatus="s">
								<c:choose>
								<c:when test="${s.count==1}">
									<span class="active"> <b>${str}</b></span>
								</c:when>
								<c:otherwise>
									<span > <b>${str}</b></span>
								</c:otherwise>
							</c:choose>

							</c:forEach>
							<%--<span class="active"><b>资产类数据</b></span>
								<span <c:if test="${s==0}">class="active"</c:if>
								><b>${str}</b></span>
							--%>
						</div>
					</div>

					<div class="report_models">
						<c:forEach  items="${res}" var="str" varStatus="s">
							<c:choose>
								<c:when test="${s.count==1}">
									<div class="report_model active">
								</c:when>
								<c:otherwise>
									<div class="report_model">
								</c:otherwise>
							</c:choose>
								<table class="module_table">
									<c:forEach var="map" items="${str}">
										<c:choose>
											<c:when test="${map.key=='lie'}">
												<tr>
												<c:forEach var="key" items="${map.value}">
													<th>${key}</th>
												</c:forEach>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="val" items="${map.value}">
													<tr>
													<c:forEach var="stm" items="${val}">
														<td>${stm.name}
															<c:if test="${stm.required==1}">
																<span style="color: red;">*</span>
															</c:if>
														</td>
														<td></td>
														<td></td>
													</c:forEach>
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</table>
							</div>

						</c:forEach>


					</div>
				</div>
			</div>
			<!-- footer.html start -->
			<%@ include file="../commons/foot.jsp"%>
			<!-- footer.html end -->
		</div>

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
<!-- 启用停用 end -->
<script>
    $('.model_type_nav b').click(function(){
        var _this = $(this);
        var parent = _this.parent();
        var index = parent.index();
        parent.addClass('active').siblings('.active').removeClass('active');

        var which_show = $('.report_model.active').index();
        if(index == which_show){
            return false;
        }else{
            $('.report_model.active').removeClass('active');
            $('.report_model').eq(index).addClass('active');
        }
    })
</script>
</body>
</html>

