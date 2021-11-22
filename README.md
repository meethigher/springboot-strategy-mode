源码[meethigher/springboot-strategy-mode](https://github.com/meethigher/springboot-strategy-mode)

参考文章

* [SpringBoot下的策略模式，消灭了大量的ifelse，真香！_程序新视界-CSDN博客](https://blog.csdn.net/wo541075754/article/details/109253234)

* [springboot基于注解方式实现策略模式_aogula的博客-CSDN博客](https://blog.csdn.net/aogula/article/details/112308725)，主要抄袭自这篇文章

* [适配器模式和策略模式 - 知乎](https://zhuanlan.zhihu.com/p/351174501)

* [@SuppressWarnings注解常见用法_小楼夜听雨的博客-CSDN博客](https://blog.csdn.net/qq_37855749/article/details/119866558)

还是来自于工作上的一点心得。之前我做的数据库的通用调用存储过程的代码，是使用抽象工厂来实现的，里面有if..else..的操作。如果要频繁的新加数据库实现逻辑，就要不断的添加实现类和else if。这边就想用注解的方式，来去除if..else..

案例：发送不同类型的消息

创建注解

```java
@Target({ElementType.TYPE})//作用在类上
@Retention(RetentionPolicy.RUNTIME)//当前被描述的注解，会保留到class字节码文件中，并被jvm读取到。一般也只会用到这个
@Documented//注解被抽取到api文档中
@Inherited//注解被子类继承
public @interface MsgType {
    MessageType value();
}
```

创建类型

```java
public enum MessageType {
    /**
     * 微信·
     */
    WECHAT_MSG,
    /**
     * 短信
     */
    SMS_MSG
}
```

创建接口

```java
public interface MessageHandler {

    /**
     * 发送消息
     * @param msg
     */
    String sendMessage(String msg);
}
```

创建SMS实现类

```java
@Service
@MsgType(value = MessageType.SMS_MSG)
public class SmsMessageHandler implements MessageHandler {
    @Override
    public String sendMessage(String msg) {
        String message = "短信消息：" + msg;
        System.out.println(message);
        return message;
    }
}
```

创建WECHAT实现类

```java
@Service
@MsgType(value = MessageType.WECHAT_MSG)
public class WechatMessageHandler implements MessageHandler {
    @Override
    public String sendMessage(String msg) {
        String message = "微信消息：" + msg;
        System.out.println(message);
        return message;
    }
}
```

创建配置类


1. 通过注解拿到所有被标注的bean类
2. 遍历所有bean，拿到bean的类型、字节码
3. 将类型、字节码存入全局map
4. 使用时，通过类型，将字节码取出，instance或者通过spring放入bean容器


```java
@Component
public class MessageConfig implements ApplicationContextAware {
    private static Map<MessageType, Class<MessageHandler>> messageTypeClassMap = new HashMap<>();

    @Autowired
    private ApplicationContext applicationContext;


    /**
     * 1. 通过注解拿到所有被标注的bean类
     * 2. 遍历所有bean，拿到bean的类型、字节码
     * 3. 将类型、字节码存入全局map
     * 4. 使用时，通过类型，将字节码取出，instance或者通过spring放入bean容器
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //比较平易近人的写法
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(MsgType.class);
        Iterator<String> iterator = beans.keySet().iterator();
        while (iterator.hasNext()) {
            String beanName = iterator.next();
            @SuppressWarnings("unchecked")
            Class<MessageHandler> messageHandlerClass = (Class<MessageHandler>) beans.get(beanName).getClass();
            MessageType messageType = messageHandlerClass.getAnnotation(MsgType.class).value();
            messageTypeClassMap.put(messageType, messageHandlerClass);
        }
        //比较装逼的写法
//        //获取所有带有指定注解的Bean对象
//        applicationContext.getBeansWithAnnotation(MsgType.class)
//                .entrySet()
//                .iterator()
//                .forEachRemaining(stringObjectEntry -> {
//                    Class<MessageHandler> messageHandlerClass = (Class<MessageHandler>) stringObjectEntry.getValue().getClass();
//                    MessageType messageType = messageHandlerClass.getAnnotation(MsgType.class).value();
//                    messageTypeClassMap.put(messageType, messageHandlerClass);
//                });
    }

    /**
     * 通过类型拿到实例化的对象
     * @param messageType
     * @return
     */
    public MessageHandler getMessageHandler(MessageType messageType) {
        Class<MessageHandler> messageHandlerClass = messageTypeClassMap.get(messageType);
        if (ObjectUtils.isEmpty(messageHandlerClass)) {
            throw new IllegalArgumentException("没有指定类型");
        }
        return applicationContext.getBean(messageHandlerClass);
    }
}
```

