package com.quaint.test.base;

import java.io.*;
import java.util.zip.ZipOutputStream;

/**
 * @author quaint
 * @since 16 February 2020
 */
public class QuaintFunction {



    protected static void testFile(){
        try {

            String filePath = System.getProperty("user.dir")+"/demo-test/src/main/resources/file.txt";
            File file = new File(filePath);

            // 第一层
            // --> 装饰成字节流
            InputStream is = new FileInputStream(file);
            OutputStream os = new FileOutputStream(file);
            // --> 装饰成字符流
            Writer writer = new FileWriter(file);
            Reader reader = new FileReader(file);

            // 第二层
            // --> 过滤流
            FilterOutputStream fos = new FilterOutputStream(os);

            // --> 缓冲流
            BufferedInputStream bis = new BufferedInputStream(is);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            BufferedReader br = new BufferedReader(reader);
            BufferedWriter bw = new BufferedWriter(writer);

            // 第三层
            // 缓冲流装饰层管道流
            ZipOutputStream zos = new ZipOutputStream(fos);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
