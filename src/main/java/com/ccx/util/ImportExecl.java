package com.ccx.util;

import java.io.*;

import java.util.*;

import com.ccx.Constans;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Description:
 * @author:lilong
 * @Date: 2017/9/30
 */

public class ImportExecl
{

    /** 总行数 */

    private int totalRows = 0;

    /** 总列数 */

    private int totalCells = 0;

    /** 错误信息 */

    private String errorInfo;

    /**一行全为空的行标,key为sheet的名称，value为空行集合**/
   Map< String,List<Integer>> emptyRow;

    private  Map<String,List<List<String>>> map =null;

    /** 构造方法 */

    public ImportExecl(String filePath)
    {
        read(filePath);
    }
    public ImportExecl(MultipartFile excelFile)
    {
        read(excelFile);
    }

    public Map<String,List<List<String>>> getData(){
        return  this.map;
    }

    /**
     *
     * @描述：得到总行数
     *
     * @参数：@return
     *
     * @返回值：int
     */

    public int getTotalRows()
    {

        return totalRows;

    }

    /**
     *
     * @描述：得到总列数
     *
     * @参数：@return
     *
     * @返回值：int
     */

    public int getTotalCells()
    {

        return totalCells;

    }

    /**
     *
     * @描述：得到错误信息
     *
     * @参数：@return
     *
     * @返回值：String
     */

    public String getErrorInfo()
    {

        return errorInfo;

    }

    /**
     *
     * @描述：验证excel文件
     *
     * @参数：@param filePath　文件完整路径
     *
     * @参数：@return
     *
     * @返回值：boolean
     */

