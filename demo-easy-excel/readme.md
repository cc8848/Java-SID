## 前言

Java解析、生成Excel比较有名的框架有Apache poi、jxl。但他们都存在一个严重的问题就是非常的耗内存，poi有一套SAX模式的API可以一定程度的解决一些内存溢出的问题，但POI还是有一些缺陷，比如07版Excel解压缩以及解压后存储都是在内存中完成的，内存消耗依然很大。easyexcel重写了poi对07版Excel的解析，能够原本一个3M的excel用POI sax依然需要100M左右内存降低到几M，并且再大的excel不会出现内存溢出，03版依赖POI的sax模式。在上层做了模型转换的封装，让使用者更加简单方便。  

<p align="right">——<a href="https://github.com/alibaba/easyexcel" target="_blank">easyexcel</a></p>

## 起步

- maven or gradle
- springboot
- api or blog



## 快速上手

[EasyExcelApi](https://alibaba-easyexcel.github.io/quickstart/api.html)

[EasyExcelGitHubUrl](https://github.com/alibaba/easyexcel)



## 简单需求demo


- 内容大致浏览

  ![目录结构](https://images.cnblogs.com/cnblogs_com/quaint/1644214/o_200215053328article003.png)

- 引入easyexcel

引入easyexcel (maven为例)，引入easyexcel

```xml
<dependency>
  <groupId>com.alibaba</groupId>
  <artifactId>easyexcel</artifactId>
  <version>2.1.4</version>
</dependency>
```

- 创建对应Excel的Dto

业务中有各种类型，这里基于java8常用的类型进行测试。

```java
/**
 * 父类 可能业务需要继承
 * @author quaint
 * @date 2020-01-14 11:23
 */
@Data
public class DemoParentDto {

    @ExcelProperty(index = 0,value = {"序号"})
    private Integer num;

}

/**
 * 子类 一般业务一个子类即可
 * @author quaint
 * @date 2020-01-14 11:20
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DemoUserDto extends DemoParentDto{

    @ExcelProperty(value = {"姓名"})
    private String name;

    @ExcelProperty(value = {"性别"})
    private String sex;

    /**
     * @see LocalDateConverter （时间格式转换器）LocalDateTime同理，代码也会贴出来
     */
    @ExcelProperty(value = "生日",converter = LocalDateConverter.class)
    @DateTimeFormat("yyyy-MM-dd")
    private LocalDate birthday;

    @ExcelProperty(value = {"存款"})
    private BigDecimal money;
  
  	/**
     * 获取6个测试数据
     * @return 6个
     */
    public static List<DemoUserDto> getUserDtoTest6(String search){
        List<DemoUserDto> list = new ArrayList<>();
        list.add(new DemoUserDto("quaint","男",LocalDate.of(2011,11,11),BigDecimal.ONE));
        list.add(new DemoUserDto("quaint2","女",LocalDate.of(2001,11,1),BigDecimal.TEN));
        list.add(new DemoUserDto("quaint3","男",LocalDate.of(2010,2,7),new BigDecimal(11.11)));
        list.add(new DemoUserDto("quaint4","男",LocalDate.of(2011,1,11),new BigDecimal(10.24)));
        list.add(new DemoUserDto("quaint5","女",LocalDate.of(2021,5,12),BigDecimal.ZERO));
        list.add(new DemoUserDto(search,"男",LocalDate.of(2010,7,11),BigDecimal.TEN));
        return list;
    }
}
```

- 创建converter（导入导出时自定义转换对应字段）

```java
/**
 * LocalDate and string converter
 * @author quait
 */
public class LocalDateConverter implements Converter<LocalDate> {

    @Override
    public Class supportJavaTypeKey() {
        return LocalDate.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDate convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration){
        // 将excel 中的 数据 转换为 LocalDate
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            return LocalDate.parse(cellData.getStringValue(), DateTimeFormatter.ISO_LOCAL_DATE);
        } else {
            // 获取注解的 format  注意,注解需要导入这个 excel.annotation.format.DateTimeFormat;
            return LocalDate.parse(cellData.getStringValue(),
                DateTimeFormatter.ofPattern(contentProperty.getDateTimeFormatProperty().getFormat()));
        }
    }

    @Override
    public CellData convertToExcelData(LocalDate value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        // 将 LocalDateTime 转换为 String
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            return new CellData(value.toString());
        } else {
            return new CellData(value.format(DateTimeFormatter.ofPattern(contentProperty.getDateTimeFormatProperty().getFormat())));
        }
    }
}


/**
 * LocalDateTime and string converter
 *
 * @author quait
 */
public class LocalDateTimeConverter implements Converter<LocalDateTime> {

    @Override
    public Class supportJavaTypeKey() {
        return LocalDateTime.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDateTime convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration){
        // 将excel 中的 数据 转换为 LocalDateTime
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            return LocalDateTime.parse(cellData.getStringValue(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } else {
            // 获取注解的 format  注意,注解需要导入这个 excel.annotation.format.DateTimeFormat;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(contentProperty.getDateTimeFormatProperty().getFormat());
            return LocalDateTime.parse(cellData.getStringValue(), formatter);
        }
    }

    @Override
    public CellData convertToExcelData(LocalDateTime value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        // 将 LocalDateTime 转换为 String
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            return new CellData(value.toString());
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(contentProperty.getDateTimeFormatProperty().getFormat());
            return new CellData(value.format(formatter));
        }
    }
}
```

- 创建Listener（监听Excel导入）

```java
/**
 * 官方提示:有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
 *
 * 如果想被spring 管理的话, 改为原型模式, Controller 以 getBean 形式获取 本博客展示被spring
 * @author quaint
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@Scope(SCOPE_PROTOTYPE)
@Component
public class DemoUserListener extends AnalysisEventListener<DemoUserDto> {

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;

    private List<DemoUserDto> list = new ArrayList<>();

   /**
     * 方式一
     *  可以换成 @Autowired 注入 service 或者mapper
     *  不被spring管理的话  使用构造函数 接收外面被spring管理的mapper -->constructor
     * @Autowired
     * DemoUserMapper demoUserMapper;
     */
    private List<DemoUserDto> virtualDataBase = new ArrayList<>();

    /**
     * 方式二
     * 假设 virtualDataBase 是 mapper, 这里就在外面new该类的时候传进来  调用方注入过得mapper
     * 并且 把Scope、Component注解去掉
     */
//    public DemoUserListener(List<DemoUserDto> virtualDataBase) {
//        this.virtualDataBase = virtualDataBase;
//    }
  
    /**
     * 这个每一条数据解析都会来调用
     */
    @Override
    public void invoke(DemoUserDto data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSONObject.toJSONString(data));
        list.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            list.clear();
        }
    }

    /**
     * 所有数据解析完成了 会来调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     * @param exception exception
     * @param context context
     * @throws Exception e
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        log.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
        // 如果是某一个单元格的转换异常 能获取到具体行号
        // 如果要获取头的信息 配合invokeHeadMap使用
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException)exception;
            log.error("第{}行，第{}列解析异常", excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex());
        }
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", list.size());
        virtualDataBase.addAll(list);
        log.info("存储数据库成功！");
    }
}
```



- 创建Handler

```java
/**
 * 自定义拦截器。对第一行第一列的头超链接到:https://github.com/alibaba/easyexcel
 * 这里没有采用 spring 管理
 * @author Jiaju Zhuang
 */
@Slf4j
public class CustomCellWriteHandler implements CellWriteHandler {


    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row,
                                 Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {
        log.info("cell 创建之前");

    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell,
                                Head head, Integer relativeRowIndex, Boolean isHead) {
        log.info("cell 创建后");
    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder,
                                 List<CellData> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        // 这里可以对cell进行任何操作
        log.info("第{}行，第{}列写入完成。", cell.getRowIndex(), cell.getColumnIndex());

    }

}
```

- 创建handler控制单元格样式

```java
/**
 * 自定义写Excel handler 实现style 策略。
 * @author quaint
 * @date 14 February 2020
 * @since 1.30
 */
public class ProductWriteErrHandler extends AbstractCellStyleStrategy {


    /**
     * 存储解析失败的行号和列号
     */
    private Map<Integer, Integer> failureRowCol;
    
    /**
     * 可以这么理解: 外部定义样式
     */
    private WriteCellStyle writeErrCellStyle;

    /**
     * 单元格样式
     */
    private CellStyle errCellStyle;

    /**
     * 在这里自定义样式, 或者在外面定义样式
     */
    public ProductWriteErrHandler(WriteCellStyle writeCellStyle,Map<Integer, Integer> failureRowCol) {
        this.writeErrCellStyle = writeCellStyle;
        this.failureRowCol = failureRowCol;
    }

    /**
     * 单元格样式初始化方法
     * @param workbook
     */
    @Override
    protected void initCellStyle(Workbook workbook) {
        // 初始化
        if (writeErrCellStyle!=null){
            errCellStyle = StyleUtil.buildContentCellStyle(workbook, writeErrCellStyle);
        }
    }

    /**
     * 写头部样式
     * @param cell
     * @param head
     * @param relativeRowIndex
     */
    @Override
    protected void setHeadCellStyle(Cell cell, Head head, Integer relativeRowIndex) {

    }

    /**
     * 写内容样式
     * @param cell
     * @param head
     * @param relativeRowIndex
     */
    @Override
    protected void setContentCellStyle(Cell cell, Head head, Integer relativeRowIndex) {
        // 判断 是否传入 错误的 map
        if (!CollectionUtils.isEmpty(failureRowCol)){
            // 如果错误 的行 和列 对应成功 --> 染色
            if (failureRowCol.containsKey(cell.getRowIndex()) 
                    && failureRowCol.get(cell.getRowIndex()).equals(cell.getColumnIndex())){
                cell.setCellStyle(errCellStyle);
            }
        }
    }

}
```



- 控制层Controller

```java
/**
 * @author quaint
 * @date 2020-01-14 11:13
 */
@Controller
@Slf4j
public class DemoEasyExcelSpi implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @PostMapping("/in/excel")
    public String inExcel(@RequestParam("inExcel") MultipartFile inExcel, Model model){

        DemoUserListener demoUserListener = applicationContext.getBean(DemoUserListener.class);

        log.info("demoUserListener 在 spi 调用之前 hashCode为 [{}]", demoUserListener.hashCode());

        if (inExcel.isEmpty()){
            // 读取 local 指定文件
            List<DemoUserDto> demoUserList;
            String filePath = System.getProperty("user.dir")+"/demo-easy-excel/src/main/resources/ExcelTest.xlsx";
            try {
                // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
                EasyExcel.read(filePath, DemoUserDto.class, demoUserListener).sheet().doRead();
                demoUserList = demoUserListener.getVirtualDataBase();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            model.addAttribute("users", demoUserList);

        } else {
            // 读取 web 上传的文件
            List<DemoUserDto> demoUserList;
            try {
                EasyExcel.read(inExcel.getInputStream(), DemoUserDto.class, demoUserListener).sheet().doRead();
                demoUserList = demoUserListener.getVirtualDataBase();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            model.addAttribute("users", demoUserList);
        }
        log.info("demoUserListener 在 spi 调用之后 hashCode为 [{}]", demoUserListener.hashCode());
        return "index";
    }

    @PostMapping("/out/excel")
    public void export(HttpServletResponse response){

        String search = "@RequestBody Object search";
        // 根据前端传入的查询条件 去库里查到要导出的dto
        List<DemoUserDto> userDto = DemoUserDto.getUserDtoTest6(search);
        // 要忽略的 字段
        List<String> ignoreIndices = Collections.singletonList("性别");

        // 根据类型获取要反射的对象
        Class clazz = DemoUserDto.class;

        // 遍历所有字段, 找到忽略的字段
        Set<String> excludeFiledNames = new HashSet<>();
        while (clazz != Object.class){
            Arrays.stream(clazz.getDeclaredFields()).forEach(field -> {
                ExcelProperty ann = field.getAnnotation(ExcelProperty.class);
                if (ann!=null && ignoreIndices.contains(ann.value()[0])){
                    // 忽略 该字段
                    excludeFiledNames.add(field.getName());
                }
            });
            clazz = clazz.getSuperclass();
        }

        // 设置序号
        AtomicInteger i = new AtomicInteger(1);
        userDto.forEach(u-> u.setNum(i.getAndIncrement()));

        // 创建本地文件
       EasyExcelUtils.exportLocalExcel(userDto,DemoUserDto.class,"ExcelTest",excludeFiledNames);
        // 创建web文件
        EasyExcelUtils.exportWebExcel(response,userDto,DemoUserDto.class,"ExcelTest",null);
    }
}
```



- 导出工具类

```java
/**
 * EasyExcelUtils  
 * @author quaint
 * @date 2020-01-14 14:26
 */
public abstract class EasyExcelUtils {


    /**
     * 导出excel
     * @param response http下载
     * @param dataList 导出的数据
     * @param clazz 导出的模板类
     * @param fileName 导出的文件名
     * @param excludeFiledNames 要排除的filed
     * @param <T> 模板
     */
    public static <T> void exportWebExcel(HttpServletResponse response, List<T> dataList, Class<T> clazz,
                                 String fileName, Set<String> excludeFiledNames) {

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        // 单元格样式策略 定义
        WriteCellStyle style = new WriteCellStyle();
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
        style.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.RED.getIndex());

        Map<Integer,Integer> errRecord = new HashMap<>();
        errRecord.put(1,1);
        errRecord.put(2,2);
        ProductWriteErrHandler handler = new ProductWriteErrHandler(style,errRecord);


        try {
            // 导出excel
            EasyExcel.write(response.getOutputStream(), clazz)
                    // 设置过滤字段策略
                    .excludeColumnFiledNames(excludeFiledNames)
                    // 选择导入时的 handler
                    .registerWriteHandler(handler)
                    .sheet("fileName")
                    .doWrite(dataList);
        } catch (IOException e) {
            System.err.println("创建文件异常!");
        }

    }

    /**
     * 导出excel
     * @param dataList 导出的数据
     * @param clazz 导出的模板类
     * @param fileName 导出的文件名
     * @param excludeFiledNames 要排除的filed
     * @param <T> 模板
     */
    public static <T> void exportLocalExcel(List<T> dataList, Class<T> clazz, String fileName,
                                            Set<String> excludeFiledNames){
        //创建本地文件 test 使用
        String filePath = System.getProperty("user.dir")+"/demo-easy-excel/src/main/resources/"+fileName+".xlsx";

        File dbfFile = new File(filePath);
        if (!dbfFile.exists() || dbfFile.isDirectory()) {
            try {
                dbfFile.createNewFile();
            } catch (IOException e) {
                System.err.println("创建文件异常!");
                return;
            }
        }

        // 导出excel
        EasyExcel.write(filePath, clazz)
                .registerWriteHandler(new CustomCellWriteHandler())
                .excludeColumnFiledNames(excludeFiledNames)
                .sheet("SheetName").doWrite(dataList);


    }

}
```

- 前端代码

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<style>
    .data-local{
        border: 1px black;
    }

</style>
<body>

    <form th:action="@{/in/excel}" method="post" enctype="multipart/form-data">
        <input name="inExcel" type="file" value="上传文件"/>
        <input type="submit" value="导入excel"/>
    </form>
    <h2>导入的数据展示位置:</h2>
    <div class="data-local" th:each="user : ${users}">
        <span th:text="${user}"></span>
    </div>

    <form th:action="@{/out/excel}" method="post">
        <input type="submit" value="导出下载文件"/>
    </form>

</body>
</html>
```



- 导入效果图

![效果图](https://images.cnblogs.com/cnblogs_com/quaint/1644214/o_200213135232article001.png)

- 导出效果图

![导出效果图](https://images.cnblogs.com/cnblogs_com/quaint/1644214/o_200215051538article002.png)

## 总结

Listener和Handler的自定义写法可以满足绝大多数需求，大佬设计的代码用起来就是舒服。就是@ExcelProperty注解的index属性的排序混合使用，还需要看源码是如何排序的。这里知识匮乏，望以后可以补充。



