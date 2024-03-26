package com.rongpan.centerctrl.demos.web.netty;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.rongpan.centerctrl.demos.web.BasicController;
import com.rongpan.centerctrl.service.TcpClientService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;
import com.rongpan.centerctrl.service.MyWebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
/**
 * 
 * I/O数据读写处理类
 * 蚂蚁舞
 */
@Slf4j
@ChannelHandler.Sharable
public class BootNettyChannelInboundHandlerAdapter extends ChannelInboundHandlerAdapter{
//    private final Promise<String> promise;
    public static StringBuilder accumulatedMsg = new StringBuilder();
    public MyWebSocketHandler myWebSocketHandler = new MyWebSocketHandler();
    private Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

//    public  BootNettyChannelInboundHandlerAdapter(Promise<String> promise, StringBuilder accumulatedMsg) {
//        this.promise = promise;
//    }
    /**
     * 从服务端收到新的数据时，这个方法会在收到消息时被调用
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception, IOException {
        if(BasicController.requestType.equals("change_status")){
//            MyWebSocketHandler.broadcastMessage("ChangeSuccess");
            BasicController.changeStatusResponse = "ChangeSuccess";
            if(!TcpClientService.promise.isSuccess()){
                TcpClientService.promise.setSuccess("ChangeSuccess");
            }
//            accumulatedMsg.setLength(0);
        }
//        BootNettyClientChannel bootNettyClientChannel = BootNettyClientChannelCache.get("clientId:"+ctx.channel().id().toString());
//        if(bootNettyClientChannel != null && BasicController.requestType.equals(("status"))) {
//            accumulatedMsg.append(msg.toString());
//        }
//        bootNettyClientChannel.setLast_data(msg.toString());
        if(BasicController.requestType.equals("status")) {
            //如果msg是ParamVdieoChnOutIn:008GHFEHBBBENDParamAVideoChnOutIn:012GHFEHBBBICCEEND V: 06，则去除V:之前的内容，msg变成V: 06
            if(msg.toString().contains("ParamVdeioChnOutIn") || msg.toString().contains("ParamAudioChnOutIn") || msg.toString().contains("ParamAVideoChnOutIn")) {
                String[] msgArray = msg.toString().split("V:");
                if(msgArray.length > 1) {
                    msg = "V:" + msgArray[1];
                }
            }
            accumulatedMsg.append(msg.toString());
        }



    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws IOException {
        String msg = accumulatedMsg.toString();
        //不包含ParamAVideoChnOutIn的消息，且长度大于100的消息，广播出去
        if(msg.length() > 100 && msg.contains("->") && !msg.contains("ParamAVideoChnOutIn")) {
            log.info("channelReadComplete:{}",msg);
            log.info("msg length:{}",msg.length());
            MyWebSocketHandler.broadcastMessage(msg);
            accumulatedMsg.setLength(0);
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws IOException {
    	System.out.println("exceptionCaught");
        cause.printStackTrace();
        ctx.close();//抛出异常，断开与客户端的连接
    }
    /**
     * 客户端与服务端第一次建立连接时 执行
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception, IOException {
        super.channelActive(ctx);
        InetSocketAddress inSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = inSocket.getAddress().getHostAddress();
        System.out.println(clientIp);
    }

    /**
     * 客户端与服务端 断连时 执行
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception, IOException {
        super.channelInactive(ctx);
        InetSocketAddress inSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = inSocket.getAddress().getHostAddress();
        ctx.close(); //断开连接时，必须关闭，否则造成资源浪费
        System.out.println("channelInactive:"+clientIp);
    }

}
