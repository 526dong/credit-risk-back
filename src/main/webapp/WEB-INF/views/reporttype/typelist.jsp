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
    var href = "${ctx}/rtype/index";
	$(function () {
        //评级数据列表
        getData(1);
	});
	//查评级ajax
	function getData(pageNo) {
        $.ajax({
            url:"${ctx}/rtype/findAll",
            type:"POST",
            dataType:"json",
            data:{
            	"pageNo": pageNo, 
            	"pageSize": pageSize
            },
            success:function (data) {
				if (200 == data.code) {
					var page = data.page;
					var data = page.rows;
					//clear
					$("#rateDataConent").html("");
					var htmlContent = "";
					for (var i=0; i<data.length; i++) {
						htmlContent += "<tr>";
							htmlContent += "<td>"+(parseInt(i)+1)+"</td>";
                        	htmlContent += "<td title='"+data[i].name+"'>"+ data[i].name+"</td>";
                        	htmlContent += "<td>"+ (data[i].ifcheck == 0 ? '未配置' : '已配置') +"</td>";
                            htmlContent += "<td title='"+(data[i].tnames == null ? '' : data[i].tnames)+"'>"+ (data[i].tnames == null ? '' : data[i].tnames) +"</td>";
                            htmlContent += "<td>"+ (data[i].status == 0 ? '可用' :data[i].status == 1 ? '历史版本':data[i].status == 2 ?'新添加':'禁用') +"</td>";
                            htmlContent += "<td>"+ data[i].cname+"</td>";
                            htmlContent += "<td>"+ (data[i].ctimeStr==null ? '':data[i].ctimeStr)+"</td>";
				   	   	   	htmlContent += "<td class='operate'>";
				   	   	   		htmlContent += " <shiro:hasPermission name='/rtype/detail'><a  onclick='detail("+data[i].id+");' href='javascript:;'>查看</a></shiro:hasPermission>";
				   	   	   		if (data[i].status!=0){
                                    htmlContent += " <shiro:hasPermission name='/rtype/toUpdate'><a  onclick='update("+data[i].id+");' href='javascript:;'>修改</a></shiro:hasPermission>";
                                    htmlContent += " <shiro:hasPermission name='/rtype/setRule'><a  onclick='setrules("+data[i].id+");' href='javascript:;'>设置规则</a></shiro:hasPermission>";
                                    htmlContent += "<shiro:hasPermission name='/rtype/setind'><a  onclick='setind("+data[i].id+")'; href='javascript:;'>匹配行业</a></shiro:hasPermission>";
                                    if(data[i].tnames != null){
                                    	htmlContent += " <shiro:hasPermission name='/rtype/status'><a  onclick='updateStatus("+data[i].id+",0);' href='javascript:;'>启用</a></shiro:hasPermission>";
                                    }
                                    htmlContent += " <shiro:hasPermission name='/rtype/status'><a  onclick='updateStatus("+data[i].id+",4);' href='javascript:;'>删除</a></shiro:hasPermission>";
                                    htmlContent += "</td>";
                                }
                        htmlContent += " <shiro:hasPermission name='/rtype/status'><a  onclick='updateStatus("+data[i].id+",3);' href='javascript:;'>禁用</a></shiro:hasPermission>";
                        htmlContent += "</tr>";
                    }
					
					$("#rateDataConent").html(htmlContent);

                    //page
                    var pageStr = creatPage(page.total, page.pageNo, page.totalPage);
                    $("#pageDiv").html(pageStr);

				} else {
				    alert("加载失败！",href);
				}
            }
        });
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
    //类型修改
    function update(id) {
        window.location.href = "${ctx}/rtype/toUpdate?typeId="+id;
    }
    //匹配行业
    function setind(id) {
        window.location.href = "${ctx}/rtype/setind?typeId="+id;
    }
   //查看详情
    function detail(id) {
        window.location.href = "${ctx}/rtype/detail?typeId="+id;
    }
    //类型删除
    function updateStatus(id,status) {
	    var msg=status==0?'启用':(status==3?'禁用':'删除');
	    var code=status==0?'on':(status==3?'off':'del');
        confirm("确定"+msg+"该类型吗?", function(){
            $.ajax({
                url:"${ctx}/rtype/status/"+code,
                type:'POST',
				data:{
					typeId:id
				},
                success:function(data){
                    if(data){
                        if(data.code == 100){
                            alert(msg+"成功！",href);
                        }else{
                            alert(data.msg,href);
                        }
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
    /*//新建评级
	function add() {
		window.location.href = "${ctx}/rateData/add";
    }
	//上传/导入评级
	function importExcel() {
		window.location.href = "${ctx}/rateData/importRateDataExcel";
    }*/
	function setrules(id) {
        window.location.href = "${ctx}/rtype/setRule?id="+id;
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
				<a href="${ctx}/rtype/index">报表类型管理</a>
			</h3>

			<div class="shade main_minHeight">
				<div class="search_box clear">
					<div class="fr function_btn">
						<shiro:hasPermission name='/rtype/toImport'>
						<a href="${ctx}/rtype/toImport" class="fl">新增报表类型</a>
						</shiro:hasPermission>
					</div>
				</div>
				<div class="module_height">
					<form>
						<table class="module_table">
							<thead>
							<tr>
								<th class="module_width1">序号</th>
								<th>报表类型名称</th>
								<th>核验规则</th>
								<th>匹配行业</th>
								<th>状态</th>
								<th>创建人</th>
								<th>创建时间</th>
								<th class="module_width4">操作</th>
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

