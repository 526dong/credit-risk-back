package com.ccx.util;

import com.ccx.Constans;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @Description:
 * @author:lilong
 * @Date: 2017/9/29
 */
public class DownloadUtil {

    /**
     * 下载服务器文件
     * @param filePath 文件路径，如 /data/aa.txt、c://aa.txt 等
     * @param filename 文件下载后的名称，如 bb.txt
     * @param response
     * @throws IOException
     */
    public static void download(String filePath,String filename, HttpServletResponse response) throws IOException {
        //Excel模板所在的路径

        File file = new File(filePath);

        //设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");

        try {
            //下载文件的名称
            response.setHeader("Content-Disposition",
                    "attachment;filename="+ new String((filename).getBytes(), "iso-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            bos = new BufferedOutputStream(out);

            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
    }
    /**
     * 读取某个文件夹下的所有文件
     */
    public static boolean readfile(String filepath) throws FileNotFoundException, IOException {
        try {

            File file = new File(filepath);
            if (file.isDirectory()) {
                System.out.println("文件夹");
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + "\\" + filelist[i]);
                    if (!readfile.isDirectory()) {
                        System.out.println("path=" + readfile.getPath());
                        System.out.println("absolutepath="
                                + readfile.getAbsolutePath());
                        System.out.println("name=" + readfile.getName());

                    } else if (readfile.isDirectory()) {
                        readfile(filepath + "\\" + filelist[i]);
                    }
                }

            }

        } catch (FileNotFoundException e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }
        return true;
    }
    /**
     * 关键字查找文件
     */
    public static File findFile(String filepath,String keyWord)  {
        try {
            File file = new File(filepath);
            System.out.println("1111111111111111111111111111111111111");
            if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + "\\" + filelist[i]);
                    if (!readfile.isDirectory()) {
                        if(readfile.getName().contains(keyWord)){
                            System.out.println("path=" + readfile.getPath());
                            System.out.println("absolutepath="
                                    + readfile.getAbsolutePath());
                            System.out.println("name=" + readfile.getName());
                            return readfile;
                        }
                    } else if (readfile.isDirectory()) {
                        findFile(filepath + "\\" + filelist[i],keyWord);
                    }
                }

            }
        } catch (Exception e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        findFile(Constans.TYPE_EXCEL_SAVE_DIR,"53.");
    }
}
