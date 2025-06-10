package com.yhh.springboot_hbase_ifc.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
//应变传感器实体类
public class CHStrain {
    //id
    private Long id;
    //time
    private Timestamp timestamp;
    //sensing通道
    private String sensing;
    //value
    private double res_value;
}
