package com.ccx.reporttype.service.impl;

import com.alibaba.fastjson.JSON;
import com.ccx.Constans;
import com.ccx.enterprise.model.IndustryShow;
import com.ccx.financialAnaly.service.FinancialAnalyService;
import com.ccx.index.dao.AbsIndexModelMapper;
import com.ccx.index.service.AbsIndexService;
import com.ccx.pricedata.model.PriceData;
import com.ccx.ratedata.controller.RateDataController;
import com.ccx.reporttype.dao.*;
import com.ccx.reporttype.model.*;
import com.ccx.reporttype.pojo.AbsReportType;
import com.ccx.reporttype.service.ReportTypeService;
import com.ccx.reporttype.service.TypeUpService;
import com.ccx.system.model.UserBg;
import com.ccx.util.ControllerUtil;
import com.ccx.util.ImportExecl;
import com.ccx.util.JsonUtils;
import com.ccx.util.StringUtils;
import com.ccx.util.page.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @Description:
 * @author:lilong
 * @Date: 2017/9/29
 */
@Service
public class ReportTypeServiceImpl implements ReportTypeService {
    private  Logger logger = LogManager.getLogger(RateDataController.class);

    @Autowired
    private AbsEnterpriseReportTypeMapper absEnterpriseReportTypeMapper;

    @Autowired
    private AbsEnterpriseReportSheetMapper absEnterpriseReportSheetMapper;

    @Autowired
    private AbsEnterpriseReportModelMapper absEnterpriseReportModelMapper;

    @Autowired
    private AbsEnterpriseReportCloumnsFormulaMapper absEnterpriseReportCloumnsFormulaMapper;

    @Autowired
    private AbsEnterpriseIndustryTypeMapper absEnterpriseIndustryTypeMapper;

    @Autowired
    private TypeUpService typeUpService;

   @Autowired    FinancialAnalyService financialAnalyService;

    @Autowired
    AbsIndexService absIndexService;

    @Autowired
    private AbsIndexModelMapper modelMapper;

    /**
     * 类型新增或者修改
     * @param excelFile
     * @param request
     * @param name
     * @param oldTypeId
     * @return
     */
    @Override
    public Map<String, Object> saveOrUpdateType(MultipartFile excelFile, HttpServletRequest request,String name,Integer oldTypeId) {
        Map<String, Object> resultMap = new HashMap<>();
        //获取当前登录用户信息
        UserBg user = (UserBg) ControllerUtil.getSessionUser(request);
        ImportExecl poi = new ImportExecl(excelFile);
        // List<List<String>> list = poi.read("d:/aaa.xls");
        poi.print();

        Map<AbsEnterpriseReportSheet,List<Map<String,Object>>> map = getData(poi.getData(),resultMap);
        if(resultMap.size()>0){
            return resultMap;
        }
       // System.out.println(JsonUtils.toJson(map));
        //修改时验证
        if(oldTypeId!=null&&checkIfUpdate(oldTypeId)==1) {
            resultMap.put("code",113);
            resultMap.put("msg","当前类型不能被修改");
            return  resultMap;
        }
       Integer newId= saveMsg(map,user,name);
       //修改时校验其他更改项是否成功，不成功回滚
       if(oldTypeId!=null){
           Integer r1=null;
           boolean r2=false;
           try {
                r1=typeUpService.updateStatus(newId,oldTypeId);
               absIndexService.syncIndex(oldTypeId,newId);
               r2=financialAnalyService.modifiFinancialTemplateReportType(oldTypeId,newId);
           }catch (Exception e){
               throw  new RuntimeException("修改失败");
           }
           if(r1!=1&&!r2){
               throw  new RuntimeException("修改失败");
           }
       }
       //将文件存储到本地服务器
       ImportExecl.writeToLocal(new StringBuilder(Constans.TYPE_EXCEL_SAVE_DIR).append(newId).append(excelFile.getOriginalFilename().substring(excelFile.getOriginalFilename().lastIndexOf("."))).toString(),excelFile);
        resultMap.put("code",100);
        resultMap.put("msg","成功");
        return resultMap;
    }

    /**
     * 判断类型是否能被修改
     * @param oldeTypeId
     * @return
     */
    private Integer checkIfUpdate(Integer oldeTypeId){
        AbsEnterpriseReportType type= absEnterpriseReportTypeMapper.selectByPrimaryKey(oldeTypeId);
        Integer status=type.getStatus();
        if(status==0||status==1||status==4){
            return  1;
        }
        return 0;
    }

