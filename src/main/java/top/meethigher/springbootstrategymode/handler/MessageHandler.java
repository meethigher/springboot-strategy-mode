package top.meethigher.springbootstrategymode.handler;
/**
 * @author chenchuancheng
 * @since 2021/11/22 14:13
 */
public interface MessageHandler {

    /**
     * 发送消息
     * @param msg
     */
    String sendMessage(String msg);
}
