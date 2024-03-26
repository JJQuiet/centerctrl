package com.rongpan.centerctrl.demos.web.netty;
import io.netty.channel.Channel;

/**
 * 	蚂蚁舞
 */
public class BootNettyClientChannel {

	//	连接客户端唯一的code
	private String code;

	//	客户端最新发送的消息内容
	private String last_data;

	private transient volatile Channel channel;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public String getLast_data() {
		return last_data;
	}

	public void setLast_data(String last_data) {
		this.last_data = last_data;
	}
}
