package com.rongpan.centerctrl.demos.web.netty;


import com.rongpan.centerctrl.demos.web.config.GlobalConfig;
import com.rongpan.centerctrl.service.TcpClientService;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  蚂蚁舞
 */
@Slf4j
@Service
public class BootNettyHeartTimer {

    @Autowired
    TcpClientService tcpClientService;


    static HashMap<String,Boolean> warnHostMap = new HashMap<>();

    @Scheduled(fixedDelay=20000)
    public void heart_timer() {
        if(GlobalConfig.isRunnerSuccess == false)return;
        if(warnHostMap.size()<=0)
        {
            this.init();
        }
        HashMap<String,Boolean> resetMap = warnHostMap;
        for (Map.Entry<String, Boolean> entry : resetMap.entrySet()) {
            resetMap.put(entry.getKey(),false);
        }
        if(BootNettyClientChannelCache.channelMapCache.size() > 0){
            for (Map.Entry<String, BootNettyClientChannel> entry : BootNettyClientChannelCache.channelMapCache.entrySet()) {
                BootNettyClientChannel bootNettyChannel = entry.getValue();
                if(bootNettyChannel != null && bootNettyChannel.getChannel().isOpen()){
//
                    SocketAddress address = bootNettyChannel.getChannel().remoteAddress();
                    String hostName = ( ((InetSocketAddress) address).getAddress()).getHostAddress();
                    if(warnHostMap.containsKey(hostName))
                    {
                        warnHostMap.put(hostName,true);
                    }
                }
                else if(bootNettyChannel != null)
                {
                    SocketAddress address = bootNettyChannel.getChannel().remoteAddress();
                    String hostName = ( ((InetSocketAddress) address).getAddress()).getHostAddress();
                    if(warnHostMap.containsKey(hostName)){
                        warnHostMap.put(hostName,false);
                    }
                    bootNettyChannel.getChannel().close();

                }
            }
        }
        HashMap<String,Boolean> map = warnHostMap;
        for (Map.Entry<String, Boolean> entry : map.entrySet()) {
            if(entry.getValue() == false)
            {
                //断线了 清除数据 开始重连
                log.error(entry.getKey()+" modbus heart_timer 断线了 清除数据 开始重连 ");
                BootNettyClientChannelCache.removeByHost(entry.getKey());
                tcpClientService.tcpClientRun(entry.getKey(),4001);
            }
        }
    }
    private void init()
    {
        warnHostMap.put(GlobalConfig.centerIp,false);
    }
}
