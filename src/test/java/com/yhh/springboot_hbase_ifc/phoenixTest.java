package com.yhh.springboot_hbase_ifc;

import com.yhh.springboot_hbase_ifc.service.phoenix.StrainServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class phoenixTest {

    @Autowired
    private StrainServiceImpl strainService;

    @Test
    public void test() {
        strainService.selectLast10().forEach(System.out::println);
        System.out.println("phoenixTest");
    }
}
