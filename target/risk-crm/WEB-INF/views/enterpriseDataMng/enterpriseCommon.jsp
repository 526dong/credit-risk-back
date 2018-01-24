<%@ page language="java" import="java.util.*" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 企业信息 start-->
<%--详细--%>
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
					<strong>企业性质</strong>
					<p>${enterprise.natureName}</p>
				</div>
			</div>
			<div class="col-lg-6 col-sm-6 col-md-6">
				<div class="info_box info_box1 clear">
					<strong>代码标识</strong>
					<p>${enterprise.creditCode}</p>
					<%--<c:if test="${enterprise.creditCodeType == 0}">
						<p>统一社会信用代码-${enterprise.creditCode}</p>
					</c:if>
					<c:if test="${enterprise.creditCodeType == 1}">
						<p>组织机构代码-${enterprise.creditCode}</p>
					</c:if>
					<c:if test="${enterprise.creditCodeType == 2}">
						<p>事证号-${enterprise.creditCode}</p>
					</c:if>--%>
				</div>
			</div>
			<div class="col-lg-6 col-sm-6 col-md-6">
				<div class="info_box info_box1 clear">
					<strong>企业规模</strong>
					<c:if test="${enterprise.scale == 0}">
						<p>大中型企业</p>
					</c:if>
					<c:if test="${enterprise.scale == 1}">
						<%--<p>小微企业</p>--%>
						<p></p>
					</c:if>
				</div>
			</div>
			<div class="col-lg-12 col-sm-12 col-md-12">
				<div class="info_box info_box1 clear">
					<strong>行业类别</strong>
					<p><span>${enterprise.industry1Name}</span><span style="margin-left:30px;">${enterprise.industry2Name}</span></p>
				</div>
			</div>
			<div class="col-lg-12 col-sm-12 col-md-12">
				<div class="info_box info_box1 clear">
					<strong>注册地址</strong>
					<input id="provinceId" name="provinceId" value="${enterprise.provinceId}" type="hidden"/>
					<input id="cityId" name="cityId" value="${enterprise.cityId}" type="hidden"/>
					<input id="countyId" name="countyId" value="${enterprise.countyId}" type="hidden"/>
					<p><span id="regionSpan"></span>-<span>${enterprise.address}</span></p>
				</div>
			</div>
		</div>
	</div>
	<div class="corporate_detail" style="display: none">
		<div class="little_title">
			<h2 class="main_title">法人／大股东（个人）信息</h2>
		</div>
		<div class="container-fluid main_padd">
			<div class="row">
				<div class="col-lg-6 col-sm-6 col-md-6">
					<div class="info_box info_box1 clear">
						<strong>姓名</strong>
						<p>${enterprise.corporateName}</p>
					</div>
				</div>
				<div class="col-lg-6 col-sm-6 col-md-6">
					<div class="info_box info_box1 clear">
						<strong>手机号码</strong>
						<p>${enterprise.corporatePhone}</p>
					</div>
				</div>
				<div class="col-lg-6 col-sm-6 col-md-6">
					<div class="info_box info_box1 clear">
						<strong>身份证号码</strong>
						<p>${enterprise.corporateCid}</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 主体信息 end -->
