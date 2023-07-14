package com.rabbitmq.demo.websocket;

/**
 * @author 核桃(wy)
 * @date 2020/8/10 14:57
 * @description
 **/
public class MessageDTO {

    private String fromUserId;
    private String toUserId;
    private String message;

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
