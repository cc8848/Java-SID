# demo-easy-excel

## DESC: 

 - spring integration easy excel

## 官方链接

https://alibaba-easyexcel.github.io/index.html

## 快速上手:

#### pom.xml

```xml
<dependencies>
    <!-- spring 等公用部分由 父工程提供, 这里仅展示核心 -->
    <!-- https://mvnrepository.com/artifact/com.alibaba/easyexcel -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>easyexcel</artifactId>
        <version>2.1.4</version>
    </dependency>
</dependencies>
```

#### 编写Dto

```java
@Data
public class DemoParentDto {

    @ExcelProperty(index = 0,value = {"序号"})
    private Integer num;

}
```

```java
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
     * @see LocalDateStringConverter
     */
    @ExcelProperty(value = "生日",converter = LocalDateStringConverter.class)
    @DateTimeFormat("yyyy-MM-dd")
    private LocalDate birthday;

    @ExcelProperty(value = {"年龄"})
    private Integer age;


    /**
     * 获取6个测试数据
     * @return 6个
     */
    public static List<DemoUserDto> getUserDtoTest6(Object search){
        List<DemoUserDto> list = new ArrayList<>();
        list.add(new DemoUserDto("quaint","男",LocalDate.of(2011,11,11),9));
        list.add(new DemoUserDto("quaint2","女",LocalDate.of(2001,11,1),19));
        list.add(new DemoUserDto("quaint3","男",LocalDate.of(2010,2,7),10));
        list.add(new DemoUserDto("quaint4","男",LocalDate.of(2011,1,11),9));
        list.add(new DemoUserDto("quaint5","女",LocalDate.of(2021,5,12),-1));
        list.add(new DemoUserDto("quaint6","男",LocalDate.of(2010,7,11),10));
        return list;
    }

}
```

#### 编写工具类

```java
/**
 * EasyExcelUtils
 * @author quaint
 * @date 2020-01-14 14:26
 */
public abstract class EasyExcelUtils {

    /**
     * 读取web excel demo , 本地请参考项目内代码
     * @param file web 文件
     * @param clazz 指定转化类
     * @param <T> 类的泛型
     * @return web导入的数据
     */
    public static <T> List<T> readWebExcel(MultipartFile file, Class<T> clazz){

        DemoUserListener demoUserListener = new DemoUserListener();
        try {
            EasyExcel.read(file.getInputStream(), clazz, demoUserListener).sheet().doRead();
            return (List<T>) demoUserListener.getVirtualDataBase();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 导出excel web下载, 非web请参考项目内代码
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

        try {
            // 导出excel
            EasyExcel.write(response.getOutputStream(), clazz)
                    .excludeColumnFiledNames(excludeFiledNames)
                    .sheet("fileName")
                    .doWrite(dataList);
        } catch (IOException e) {
            System.err.println("创建文件异常!");
        }

    }

}
```

#### 编写监听器(导入使用)
```java
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class DemoUserListener extends AnalysisEventListener<DemoUserDto> {

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;

    private List<DemoUserDto> list = new ArrayList<>();

    /**
     * 虚拟数据库
     */
    private List<DemoUserDto> virtualDataBase = new ArrayList<>();


    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data
     *            one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
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
     * 所有数据解析完成了 都会来调用
     *
     * @param context
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

#### 编写自定义转换器(LocalDate 导入导出 转换)

```java
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
    public LocalDateTime convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
                                           GlobalConfiguration globalConfiguration){
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
    public CellData convertToExcelData(LocalDateTime value, ExcelContentProperty contentProperty,
                                       GlobalConfiguration globalConfiguration) {
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

#### 编写spi测试

```java
@Controller
public class DemoEasyExcelSpi {
    
    @PostMapping("/in/excel")
    public String inExcel(@RequestParam("inExcel") MultipartFile inExcel, Model model){

        if (inExcel.isEmpty()){
            // 读取 local 指定文件
            List<DemoUserDto> demoUserList = EasyExcelUtils.readLocalExcel("ExcelTest", DemoUserDto.class);
            model.addAttribute("users", demoUserList);
        } else {
            // 读取 web 上传的文件
            List<DemoUserDto> demoUserList = EasyExcelUtils.readWebExcel(inExcel, DemoUserDto.class);
            model.addAttribute("users", demoUserList);
        }
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
