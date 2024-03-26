package com.rongpan.centerctrl.demos.web.netty;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * netty 客户端
 * 蚂蚁舞
 */
@Slf4j
public class BootNettyClientThread extends Thread {

	private final int port;

	private final String address;
	public BootNettyClientThread(int port, String address){
		this.port = port;
		this.address = address;
	}

	public void run() {
		try {
			new BootNettyClient().connect(port, address);
		} catch (Exception e) {
			log.error("modbus tcp connect error "+e.getMessage());
		}
	}
}
