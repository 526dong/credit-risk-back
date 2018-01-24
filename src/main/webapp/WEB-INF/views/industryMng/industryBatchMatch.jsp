<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>行业管理 </title>
<script src="${ctx}/resources/js/jquery-1.8.3.min.js"></script>

<!-- 自动补全 -->
<script src="${ctx}/resources/js/autocomplete/jquery.autocomplete.js"></script>
<script src="${ctx}/resources/js/autocomplete/browser.js"></script>

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

	//自动补全后台行业初始化
	var backIndustryListString = eval('${backIndustryListString}');
	
	$(function () {
		//加载列表
		initIndustry();
		
		$('input[name="backName"]').each(function(i,ele){
			$(ele).autocomplete(backIndustryListString, {
		    	minChars: 1,
		    	max: 999999999,    //列表里的条目数
		        minChars: 0,    //自动完成激活之前填入的最小字符
		        width: 200,     //提示的宽度，溢出隐藏
		        scrollHeight: 400,   //提示的高度，溢出显示滚动条
		        matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
		        autoFill: false,    //自动填充
		        showNoSuggestionNotice: true,
		        noSuggestionNotice: 'Sorry, no matching results',
		        formatItem: function(row) {
		            return  row.name ;
		        },
		    	formatResult : function(row) { //定义最终返回的数据
		        	return row.name;
		    	} 
		    }).result(function(event, row, formatted) {
		       //ele.previousElementsibling.value = row.id;
		       //var aa = ele.previoussibling;
		       $(ele).parent().find("input").eq(0).val(row.id);
		    });
		});
    });
	
	//初始化行业
    function initIndustry() {
        findIndustry(pageNo);
    }
    
    //
	function previousPage() {
		findIndustry(--pageNo);
    }
    
    //
	function nextPage() {
        findIndustry(++pageNo);
    }

	//查行业ajax
	function findIndustry(pageNo) {
        $.ajax({
            url:"${ctx}/industry/findAllIndustry",
            type:"POST",
            dataType:"json",
            data:{"pageNo":pageNo, "pageSize":pageSize},
            async:false,
            success:function (data) {
				if (200 == data.code) {
					var page = data.page;
					var totalPage = page.totalPage;
					var data = page.rows;

					//clear
					$("#industryConent").html("")
					
					var htmlContent = "";
                    debugger;
					for (var i=0; i<data.length; i++) {
						htmlContent += "<tr>";
				           htmlContent += "<td class='audit_td1'>"+(parseInt(i)+1)+"</td>";
				           htmlContent += "<td style='display:none'><input type='hidden' name='id' value='"+data[i].id+"'></td>";
				           htmlContent += "<td>"+data[i].name1+"</td>";
				           htmlContent += "<td>"+data[i].name2+"</td>";
				           
				           //对应后台名称
				           if (data[i].backName == null || data[i].backName == "") {
				        	   htmlContent += "<td><input type='hidden' name='backId' value=''><input name='backName' value=''></td>";
						   } else {
							   htmlContent += "<td><input type='hidden' name='backId' value='"+data[i].backId+"'><input name='backName' value='"+data[i].backName+"'></td>";
						   }
				       htmlContent += "</tr>";
                    }
					
					$("#industryConent").html(htmlContent);

				} else {
				    alert("加载失败！")
				}
            }
        });
    }
	
	//保存
	function save(){
		var ids = [];
		var backIds = [];
		
		$('input[name="id"]').each(function(i,obj){
			ids.push(obj.value);
		});
		
		$('input[name="backId"]').each(function(i,obj){
			if (obj.value == null || obj.value == "") {
				backIds.push(-1);
			} else {
				backIds.push(obj.value);
			}
		});
		
		$.ajax({
            url:"${ctx}/industry/doBatchMatch",
            type:"POST",
            dataType:"json",
            data:{"ids":ids.join(","), "backIds":backIds.join(",")},
            async:false,
            success:function (data) {
            	if (data.result == 1) {
            		alert("保存成功");
            		
            		window.location.href="${ctx}/industry/list";
            	} else {
            		alert("保存失败");
            		
            		window.location.href="${ctx}/industry/list";
            	}
            }
		});
	}
	
	//取消，返回上一级
	function cancel(){
		history.back(-1);
	}
</script>
</head>
<body>
<div class="row">
     <div class="col-sm-12">
         <div class="ibox float-e-margins">
             <div class="ibox-title">
                 <h5>行业管理<span>／</span>批量行业匹配</h5>
             </div>
             <!-- <div class="ibox-content" style="margin-left: 40px;">
             	 <a style="margin-right: 5px;width:40px;" href="javaScript:;" >使用环境</a>
             	 <a style="margin-right: 5px;width:500px;" href="javaScript:;" >前台</a>
             	 <a width="500px;" href="javaScript:;" >后台</a>
             </div> -->
             <div>
             	 <form action="" method="post">
		             <div class="preIndustryCla">
		                 <table id="" class="tb">
							 <tr>
								 <th>序号</th>
								 <th style="width:160px;">一级行业名称</th>
								 <th style="width:160px;">二级行业名称</th>
								 <th style="width:130px;">对应行业名称</th>
							 </tr>
							 <tbody id="industryConent"></tbody>
						 </table>
		         	 </div>
		         	 
		         	 <div>
						<a href="javascript:void(0);" onclick="previousPage();">上一页</a>
						<a href="javascript:void(0);" onclick="nextPage();">下一页</a>
					 </div>
		         	 
		         	 <div style="margin-top: 50px;margin-left: 250px;">
						<a style="margin-right: 20px;" href="javascript:void(0);" onclick="save();">保存</a>
						<a href="javascript:void(0);" onclick="cancel();">取消</a>
					 </div>
				 </form>
			 </div>
         </div>
     </div>
</div>
</body>
</html>