    public boolean validateExcel(String filePath)
    {
        /** 检查文件名是否为空或者是否是Excel格式的文件 */

        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath)))
        {
            errorInfo = "文件名不是excel格式";
            return false;
        }

        /** 检查文件是否存在 */

        File file = new File(filePath);
        if (file == null || !file.exists())
        {
            errorInfo = "文件不存在";
            return false;
        }
        return true;
    }
    public boolean validateExcel(MultipartFile excelFile)
    {
        /** 检查文件名是否为空或者是否是Excel格式的文件 */

        if (!(isExcel2003(excelFile.getOriginalFilename()) || isExcel2007(excelFile.getOriginalFilename())))
        {
            errorInfo = "文件名不是excel格式";
            return false;
        }
        return true;
    }
    /**
     *
     * @描述：根据文件名读取excel文件
     *
     * @参数：@param filePath 文件完整路径
     *
     * @参数：@return
     *
     * @返回值：List
     */

    private void read(String filePath)
    {
        InputStream is = null;
        try
        {
            /** 验证文件是否合法 */

            if (!validateExcel(filePath))
            {
                System.out.println(errorInfo);
                return ;
            }
            /** 判断文件的类型，是2003还是2007 */
            boolean isExcel2003 = true;
            if (isExcel2007(filePath))
            {
                isExcel2003 = false;
            }
            /** 调用本类提供的根据流读取的方法 */

            File file = new File(filePath);
            is = new FileInputStream(file);
            map = read(is, isExcel2003);
            is.close();

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            close(is);
        }
    }
    /**
     *
     * @描述：根据文件名读取excel文件
     *
     * @参数：@param filePath 文件完整路径
     *
     * @参数：@return
     *
     * @返回值：List
     */

    private void read(MultipartFile excelFile )
    {
        InputStream is =null;
        try
        {
            /** 验证文件是否合法 */
            System.out.println("getOriginalFilename:::::::"+excelFile.getOriginalFilename());
            System.out.println("NAME:::::::"+excelFile.getName());
            if (!validateExcel(excelFile))
            {
                System.out.println(errorInfo);
                return ;
            }
            /** 判断文件的类型，是2003还是2007 */
            boolean isExcel2003 = true;
            if (isExcel2007(excelFile.getOriginalFilename()))
            {
                isExcel2003 = false;
            }
            /** 调用本类提供的根据流读取的方法 */

            is=excelFile.getInputStream();

            map = read(is, isExcel2003);

            is.close();

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            close(is);
        }
    }

    void close(InputStream is){
        if (is != null)
        {
            try
            {
                is.close();
            }
            catch (IOException e)
            {
                is = null;
                e.printStackTrace();

            }

        }
    }

    /**
     *
     * @描述：根据流读取Excel文件
     *
     * @参数：@param inputStream
     *
     * @参数：@param isExcel2003
     *
     * @参数：@return
     *
     * @返回值：List
     */

    private Map<String,List<List<String>>> read(InputStream inputStream, boolean isExcel2003)
    {
        Map<String,List<List<String>>> map = new LinkedHashMap<>();
        try
        {
            /** 根据版本选择创建Workbook的方式 */
            Workbook wb = null;
            if (isExcel2003)
            {
                wb = new HSSFWorkbook(inputStream);
            }
            else
            {
                wb = new XSSFWorkbook(inputStream);
            }
            for (int i = 0; i <wb.getNumberOfSheets() ; i++) {

                Sheet sheet = wb.getSheetAt(i);
                map.put(sheet.getSheetName(),read(sheet,i));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return map;

    }

    /**
     *
     * @描述：读取数据
     *
     * @参数：@param Workbook
     *
     * @参数：@return
     *
     * @返回值：List<List<String>>
     */

    private List<List<String>> read( Sheet sheet,int i)
    {
        List<List<String>> dataLst = new ArrayList<List<String>>();
        /** 得到Excel的行数 */
        this.totalRows = sheet.getPhysicalNumberOfRows();
        /** 得到Excel的列数 */
        if (this.totalRows >= 1 && sheet.getRow(0) != null)
        {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        /** 循环Excel的行 */
        for (int r = 0; r < this.totalRows; r++)
        {
            Row row = sheet.getRow(r);
            if (row == null)
            {
                continue;
            }

            List<String> rowLst = new ArrayList<String>();
            /** 循环Excel的列 */
            boolean isempty=true;
            for (int c = 0; c < this.getTotalCells(); c++)
            {
                Cell cell = row.getCell(c);
                String cellValue = "";
                if (null != cell)
                {
                    // 以下是判断数据的类型
                    switch (cell.getCellType())
                    {
                        case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                            cellValue = cell.getNumericCellValue() + "";
                            break;

                        case HSSFCell.CELL_TYPE_STRING: // 字符串
                            cellValue = cell.getStringCellValue();
                            break;

                        case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                            cellValue = cell.getBooleanCellValue() + "";
                            break;

                        case HSSFCell.CELL_TYPE_FORMULA: // 公式
                            cellValue ="="+cell.getCellFormula() + "";
                            break;

                        case HSSFCell.CELL_TYPE_BLANK: // 空值
                            cellValue = "";
                            break;

                        case HSSFCell.CELL_TYPE_ERROR: // 故障
                            cellValue = "非法字符";
                            break;

                        default:
                            cellValue = "未知类型";
                            break;
                    }
                }
                if(!"".equals(cellValue))  isempty=false;
                rowLst.add(cellValue);

            }
            if(isempty) {
                if(emptyRow==null) emptyRow=new LinkedHashMap<>();
                List<Integer> list = null;
                if(emptyRow.get(sheet.getSheetName())==null){
                    list=new ArrayList<>();
                    emptyRow.put(sheet.getSheetName(),list);
                }
                list=emptyRow.get(sheet.getSheetName());
                list.add(row.getRowNum());
            }

            /** 保存第r行的第c列 */

            dataLst.add(rowLst);

        }
        return dataLst;
    }
    /**
     *
     * @描述：是否是2003的excel，返回true是2003
     *
     * @参数：@param filePath　文件完整路径
     *
     * @参数：@return
     *
     * @返回值：boolean
     */

    private  boolean isExcel2003(String filePath)
    {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    /**
     *
     * @描述：是否是2007的excel，返回true是2007
     *
     * @参数：@param filePath　文件完整路径
     *
     * @参数：@return
     *
     * @返回值：boolean
     */

    private  boolean isExcel2007(String filePath)
    {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    public Map<String,List<Integer>> getEmptyRow() {
        return emptyRow;
    }

    public void print(){
        if (map != null)
        {
            for (Map.Entry<String,List<List<String>>> str:map.entrySet())
            {
                System.out.println("==================sheet:"+str.getKey()+"=====================");
                List<List<String>> cellList = str.getValue();

                for (int j = 0; j < cellList.size(); j++)
                {
                    System.out.print("第" + (j) + "行");
                    // System.out.print("    第" + (j + 1) + "列值：");

                    System.out.println("    " + cellList.get(j));
                }
                System.out.println();

            }
        }
    }
    /**
     *
     * @描述：main测试方法
     *
     * @参数：@param args
     *
     * @参数：@throws Exception
     *
     * @返回值：void
     */

    /**
     * 将InputStream写入本地文件
     * @param destination 写入本地目录
     * @param input 输入流
     * @throws IOException
     */
    public static void writeToLocal(String destination, InputStream input)
            throws IOException {
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(destination);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        downloadFile.close();
        input.close();
    }
    /**
 * 将InputStream写入本地文件
 * @param destination 写入本地目录
 * @param excelFile 输入流
 * @throws IOException
 */
public static void writeToLocal(String destination, MultipartFile excelFile)
{
    int index;
    InputStream input=null;
    FileOutputStream downloadFile =null;
    try {
        input= excelFile.getInputStream();
        byte[] bytes = new byte[1024];
        downloadFile = new FileOutputStream(destination);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        downloadFile.close();
        input.close();
    }catch (Exception e){
        e.printStackTrace();
    }finally {
        if(downloadFile!=null){
            try {
                downloadFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(input!=null){
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

    /**
     * 本地目录读取文件转换成byte[]
     * @param filePath
     * @return
     */
    public static byte[] File2byte(String filePath)
    {
        byte[] buffer = null;
        try
        {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return buffer;
    }
    public static void main(String[] args) throws Exception
    {

    /*   ImportExecl poi = new ImportExecl("E:/abs项目/财务数据模版8-17.xls");
       // ImportExecl poi = new ImportExecl("E:\\abs项目\\新增报表\\医疗卫生行业财务报表模板20170914.xlsx");
        System.out.println(poi.emptyRow);
        // List<List<String>> list = poi.read("d:/aaa.xls");
        poi.print();*/
    }


}
