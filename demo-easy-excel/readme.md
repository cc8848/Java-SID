# demo-mail

## DESC: 

 - spring integration easy excel

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

#### 编写spi测试

```java
@RestController
public class DemoEasyExcelSpi {

    @PostMapping("/out/excel")
    public void export(HttpServletResponse response){

        // === 假装有前端 start ===
        Object search = "@RequestBody Object search";
        // 根据前端传入的查询条件 去库里查到要导出的dto
        List<DemoUserDto> userDto = DemoUserDto.getUserDtoTest6(search);
        // 要忽略的 字段
        List<String> ignoreIndices = Collections.singletonList("性别");
        // === 假装有前端 end ===

        // 根据类型获取要反射的对象
        Class clazz = DemoUserDto.class;

        // 遍历所有字段, 找到忽略的字段
        Set<String> excludeFiledNames = new HashSet<>();
        while (clazz != Object.class){
            Arrays.stream(clazz.getDeclaredFields()).forEach(field -> {
                ExcelProperty ann = field.getAnnotation(ExcelProperty.class);
                if (ann!=null && ignoreIndices.contains(ann.value()[0])){
                    // json 忽略 该字段
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
        // 创建web文件 需要编写前端代码, 详情参考项目代码
//        EasyExcelUtils.exportWebExcel(response,userDto,DemoUserDto.class,"ExcelTest",null);

    }
}
```
