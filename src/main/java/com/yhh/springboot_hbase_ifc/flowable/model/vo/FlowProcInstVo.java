package com.yhh.springboot_hbase_ifc.flowable.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.flowable.engine.runtime.ProcessInstance;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowProcInstVo implements Serializable {
    //流程实例id
    private String id;
    //流程定义name
    private String name;
    //发起时间
    private String startTime;
    //发起人
    private String startUserName;
    //Description
    private String description;
    //状态
    private String status;

    // 静态方法用于转换 ProcessInstance 到 FlowProcInstVo
    public static FlowProcInstVo fromProcessInstance(ProcessInstance processInstance) {
        FlowProcInstVo flowProcInstVo = new FlowProcInstVo();
        flowProcInstVo.setId(processInstance.getId());
        flowProcInstVo.setStartTime(processInstance.getStartTime().toString());
        flowProcInstVo.setStartUserName(processInstance.getStartUserId());
        flowProcInstVo.setName(processInstance.getProcessDefinitionName());
        flowProcInstVo.setDescription(processInstance.getDescription());
        flowProcInstVo.setStatus(processInstance.isEnded() ? "已结束" :(processInstance.isSuspended() ? "已暂停":"运行中"));
        return flowProcInstVo;
    }

}
