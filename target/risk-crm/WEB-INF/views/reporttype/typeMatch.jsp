<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../commons/taglibs.jsp"%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<title>业务管理-报表类型管理-行业设置</title>
	<link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
	<style>
		.module{
			padding: 30px 0 0 26px;
		}
		.module_head{
			line-height: 36px;
			font-size: 16px;
			font-weight: bold;
		}
		.base_info{
			font-size: 14px;
			line-height: 34px;
			height: 34px;
			padding-bottom: 24px;
		}
		.base_info span{margin-left: 10px;}
		.wx_btn{
			display: inline-block;
			width: 100px;
			height: 30px;
			line-height: 30px;
			background: #38abf8;
			border-radius: 3px;
			font-size: 12px;
			color: #fff;
			text-align: center;
			cursor: pointer;
		}
		.add_trade{margin: 10px 0;}
		.trade_list{margin-bottom: 20px;}
		.trade_list td{
			padding: 5px 60px 5px 0;
			font-size: 14px;
		}
		.del_trade{
			display: inline-block;
			width: 14px;
			height: 14px;
			background: url(${ctx}/resources/image/x.png) no-repeat;
			cursor: pointer;
		}
		.cancel_pop{
			background-color: rgb(235, 239, 244);
			color: #646876;
		}
	</style>
