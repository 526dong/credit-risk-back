$(function(){
fnMinHeight(); //页面高度设置
fnUserLogin(); //用户名下拉菜单
fnDownMenu(); //选择下拉菜单
fnPage(); //设置分页父级宽度子级居中
//复选框
fnCheckBox('.newModeling_tab a') //客服管理-账户管理-模块分配-新增模块分配
fnCheckBox('.mateInfo_table a') //业务管理-评分卡管理-添加单个评分卡匹配
//企业数据管理-企业评级管理-查看企业历史评级-查看评级详情
fnTabControl('#card_list li','#card_box .module_box');
fnTabControl('#card_btn1 li','#card_box1 .table_content_son');
//单选框
fnRadio('.popup_symbol_list li'); //指标公式库-创建指标公式-选择运算符号
//指标公式库-创建指标公式-选择财报数据
fnTabControl('#popup_symbol_dl dd','.popup_statement_son',false,true); 

})

/*
 * 覆盖confirm函数 赵天明添加
 * msg:提示信息 callback：回掉函数
 */
function confirm(msg, callback) {
    var flag = true;
    var confirmDiv = "";
    var layer = $("#layer");

    if (!layer.get(0)) {
        confirmDiv += "<div class='layer' id='layer' style='display: block;'></div>";
    } else {
        layer.css("display", "block");
    }

    confirmDiv +=
        "<div class='popup popup2' id='myconfirm' style='display: block;'>" +
        "<a href='javaScript:;' class='colse'></a>" +
        "<p>"+msg+"</p>" +
        "<div class='popup_btn'>" +
        "<a href='javaScript:;' id='myconfirmBtnOK' class='a1 fl'>确定</a>" +
        "<a href='javaScript:;' id='myconfirmBtnCancel' class='a2 fr'>取消</a>" +
        "</div>" +
        "</div>";
    $("body").append(confirmDiv);

    $("#myconfirmBtnOK").off("click");
    $(document).on("click", "#myconfirmBtnOK", function () {
        $("#myconfirm").remove();
        $("#layer").css("display", "none");
        callback();
    });
    $("#myconfirmBtnCancel").off("click");
    $(document).on("click", "#myconfirmBtnCancel", function () {
        $("#myconfirm").remove();
        $("#layer").css("display", "none");
    });
}

/*
 * 覆盖alert函数 赵天明添加
 * msg:提示信息 callback：回掉url
 */
var zIndex = 2000;
function alert(msg, callback) {
    var layerFlag = false;
    var alertDiv = "";
    var layer = $("#layer");

    if (layer.length == 0) {
        alertDiv += "<div class='layer' id='layer' style='display: block;'></div>";
    } else {
        layer.css("display", "block");
    }

    $(".popup").each(function(){
        if ('none' == $(this).css("display")) {return;}
        if (zIndex == $(this).css("z-index")) {
            layerFlag = true;
            $(this).css("z-index",0);
        }
    });

    //如果存在弹框，则直接把信息放到p标签中
    if ($("#myPopupAlert").length > 0){
        $("#myPopupAlert").find("p").text(msg);
        $("#myPopupAlert").css("z-index",zIndex);
    } else {
        alertDiv +=
            "<div class='popup popup2' id='myPopupAlert' style='display: block;'>" +
            "<a href='javaScript:;' class='colse'></a>" +
            "<div style='height:100px;'>"+
            "<p style='word-break:break-word; line-height:25px; margin:0 15px;'>"+msg+"</p>" +
            "</div>"+
            "<div class='popup_btn' style='width:75px; margin-top:50px;'>" +
            "<a href='javaScript:;' class='a1 fl'>确定</a>" +
            "</div>" +
            "</div>";
        $("body").append(alertDiv);
        $("#myPopupAlert").css("z-index",zIndex);
	}

    $("#myPopupAlert").click(function () {
        $("#myPopupAlert").remove();
        if (!layerFlag){
            $("#layer").css("display", "none");
        }
        $(".popup").each(function(){
            $(this).css("z-index",zIndex);
        });
        //
        if (callback) {
            //
            window.location.href = callback;
        }
    });
}


