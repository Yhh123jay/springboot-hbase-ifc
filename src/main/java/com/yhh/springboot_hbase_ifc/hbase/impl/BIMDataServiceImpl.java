package com.yhh.springboot_hbase_ifc.hbase.impl;

import com.yhh.springboot_hbase_ifc.hbase.BIMDataService;
import com.yhh.springboot_hbase_ifc.hbase.HBaseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 从hbase中查询IFC数据
 * 可能的应用：
 * 1、重新生成ifc文件
 * 2、提取子模型--根据MVD
 * 3、提取模型信息--根据EBS编码提取构件信息
 */
@Service
public class BIMDataServiceImpl implements BIMDataService {
    @Autowired
    private HBaseClient hBaseClient;
    @Override
    public String selectBIMData(String tableName, String rowkey, String family, String column) {
        return hBaseClient.getValue(tableName, rowkey, family, column);
    }

    @Override
    //根据EBS编码提取构建的信息，根据rowkey的前缀和Ifc实体进行查询
    //HBase查询可以通过前缀进行查询，例如：
    //hbase(main):001:0> scan 't1'
    //scan 't1',{FILTER=>"PrefixFilter('2015')"}
    public String selectBIMDataByEBS(String tableName, String ebsCode) {
        try {
            //ebs编码是rowkey的前缀
            String rowKeyFilter = "PrefixFilter('" + ebsCode + "')";
            String bimData = hBaseClient.scanTable(tableName, ebsCode);
            return bimData;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String selectBIMDataAll(String tableName) {
        return "";
    }


}