<script type="text/javascript">
    var pageSize = 5;
	var checkedMap ={

    };
	var chooiceval={

	};
    //添加行业guan关联
    function addIndustry() {
        findmodel(1);
        fnPopup('#popup')
    }

    //
	function search() {
        findmodel(1);
    }

    //
    function jumpToPage(pageNo) {
        $("#industryContent").html("");
        findmodel(pageNo);
    }

    //查modelajax
    function findmodel(pageNo) {
        $.ajax({
            url:"${ctx}/rtype/modelIndustryList",
            type:"POST",
			async:false,
            dataType:"json",
            data:{"pageNo":pageNo, "pageSize":pageSize, "name":$("#name").val()},
            success:function (data) {
                if (100 == data.code) {
                    var page = data.page;
                    var rows = page.rows;
                    var pageNo = page.pageNo;
                    var pageSize = page.pageSize;
                    var totalPage = page.totalPage;

                    initCheckedMap();
                    $("#industryContent").html("");
                    for (var i=0; i<rows.length; i++) {
                        var row = rows[i];
                        var id2 = row.id2;
						var tr = "<tr>"+
							"<td>";
						if (checkedMap[id2]||row.modelName!=null) {
                            tr += '<a href="javaScript:;" data-id="1"  class="curr" onclick="$(this).off(\'click\')"></a>';
						} else {
                            tr += '<a href="javaScript:;" data-id="0" onclick="chooice(this)"></a>';
						}

                        tr += "<span>"+(i+1+(pageNo-1)*pageSize*2)+"</span>"+
								"<input type='hidden' value='"+id2+"' />"+
							"</td>"+
							"<td>"+row.name1+"</td>"+
							"<td>"+row.name2+"</td>"+
							"<td>"+(row.modelName==null? '': row.modelName)+"</td>";
						tr += "</td>"+
							"</tr>";
						$("#industryContent").append(tr);
                    }

                    if (0 ==rows.length) {
                        $("#industryContent").append('<tr><td colspan="7">暂无数据</td></tr>');
                    }
                    var pageStr = creatPage(page.total, pageNo, totalPage);
                    $("#pageDiv").html(pageStr);
                    fnCheckBox('.mateInfo_table a')
                } else {
                    alert("加载失败！")
                }
            }
        });
    }
    function chooice(a) {
       // alert("aa::::"+eval("9-9+5*3"));
        var id= $(a).next().next().val();
        if ($(a).attr("data-id")=='0'){
            chooiceval[id]=true;
        }else{
            chooiceval[id]=false;
		}
    }
    function checkChooice() {
        $.each(chooiceval,function(name,value) {
           // alert(name+"  "+value);
        });
    }
    
    //增加选中
	function addSelectInd() {
        var div = "";
       // checkChooice();
        $(".mateInfo_table a[class='curr']").each(function (i, ele) {
            var ind1Name = $(ele).parent().next().text();
            var ind2Name = $(ele).parent().next().next().text();
            var ind2Id = $(ele).next().next().val();
            var flag = false;

           /* $(".mateInfo .clear").each(function () {
				var ind2IdAdded = $(this).find("input[name='industryId']").val();
				if (ind2IdAdded==ind2Id ) {
					flag = true;
					return false;
				}
            });
            if (flag) {return true}*/
          // alert("id:::"+ind2Id);
          // alert("id:::"+ind2Id+"    val::"+chooiceval[ind2Id]);
			if (chooiceval[ind2Id]==null||!chooiceval[ind2Id]) return true;
            div =' <tr>'+
                '<td><b>行业</b></td>'+
            	'<td>'+ind1Name+" - "+ind2Name+'</td>'+
           		 '<td><input type="hidden" name="industryId" value="'+ind2Id+'" /><span  class="del_trade" onClick="del(this);"></span></td>'+
            	'   </tr>';

			/*div = "<div class='clear'>" +
                "<div class='info_box info_box1 info_box2 fl'>" +
					"<strong>行业</strong>" +
					"<p>"+ind1Name+" - "+ind2Name+"</p>" +
					"<input type='hidden' name='industryId' value='"+ind2Id+"' />" +
                "</div>" +
                "<div class='info_box info_box1 info_box2 fl'>" +
                "<a href='javaScript:;' class='grade_btn fr' onClick='del(this);'>删除</a>" +
                "</div>" +
                "</div>";*/
            $("#modelContent").append(div);
            initCheckedMap();
            $(".curr").off("click");
        });
        if ("" != div) {
            chooiceval={};
			alert("添加成功");
		}
    }

    function fnDelete(obj){
        $(obj).parent().parent().remove();
    }

    var delId = [];
    //删除选中
	function del(ele, delIndId) {
        fnDelete($(ele));
        delId.push(delIndId);
        initCheckedMap()
    }

    var confirmFlag = false;
    //保存匹配
	function saveMatch() {
    	if(null!=getIds()&&""!=getIds()){
    		if (confirmFlag){return;}
    	    confirmFlag = true;
    		$.ajax({
    			url:"${ctx}/rtype/typeMatchSave",
    			type:"POST",
    			dataType:"json",
    			data:{
    				ids:getIds(),
    				typeId:'${typeId}'
    			},
    			success:function (data) {
                    if (100 == data.code) {
                        alert("保存成功", "${ctx}/rtype/index");

    				} else {
                        alert(data.msg);
                        confirmFlag = false;
                        delId.splice(0, delId.length);
    				}
    			}
    		});
    	}else{
    		 alert("未添加行业，请添加后再点击确定");
    	}
	}

	//
	function initCheckedMap() {
        checkedMap={};
		var indArr = $("input[name='industryId']");
		for (var i=0; i<indArr.length; i++) {
		    var id = indArr.eq(i).val();
		    checkedMap[id] = true;
		}
    }
	function getIds() {
        var ids='';
		var indArr = $("input[name='industryId']");
		for (var i=0; i<indArr.length; i++) {
		    var id = indArr.eq(i).val();
		  	ids+=id+',';
		}
// 		alert("ids:::::"+ids);
		return ids;
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

		<div class="right_content">
			<h3 class="place_title">
				<span>当前位置：</span>
				<a href="javascript:void(0);">业务管理</a>
				<span>></span>
				<a href="${ctx}/rtype/index">报表类型管理</a>
				<span>></span>
				<a href="javascript:void(0);">行业设置</a>
			</h3>
			<div class="backtrack">
				<a href="${ctx}/rtype/index">返回</a>
			</div>
			<div class="shade main_minHeight">
				<div class="module">
					<h4 class="module_head">基本信息</h4>
					<p class="base_info"><b>报表类型名称</b><span>${typeName}</span></p>

					<h4 class="module_head">对应行业</h4>
					<p><span class="wx_btn add_trade" onClick="addIndustry();">添加行业</span></p>
					<input type="hidden" name="id" value="${model.id}" />
					<input type="hidden" name="delId" id="delId" />
					<input type="hidden" name="batchFlag" value="false" />
					<form id="frm">
					<table class="trade_list" id="modelContent">
                      <c:forEach items="${list}" var="str">
						<tr>
							<td><b>行业</b></td>
							<td>${str.pname}-${str.name}</td>
							<td>
								<input type='hidden' name='industryId' value='${str.industry_id}' />
								<span  class="del_trade" onClick="del(this, ${str.industry_id}, ${str.industry_id});"></span>
							</td>
						</tr>
					  </c:forEach>
					</table>
					</form>
					<div>
						<span  class="wx_btn" href="javascript:;" onclick="saveMatch()">确定</span>
						<a class="wx_btn" href="${ctx}/rtype/index">取消</a>
					</div>
				</div>
			</div>


		<%--<!-- 右侧内容.html start -->
		<div class="right_content">
			<h3 class="place_title">
				<span>当前位置：</span>
				<a href="javascript:void(0);">业务管理</a>
				<span>></span>
				<a href="${ctx}/rtype/index">报表类型管理</a>
				<span>></span>
				<a href="javascript:void(0);">行业设置</a>
			</h3>
			&lt;%&ndash;<div class="backtrack">
				<a href="javascript:history.go(-1);">返回</a>
			</div>&ndash;%&gt;
			<div class="search_box clear">
				<div class="checkData_box addCard clear">
					<strong>报表类型名称</strong>
					<span>${typeName}</span>
				</div>
			</div>
			<p class="clear mateInfo_btn">
				<a href="javaScript:;" class="grade_btn fl" onClick="addIndustry();">添加行业</a>
			</p>
			<form id="frm">
				<div class="mateInfo" id="modelContent">
					<input type="hidden" name="id" value="${model.id}" />
					<input type="hidden" name="delId" id="delId" />
&lt;%&ndash;
					<input type="hidden" name="delType" id="delType" />
&ndash;%&gt;
					<input type="hidden" name="batchFlag" value="false" />
					<c:forEach items="${list}" var="str">
						<div class="clear">
							<div class="info_box info_box1 info_box2 fl">
								<strong>行业</strong>
								<p>${str.name}</p>
								<input type='hidden' name='industryId' value='${str.industry_id}' />
							</div>
							<div class="info_box info_box1 info_box2 fl">
								<a href="javaScript:;" class="grade_btn fr" onClick="del(this, ${str.industry_id}, ${str.industry_id});">删除</a>
							</div>
						</div>
					</c:forEach>
				</div>
			</form>
			<div class="main_btn main_btn1">
				<a href="javaScript:;" class="fl" onclick="saveMatch();">保存</a>
				<a href="javascript:history.go(-1);" class="fr">取消</a>
			</div>--%>

			 <%--弹出div--%>
			 <%--<div id="pop" class="pop">
				 一级或二级行业名：<input type="text" name="name" id="name" /><input type="button" value="查询" onclick="search();" />
				 <table id="" class="tb">
					 <tr>
						 <th>序号</th>
						 <th>一级行业</th>
						 <th>二级行业</th>
						 <th>规模</th>
					 </tr>
					 <tbody id="industryContent"></tbody>
				 </table>
				 <input type="button" value="确定" onclick="$('#pop').css('display', 'none');" />
			 </div>--%>
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
<!-- 添加单个评分卡匹配 start -->
<div class="popup popup4" id="popup">
	<a href="javaScript:;" class="close"></a>
	<div class="clear popup_btn_m">
		<a href="javaScript:;" class="grade_btn fl" onclick="addSelectInd();">添加当前页</a>
		<a href="javaScript:;" class="grade_btn fl" onclick="fnShutDown($('#popup').get(0))">取消</a>
	</div>
	<div class="module_height">
		<form>
			<table class="module_table mateInfo_table">
				<thead>
				<tr>
					<th class="module_width8">序号</th>
					<th>一级行业</th>
					<th>二级行业</th>
					<th>已匹配类型</th>
				</tr>
				</thead>
				<tbody  id="industryContent"></tbody>
			</table>
		</form>
	</div>
	<!-- 分页.html start -->
	<%@include file="../commons/tabPage.jsp" %>
	<!-- 分页.html end -->
</div>
<!-- 添加单个评分卡匹配 end -->
</body>
</html>
