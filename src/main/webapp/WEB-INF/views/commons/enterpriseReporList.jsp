<%@ page language="java" import="java.util.*" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 报表列表 start-->
<%--列表--%>
<h2 class="main_title">报表数据</h2>
<div class="module_height main_paddTop table_hide">
    <form>
        <input id="reportId" type="hidden" name="id" value="0">
        <table class="module_table">
            <thead>
            <tr>
                <th class="module_width1">序号</th>
                <th>报表时间</th>
                <th>报表口径</th>
                <th>报表类型</th>
                <th>报表周期</th>
                <th>报表币种</th>
                <th>是否审计</th>
                <th>添加时间</th>
                <%--<th>评级状态</th>--%>
                <th class="module_width1">操作</th>
            </tr>
            </thead>
            <tbody id="report_info">
            </tbody>
        </table>
    </form>
</div>
<!-- 报表列表 end -->
   