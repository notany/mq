package com.rabbitmq.demo.websocket;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 核桃(wy)
 * @date 2020/8/10 14:22
 * @description
 **/
@ServerEndpoint("/server/{userId}")
@Component
public class WebSocketServer {

    private static Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    private static int onLineCount = 0;// 在线人数
    private static ConcurrentHashMap<String, WebSocketServer> map = new ConcurrentHashMap<>();
    private Session session = null;
    private String userId = "";

    /**
     * 连接建立成功
     * @param session
     * @param userId
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId){
        this.session = session;
        this.userId = userId;
        if(map.containsKey(userId)){
            map.remove(userId);
            map.put(userId, this);
        } else {
            map.put(userId, this);
            addLineCount();
        }
        logger.info("用户userId:{} 加入，当前在线人数:{}", userId, WebSocketServer.onLineCount);
        
        try{
            MessageDTO dto = new MessageDTO();
            dto.setMessage("连接成功");
            sendMsg(dto);
        } catch (Exception e){
            logger.error("网络异常，userId:{}", userId);
            e.printStackTrace();
        }
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose(){
        if(map.containsKey(userId)){
            map.remove(userId);
            subLineCount();
        }
        logger.info("用户:{} 退出，当前在线人数:{}", userId, WebSocketServer.onLineCount);
    }

    /**
     * 收到客户端消息
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session){
        logger.info("用户:{},发送消息:{}", userId, message);
        if(null != message && message != ""){
            MessageDTO dto = JSON.parseObject(message, MessageDTO.class);
            dto.setFromUserId(userId);
            String toUserId = dto.getToUserId();
            if(null != toUserId && toUserId != "" && map.containsKey(toUserId)){
                try {
                    map.get(toUserId).sendMsg(dto);
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                logger.error("用户:{} 不在线!", toUserId);
            }
        }
    }

    /**
     * 主动推送消息
     * @param dto
     * @throws Exception
     */
    public static void sendInfo(MessageDTO dto) throws Exception {
        logger.info("发送消息message:{}", JSON.toJSONString(dto));
        if(map.containsKey(dto.getToUserId())){
            map.get(dto.getToUserId()).sendMsg(dto);
        } else {
            logger.error("用户:{}不在线", dto.getToUserId());
        }
    }

    /**
     * 发生异常
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session, Throwable throwable){
        throwable.printStackTrace();
    }

    private void subLineCount() {
        WebSocketServer.onLineCount --;
    }

    private void sendMsg(MessageDTO dto) throws Exception {
        this.session.getBasicRemote().sendText(JSON.toJSONString(dto));
    }

    private void addLineCount() {
        WebSocketServer.onLineCount ++;
    }
}
