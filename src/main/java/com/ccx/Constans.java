package com.ccx;

import com.ccx.util.PropertiesUtil;

/**
 * @Description:
 * @author:lilong
 * @Date: 2017/9/29
 */
public class Constans {

    /**
     * 类型模板示例路径
     */
    //public static final String TYPE_EXCEL_MODLE="E://data/报表类型模版 .xls";
    public static final String TYPE_EXCEL_MODLE= PropertiesUtil.getData("file-path.properties","type_excel_modle");
    /**
     * 类型模板存储路径
     */
    public static final String TYPE_EXCEL_SAVE_DIR= PropertiesUtil.getData("file-path.properties","type_excel_save_dir");



}