    /**
     * 数据库存储
     * @param map
     * @param user
     * @param name
     * @return
     */
    private  Integer saveMsg( Map<AbsEnterpriseReportSheet,List<Map<String,Object>>> map,UserBg user,String name){
        AbsEnterpriseReportType type=new AbsEnterpriseReportType();
        type.setCreatorName(user.getLoginName());
        type.setVersion(1);
        type.setName(name);
        type.setStatus(2);
        type.setCreateDate(new Date());
        absEnterpriseReportTypeMapper.insert(type);
        Integer typeId=type.getId();
        if(typeId==null) {
            throw  new RuntimeException("");
        }
        System.out.println("jsonAll::::"+ JsonUtils.toJson(map));
        for(Map.Entry<AbsEnterpriseReportSheet,List<Map<String,Object>>> str:map.entrySet()){
            AbsEnterpriseReportSheet sheet=str.getKey();
            sheet.setReportType(typeId);
            absEnterpriseReportSheetMapper.insert(sheet);
            Integer sheetId=sheet.getId();
            for(Map<String,Object> st:str.getValue()){
                if(st==null) continue;
                AbsEnterpriseReportModel model =(AbsEnterpriseReportModel)st.get("model");
                model.setReportType(typeId);
                model.setReportSonType(sheetId);
                absEnterpriseReportModelMapper.insert(model);
                if(st.get("formula")!=null){
                    AbsEnterpriseReportCloumnsFormula formula= (AbsEnterpriseReportCloumnsFormula) st.get("formula");
                    formula.setModelId(model.getId());
                    formula.setReportSonType(sheetId);
                    formula.setReportType(typeId);
                    absEnterpriseReportCloumnsFormulaMapper.insert(formula);
                }
            }
        }
        return type.getId();
    }

