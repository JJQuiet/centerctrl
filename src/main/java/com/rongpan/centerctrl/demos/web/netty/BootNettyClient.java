package com.rongpan.centerctrl.demos.web.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 
 * netty 客户端
 * 蚂蚁舞
 */
public class BootNettyClient {

	public void connect(int port, String host) throws Exception{
		
		/**
		 * 客户端的NIO线程组
		 * 
		 */
        EventLoopGroup group = new NioEventLoopGroup();
        
        try {
        	/**
        	 * Bootstrap 是一个启动NIO服务的辅助启动类 客户端的
        	 */
        	Bootstrap bootstrap = new Bootstrap();
        	bootstrap = bootstrap.group(group);
        	bootstrap = bootstrap.channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true);
        	/**
        	 * 设置 I/O处理类,主要用于网络I/O事件，记录日志，编码、解码消息
        	 */
        	bootstrap = bootstrap.handler(new BootNettyChannelInitializer<SocketChannel>());
        	/**
        	 * 连接服务端
        	 */
			ChannelFuture future = bootstrap.connect(host, port).sync();
			if(future.isSuccess()) {
				Channel channel = future.channel();
				String id = future.channel().id().toString();
				BootNettyClientChannel bootNettyClientChannel = new BootNettyClientChannel();
				bootNettyClientChannel.setChannel(channel);
				bootNettyClientChannel.setCode("clientId:"+id);
				BootNettyClientChannelCache.save("clientId:"+id, bootNettyClientChannel);
				System.out.println("netty client start success="+id);
				/**
				 * 等待连接端口关闭
				 */
				future.channel().closeFuture().sync();
			}
		} finally {
			/**
			 * 退出，释放资源
			 */
			group.shutdownGracefully().sync();
		}
        
	}
	

}
