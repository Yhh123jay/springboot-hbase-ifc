package com.yhh.springboot_hbase_ifc.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "equipment_info",schema = "bridge")
public class Equipment {
    private String equipment_id;
    private String equipment_type;
    private String equipment_name;

    private String frequency;
    private Integer channels;
    private String status;
}
