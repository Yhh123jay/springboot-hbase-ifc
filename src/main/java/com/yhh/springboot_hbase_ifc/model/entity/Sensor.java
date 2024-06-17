package com.yhh.springboot_hbase_ifc.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sensor_info",schema = "bridge")
public class Sensor {
    private String sensor_id;
    private String sensor_type;
    private String sensor_name;
    //监测内容
    private String monitor_context;
    private String group_id;

    private String status;
    private String channel;
    //传输方式：4G/无线/物联网技术
    private String signal;
    private String EBSCode;

}
