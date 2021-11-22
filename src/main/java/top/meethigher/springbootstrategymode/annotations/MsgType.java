package top.meethigher.springbootstrategymode.annotations;

import top.meethigher.springbootstrategymode.enums.MessageType;

import java.lang.annotation.*;

/**
 * @author chenchuancheng
 * @since 2021/11/22 14:06
 */
@Target({ElementType.TYPE})//作用在类上
@Retention(RetentionPolicy.RUNTIME)//当前被描述的注解，会保留到class字节码文件中，并被jvm读取到。一般也只会用到这个
@Documented//注解被抽取到api文档中
@Inherited//注解被子类继承
public @interface MsgType {
    MessageType value();
}
