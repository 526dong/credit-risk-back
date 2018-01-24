<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!doctype html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>客户管理-账号管理-角色管理-分配权限</title>

<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!-- <script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script> -->
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/resources/js/tools/easyui/themes/gray/easyui.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/resources/js/tools/easyui/themes/icon.css"/>
<script type="text/javascript" src="${ctx}/resources/js/tools/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/tools/easyui/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
$(function() {
	 var insId = $("#insId").val();
	 var roleId = $("#rid").val();
	 $('#tree').tree({
        url:'${ctx}/custom/findInsPermissionTree?roleId='+roleId+'&&insId='+insId,
        checkbox:true,
        onLoadSuccess: function(e, node){ 
          $('#tree').tree('select', node.target);  
        }
      });
});


function savePermission(){
   var nodes = $('#tree').tree('getChecked');
   //定义功能权限ID数组
   var third_stage_array = '';
   //定义二级菜单ID数组
   var second_stage_array='';
   //定义一级菜单ID数组
   var first_stage_array='';
   //定义权限数组
   var permission_array='';
   //遍历出选中的功能权限
   for (var i = 0; i < nodes.length; i++) {
       if (third_stage_array != ''){
       	third_stage_array += ',';
       }
       //获取功能权限ID
       third_stage_array += nodes[i].id;
      
   	//获取二级菜单节点
       var secondNode = $("#tree").tree("getParent", nodes[i].target);
       if(null!=secondNode){
       	if (second_stage_array != ''){
       		second_stage_array += ',';
           }
       	//获取二级菜单ID
       	second_stage_array += secondNode.id
       	
       	//获取一级菜单节点
       	var firstNode = $("#tree").tree("getParent", secondNode.target);
       	if(null!=firstNode){
       		if (first_stage_array != ''){
           		first_stage_array += ',';
               }
           	//获取一级菜单ID
           	first_stage_array += firstNode.id
       	}
       }
   }
   
   permission_array=first_stage_array+","+second_stage_array+","+third_stage_array;
   $("#permission_array").val(permission_array);
   var rid = $("#rid").val();
	var insId= $("#insId").val();
   $.ajax({
		url : '${ctx}/custom/addInsRolePermission',
		data:JSON.stringify(
    		{
    			"rid":rid,
    			"permission_array":permission_array
   		    }
    	),
		datatype: 'json',
	    contentType: 'application/json',
	    type : "POST",
	    cache: true,
	    async : true,
	   success: function(result){
	    	result=eval(result);
	    	var href = "${ctx}/custom/toInsRoleManagePage?id="+insId;
	    	if (result=="0000") {
	    		alert("权限分配成功！",href);
			}else if(result=="888"){
				alert("传参错误！",href);
			}else{
				alert("权限分配失败！",href);
			}
      } 
   });  
}


function goBack(){
	var insId= $("#insId").val();
	window.location.href = "${ctx}/custom/toInsRoleManagePage?id="+insId;
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
                <a href="javascript:void(0);">客户管理</a>
                <span>></span>
                <a href="${ctx}/custom/insAccountManager">账号管理</a>
                <span>></span>
                <a href="${ctx}/custom/toInsRoleManagePage?id=${insId }">角色管理</a>
                <span>></span>
                <a href="${ctx}/custom/toPermissionAssignPage?roleId=${roleId }&&insId=${insId }">分配权限</a>
            </h3>
            <div class="backtrack">
            	<a href="${ctx}/custom/toInsRoleManagePage?id=${insId }">返回</a>
            </div>
            <div class="shade main_minHeight">
            	  <input type="hidden"  id="roleId" value="${roleId}">
            	  <input type="hidden"  id="insId" value="${insId}">
            	<div class="permissions">
                    <h2 class="permissions_title">机构信息</h2>
                    <div class="permissions_title1 clear">
                        <h3 class="fl">
                            <span>机构名称</span>
                            <strong>${institution.name }</strong>
                        </h3>
                        <h3 class="fl">
                            <span>办公电话</span>
                            <strong>${institution.officePhoneAreacode }-${institution.officePhone }</strong>
                        </h3>
                        <h3 class="fl">
                            <span>
                            	<c:choose>  
								  <c:when test="${institution.organizationCodeFlag == '1' }">
								  	统一社会信用代码
								  </c:when> 
								  <c:when test="${institution.organizationCodeFlag == '2' }">
								  	组织机构代码
								  </c:when> 
								  <c:when test="${institution.organizationCodeFlag == '3' }">
								  	事证号
								  </c:when>  
								  <c:when test="${institution.organizationCodeFlag == '4' }">
								  	其他
								  </c:when> 
								  <c:otherwise>
								  	其他
								  </c:otherwise>  
								</c:choose> 
                            </span>
                            <strong>${institution.organizationCode }</strong>
                        </h3>
                    </div>
                    <h2 class="permissions_title">角色信息</h2>
                    <div class="permissions_title1 clear">
                        <h3 class="fl">
                            <span>角色名称</span>
                            <strong>${roleFg.name }</strong>
                        </h3>
                        <h3 class="fl intro">
                            <span>角色简介</span>
                            <strong title="${roleFg.description }">
                            	<c:choose>  
								  <c:when test="${ not empty roleFg && fn:length(roleFg.description)>'24' }">
								  	${fn:substring(roleFg.description, 0, 24)}...
								  </c:when>
								  <c:otherwise>
								  	${roleFg.description }
								  </c:otherwise>   
								</c:choose> 
                            </strong>
                        </h3>
                    </div>
                    <div class="easyui_parent">
                        <ul class="easyui-tree" data-options="checkbox:'true',animate:'true'" id="tree">   
                              
                        </ul>
                    </div>
                    <div class="main_btn main_btn1 main_btn2">
                        <a href="javaScript:savePermission();" class="fl">保存</a>
                        <a href="${ctx}/custom/toInsRoleManagePage?id=${insId }" class="fr">取消</a>
                    </div>
                    <form action="" id="form1" method="get">
					 	<input type="hidden" name="rid" id="rid" value="${roleId }"/>
					 	<input type="hidden" id="permission_array" name="permission_array"/>
					</form>		
                </div>
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