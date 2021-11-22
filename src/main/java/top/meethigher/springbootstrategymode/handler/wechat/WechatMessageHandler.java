package top.meethigher.springbootstrategymode.handler.wechat;

import org.springframework.stereotype.Service;
import top.meethigher.springbootstrategymode.annotations.MsgType;
import top.meethigher.springbootstrategymode.enums.MessageType;
import top.meethigher.springbootstrategymode.handler.MessageHandler;

/**
 * @author chenchuancheng
 * @since 2021/11/22 14:18
 */
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
