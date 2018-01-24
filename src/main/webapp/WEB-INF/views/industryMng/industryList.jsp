<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>行业管理 </title>
<script src="${ctx}/resources/js/jquery-1.8.3.min.js"></script>
<!--      谷歌记录历史输入选中，输入框黄色修改  -->
<style type="text/css">
  	input:-webkit-autofill {
	-webkit-box-shadow: 0 0 0px 1000px white inset !important;  
 	-webkit-text-fill-color: #333;
	}
	.tb{
		/* width: 90%; */
		/* margin: 20px 100px; */
	}
	.tb tr{
		line-height: 30px;
	}
	.tb tr td{
		width: 100px;;
		heigth: 30px;
		text-align: center;
	}
</style>
<script type="text/javascript">
	var pageNo = 1;
	var pageSize = 10;

	$(function () {
        //调用前台行业
        preIndustry();
        
        //前台行业修改
		$(".preIndustryCla").on("click",'.update_btn',function(){
			
			var trData = $(this).parent().parent();
			var id = trData.find("td:eq(1)").html();
			
			window.location.href = "${ctx}/industry/update?id="+id;
		});
        
		//行业匹配
		$(".preIndustryCla").on("click",'.match_btn',function(){
			
			var trData = $(this).parent().parent();
			var id = trData.find("td:eq(1)").html();
			
			window.location.href = "${ctx}/industry/match?id="+id;
		});
		
		//前台行业删除
		$(".preIndustryCla").on("click",'.delete_btn',function(){
			var flag = confirm("确定删除该行业信息吗?");
			
			if(flag == true){
				var trData = $(this).parent().parent();
				var id = trData.find("td:eq(1)").html();
				
				$.ajax({
					 url:"${ctx}/industry/delete?id="+id,
					 type:'POST',
					 success:function(data){
						 if(data){
							 if(data.result == 1){
								 alert("删除成功！");
								 
								 window.location.href = "${ctx}/industry/list";
							 }else{
								 alert("删除失败！");
								 
								 window.location.href = "${ctx}/industry/list";
							 }
						 }
					 }
				});
			}
		});
		
		//后台行业修改
		$(".backIndustryCla").on("click",'.back_update_btn',function(){
			
			var trData = $(this).parent().parent();
			var id = trData.find("td:eq(1)").html();
			
			window.location.href = "${ctx}/industry/backUpdate?id="+id;
		});
        
		//后台行业删除
		$(".backIndustryCla").on("click",'.back_delete_btn',function(){
			var flag = confirm("确定删除该行业信息吗?");
			
			if(flag == true){
				var trData = $(this).parent().parent();
				var id = trData.find("td:eq(1)").html();
				
				$.ajax({
					 url:"${ctx}/industry/backDelete?id="+id,
					 type:'POST',
					 success:function(data){
						 if(data){
							 if(data.result == 1){
								 alert("删除成功！");
								 
								 window.location.href = "${ctx}/industry/list";
							 }else{
								 alert("删除失败！");
								 
								 window.location.href = "${ctx}/industry/list";
							 }
						 }
					 }
				});
			}
		});
		
    });

    //初始化前台行业
    function preIndustry() {
    	//展示前台行业
		$("#preIndustryDiv").show();
		//隐藏后台行业
		$("#backIndustryDiv").hide();
    	
        findIndustry(pageNo);
    }
    
    //上一页
	function previousPage() {
		findIndustry(--pageNo);
    }
    
    //下一页
	function nextPage() {
        findIndustry(++pageNo);
    }

	//查行业ajax
	function findIndustry(pageNo) {
		var searchContent = $("#searchContent").val();
		
        $.ajax({
            url:"${ctx}/industry/findAllIndustry",
            type:"POST",
            dataType:"json",
            data:{"pageNo":pageNo, "pageSize":pageSize, "searchContent":searchContent},
            success:function (data) {
				if (200 == data.code) {
					var page = data.page;
					var totalPage = page.totalPage;
					var data = page.rows;

					//clear
					$("#industryConent").html("")
					
					var htmlContent = "";
                    
					for (var i=0; i<data.length; i++) {
						htmlContent += "<tr>";
				           htmlContent += "<td class='audit_td1'>"+(parseInt(i)+1)+"</td>";
				           htmlContent += "<td style='display:none'>"+data[i].id+"</td>";
				           htmlContent += "<td>"+data[i].code1+"</td>";
				           htmlContent += "<td>"+data[i].name1+"</td>";
				           htmlContent += "<td>"+data[i].code2+"</td>";
				           htmlContent += "<td>"+data[i].name2+"</td>";
				           
				           if (data[i].backName == null || data[i].backName == "") {
				        	   htmlContent += "<td>无</td>";
						   } else {
							   htmlContent += "<td>"+data[i].backName+"</td>";
						   }
				           
				           htmlContent += "<td>"+data[i].creatorName+"</td>";
				           htmlContent += "<td>"+data[i].createDate+"</td>";
				   	   	   htmlContent += "<td class='audit_td3 audit_td4'>";
				   	   	   		htmlContent += "<a style='margin-right:10px;' class='update_btn' href='javascript:;'>修改</a>";
				   	       		htmlContent += "<a style='margin-right:10px;' class='match_btn' href='javascript:;'>行业匹配</a>";
				   	       	    htmlContent += "<a class='delete_btn' href='javascript:;'>删除</a>";
				       	   htmlContent += "</td>";
				       htmlContent += "</tr>";
                    }
					
					$("#industryConent").html(htmlContent);

				} else {
				    alert("加载失败！")
				}
            }
        });
    }
	
	//查询
	function search(){
		findIndustry(pageNo);
	}
	
	//初始化后台行业
    function backIndustry() {
    	//隐藏前台行业
		$("#preIndustryDiv").hide();
		//展示后台行业
		$("#backIndustryDiv").show();
		
        findBackIndustry(pageNo);
    }
    
    //后台上一页
	function previousPageBack() {
		findBackIndustry(--pageNo);
    }
    
    //后台上一页
	function nextPageBack() {
		findBackIndustry(++pageNo);
    }

	//查行业ajax
	function findBackIndustry(pageNo) {
        $.ajax({
            url:"${ctx}/industry/findAllBackIndustry",
            type:"POST",
            dataType:"json",
            data:{"pageNo":pageNo, "pageSize":pageSize},
            success:function (data) {
				if (200 == data.code) {
					var page = data.page;
					var totalPage = page.totalPage;
					var data = page.rows;

					//clear
					$("#backIndustryConent").html("")
					
					var htmlContent = "";
                    
					for (var i=0; i<data.length; i++) {
						htmlContent += "<tr>";
				           htmlContent += "<td class='audit_td1'>"+(parseInt(i)+1)+"</td>";
				           htmlContent += "<td style='display:none'>"+data[i].id+"</td>";
				           htmlContent += "<td>"+data[i].code+"</td>";
				           htmlContent += "<td>"+data[i].name+"</td>";
				           htmlContent += "<td>"+data[i].creator+"</td>";
				           htmlContent += "<td>"+data[i].cerateTime+"</td>";
				           
				   	   	   htmlContent += "<td class='audit_td3 audit_td4'>";
				   	   	   		htmlContent += "<a style='margin-right:10px;' class='back_update_btn' href='javascript:;'>修改</a>";
				   	       	    htmlContent += "<a class='back_delete_btn' href='javascript:;'>删除</a>";
				       	   htmlContent += "</td>";
				       htmlContent += "</tr>";
                    }
					
					$("#backIndustryConent").html(htmlContent);

				} else {
				    alert("加载失败！")
				}
            }
        });
    }
    
    //新建行业
	function add() {
		window.location.href = "${ctx}/industry/add";
    }
	
	//新建后台行业
	function backAdd() {
		window.location.href = "${ctx}/industry/backAdd";
    }
	
	//批量行业匹配
	function batchMatch() {
		window.location.href = "${ctx}/industry/batchMatch";
    }
	
