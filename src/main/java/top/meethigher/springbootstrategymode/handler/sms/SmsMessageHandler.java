package top.meethigher.springbootstrategymode.handler.sms;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import top.meethigher.springbootstrategymode.annotations.MsgType;
import top.meethigher.springbootstrategymode.enums.MessageType;
import top.meethigher.springbootstrategymode.handler.MessageHandler;

/**
 * @author chenchuancheng
 * @since 2021/11/22 14:20
 */
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
