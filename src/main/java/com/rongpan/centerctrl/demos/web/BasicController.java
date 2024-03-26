/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rongpan.centerctrl.demos.web;

import com.rongpan.centerctrl.demos.web.config.GlobalConfig;
import com.rongpan.centerctrl.service.TcpClientService;
import jdk.nashorn.internal.lookup.MethodHandleFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@RequestMapping()
@RestController
@CrossOrigin
@Slf4j
public class BasicController {

    @Autowired
    TcpClientService tcpClientService;
    public static String requestType = "";
    public static String changeStatusResponse = "";

    //hello接口接收三个参数input,output, type
    @GetMapping("/change_status")
    public String change_status(@RequestParam String input, @RequestParam String output, @RequestParam String type) throws ExecutionException, InterruptedException, TimeoutException {
        //打印参数
        log.info("input+type+output:{}",input+type+output+'.');
        //调用tcpClientService发送消息
        requestType = "change_status";
        String res = tcpClientService.send(input+type+output+'.', GlobalConfig.centerIp);
        log.info("res:{}",res);
        return res;
    }
    @GetMapping("/status")
    public void stauts() throws ExecutionException, InterruptedException, TimeoutException {
        requestType = "status";
        tcpClientService.send("Status.", GlobalConfig.centerIp);
    }
    @RequestMapping("/user")
    @ResponseBody
    public User user() {
        User user = new User();
        user.setName("theonefx");
        user.setAge(666);
        return user;
    }
    @RequestMapping("/save_user")
    @ResponseBody
    public String saveUser(User u) {
        return "user will save: name=" + u.getName() + ", age=" + u.getAge();
    }

    @RequestMapping("/html")
    public String html() {
        return "index.html";
    }

    @ModelAttribute
    public void parseUser(@RequestParam(name = "name", defaultValue = "unknown user") String name
            , @RequestParam(name = "age", defaultValue = "12") Integer age, User user) {
        user.setName("zhangsan");
        user.setAge(18);
    }
}
