package cn.th.alive.service.send.impl;

import cn.th.alive.service.send.SendService;
import cn.th.alive.utils.HttpClientResult;
import cn.th.alive.utils.HttpClientUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SendServiceImpl implements SendService {
    @Override
    public void send(String text) {
        String host = "https://alive.bar";
        String api = "/api/v1/statuses";
        String url = host + api;

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer mPXmbiXOg2M7tcKer8qDM");

        Map<String, String> params = new HashMap<>();
        params.put("status", text);
        HttpClientUtils.doPost(url, headers, params);
    }

    @Override
    public Object getLG() {
        String url="https://www.lagou.com/";

        HttpClientResult r = HttpClientUtils.doGet(url, null, null);
        System.out.println(r.getCode());
        System.out.println(r.getHeader());
        System.out.println(r.getContent());
        return r;
    }

}
