//基础运动
function baseMove(obj,attr,iTarget,fn1,fn2){
	obj.iTimer = obj.iTimer || {};
	clearInterval(obj.iTimer[attr]);
	var iSpeed =0;
	var iCur =0;
	obj.iTimer[attr] = setInterval(function(){
		
		if(attr == 'opacity'){
			iCur =parseInt(parseFloat(css(obj,attr))*100);
		}else{
			iCur =parseInt(css(obj,attr));
		}
		iSpeed =(iTarget - iCur)/4;
		iSpeed =iSpeed>0?Math.ceil(iSpeed):Math.floor(iSpeed);
		
		if(fn1){
			fn1(iCur, iSpeed);
		}
		
		if(iCur == iTarget){
			clearInterval(obj.iTimer[attr]);	
			if(fn2){
				fn2();
			}
		}else{
			if(attr == 'opacity'){
				
				obj.style.filter='alpha(opacity:'+(iCur+iSpeed)+')';
				obj.style.opacity =(iCur+iSpeed)/100;
			
			}else{
				obj.style[attr] =iCur+iSpeed+'px';
			}
			
		}
	
	},30);

}
//完美运动
function startMove(obj,json,fn){
	clearInterval(obj.iTimer);
	var iSpeed =0;
	var iCur =0;
	obj.iTimer =setInterval(function(){
		var iStop =true;
		for(var attr in json){
			
			if(attr =='opacity'){
				iCur =parseInt(parseFloat(css(obj,attr))*100);
			}else{
				iCur =parseInt(css(obj,attr));
			}
			
			iSpeed =(json[attr]-iCur)/4;
			iSpeed =iSpeed>0?Math.ceil(iSpeed):Math.floor(iSpeed);
			
			
			if(iCur !=json[attr]){
				iStop =false;
				if(attr =='opacity'){
					obj.style.filter='alpha(opacity:'+(iCur+iSpeed)+')';
					obj.style.opacity =(iCur+iSpeed)/100;
				}else{
					obj.style[attr] =iCur+iSpeed+'px';
				}
			}

		}
		if(iStop){
			clearInterval(obj.iTimer);
			fn && fn.call(obj);
		}
		
	},30);


}
//设置获取样式
function css(obj,attr) {
	if (obj.currentStyle) {
		return obj.currentStyle[attr];
	} else {
		return getComputedStyle(obj, false)[attr];
	}
}
