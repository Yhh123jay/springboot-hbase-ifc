package com.yhh.springboot_hbase_ifc.FileSystem.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("hadoop")
public class HadoopProperties {
    /** fs uri地址：默认地址,可以通过配置文件中的hadoop.nameNode进行覆盖。 */
    private String nameNode = "hdfs://127.0.0.1:9000/";
    /** 默认文件夹 */
    private String directoryPath = "/";
    /** 默认用户 */
    private String user = "yhh";

    public String getDirectoryPath() {
        StringBuilder sb = new StringBuilder(directoryPath);
        if (!(directoryPath.indexOf("/") == directoryPath.length())) {
            sb.append("/");
        }
        return sb.toString();
    }

    public String getPath() {
        return this.nameNode + this.directoryPath;
    }
}
