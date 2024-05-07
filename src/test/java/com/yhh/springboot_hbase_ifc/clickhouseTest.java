package com.yhh.springboot_hbase_ifc;

import com.yhh.springboot_hbase_ifc.service.clickhouse.CHStrainServiceImpl;
import com.yhh.springboot_hbase_ifc.service.phoenix.StrainServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class clickhouseTest {
    @Autowired
    private CHStrainServiceImpl strainService;

    @Test
    public void test() {
        strainService.selectBySensing("AI5-01").forEach(System.out::println);
        System.out.println("clickhouseTesting");
    }
}
