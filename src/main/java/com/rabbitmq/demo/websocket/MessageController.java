package com.rabbitmq.demo.websocket;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 核桃(wy)
 * @date 2020/8/10 14:49
 * @description
 **/
@RestController
public class MessageController {

    /**
     * 主动给不同的人推送消息
     * @param message
     * @param userId
     * @return
     * @throws Exception
     */
    @RequestMapping("/sendTo/{userId}")
    public String sendMsg(String message, @PathVariable String userId) throws Exception {
        MessageDTO dto = new MessageDTO();
        dto.setMessage(message);
        dto.setFromUserId("0");// 标识系统推送
        dto.setToUserId(userId);
        WebSocketServer.sendInfo(dto);
        return "success";
    }
}
