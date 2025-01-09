package com.yhh.springboot_hbase_ifc.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IFCHBaseReader {
    private final Connection connection;
    private final String namespace;
    private final String tableName;
    private final ObjectMapper objectMapper;

    public IFCHBaseReader(String zkQuorum, String namespace, String tableName) throws Exception {
        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", zkQuorum);
        this.connection = ConnectionFactory.createConnection(config);
        this.namespace = namespace;
        this.tableName = tableName;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 获取特定构件的信息
     * @param entityType IFC实体类型
     * @param guid 全局唯一标识符
     * @return 构件信息map
     */
    public Map<String, Object> getEntityInfo(String entityType, String guid) throws Exception {
        String rowKey = entityType + "_" + guid;
        Table table = connection.getTable(TableName.valueOf(namespace + ":" + tableName));

        try {
            Get get = new Get(Bytes.toBytes(rowKey));
            Result result = table.get(get);

            if (result.isEmpty()) {
                return null;
            }

            Map<String, Object> entityInfo = new HashMap<>();

            // 处理Direct列族中的简单属性
            processDirectFamily(result, entityInfo);

            // 处理Complex列族中的序列化数据
            processComplexFamily(result, entityInfo);

            // 处理Inverse列族中的关联关系
            processInverseFamily(result, entityInfo);

            return entityInfo;
        } finally {
            table.close();
        }
    }

    /**
     * 处理Direct列族中的简单属性
     */
    private void processDirectFamily(Result result, Map<String, Object> entityInfo) {
        result.getFamilyMap(Bytes.toBytes("Direct")).forEach((qualifier, value) -> {
            String key = Bytes.toString(qualifier);
            String val = Bytes.toString(value);
            entityInfo.put(key, val);
        });
    }

    /**
     * 处理Complex列族中的序列化数据
     */
    private void processComplexFamily(Result result, Map<String, Object> entityInfo) throws Exception {
        Map<byte[], byte[]> complexFamily = result.getFamilyMap(Bytes.toBytes("Complex"));
        if (complexFamily != null) {
            for (Map.Entry<byte[], byte[]> entry : complexFamily.entrySet()) {
                String key = Bytes.toString(entry.getKey());
                byte[] serializedValue = entry.getValue();

                // Base64解码
                byte[] decodedValue = Base64.decodeBase64(serializedValue);

                // 使用Jackson反序列化Python pickle后的数据
                try (ByteArrayInputStream bis = new ByteArrayInputStream(decodedValue);
                     ObjectInputStream ois = new ObjectInputStream(bis)) {
                    Object obj = ois.readObject();
                    entityInfo.put(key, obj);
                }
            }
        }
    }

    /**
     * 处理Inverse列族中的关联关系
     */
    private void processInverseFamily(Result result, Map<String, Object> entityInfo) throws Exception {
        List<Map<String, String>> relatedEntities = new ArrayList<>();
        Map<byte[], byte[]> inverseFamily = result.getFamilyMap(Bytes.toBytes("Inverse"));

        if (inverseFamily != null) {
            for (Map.Entry<byte[], byte[]> entry : inverseFamily.entrySet()) {
                String key = Bytes.toString(entry.getKey());
                byte[] serializedValue = entry.getValue();

                // Base64解码
                byte[] decodedValue = Base64.decodeBase64(serializedValue);

                // 反序列化关联实体信息
                Map<String, String> relatedEntity = objectMapper.readValue(decodedValue, Map.class);
                relatedEntities.add(relatedEntity);
            }
        }

        entityInfo.put("relatedEntities", relatedEntities);
    }

    /**
     * 根据实体类型查询所有构件
     * @param entityType IFC实体类型
     * @return 构件列表
     */
    public List<Map<String, Object>> queryEntitiesByType(String entityType) throws Exception {
        List<Map<String, Object>> entities = new ArrayList<>();
        Table table = connection.getTable(TableName.valueOf(namespace + ":" + tableName));

        try {
            Scan scan = new Scan();
            scan.setRowPrefixFilter(Bytes.toBytes(entityType + "_"));

            ResultScanner scanner = table.getScanner(scan);
            for (Result result : scanner) {
                Map<String, Object> entityInfo = new HashMap<>();
                processDirectFamily(result, entityInfo);
                processComplexFamily(result, entityInfo);
                processInverseFamily(result, entityInfo);
                entities.add(entityInfo);
            }
        } finally {
            table.close();
        }
        return entities;
    }

    /**
     * 示例使用方法
     */
    public static void main(String[] args) {
        try {
            // 创建HBase读取器实例
            IFCHBaseReader reader = new IFCHBaseReader("hadoop102", "ifc_data_test", "house_building_3");

            // 读取特定构件信息
            String entityType = "IfcWall";
            String guid = "3Hu7RKu6LBqO$E4PLr1Fk2";
            Map<String, Object> wallInfo = reader.getEntityInfo(entityType, guid);
            System.out.println("Wall Info: " + wallInfo);

            // 查询所有墙体构件
            List<Map<String, Object>> walls = reader.queryEntitiesByType("IfcWall");
            System.out.println("Total walls found: " + walls.size());
            walls.forEach(wall -> System.out.println("Wall: " + wall));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
