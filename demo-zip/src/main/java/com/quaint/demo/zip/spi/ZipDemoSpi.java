package com.quaint.demo.zip.spi;

import com.quaint.demo.zip.design.CompressionStrategy;
import com.quaint.demo.zip.dto.ImageDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author quaint
 * @date 11 February 2020
 * @since master
 */
@RestController
@Slf4j
@Api(tags = {"zip测试demo","分类: 测试"})
public class ZipDemoSpi {

    /**
     * 单例 包含的对象也是单例, 方便测试, 先把解压的图片 暂时存在这里, 然后在压缩 提供web下载
     * swagger 操作流程, -->先解压, -->在压缩
     */
    private List<ImageDto> tempData;

    @Autowired
    List<CompressionStrategy<ImageDto>> compressionStrategies;

    /**
     * 解压web传来的zip
     */
    @ApiOperation("解压web传来的zip")
    @PostMapping("/web/unzip")
    public String webUnzipDemo(@RequestParam("fileData") MultipartFile file){
        // 选取解压策略
        Optional<CompressionStrategy<ImageDto>> best = compressionStrategies.stream()
                .filter(strategy -> strategy.support(file.getOriginalFilename())).findFirst();

        // 如果支持该类型
        if (best.isPresent()){
            try {
                List<ImageDto> extract = best.get().extract(file.getInputStream());
                // 测试解压结果
                extract.forEach(imageDto -> log.info("解压到一个图片-->"+imageDto.getFileName()));
                tempData = new ArrayList<>();
                tempData.addAll(extract);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return "解压失败";
        }
        return "解压成功";
    }

    /**
     * 解压local传来的zip
     */
    @ApiOperation("解压local传来的zip")
    @PostMapping("/local/unzip")
    public String localUnzipDemo(){

        // 获取当前项目文件夹的的zip文件
        String filePath = System.getProperty("user.dir")+"/demo-zip/src/main/resources/image.zip";

        String fileName = filePath.substring(filePath.lastIndexOf('/')+1);

        Optional<CompressionStrategy<ImageDto>> best = compressionStrategies.stream()
                .filter(strategy -> strategy.support(fileName)).findFirst();

        if (best.isPresent()){
            try {
                InputStream inputStream = new FileInputStream(filePath);
                List<ImageDto> extract = best.get().extract(inputStream);
                // 测试解压结果
                extract.forEach(imageDto -> log.info("解压到一个图片-->"+imageDto.getFileName()));
                tempData = new ArrayList<>();
                tempData.addAll(extract);
            } catch (IOException e) {
                e.printStackTrace();
                return "本地文件解压异常";
            }
        }
        return "本地文件解压成功";

    }

    /**
     * 压缩图片到web
     */
    @ApiOperation("压缩图片到web")
    @PostMapping("/web/compression")
    public String webCompressionDemo(HttpServletResponse response){

        Optional<CompressionStrategy<ImageDto>> best = compressionStrategies.stream()
                .filter(strategy -> strategy.support("demo.zip")).findFirst();

        if (best.isPresent()){
            try {
                // 压缩到指定输出流
                best.get().compression(tempData,response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
                return "web文件压缩异常";
            }
        }
        return "web文件压缩成功";
    }

    /**
     * 压缩图片到local
     */
    @ApiOperation("压缩图片到local")
    @PostMapping("/local/compression")
    public String localCompressionDemo(){

        // 获取当前项目文件夹的的zip文件
        String filePath = System.getProperty("user.dir")+"/demo-zip/src/main/resources/imageTest.zip";
        String fileName = filePath.substring(filePath.lastIndexOf('/')+1);

        try {
            OutputStream os = new FileOutputStream(new File(filePath));
            Optional<CompressionStrategy<ImageDto>> best = compressionStrategies.stream()
                    .filter(strategy -> strategy.support(fileName)).findFirst();
            // 压缩到指定输出流
            if (best.isPresent()){
                best.get().compression(tempData,os);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "压缩图片到本地异常";
        }

        return "压缩图片到本地成功";
    }


}
