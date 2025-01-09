package com.yhh.springboot_hbase_ifc.flowable.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskNodeInfo implements Serializable {
    private String taskId;
    private String taskName;
    private String assignee;
    private String startTime;
    private String endTime;
    private String duration;
}
