package com.rongpan.centerctrl;

import com.rongpan.centerctrl.demos.web.config.GlobalConfig;
import com.rongpan.centerctrl.service.TcpClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class CenterctrlRunner implements org.springframework.boot.ApplicationRunner {
    @Autowired
    TcpClientService tcpClientService;
    public void run(ApplicationArguments args){
        tcpClientService.tcpClientRun(GlobalConfig.centerIp,4001);
        GlobalConfig.isRunnerSuccess = true;
    }
}
