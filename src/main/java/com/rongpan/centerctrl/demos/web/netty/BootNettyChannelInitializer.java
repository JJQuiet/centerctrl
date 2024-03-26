package com.rongpan.centerctrl.demos.web.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * 通道初始化
 * 蚂蚁舞
 */
@ChannelHandler.Sharable
public class BootNettyChannelInitializer<SocketChannel> extends ChannelInitializer<Channel> {

	@Override
	protected void initChannel(Channel ch) throws Exception {

        ch.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
        ch.pipeline().addLast("decoder", new ModbusDecode());
        /**
         * 自定义ChannelInboundHandlerAdapter
         */
//        ch.pipeline().addLast(new BootNettyChannelInboundHandlerAdapter());
		
	}

}
