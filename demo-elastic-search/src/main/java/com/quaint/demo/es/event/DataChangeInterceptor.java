package com.quaint.demo.es.event;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.quaint.demo.es.enums.DataGetMode;
import com.quaint.demo.es.enums.DataType;
import com.quaint.demo.es.po.DemoArticlePO;
import com.quaint.demo.es.utils.MybatisUtils;
import com.quaint.demo.es.utils.ReflectUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author quaint
 * @date 2020-01-04 16:23
 */
@Slf4j
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class DataChangeInterceptor implements Interceptor {

    /**
     * 默认需要处理的方法以及它的DataGetMode
     */
    private static Map<String, DataGetMode> DEFAULT_METHODS_MAP;

    /**
     * 需要处理的ModelClass以及它的DataType
     */
    private static Map<Class, DataType> MODEL_CLASS_MAP;

    static{
        DEFAULT_METHODS_MAP = new HashMap<>();
        DEFAULT_METHODS_MAP.put("insert", DataGetMode.ID_OF_ENTITY);
        DEFAULT_METHODS_MAP.put("deleteById", DataGetMode.ID_OF_ENTITY);
        DEFAULT_METHODS_MAP.put("updateById", DataGetMode.ID_OF_ENTITY);

        MODEL_CLASS_MAP = new HashMap<>();
        MODEL_CLASS_MAP.put(DemoArticlePO.class,DataType.DEMO_ARTICLE_TYPE);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 执行executor操作
        Object returnVal = invocation.proceed();

        // 创建context
        Context context = new Context(invocation);

        // 处理默认支持的方法
        resolveDefault(context)
                // 处理自定义支持的方法
                .switchIfEmpty(resolveCustomize(context))
                // 过滤出能publish的context
                .filter(this::canPublish)
                // publish事件
                .doOnNext(this::publish)
                // 遇到异常直接变成Mono.empty()
                .onErrorResume(e-> Mono.empty())
                // 订阅之后的准备操作
                .doOnSubscribe(subscription -> this.prepare(subscription,context))
                // 订阅处理
                .subscribe();

        return returnVal;
    }

    /**
     * 处理默认支持的方法
     * @param context
     * @return
     */
    private Mono<Context> resolveDefault(Context context){
        return Flux.fromIterable(DEFAULT_METHODS_MAP.keySet())
                // 过滤出支持的方法
                .filter(methodName -> methodName.equals(context.getMapperMethodName()))
                // 转成methodName-IdGetMethod结构
                .map(methodName-> context.setDataGetMethod(DEFAULT_METHODS_MAP.get(methodName)))
                // 取出一个
                .next();
    }

    /**
     * 处理自定义支持的方法(dataChange注解)
     * @param context
     * @return
     */
    private Mono<Context> resolveCustomize(Context context){
        return Mono.fromSupplier(()->context.getMapperClass())
                // 找到所有包含DataChange注解的方法
                .flatMapMany(mapperClass->Flux.fromIterable(MethodUtils.getMethodsListWithAnnotation(mapperClass,DataChange.class)))
                // 找到当前方法
                .filter(method -> method.getName().equals(context.getMapperMethodName()))
                // 取第一个
                .next()
                // 将Mono转换为DataChange
                .map(method -> method.getAnnotation(DataChange.class))
                // 将dataGetMethod设置到context,并将Mono转换为Context
                .map(dataChange -> context.setDataGetMethod(dataChange.value()));
    }

    /**
     * 准备操作
     * @param subscription
     * @param context
     */
    private void prepare(Subscription subscription, Context context){
        try{
            Class mapperClass = ClassUtils.getClass(context.getMapperClassName());
            Class modelClass = MybatisUtils.extractModelClass(mapperClass);

            context.setMapperClass(mapperClass);
            context.setModelClass(modelClass);
        }catch (Exception e){
            subscription.cancel();
            log.warn("发生异常["+e.getMessage()+"],取消dataChange插件处理逻辑",e);
        }
    }

    /**
     * 是否能publish
     * @param context
     * @return
     */
    private boolean canPublish(Context context){

        DataType dataType = MODEL_CLASS_MAP.get(context.getModelClass());
        // 如果找不到对应的dataType则直接return;
        if(dataType == null){
            log.warn("不支持该modelClass:[{}]",context.getModelClass());
            return false;
        }
        context.setDataType(dataType);
        return context.getDataGetMethod() != null;
    }

    /**
     * 准备并发布事件
     * @param context
     */
    private void publish(Context context){
        try{
            // 找到modelClass类型的参数
            Object arg =  context.getInvocation().getArgs()[1];

            // 发布事件
            // 引用类型直接取参数作为data
            if(DataGetMode.REFERENCE.equals(context.getDataGetMethod())){
                DataChangeEvent.add(context.getDataType(),arg).publish();
                // 如果是实体ID,通过反射取出作为data
            }else if(DataGetMode.ID_OF_ENTITY.equals(context.getDataGetMethod())){
                // 从ParamMap中取出实体
                if(arg instanceof MapperMethod.ParamMap){
                    arg = ((MapperMethod.ParamMap) arg)
                            .get(Constants.ENTITY);
                }
                Object data = ReflectUtils.getPropertyValue("id",arg);
                DataChangeEvent.add(context.getDataType(),data).publish();
            }else{
                log.warn("不支持该DataGetMethod:[{}]",context.getDataGetMethod());
            }
        }catch (Exception e){
            log.warn(e.getMessage(),e);
        }
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 上下文
     */
    @Data
    @Accessors(chain = true)
    private class Context {
        // mapper类名
        private final String mapperClassName;
        // mapper方法名
        private final String mapperMethodName;
        private final Invocation invocation;
        // 数据获取方式
        private DataGetMode dataGetMethod;
        // 数据类型
        private DataType dataType;

        private Class mapperClass;
        private Class modelClass;

        public Context(Invocation invocation) {
            MappedStatement ms = (MappedStatement)invocation.getArgs()[0];
            this.mapperClassName = StringUtils.substringBeforeLast(ms.getId(), ".");
            this.mapperMethodName = StringUtils.substringAfterLast(ms.getId(), ".");
            this.invocation = invocation;
        }

    }
}
