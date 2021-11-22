package top.meethigher.springbootstrategymode.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import top.meethigher.springbootstrategymode.annotations.MsgType;
import top.meethigher.springbootstrategymode.enums.MessageType;
import top.meethigher.springbootstrategymode.handler.MessageHandler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 将策略注册到Map容器中
 *
 * @author chenchuancheng
 * @since 2021/11/22 14:38
 */
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
