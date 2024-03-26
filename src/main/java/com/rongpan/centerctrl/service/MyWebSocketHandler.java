package com.rongpan.centerctrl.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import org.json.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
@Component
public class MyWebSocketHandler extends TextWebSocketHandler {
    private static Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 连接建立后的逻辑
        log.info("连接建立");
        sessions.add(session);
        log.info("sessions size:{}",sessions.size());
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 处理收到的文本消息
        log.info("收到消息：" + message.getPayload());
        //{"CameraName":"单兵1","LAT_VAL":"30.429385999999997","LNG_VAL":"120.28949"}
        // 是网页的话，连接成功后发一下，我是网页
//        broadcastMessage( message.getPayload());
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 连接关闭后的逻辑
        log.info("连接关闭");
        sessions.remove(session);
    }
    public static void broadcastMessage(String message) {
        log.info("sessions size:{}",sessions.size());
        for (WebSocketSession session : sessions) {
            try {
                if (session.isOpen() ) {
                    session.sendMessage(new TextMessage(message));
                }
            } catch (IOException e) {
                log.error("Error during broadcasting message", e);
            }
        }
    }

}