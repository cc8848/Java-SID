package com.quaint.demo.zip.spi;

import com.quaint.demo.zip.design.CompressionStrategy;
import com.quaint.demo.zip.dto.ImageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * @author quaint
 * @date 11 February 2020
 * @since master
 */
@RestController
public class ZipDemoSpi {


    @Autowired
    List<CompressionStrategy<ImageDto>> compressionStrategies;

    /**
     * 解压web传来的zip
     */
    @PostMapping("/web/unzip")
    public String webUnzipDemo(@RequestParam("fileData") MultipartFile file){
        // 选取解压策略
        Optional<CompressionStrategy<ImageDto>> bast = compressionStrategies.stream()
                .filter(strategy -> strategy.support(file.getOriginalFilename())).findFirst();

        // 如果支持该类型
        if (bast.isPresent()){
            try {

                List<ImageDto> extract = bast.get().extract(file.getInputStream());





            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return "解压失败";
        }
        return "解压成功";
    }

    @PostMapping("/local/unzip")
    public void localUnzipDemo(){

        // 获取当前项目文件夹的的zip文件
        String filePath = System.getProperty("user.dir")+"/demo-zip/src/main/resources/image.zip";

        String fileName = filePath.substring(filePath.lastIndexOf('/')+1);

        Optional<CompressionStrategy<ImageDto>> bast = compressionStrategies.stream()
                .filter(strategy -> strategy.support(fileName)).findFirst();

        if (bast.isPresent()){
            try {
                InputStream inputStream = new FileInputStream(filePath);
                List<ImageDto> extract = bast.get().extract(inputStream);





            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

}
