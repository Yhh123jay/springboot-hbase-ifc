package com.yhh.springboot_hbase_ifc.hbase;

/**
 * 实现BIM数据库的应用接口
 * 本接口仅实现简单的BIM数据提取，更加复杂的提取需要使用MVD的数据需求描述方法
 */
public interface BIMDataService {

    //查询BIM数据表
    String selectBIMData(String tableName, String rowkey, String family, String column);
    //根据EBS编码查询BIM数据表
    String selectBIMDataByEBS(String tableName, String ebsCode);
    //查询整个BIM数据表生成ifc文件
    String selectBIMDataAll(String tableName);
}
