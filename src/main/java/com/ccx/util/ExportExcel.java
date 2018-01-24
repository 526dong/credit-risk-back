package com.ccx.util;

import com.ccx.ratedata.model.RateData;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xzd on 2017/9/5.
 */
public class ExportExcel<T>  {
    @SuppressWarnings("unchecked")
    public void exportExcel(List<RateData> rateData, OutputStream out) throws Exception {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet("评级模板");
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 30);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.WHITE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 12);
        /*font.setBoldweight(HSSFFont.);*/
        // 把字体应用到当前的样式
        style.setFont(font);

        //标题行
        String[] headers = {"统一社会信用代码", "组织机构代码", "事证号", "公司名称", "最新评级结果", "最新评级日期", "评级机构", "错误类型"};

        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for (short i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        //反射
        Class<?> clazz = Class.forName("com.ccx.ratedata.model.RateData");

        if (rateData != null && rateData.size() > 0) {
            for (int i = 0; i < rateData.size(); i++) {
                HSSFRow rows = sheet.createRow(i+1);
                for (int j = 0; j < headers.length; j++) {
                    Method method = clazz.getDeclaredMethod("get" + getRateDataMap().get(j));
                    Object obj = method.invoke(rateData.get(i));

                    HSSFCell cell = rows.createCell(j);
                    cell.setCellStyle(style);
                    //错误信息
                    if (j == 7) {
                        if (obj != null) {
                            int errType = Integer.valueOf(String.valueOf(obj));
                            if (errType == 0) {
                                //必填项未填-0
                                cell.setCellValue(new HSSFRichTextString("必填项未填"));
                            } else if (errType == 1) {
                                //格式错误-1
                                cell.setCellValue(new HSSFRichTextString("格式错误"));
                            } else if (errType == 2) {
                                //数据重复-2
                                cell.setCellValue(new HSSFRichTextString("数据重复"));
                            } else {
                                //数据重复-2
                                cell.setCellValue(new HSSFRichTextString("评级结果不符合填写规范"));
                            }
                        } else {
                            cell.setCellValue("");
                        }
                    } else {
                        // 判断值的类型后进行强制类型转换
                        String textValue = null;

                        if (obj != null) {
                            if (obj instanceof Integer){
                                textValue = (String.valueOf((Integer)(obj)));
                            } else {
                                //作为字符串进行处理
                                textValue = obj.toString();
                            }
                            cell.setCellValue(new HSSFRichTextString(textValue));
                        } else {
                            cell.setCellValue("");
                        }

                    }
                }
            }
        }

        try {
            out.flush();
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取评级数据map
    public static Map<Integer, String> getRateDataMap() {
        Map<Integer, String> rateDataMap = new HashMap<>();
        //rateDataMap put value
        //统一社会信用代码
        rateDataMap.put(0, "CreditCode");
        //组织机构代码
        rateDataMap.put(1, "OrganizationCode");
        //事证号
        rateDataMap.put(2, "CertificateCode");
        //公司名称
        rateDataMap.put(3, "Name");
        //最新评级结果
        rateDataMap.put(4, "RateResultName");
        //最新评级日期
        rateDataMap.put(5, "RateTime");
        //评级机构
        rateDataMap.put(6, "RateInstitutionName");
        //错误类型:必填项未填-0，格式错误-1，数据重复-2
        rateDataMap.put(7, "ErrType");
        return rateDataMap;
    }
}
