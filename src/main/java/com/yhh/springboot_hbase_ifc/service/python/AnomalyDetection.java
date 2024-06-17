package com.yhh.springboot_hbase_ifc.service.python;

import com.alibaba.fastjson.JSONObject;
import com.yhh.springboot_hbase_ifc.mapper.phoenix.StrainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


public class AnomalyDetection {

    //从数据库中查询数据，调用FastAPI接口进行异常检测，返回结果
    //每10分钟检测一次
    public String anomalyDetection(String data) {
        String url = "http://localhost:8001/predict/";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject request = new JSONObject();
        //从Phoenix中查询数据

        request.put("data", data);

        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        return response.getBody();
}
}
