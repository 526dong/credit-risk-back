<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>评级结果数据管理 </title>
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
        //调用评级结果数据
        initRateResult();
        
        //评级结果数据修改
		$(".rateResultCla").on("click",'.update_btn',function(){
			
			var trData = $(this).parent().parent();
			var id = trData.find("td:eq(1)").html();
			
			window.location.href = "${ctx}/dictionary/saveOrUpdateRateResult?id="+id;
		});
        
		//评级结果数据删除
		$(".rateResultCla").on("click",'.delete_btn',function(){
			var flag = confirm("确定删除该评级结果信息吗?");
			
			if(flag == true){
				var trData = $(this).parent().parent();
				var id = trData.find("td:eq(1)").html();
				
				$.ajax({
					 url:"${ctx}/dictionary/deleteRateResult?id="+id,
					 type:'POST',
					 success:function(data){
						 if(data){
							 if(data.code == 200){
								 alert("删除成功！");
								 
								 window.location.href = "${ctx}/dictionary/rateResult";
							 }else{
								 alert("删除失败！");
								 
								 window.location.href = "${ctx}/dictionary/rateResult";
							 }
						 }
					 }
				});
			}
		});
	});
	
    //初始化评级结果数据
    function initRateResult() {
    	findRateResult(pageNo);
    }
    
    //上一页
	function prePage() {
		findRateResult(--pageNo);
    }
    
    //下一页
	function nextPage() {
		findRateResult(++pageNo);
    }

	//查评级结果ajax
	function findRateResult(pageNo) {
        $.ajax({
            url:"${ctx}/dictionary/findAllRateResult",
            type:"POST",
            dataType:"json",
            data:{
            	"pageNo":pageNo, 
            	"pageSize":pageSize
            },
            success:function (data) {
				if (200 == data.code) {
					var page = data.page;
					var totalPage = page.totalPage;
					var data = page.rows;

					//clear
					$("#rateResultConent").html("");
					
					var htmlContent = "";
                    
					for (var i=0; i<data.length; i++) {
						htmlContent += "<tr>";
				           htmlContent += "<td class='audit_td1'>"+(parseInt(i)+1)+"</td>";
				           htmlContent += "<td style='display:none'>"+data[i].id+"</td>";
				           htmlContent += "<td>"+data[i].name+"</td>";
				           htmlContent += "<td>"+data[i].creatorName+"</td>";
				           htmlContent += "<td>"+data[i].createTime+"</td>";
				   	   	   htmlContent += "<td class='audit_td3 audit_td4'>";
				   	   	   		htmlContent += "<a style='margin-right:10px;' class='update_btn' href='javascript:;'>修改</a>";
				   	       	    htmlContent += "<a class='delete_btn' href='javascript:;'>删除</a>";
				       	   htmlContent += "</td>";
				       htmlContent += "</tr>";
                    }
					
					$("#rateResultConent").html(htmlContent);

				} else {
				    alert("加载失败！");
				}
            }
        });
    }
	
	//查询
	function search(){
		findRateResult(pageNo);
	}
	
    //新建评级结果
	function add() {
		window.location.href = "${ctx}/dictionary/saveOrUpdateRateResult";
    }
    
</script>
</head>
<body>
<div class="row">
     <div class="col-sm-12">
         <div class="ibox float-e-margins">
             <div class="ibox-title">
                 <h5>字典管理<span>／</span>评级结果列表</h5>
             </div>
             <div>
             	<div>
					 <div style="margin-left: 40px;margin-top: 10px" class="ibox-content">
		             	 <input type="button" onclick="add();" value="新增评级结果"/>
		             </div>
		             <div>
			             <div class="rateResultCla">
			                 <table id="" class="tb">
								 <tr>
									 <th>序号</th>
									 <th>结果名称</th>
									 <th>创建人</th>
									 <th>创建时间</th>
									 <th style="width:150px;">操作</th>
								 </tr>
								 <tbody id="rateResultConent"></tbody>
							 </table>
			         	 </div>
						 <div>
							<a href="javascript:void(0);" onclick="prePage();">上一页</a>
							<a href="javascript:void(0);" onclick="nextPage();">下一页</a>
						 </div>
					 </div>
				</div>
			 </div>
         </div>
     </div>
</div>
</body>
</html>
