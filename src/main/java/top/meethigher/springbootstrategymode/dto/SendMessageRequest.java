package top.meethigher.springbootstrategymode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import top.meethigher.springbootstrategymode.enums.MessageType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author chenchuancheng
 * @since 2021/11/22 15:54
 */
@ApiModel(value = "SendMessageRequest", description = "发送消息请求")
public class SendMessageRequest {
    @ApiModelProperty(value = "消息类型")
    @NotNull
    private MessageType messageType;

    @ApiModelProperty(value = "消息内容")
    @NotBlank
    private String message;

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
