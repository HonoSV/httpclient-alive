package cn.th.alive.controller;

import cn.th.alive.service.send.SendService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SendController {

    @Resource
    private SendService sendService;

    @PostMapping("/send")
    public String send(String text) {
        sendService.send(text);
        return "200 OK";
    }

    @PostMapping("/lg")
    public Object getLG() {
        return sendService.getLG();
    }

}
