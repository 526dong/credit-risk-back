package com.ccx.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ModelAttribute;
/**
 * 
 * @author xzd
 * @date 2017/6/21
 */
public class BasicController {
	protected HttpServletRequest request;
    protected HttpServletResponse response;

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
    }

    /**
     * 页数
     * @return
     */
    public int getPageNum() {
        int pageNum;
        if("".equals(request.getParameter("pageNum")) || request.getParameter("pageNum") == null){
            pageNum = 1;
        }else{
            pageNum=Integer.parseInt(request.getParameter("pageNum"));
        }
        return pageNum;

    }
    
    /**
     * 页面数据条数
     * @return
     */
    public int getPageSize() {
        int pageSize;
        if("".equals(request.getParameter("pageSize")) || request.getParameter("pageSize") == null){
            pageSize = 10;//默认每页显示10
        }else{
            pageSize = Integer.parseInt(request.getParameter("pageSize"));
        }
        return pageSize;
    }

}
