## 前言

> > 1. Java实现Zip压缩解压可以使用JDK的原生类java.util.zip，但是JDK 7 之前存在中文文件名乱码问题。
> >
> > 2. 使用 ant.jar 的org.apache.tools.zip包，可以避免乱码问题。
> >
> > 3. 使用专门的压缩解压第三方组件，如zip4j，zt-zip等，这种实现方式当然更强大，不过一般场景压缩解压就可以满足需求了。
> >
> >
> > 本博客简单介绍java8下的zip压缩解压。



## 起步

- java8



## 开始

java的io包下运用了装饰模式，对结构不清晰的小伙伴可以先看下装饰模式，在尝试下看java的io包下的源码，来熟悉io操作。大致设计思想：

|      | 装饰成InputStream/OutputStream | 装饰成BufferedStream                       |
| ---- | ------------------------------ | ------------------------------------------ |
| File | FileInputStream(new File)      | BufferedInputStream(new FileInputStream)   |
|      | FileOutputStream(new File)     | BufferedOutputStream(new FileOutputStream) |
|      | **Writer/Reader**              | **BufferedReader/BufferedWriter**          |
|      | FileReader(new File)           | BufferedReader(new FileReader)             |
|      | FileWriter(new File)           | BufferedWriter(new FileWriter)             |

熟悉之后，让我们开始使用ZipInputStream和ZipOutputStream吧。

这里我们采用 **策略模式** 设计demo。

- demo地址

喜欢直接看项目的可以直接 >> [demo-zip](https://github.com/quaintclever/Java-SID/tree/master/demo-zip)

- 代码目录结构

![目录结构](https://images.cnblogs.com/cnblogs_com/quaint/1644214/o_200216053823004.png)



- 抽象压缩策略类

```java
/**
 * @author quaint
 * @date 15 February 2020
 * @since master
 */
public interface CompressionStrategy <T> {

    /**
     * 是否支持
     * @param fileName 文件名称
     * @return true
     */
    boolean support(String fileName);

    /**
     * 提取策略
     * @param inputStream 文件
     * @return 数据
     * @throws IOException io
     */
    List<T> extract(InputStream inputStream) throws IOException;

    /**
     * 压缩策略
     * @param dataList 数据
     * @param os 输出流
     * @throws IOException io
     */
    void compression(List<T> dataList, OutputStream os) throws IOException;

}
```

- zip策略实现

```java
/**
 * @author quaint
 * @date 15 February 2020
 * @since master
 */
@Component
public class ZipImageStrategy implements CompressionStrategy<ImageDto> {

    /**
     * 传入文件类型
     */
    private static final String ZIP_FORMAT = ".zip";

    /**
     * 目标类型
     */
    private static final List<String> TARGET_TYPE = Arrays.asList(".png", ".jpeg", ".jpg", ".gif");

    @Override
    public boolean support(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return false;
        }
        return fileName.endsWith(ZIP_FORMAT);
    }

    @Override
    public List<ImageDto> extract(InputStream inputStream) throws IOException {

        if (inputStream == null){
            return null;
        }

        // 定义储存数据的list
        List<ImageDto> dataList = new ArrayList<>();

        // 把输入流 包装为 压缩流
        ZipInputStream zis = new ZipInputStream(inputStream);
        ZipEntry ze;
        while ((ze = zis.getNextEntry()) != null) {

            String name = ze.getName();
            // 过滤掉 多余的文件/不是图片的文件
            if (ze.isDirectory() || name == null || name.contains("__MACOSX") || name.contains(".DS_Store")
                    || !TARGET_TYPE.contains(name.substring(name.lastIndexOf('.')))) {
                continue;
            }

            // 添加图片到集合
            ImageDto imageDto = new ImageDto();
            imageDto.setFileName(name.substring(name.lastIndexOf(File.separator) + 1));

            // 将文件转换为 byte 数组
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int n;
            while(-1 != (n = zis.read(buffer))) {
                output.write(buffer, 0, n);
            }

            imageDto.setBytes(output.toByteArray());
            dataList.add(imageDto);

        }
        zis.close();
        return dataList;
    }

    @Override
    public void compression(List<ImageDto> dataList, OutputStream os) throws IOException {

        if (CollectionUtils.isEmpty(dataList) || os == null){
            return;
        }

        // 把输出流包装为 压缩流
        ZipOutputStream zos = new ZipOutputStream(os);

        // 循环写压缩文件
        for (ImageDto file : dataList) {
            ZipEntry ze = new ZipEntry(file.getFileName());
            zos.putNextEntry(ze);
            zos.write(file.getBytes(),0,file.getBytes().length);
            zos.closeEntry();
        }
        zos.close();
    }
    
}
```

- 图片dto

```java
/**
 * 图片实体类,简单版
 * @author quaint
 * @date 15 February 2020
 * @since master
 */
@Data
public class ImageDto {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件字节码
     */
    private byte[] bytes;

}
```

- spi接口

```java
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
```



## 致谢

一直往前走，别往后看。顺其自然，内心就会逐渐清朗，时光越老，人心越淡。常怀宽容感激之心，宽容那就是一种美德是一种智慧，海纳百川是多么广阔，感激你的朋友，是他们给了你帮助；感激你的敌人，是他们是让你变的坚强。感谢你的阅读，你努力的样子很可爱呀。