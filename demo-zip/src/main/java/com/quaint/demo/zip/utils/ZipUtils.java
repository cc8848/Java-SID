package com.quaint.demo.zip.utils;


import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 参考代码
 * @author quaint
 * @date 12 February 2020
 * @since master
 */
public abstract class ZipUtils {


    public static void compression() throws IOException {

        // 压缩流
        FileOutputStream fos = new FileOutputStream("E://TestCase//day20//demo.zip");
        ZipOutputStream zout = new ZipOutputStream(fos);

        // 压缩图片
        // 文件输入流
        FileInputStream fis = new FileInputStream("E://TestCase//day20//demo.jpg");
        // 压缩条目
        ZipEntry entry = new ZipEntry("picture.jpg");
        // 放入下一个条目
        zout.putNextEntry(entry);

        byte[] buffer = new byte[1024];
        int len = -1;
        while((len = fis.read(buffer)) != -1) {
            zout.write(buffer, 0, len);
        }

        // 关闭条目
        zout.closeEntry();
        fis.close();

        // 压缩txt
        fis = new FileInputStream("E://TestCase//day20//fiction.txt");

        ZipEntry entry2 = new ZipEntry("fiction.txt");
        zout.putNextEntry(entry2);

        while((len = fis.read(buffer)) != -1) {
            zout.write(buffer, 0, len);
        }

        zout.closeEntry();
        zout.close();
        fos.close();
        fis.close();

        System.out.println("end");

    }


    public static void unzip(String zipPath){

        if (StringUtils.isEmpty(zipPath)){
            return;
        }

        FileInputStream fis = null;
        ZipInputStream zins = null;
        try {

            fis = new FileInputStream(zipPath);
            zins = new ZipInputStream(fis);

            ZipEntry entry = null;

            while((entry = zins.getNextEntry()) != null) {
                FileOutputStream fos = new FileOutputStream("E://TestCase//day20//undo//" + entry.getName());
                byte[] buffer = new byte[1024];
                int len = -1;
                while((len = zins.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zins.closeEntry();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (zins!=null){
                    zins.close();
                }
                if (fis != null){
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
