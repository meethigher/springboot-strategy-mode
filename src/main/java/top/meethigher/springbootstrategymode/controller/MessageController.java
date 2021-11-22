package top.meethigher.springbootstrategymode.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.meethigher.springbootstrategymode.config.MessageConfig;
import top.meethigher.springbootstrategymode.dto.SendMessageRequest;
import top.meethigher.springbootstrategymode.handler.MessageHandler;

/**
 * @author chenchuancheng
 * @since 2021/11/22 15:46
 */
@RestController
@Api(value = "MessageController", tags = "消息")
public class MessageController {

    @Autowired
    private MessageConfig messageConfig;

    @PostMapping("/sendMessage")
    @ApiOperation(value = "发送消息", notes = "发送消息")
    public String sendMessage(@RequestBody @Validated SendMessageRequest request) {
        MessageHandler messageHandler = messageConfig.getMessageHandler(request.getMessageType());
        return messageHandler.sendMessage(request.getMessage());
    }
}