    /**
     * 数据组装
     * @param map 原始数据
     * @param resultMap 返回信息
     * @return
     */
    private Map<AbsEnterpriseReportSheet,List<Map<String,Object>>> getData( Map<String,List<List<String>>> map,Map<String, Object> resultMap){
       // Map<String,List<List<String>>> map = poi.getData();
       Map<AbsEnterpriseReportSheet,List<Map<String,Object>>> data =new LinkedHashMap<>();
        int sheetOrderId=1;
        if (map == null) {
            return  null;
        }
        for (Map.Entry<String,List<List<String>>> str:map.entrySet())
        {
            List<List<String>> cellList = str.getValue();
           /* checkH(cellList,resultMap);
            //校验类型模板列数是否正确
            if(resultMap.size()>0){
                return  null;
            }*/
            AbsEnterpriseReportSheet sheet =new AbsEnterpriseReportSheet();
            sheet.setSheetOrder(sheetOrderId++);
            sheet.setReportSonNo(cellList.get(0).size()==3?1:2);
            sheet.setName(str.getKey());

            String title;
            //数据开始行
            int begin=1;
            int order=1;
            title=getTitile(cellList.get(0));
            if(title==null){
                resultMap.put("code", 502);
                resultMap.put("msg", "excel模板格式不正确");
                return  null;
            }
            //检验子表格式问题
            checkE(str,resultMap,begin);
            //校验模板有无空行
            if(resultMap.size()>0){
                return  null;
            }
            sheet.setColumnsFirstName(title);

            //modle存储 包括字段信息和字段公式信息
            List<Map<String,Object>> list = new ArrayList<>();
            for (int i = begin; i < cellList.size(); i++) {
                Map<String,Object> rowdata=getModle(i,cellList.get(i),0,order++,1);
                if(rowdata!=null) list.add(rowdata);
                System.out.print("第" + (i) + "行");
                System.out.println("    " + cellList.get(i));
            }
            if(sheet.getReportSonNo()==2){
                for (int i = begin; i < cellList.size(); i++) {
                    Map<String,Object> rowdata=getModle(i,cellList.get(i),3,order++,2);
                    if(rowdata!=null)
                    list.add(rowdata);
                    System.out.print("第" + (i) + "行");
                    System.out.println("    " + cellList.get(i));
                }
            }
            data.put(sheet,list);
        }
        return  data;
    }
    /**
     * 判断是否为标题列并获取
     * @param li
     * @return
     */
    private String getTitile(List<String> li){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i <li.size() ; i++) {
            if("".equals(li.get(i))&&i%3!=0) return null;
            builder.append(li.get(i).trim()).append(";;");
        }
        return builder.substring(0,builder.length()-2);
    }

    /**
     * 字段组装
     * @param row ecxel中第几行
     * @param list 某行的数据
     * @param base 列数字
     * @param order 字段排序
     * @param reportSonNo
     * @return 三列：1 六列：2
     */
    private Map<String,Object> getModle(int row, List<String> list,int base, int order,int reportSonNo){
        Map<String,Object> map = new HashMap<>();
        AbsEnterpriseReportModel model = new AbsEnterpriseReportModel();
        String val0=list.get(base).trim();
        if("".equals(val0)) return null;
        String val1=list.get(base+1);
        String val2=list.get(base+2);


        model.setOrderNo(order);
        model.setColumnExcel(new StringBuilder(reportSonNo==1?"A":"D").append(row+1).toString());
        model.setFinancialSubject(val0.endsWith("*")?val0.substring(0,val0.length()-1):val0);
        model.setReportSonNo(reportSonNo);
        model.setRequired(val0.endsWith("*")?1:(val1.startsWith("=")?2:0));

        AbsEnterpriseReportCloumnsFormula formula=null;
        if(val1!=null&&val1.trim().startsWith("=")){
            formula=new AbsEnterpriseReportCloumnsFormula();
            formula.setBeginFormula(val1.trim());
        }
        if(val2!=null&&val2.trim().startsWith("=")){
            if(formula==null) formula=new AbsEnterpriseReportCloumnsFormula();
            formula.setEndFormula(val2.trim());
        }
        map.put("model",model);
        if(formula!=null) map.put("formula",formula);
        return  map;
    }

    /**
     *检验子表格式问题
     * @param str 子表数据
     * @param resultMap 返回结果
     * @param begin 数据开始行
     */
    private  void  checkE(Map.Entry<String,List<List<String>>> str , Map<String, Object> resultMap,int begin){
            List<List<String>> list = str.getValue();
            //标记为空的行
            Integer empty1=null;
            Integer empty2=null;
            Map<String,Integer> bj =new HashMap<>();
            for (int i = begin; i <list.size() ; i++) {
                List<String> ii=list.get(i);
                if(ii.size()!=3&&ii.size()!=6){
                    //excel模板上传列数有误
                    resultMap.put("code", 501);
                    resultMap.put("msg", "exexl类型模板只能上传3列或者6列");
                    return;
                }
               check(bj,str.getKey(),resultMap,empty1,i,ii.get(0),ii.get(1),ii.get(2));
                if(resultMap.size()>0){
                    return;
                }
                if(ii.size()==6){
                    check(bj,str.getKey(),resultMap,empty2,i,ii.get(3),ii.get(4),ii.get(5));
                    if(resultMap.size()>0){
                        return;
                    }
                }
            }
    }
    //校验
    private void check(Map<String,Integer> bj, String sheetName,Map<String, Object> resultMap,Integer empty,int i, String... a){
       String md=a[0].trim();
        if(!"".equals(md)){
           if(bj.get(md)!=null){
               resultMap.put("code", 500);
               resultMap.put("msg", "excel模板，子表：“"+sheetName+"”，第"+(i+1)+"行字段重复！");
               return;
           }
           bj.put(md,1);
       }
        if(a.length!=3){
            return;
        }
        if(StringUtils.isContainChinese(a[1])||StringUtils.isContainChinese(a[2])){
            resultMap.put("code", 500);
            resultMap.put("msg", "excel模板，子表：“"+sheetName+"”，第"+(i+1)+"行数据列不能出现中文！");
            return;
        }
       if("".equals(a[0].trim())){
           if(empty!=null&&empty+1!=i){
               resultMap.put("code", 500);
               resultMap.put("msg", "excel模板，子表：“"+sheetName+"”，第"+(i+1)+"行数据列不能出现空行！");
               return;
           }
           return ;
       }
    }
    //检查列数是否正确
    private void  checkH(List<List<String>> cellList, Map<String, Object> resultMap){
        if(cellList.get(0).size()!=3&&cellList.get(0).size()!=6){
            //excel模板上传列数有误
            resultMap.put("code", 501);
            resultMap.put("msg", "exexl类型模板只能上传3列或者6列");
        }
    }

    /**
     * 获取Modle集合
     * @param sheet
     * @param beiginrow
     * @param reportSonNo
     * @return
     */
    private List<Map<String,Object>> getModles(Sheet sheet,int beiginrow,int reportSonNo){
        //排序
        int order=1;
        List<Map<String,Object>> list =new ArrayList<>();
        if(reportSonNo==1){
            for (int r=beiginrow;r<=sheet.getLastRowNum();r++) {
                Row row = sheet.getRow(r);
                System.out.println("=================第"+row.getRowNum()+"行====================");
                int base=0;
                if (row != null && !"".equals(row.toString().trim())) {
                   // list.add( getModle(row,base,order,1));
                }
            }
        }
        return list;
    }

    /**
     * 获取所有的行业信息及与之匹配的模板类型
     * @param page
     * @return
     */
    @Override
    public Page<AbsReportType> getPageList(Page<AbsReportType> page) {
        //进行小数格式化处理
       // DecimalFormat df = new DecimalFormat("#.##");
        List<AbsReportType> list = absEnterpriseReportTypeMapper.getList(page);
        page.setRows(list);
        return page;
    }


    @Override
    public List<Map<String, Object>> getTypes(Integer typeId) {
        return absEnterpriseReportTypeMapper.getTypes(typeId);
    }

    @Override
    public Page<IndustryShow> getPageList2(Page<IndustryShow> page) {
        page.setRows(absEnterpriseReportTypeMapper.getPageList2(page));
        return page;
    }

    @Override
    public AbsEnterpriseReportType getTypeByid(Integer typeId) {
        return absEnterpriseReportTypeMapper.selectByPrimaryKey(typeId);
    }

    /**
     * 保存行业与类型模板的关系
     * @param ids
     * @param typeId
     * @return
     */
    @Override
    public synchronized Map<String, Object> saveTypeAndIns(String ids, Integer typeId) {
        Map<String, Object> res=new HashMap<>();
        List<Map<String,Object>> all=absEnterpriseReportTypeMapper.getTypesAndIns(typeId);
        //页面传来的行业
        List<String> list=new ArrayList<>();
        for(String str:ids.split(",")){
            if(!"".equals(str.trim()))
            list.add(str.trim());
        }
        //原来的行业
        List<String > adata=new ArrayList<>();
        for(Map<String,Object> str:all){
            String indId= str.get("industry_id").toString();
            adata.add(indId);
            //之前的不在现在所属的行业中，删除
            if(!list.contains(indId)){
                AbsEnterpriseIndustryType type=new AbsEnterpriseIndustryType();
                type.setId(Integer.valueOf(str.get("id").toString()));
                type.setStatus(2);
                type.setReportType(str.get("report_type").toString());
                absEnterpriseIndustryTypeMapper.updateByPrimaryKey(type);
            }
        }
        //原来的行业不在现在所属的列表中，增加
        for(String str:list){
            if(!adata.contains(str)){
                AbsEnterpriseIndustryType type=new AbsEnterpriseIndustryType();
                type.setIndustryId(Integer.valueOf(str));
                type.setReportType(String.valueOf(typeId));
                type.setAddtime(new Date());
                type.setStatus(0);
                absEnterpriseIndustryTypeMapper.insert(type);
            }
        }
        res.put("code",100);
        res.put("msg","更新成功");
        return  res;
    }
