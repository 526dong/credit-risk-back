package com.ccx.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class test2 {
	 /**
     * 只是一个demo，这里假设修改的值是String类型
     * @param exlFile
     * @param sheetIndex
     * @param col
     * @param row
     * @param value
     * @throws Exception
     */
    public static void updateExcel(String filePath,String value){
    	try { 
        	File file = new File(filePath);
            //建立数据的输入管道
            FileInputStream fileInputStream=new FileInputStream(file);
            //初始化一个工作簿
            Workbook wb = new HSSFWorkbook(fileInputStream);
            //设置单元格格式
            CellStyle cellStyle = wb.createCellStyle();  
            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));  
            //获得第二个工作表对象(ecxel中sheet的编号从0开始,0,1,2,3,....)
			Sheet sheet = wb.getSheetAt(1);
			//计算总行数
			int rowNum = sheet.getLastRowNum()+1;  //总行数;   
			System.out.println("sheet表行数为："+rowNum);   //打印总行数 
			//循环行与列;
	    	for(int i=0;i<rowNum;i++){
		    	Row row = sheet.getRow(i);//获取指定行的列
		    	if(UsedUtil.isNotNull(row)){
		    		//获取总列数
//			    	int cellNum = row.getLastCellNum (); //计算总列数
//					System.out.println("第"+i+"行有"+cellNum+"列");   //打印总列数 
		    		//直接获取第一列
					Cell cell = row.getCell(0);
					//“cellValue”指的是单元格内容，也就是 要提取的数据
					String cellValue = null;
					//单元格类型转换
					if(UsedUtil.isNotNull(cell)){
						cellValue = (String) getCellValue(cell);
					}
					System.out.println("第"+(i+1)+"行第1列单元格原来值为======="+cellValue);
					if("营业总收入"==cellValue||"营业总收入".equals(cellValue)){
						//赋值操作
						CellValueAssignment(row,cellStyle,value,i);
					}
					if("其中：营业收入"==cellValue||"其中：营业收入".equals(cellValue)){
						//赋值操作
						CellValueAssignment(row,cellStyle,value,i);
					}
				}
	    	}
	    	//强制刷新sheet中的公式
	    	for (int i = 0; i < wb.getNumberOfSheets(); i++) {
	    		//获得工作表对象(ecxel中sheet的编号从0开始,0,1,2,3,....)
				Sheet sheett = wb.getSheetAt(i);
				sheett.setForceFormulaRecalculation(true);
			}
	    	fileInputStream.close();//关闭文件输入流
	        FileOutputStream fos=new FileOutputStream(file);
	        wb.write(fos);
	        fos.close();//关闭文件输出流
    	}catch (Exception e) {
			e.printStackTrace();
		}
    }
 
    //单元格赋值
    private static void CellValueAssignment(Row row,CellStyle cellStyle,String value,int i) {
    	//循环列，只需要赋值 几列 即可 根据数据判断 从第二列到第七列
		for (int j = 1; j < 7; j++) {
			//需要赋值的列
			Cell cell1 = row.getCell(j);
			//设置单元格格式
//			cell1.setCellStyle(cellStyle);
			//更改excel单元格的值
			cell1.setCellValue(Double.parseDouble(value));
			System.err.println("第"+(i+1)+"行第"+(j+1)+"列单元格值被更新为-----"+value);
		}
    }
    
    private static Object getCellValue(Cell cell) {
    	Object cellValue = "";
        switch (cell.getCellType()) {
            case XSSFCell.CELL_TYPE_STRING:
                cellValue = cell.getRichStringCellValue().getString().trim();
                break;
            case XSSFCell.CELL_TYPE_NUMERIC:
                cellValue = new BigDecimal(String.valueOf(cell.getNumericCellValue())).multiply(new BigDecimal("100"));
                break;
            case XSSFCell.CELL_TYPE_BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue()).trim();
                break;
            case XSSFCell.CELL_TYPE_FORMULA:
                cellValue = cell.getCellFormula();
                break;
            default:
                cellValue = "";
        }
        return cellValue;
    }
    
    
    
    public static List<Map<String,Object>> analysisExcel(String filePath){
    	List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
    	try { 
        	File file = new File(filePath);
            //建立数据的输入管道
            FileInputStream fileInputStream=new FileInputStream(file);
            //初始化一个工作簿
            Workbook wb = null;
            try {
            	wb = new HSSFWorkbook(fileInputStream);
			} catch (Exception e) {
				fileInputStream = new FileInputStream(file);
				wb = new XSSFWorkbook(fileInputStream);
			}
            //设置单元格格式
            CellStyle cellStyle = wb.createCellStyle();  
            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));  
            //获得第二个工作表对象(ecxel中sheet的编号从0开始,0,1,2,3,....)
			Sheet sheet = wb.getSheetAt(0);
			//计算总行数
			int rowNum = sheet.getLastRowNum()+1;  //总行数;   
			System.out.println("sheet表行数为："+rowNum);   //打印总行数 
			//循环行与列;
	    	for(int i=2;i<rowNum;i++){
		    	Row row = sheet.getRow(i);//获取指定行的列
		    	List<Object> list = new ArrayList<Object>();
		    	if(UsedUtil.isNotNull(row)){
		    		//获取总列数
			    	int cellNum = row.getLastCellNum (); //计算总列数
					System.out.println("第"+(i+1)+"行有"+cellNum+"列");   //打印总列数 
					//直接获取第一列
					Cell cell1 = row.getCell(1);
					//“cellValue”指的是单元格内容，也就是 要提取的数据
					Object cellValue1 = null;
					//单元格类型转换
					if(UsedUtil.isNotNull(cell1)){
						cellValue1 = getCellValue(cell1);
					}
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("ratingLevel", cellValue1);
					for (int j = 2; j < cellNum; j++) {
						//直接获取第一列
						Cell cell = row.getCell(j);
						//“cellValue”指的是单元格内容，也就是 要提取的数据
						Object cellValue = null;
						//单元格类型转换
						if(UsedUtil.isNotNull(cell)){
							cellValue = getCellValue(cell);
						}
//						System.out.println("第"+(i+1)+"行第"+(j+1)+"列单元格原来值为======="+cellValue);
						list.add(cellValue);
					}
					map.put("breakRateList", list);
			    	resultList.add(map);
				}
	    	}
	    	System.err.println(resultList.toString());
	    	fileInputStream.close();//关闭文件输入流
    	}catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
    }

    public static void main(String[] args) throws Exception{
    	//下面改成你自己的xls文件进行测试，2003格式的，不能2007
        String path = "D://联合理想违约率.xlsx";
        String updateValue = "100";
        //path:路径  updateValue:要修改的值
        analysisExcel(path);
    }
	
}