//用户名下拉菜单
function fnUserLogin(){
	var oBtn =document.getElementById('header_login');
	var oMenu =document.getElementById('login_list');
	var iTimer =null;
	oMenu.onmouseover = oBtn.onmouseover =function(){
		clearTimeout(iTimer)
		oMenu.style.display ='block';
	};
	oMenu.onmouseout = oBtn.onmouseout =function(){
		iTimer =setTimeout(function(){
			oMenu.style.display ='none';
		},200);
	};
}
//页面高度设置
function fnMinHeight(){
	var iH =$(document).height()-$('.header').height()-1;
	$('.leftsideBar').css('min-height',iH);
	$('.right_content').css('min-height',iH);
	$(window).on('resize',function(){
		$('.leftsideBar').css('min-height',iH);
	});
}
//选择下拉菜单
function fnDownMenu(){
	var oDiv,dis;
	$('.select_btn').on('click',function(ev){
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
		
	$(document).click(function(){
		$(oDiv).hide();
	});

}
//设置分页父级宽度子级居中
function fnPage(){
$('.page_parent').css('width',$('.right_content').width());
}
//选项卡组件
function fnTabControl(oBtn,oDiv,fn,index){
	$(oBtn).click(function(){
		if(fn){
			fn($(this).index());
		}
		$(oBtn).removeClass('active');
		$(this).addClass('active');
		$(oDiv).hide();
		if(index){
			$(oDiv).eq($(this).index()-1).show();
		}else{
			$(oDiv).eq($(this).index()).show();
		}
		
	});
	
}
//打开弹窗
function fnPopup(obj,hit){
	$('#layer').show();
	$(obj).show();
	if(hit){
		$(obj).find('p').html(hit);
	}
	$(obj).find('a:eq(0)').click(function(){
		fnShutDown(obj);
	})
	$(obj).find('.a1').click(function(){
		fnShutDown(obj);
	});
	$(obj).find('.a2').click(function(){
		fnShutDown(obj);
	});
}
//关闭弹窗
function fnShutDown(obj){
	$('#layer').hide();
	$(obj).hide();
}
//新增评分卡
function fnCreditMark(obj){

		if($(obj).attr('data-id')==0){
			$(obj).parent().parent().remove();
		}else if($(obj).attr('data-id')==1){
			/*if($('#credit_body tr').size()<20){*/
				$('#addCard_tbody').append('<tr><td>001</td><td>0</td><td>3</td><td>2</td><td>AAA</td><td class="delBor"><a href="javaScript:;" class="credit_a" data-id="0" onclick="fnCreditMark($(this))"></a></td></tr>');
				
			/*}*/
			//alert($('#credit_body tr').size())
		}
}
//复选框
function fnCheckBox(obj){

	$(obj).click(function(){
		if($(this).attr('data-id')==0){
			$(this).addClass('curr');
			$(this).attr('data-id',1);	
		}else{
			$(this).removeClass('curr');
			$(this).attr('data-id',0);	
		}
	})
	
}
//单选框
function fnRadio(obj){
	$(obj).click(function(){
		$(obj).removeClass('active');
		$(obj).attr('data-id',0);
		$(this).addClass('active');
		$(this).attr('data-id',1);
	})	
}
//指标公式库-创建指标公式-选择财报数据滚动
function fnPopupScroll(oDivPar,oDiv,oBtnPar,oBtn){
	var oListParent =document.getElementById(oDivPar);
	var oUl =document.getElementById(oDiv);
	var oScrollParent =document.getElementById(oBtnPar);
	var oScroll =document.getElementById(oBtn);
	var disY =0;
	var bBtn = true;
	
	/*oScroll.onmousedown =function(ev){
		var oEvent =ev || event;
		
		
		disY =oEvent.clientY -oScroll.offsetTop;
		
		document.onmousemove =function(ev){
			
			var oEvent =ev || event;
			
			var T =oEvent.clientY -disY;
			
			t(T);
			
			document.onmouseup =function(){
				document.onmousemove =null;
			};
		};
		
		return false;
		
	};*/

	if(oListParent.addEventListener){
		oListParent.addEventListener('DOMMouseScroll',show,false);
	}
	oListParent.onmousewheel =show;
	
	function show(ev){
		  var oEvent =ev || event;
		  var T = 0;
		  if(ev.detail){
			  bBtn = oEvent.detail>0 ? true : false;
		  }else{
			  bBtn = oEvent.wheelDelta<0 ? true : false;
		  }
		  if(bBtn){  
			  T = oScroll.offsetTop + 30;
		  }else{
			  T = oScroll.offsetTop - 30;	
		  }
		  t(T);
		  if(oEvent.preventDefault){
			  oEvent.preventDefault();
		  }else{
			  return false;
		  }
		};
		function t(T){
			
			if(T<0){
				T=0;
			}else if(T>oListParent.offsetHeight-oScroll.offsetHeight){
				
				T=oListParent.offsetHeight-oScroll.offsetHeight;
			
			}
			
			oScroll.style.top = T+'px';
			
			var scale = T/(oScrollParent.offsetHeight-oScroll.offsetHeight);
			
			oUl.style.top = -scale * (oUl.offsetHeight-oListParent.offsetHeight) + 'px';
		
		
		}
}
//格式化时间
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}


//分层管理-违约与回收
function fnScroll(oDivPar,oDiv,oBtnPar,oBtn,eve){
	var oListParent =document.getElementById(oDivPar);
	var oUl =document.getElementById(oDiv);
	var oScrollParent =document.getElementById(oBtnPar);
	var oScroll =document.getElementById(oBtn);
	var disX =0;
	var bBtn = true;
	
	oScroll.onmousedown =function(ev){
		var oEvent =ev || event;
		
		
		disX =oEvent.clientX -oScroll.offsetLeft;
		
		document.onmousemove =function(ev){
			
			var oEvent =ev || event;
			
			var T =oEvent.clientX -disX;
			
			t(T);
			
			document.onmouseup =function(){
				document.onmousemove =null;
			};
		};
		
		return false;
		
	};
	if(eve){
		if(oListParent.addEventListener){
			oListParent.addEventListener('DOMMouseScroll',show,false);
		}
		oListParent.onmousewheel =show;
	}
	function show(ev){
	
		var oEvent =ev || event;
		
		var T = 0;
		
		if(ev.detail){
			bBtn = oEvent.detail>0 ? true : false;
		}else{
			bBtn = oEvent.wheelDelta<0 ? true : false;
		}
		
		if(bBtn){  
			T = oScroll.offsetLeft + 50;
		}else{
			T = oScroll.offsetLeft - 50;	
		}
		
		t(T);
		
		if(oEvent.preventDefault){
			oEvent.preventDefault();
		}else{
			return false;
		}
	
	}
	function t(T){
	
		if(T<0){
			T=0;
		}else if(T>oListParent.offsetWidth-oScroll.offsetWidth){
			
			T=oListParent.offsetWidth-oScroll.offsetWidth;
		
		}
		
		oScroll.style.left = T+'px';
		
		var scale = T/(oScrollParent.offsetWidth-oScroll.offsetWidth);
		
		oUl.style.left = -scale * (oUl.offsetWidth-oListParent.offsetWidth) + 'px';
	
	
	}

}
//分层管理-相关性设置-横向滚动条
function fnScrollSeek(oDivPar,oDiv,oDivPar1,oDiv1,oBtnPar,oBtn){
	var oListParent =document.getElementById(oDivPar);
	var oUl =document.getElementById(oDiv);
	var oListParent1 =document.getElementById(oDivPar1);
	var oUl1 =document.getElementById(oDiv1);
	var oScrollParent =document.getElementById(oBtnPar);
	var oScroll =document.getElementById(oBtn);
	var disX =0;
	var bBtn = true;
	
	oScroll.onmousedown =function(ev){
		var oEvent =ev || event;
		
		
		disX =oEvent.clientX -oScroll.offsetLeft;
		
		document.onmousemove =function(ev){
			
			var oEvent =ev || event;
			
			var T =oEvent.clientX -disX;
			
			t(T);
			
			document.onmouseup =function(){
				document.onmousemove =null;
			};
		};
		
		return false;
		
	};

	function show(ev){
	
		var oEvent =ev || event;
		
		var T = 0;
		
		if(ev.detail){
			bBtn = oEvent.detail>0 ? true : false;
		}else{
			bBtn = oEvent.wheelDelta<0 ? true : false;
		}
		
		if(bBtn){  
			T = oScroll.offsetLeft + 50;
		}else{
			T = oScroll.offsetLeft - 50;	
		}
		
		t(T);
		
		if(oEvent.preventDefault){
			oEvent.preventDefault();
		}else{
			return false;
		}
	
	}
	function t(T){
	
		if(T<0){
			T=0;
		}else if(T>oListParent.offsetWidth-oScroll.offsetWidth){
			
			T=oListParent.offsetWidth-oScroll.offsetWidth;
			T=oListParent1.offsetWidth-oScroll.offsetWidth;
		
		}
		
		oScroll.style.left = T+'px';
		
		var scale = T/(oScrollParent.offsetWidth-oScroll.offsetWidth);
		
		oUl.style.left = -scale * (oUl.offsetWidth-oListParent.offsetWidth) + 'px';
		oUl1.style.left = -scale * (oUl1.offsetWidth-oListParent1.offsetWidth) + 'px';
	
	
	}

}
//分层管理-相关性设置-纵向滚动条
function fnScrollSeek1(oDivPar,oDiv,oDivPar1,oDiv1,oBtnPar,oBtn){
	var oListParent =document.getElementById(oDivPar);
	var oUl =document.getElementById(oDiv);
	var oListParent1 =document.getElementById(oDivPar1);
	var oUl1 =document.getElementById(oDiv1);
	var oScrollParent =document.getElementById(oBtnPar);
	var oScroll =document.getElementById(oBtn);
	var disX =0;
	var bBtn = true;
	
	oScroll.onmousedown =function(ev){
		var oEvent =ev || event;
		
		
		disY =oEvent.clientY -oScroll.offsetTop;
		
		document.onmousemove =function(ev){
			
			var oEvent =ev || event;
			
			var T =oEvent.clientY -disY;
			
			t(T);
			
			document.onmouseup =function(){
				document.onmousemove =null;
			};
		};
		
		return false;
		
	};

	function show(ev){
	
		var oEvent =ev || event;
		
		var T = 0;
		
		if(ev.detail){
			bBtn = oEvent.detail>0 ? true : false;
		}else{
			bBtn = oEvent.wheelDelta<0 ? true : false;
		}
		
		if(bBtn){  
			T = oScroll.offsetTop + 50;
		}else{
			T = oScroll.offsetTop - 50;	
		}
		
		t(T);
		
		if(oEvent.preventDefault){
			oEvent.preventDefault();
		}else{
			return false;
		}
	
	}
	function t(T){
	
		if(T<0){
			T=0;
		}else if(T>oListParent.offsetHeight-oScroll.offsetHeight){
			
			T=oListParent.offsetHeight-oScroll.offsetHeight;
			T=oListParent1.offsetHeight-oScroll.offsetHeight;
		
		}
		
		oScroll.style.top = T+'px';
		
		var scale = T/(oScrollParent.offsetHeight-oScroll.offsetHeight);
		
		oUl.style.top = -scale * (oUl.offsetHeight-oListParent.offsetHeight) + 'px';
		oUl1.style.top = -scale * (oUl1.offsetHeight-oListParent1.offsetHeight) + 'px';
	
	
	}

}