</script>
</head>
<body>
<div class="row">
     <div class="col-sm-12">
         <div class="ibox float-e-margins">
             <div class="ibox-title">
                 <h5>字典管理<span>／</span>行业管理</h5>
             </div>
             <div class="ibox-content" style="margin-left: 40px;">
             	 <a style="margin-right: 20px;width:200px;" href="javaScript:;" onclick="preIndustry();" class="btn btn-primary" >前台行业</a>
             	 <a width="200px;" href="javaScript:;" onclick="backIndustry();" class="btn btn-primary" >后台行业</a>
             </div>
             <div>
             	<div id="preIndustryDiv" style="display: none">
					 <div>
						 <input type="text" id="searchContent" />
						 <input type="button" value="搜索" onclick="search()"/>
					 </div>
					 <div style="margin-left: 40px;margin-top: 10px" class="ibox-content">
		             	 <input type="button" onclick="add();" value="新增行业"/>
		             	 <input type="button" style="margin-left: 40px;" onclick="batchMatch();" value="批量行业匹配" />
		             </div>
		             <div>
			             <div class="preIndustryCla">
			                 <table id="" class="tb">
								 <tr>
									 <th>序号</th>
									 <th>一级行业编号</th>
									 <th style="width:160px;">一级行业名称</th>
									 <th>二级行业编号</th>
									 <th style="width:160px;">二级行业名称</th>
									 <th style="width:130px;">对应后台行业名称</th>
									 <th>创建人</th>
									 <th>创建时间</th>
									 <th style="width:150px;">操作</th>
								 </tr>
								 <tbody id="industryConent"></tbody>
							 </table>
			         	 </div>
						 <div>
							<a href="javascript:void(0);" onclick="previousPage();">上一页</a>
							<a href="javascript:void(0);" onclick="nextPage();">下一页</a>
						 </div>
					 </div>
				</div>
				<div id="backIndustryDiv" style="display: none">
					 <div style="margin-left: 40px;" class="ibox-content">
		             	 <a href="javaScript:;" onclick="backAdd();" class="btn btn-primary" ><h3><span>新增后台行业</span></h3></a>
		             </div>
					 <div>
			             <div class="backIndustryCla">
			                 <table id="" class="tb">
								 <tr>
									 <th>序号</th>
									 <th>行业编号</th>
									 <th style="width:160px;">行业名称</th>
									 <th>创建人</th>
									 <th>创建时间</th>
									 <th style="width:150px;">操作</th>
								 </tr>
								 <tbody id="backIndustryConent"></tbody>
							 </table>
			         	 </div>
						 <div>
							<a href="javascript:void(0);" onclick="previousPageBack();">上一页</a>
							<a href="javascript:void(0);" onclick="nextPageBack();">下一页</a>
						 </div>
					 </div>
				 </div>
			 </div>
         </div>
     </div>
</div>
</body>
</html>
