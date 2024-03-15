package com.yhh.springboot_hbase_ifc;

import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

@SpringBootTest
class SpringbootHbaseIfcApplicationTests {

    @Autowired
    private HbaseTemplate hbaseTemplate;
    @Test
    void contextLoads() throws IOException {
        // 测试连接hbase
        ResultScanner test = hbaseTemplate.getConnection().getTable(TableName.valueOf("test"), new ThreadPoolExecutor(10, 10, 10, null, null)).getScanner("test".getBytes());
        test.forEach(System.out::println);
        System.out.println("连接成功"+hbaseTemplate.getConnection());
    }
}
