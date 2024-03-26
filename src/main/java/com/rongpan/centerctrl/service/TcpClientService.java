package com.rongpan.centerctrl.service;

import com.rongpan.centerctrl.demos.web.BasicController;
import com.rongpan.centerctrl.demos.web.netty.*;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
@Slf4j
@Service
public class TcpClientService {
    //    public static volatile Map<String,Date> sendOverTimeMap = new ConcurrentHashMap<>();
    public static Promise<String> promise;

    @Async
    public void tcpClientRun(String host, int port) {
        String address = host;
        BootNettyClientThread thread = new BootNettyClientThread(port, address);
        thread.start();
    }
    @Async
    public void tcpClientRun(String host) {
        tcpClientRun(host, 50600);
    }
    public boolean getIsConnect(String host) {
        if (BootNettyClientChannelCache.channelMapCache.size() > 0) {
            for (Map.Entry<String, BootNettyClientChannel> entry : BootNettyClientChannelCache.channelMapCache.entrySet()) {
                BootNettyClientChannel bootNettyChannel = entry.getValue();
                if (bootNettyChannel != null && bootNettyChannel.getChannel().isOpen()) {
                    SocketAddress address = bootNettyChannel.getChannel().remoteAddress();
                    String hostName = ((InetSocketAddress) address).getHostName();
                    if (hostName.equals(host)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public String send(String hexStr, String host) throws ExecutionException, InterruptedException, TimeoutException {
        byte[] asciiBytes = hexStr.getBytes(StandardCharsets.US_ASCII);
        promise = null;
        if (BootNettyClientChannelCache.channelMapCache.size() > 0) {
            for (Map.Entry<String, BootNettyClientChannel> entry : BootNettyClientChannelCache.channelMapCache.entrySet()) {
                BootNettyClientChannel bootNettyChannel = entry.getValue();
                if (bootNettyChannel != null && bootNettyChannel.getChannel().isOpen()) {
                    SocketAddress address = bootNettyChannel.getChannel().remoteAddress();
                    String hostName = (((InetSocketAddress) address).getAddress()).getHostAddress();
                    if (host.equals(hostName)) {
                        Channel channel = bootNettyChannel.getChannel();
                        //新建一个Promise
                        promise = channel.eventLoop().newPromise();
                        //添加promise接收ChannelInboundHandlerAdapter中ChannelRead中获取的数据

                        channel.pipeline().addLast(new BootNettyChannelInboundHandlerAdapter());
                        //发送数据
                        channel.writeAndFlush(Unpooled.buffer().writeBytes(asciiBytes));
                        break;
                    }
                }
            }
        }
        if(promise != null && BasicController.requestType.equals("change_status")) {
            return promise.get(10, TimeUnit.SECONDS);

        }
        return hexStr;
    }
}

