package com.rongpan.centerctrl.demos.web.netty;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  蚂蚁舞
 */
public class BootNettyClientChannelCache {

    public static volatile Map<String, BootNettyClientChannel> channelMapCache = new ConcurrentHashMap<String, BootNettyClientChannel>();

    public static void add(String code, BootNettyClientChannel channel){
    	channelMapCache.put(code,channel);
    }

    public static BootNettyClientChannel get(String code){
        return channelMapCache.get(code);
    }

    public static void remove(String code){
    	channelMapCache.remove(code);
    }

    public static  void removeAll(){
        channelMapCache = new ConcurrentHashMap<String, BootNettyClientChannel>();
    }

    public static void save(String code, BootNettyClientChannel channel) {
        if(channelMapCache.get(code) == null) {
            add(code,channel);
        }
    }
    public static void removeByHost(String host){
        for (Map.Entry<String, BootNettyClientChannel> entry : channelMapCache.entrySet()) {
            BootNettyClientChannel bootNettyChannel = entry.getValue();
            SocketAddress address = bootNettyChannel.getChannel().remoteAddress();
            String hostName = ((InetSocketAddress) address).getHostName();
            if(host.equals(hostName))
            {
                channelMapCache.remove(entry.getKey());
            }
        }
    }


}
