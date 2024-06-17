package com.yhh.springboot_hbase_ifc;

import com.yhh.springboot_hbase_ifc.FileSystem.HadoopClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HDFSTest {
    @Autowired
    private HadoopClient hadoopClient;
    @Test
    public void test() {
        hadoopClient.getFileList("/");
    }
}