/*
    */
/**
     * 保存行业与类型模板的关系
     * @param ids
     * @param typeId
     * @return
     *//*

    @Override
    public synchronized Map<String, Object> saveTypeAndIns(String ids, Integer typeId) {
        Map<String, Object> res=new HashMap<>();
        List<Map<String,Object>> all=absEnterpriseReportTypeMapper.getTypesAndIns();
        Map<Integer,Integer> adata=new HashMap<Integer,Integer>();
        List<Integer> thisType=new ArrayList<Integer>();
        for(Map<String,Object> str:all){
            Integer thistype=Integer.valueOf((String)str.get("report_type"));
            adata.put((Integer)str.get("industry_id"),thistype);
            if(Integer.parseInt(thistype.toString())==typeId){
                thisType.add((Integer)str.get("industry_id"));
            }
        }
        //待增加
        List<Integer> add=new ArrayList<>();
        //待删除
        List<Integer> del=new ArrayList<>();
       */
/* //修改状态
        List<Integer> update=new ArrayList<>();*//*


       List<Integer> newType=new ArrayList<>();
       //先找到需要新增加的
       if(!"".equals(ids)){
           for(String str:ids.split(",")){
               if(!"".equals(str)){
                   newType.add(Integer.valueOf(str));
                   System.out.println("val::::"+adata.get(Integer.valueOf(str)));
                   Integer ttype=adata.get(Integer.valueOf(str));
                   if(ttype!=null&&!ttype.equals(typeId)){
                       res.put("code",200);
                       res.put("msg","类型已变更，请重新提交");
                       return  res;
                   }
                  if(!thisType.contains(Integer.valueOf(str))) {
                       add.add(Integer.valueOf(str));
                  }
               }
           }
       }
       //找到需要删除的
        for(Integer str :thisType){
            if(!newType.contains(str)){
                del.add(str);
            }
        }
        for(Integer str:add){
            AbsEnterpriseIndustryType type=new AbsEnterpriseIndustryType();
            type.setIndustryId(str);
            type.setReportType(String.valueOf(typeId));
            type.setAddtime(new Date());
            type.setStatus(0);
            absEnterpriseIndustryTypeMapper.insert(type);
        }
        for(Integer str:del){
            absEnterpriseIndustryTypeMapper.updateStatus(str);
        }
        res.put("code",100);
        res.put("msg","更新成功");
        return  res;
    }
*/

    /**
     * 待添加各种操作的逻辑关系
     * 类型模板 启用、禁用、停用
     * @param typeId
     * @param status
     * @return
     */
    @Override
    public Map<String, Object> updateStatus(Integer typeId, String status) {
        Map<String, Object> resultMap =new HashMap<>();
        resultMap.put("code",100);
        resultMap.put("msg","操作成功");
        AbsEnterpriseReportType type=absEnterpriseReportTypeMapper.selectByPrimaryKey(typeId);
        AbsEnterpriseReportType n=new AbsEnterpriseReportType();
        n.setId(typeId);
        if("on".equals(status)){
            if(type.getStatus()==0){
                resultMap.put("code",101);
                resultMap.put("msg","已经是启用状态");
                return  resultMap;
            }
            n.setStatus(0);
            absEnterpriseReportTypeMapper.updateByPrimaryKeySelective(n);
            return  resultMap;
        }
        if("off".equals(status)){
            if(type.getStatus()!=0){
                resultMap.put("code",101);
                resultMap.put("msg","无意义的操作");
            }
            n.setStatus(3);
            absEnterpriseReportTypeMapper.updateByPrimaryKeySelective(n);
            modelMapper.deleteMatch(typeId);
            return  resultMap;
        }
        if("del".equals(status)){
            if(type.getStatus()!=3&&type.getStatus()!=2){
                resultMap.put("code",101);
                resultMap.put("msg","模板不能被删除");
                return  resultMap;
            }
            Map<String,Object> c_t= absEnterpriseReportTypeMapper.selectCheckIfUse(typeId);
            Integer ct=Integer.valueOf(c_t.get("c_t").toString());
            if(ct>1){
                resultMap.put("code",888);
                resultMap.put("msg","此类型已经被使用，不能被删除");
                return resultMap;
            }
            n.setStatus(4);
            Map<String,Object> map= absEnterpriseReportTypeMapper.deleteType(typeId);
            if("1".equals(map.get("t_error").toString())){
                resultMap.put("code",777);
                resultMap.put("msg","删除异常");
            }
            //absEnterpriseReportTypeMapper.updateByPrimaryKeySelective(n);
            modelMapper.deleteMatch(typeId);
            return  resultMap;
        }
        resultMap.put("code",999);
        resultMap.put("msg","错误的操作");

        return resultMap;
    }

    /**
     * @Description: 校验新增报表类型时的类型名称唯一性
     * @Author: wxn
     * @Date: 2018/1/18 15:21:13
     * @Param:
     * @Return
     */
    @Override
    public String checkFileName(Map<String,Object> map){
        String result = "error";
        List<Map<String,Object>> list = absEnterpriseReportTypeMapper.checkFileName(map);
        if(null != list && list.size()>0){
            return result;
        }
        result = "success";
        return result;
    }

}
