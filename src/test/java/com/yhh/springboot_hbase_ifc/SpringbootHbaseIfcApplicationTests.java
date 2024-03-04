package com.yhh.springboot_hbase_ifc;

import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootHbaseIfcApplicationTests {

    @Autowired
    private HbaseTemplate hbaseTemplate;
    @Test
    void contextLoads() {
        // 测试连接hbase
        hbaseTemplate.getConnection();
        System.out.println("连接成功"+hbaseTemplate.getConnection());
    }

}
