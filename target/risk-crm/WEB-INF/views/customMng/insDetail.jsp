<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>客户管理-机构管理 -查看机构</title>
    
<link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!-- <script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script> -->
<script type="text/javascript" src="${ctx}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>


<script type="text/javascript">


</script>
  </head>
  
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
            <form id="insAddForm">
            <input type="hidden" id="insId" value="${insId }">
            <h3 class="place_title">
                <span>当前位置：</span>
                <a href="javascript:void(0);">客户管理</a>
                <span>></span>
                <a href="${ctx}/custom/insManage">机构管理</a>
                <span>></span>
                <a href="${ctx}/custom/lookDetailIns?id=${insId }">查看机构信息</a>
            </h3>
            <div class="backtrack">
            	<a href="${ctx}/custom/insManage">返回</a>
            </div>
            <div class="shade_bor">
            	<h2 class="main_title">机构基本信息</h2>
                <div class="container-fluid main_padd">
                	<div class="row">
                    	<div class="col-lg-6 col-sm-6 col-md-6">
                        	<div class="info_box info_box1 clear">
                            	<strong>机构名称</strong>
                                <p>${institution.name }</p>
                            </div>
                        </div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                        	<div class="info_box info_box1 clear">
                            	<strong>机构性质</strong>
                                <p>${institution.organizationNature }</p>
                            </div>
                        </div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                            <div class="info_box info_box1 clear">
                                <strong>代码标识</strong>
                                <p>${institution.organizationCode }</p>
                            </div>
                    	</div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                        	<div class="info_box info_box1 clear">
                            	<strong>机构规模</strong>
                                <p>${institution.organizationScale }</p>
                            </div>
                        </div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                        	<div class="info_box info_box1 clear">
                            	<strong>成立时间</strong>
                                <p>${institution.foundtime }</p>
                            </div>
                        </div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                        	<div class="info_box info_box1 clear">
                            	<strong>法人名称</strong>
                                <p>${institution.legalName }</p>
                            </div>
                        </div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                        	<div class="info_box info_box1 clear">
                            	<strong>公司电话</strong>
                                <p>${institution.officePhoneAreacode }-${institution.officePhone }</p>
                            </div>
                        </div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                        	<div class="info_box info_box1 clear">
                            	<strong>公司邮箱</strong>
                                <p>${institution.email }</p>
                            </div>
                        </div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                        	<div class="info_box info_box1 clear">
                            	<strong>公司传真</strong>
                                <p>${institution.fax }</p>
                            </div>
                        </div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                        	<div class="info_box info_box1 clear">
                            	<strong>公司官网</strong>
                                <p>${institution.webSite }</p>
                            </div>
                        </div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                        	<div class="info_box info_box1 clear">
                            	<strong>行业</strong>
                                <p>${institution.industryFirst }-${institution.industrySecond }</p>
                            </div>
                        </div>
                        <div class="col-lg-12 col-sm-12 col-md-12">
                        	<div class="info_box info_box1 clear">
                            	<strong>注册地址</strong>
                                <p>${institution.address }</p>
                            </div>
                        </div>
                        <div class="col-lg-12 col-sm-12 col-md-12">
                        	<div class="info_box info_box1 clear">
                            	<strong>办公地址</strong>
                                <p>${institution.officeAddress }</p>
                            </div>
                        </div>                     
                    </div>
                </div>
            </div>
            <div class="shade_bor main_mar">
            	<h2 class="main_title">联系人信息</h2>
                <div class="container-fluid main_padd">
                	<div class="row">
                    	<div class="col-lg-6 col-sm-6 col-md-6">
                        	<div class="info_box info_box1 clear">
                            	<strong>姓名</strong>
                               	<p>${institution.contactName }</p>
                            </div>
                        </div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                        	<div class="info_box info_box1 clear">
                            	<strong>所属部门</strong>
                                <p>${institution.contactDep }</p>
                            </div>
                        </div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                        	<div class="info_box info_box1 clear">
                            	<strong>职务</strong>
                                <p>${institution.contactJob }</p>
                            </div>
                        </div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                        	<div class="info_box info_box1 clear">
                            	<strong>办公电话</strong>
                                <p>${institution.contactPhoneAreacode }-${institution.contactPhone }</p>
                            </div>
                        </div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                        	<div class="info_box info_box1 clear">
                            	<strong>手机号码</strong>
                                <p>${institution.contactPhoneNum }</p>
                            </div>
                        </div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                        	<div class="info_box info_box1 clear">
                            	<strong>个人邮箱</strong>
                                <p>${institution.contactEmail }</p>
                            </div>
                        </div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                        	<div class="info_box info_box1 clear">
                            	<strong>微信号</strong>
                                <p>${institution.contactWechat }</p>
                            </div>
                        </div>
                        <div class="col-lg-6 col-sm-6 col-md-6">
                        	<div class="info_box info_box1 clear">
                            	<strong>QQ号</strong>
                                <p>${institution.contactQQ }</p>
                            </div>
                        </div>
            		</div>
            	</div>
            </div>
            <div class="audit_state">
            	<strong>状态：</strong>
            	<c:choose>  
				  <c:when test="${institution.examinestate == 1 }">
				  	<span>未审核</span>
				  </c:when> 
				  <c:when test="${institution.examinestate == 2 }">
				  	<span>审核拒绝</span>
				  </c:when>
				  <c:when test="${institution.examinestate == 3 }">
				  	<span>审核通过</span>
				  </c:when>
				  <c:otherwise>
				  	<span>未审核</span>
				  </c:otherwise>  
				</c:choose> 
            </div>
            <c:choose>  
			  <c:when test="${institution.examinestate == 1 }">
			  	
			  </c:when> 
			  <c:otherwise>
			  	<div class="shade_bor">
	            	<h2 class="main_title">审核操作</h2>
	                <div class="textarea_box">
	                	${institution.examineReason }
	                </div>
	            </div>
			  </c:otherwise>  
			</c:choose>
            
            <div class="main_btn main_btn1">
            	<a href="${ctx}/custom/insManage" class="fl">确定</a>
            </div>
            <!-- footer.html start -->
             <%@ include file="../commons/foot.jsp"%>
            <!-- footer.html end -->
            </form>
        </div>
        <!-- 右侧内容.html end -->
    </div>
    <!-- center.html end -->
</div>
</body>
</html>