package com.yhh.springboot_hbase_ifc.flowable.model.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.flowable.engine.repository.ProcessDefinition;

import java.io.Serializable;
import java.util.Date;

//流程定义
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("流程定义")
public class FlowProcDefDto implements Serializable {
    @ApiModelProperty("流程id")
    private String id;

    @ApiModelProperty("流程名称")
    private String name;

    @ApiModelProperty("流程key")
    private String flowKey;

    @ApiModelProperty("流程分类")
    private String category;

//    @ApiModelProperty("配置表单名称")
//    private String formName;
//
//    @ApiModelProperty("配置表单id")
//    private Long formId;

    @ApiModelProperty("版本")
    private int version;

    @ApiModelProperty("部署ID")
    private String deploymentId;

    @ApiModelProperty("流程定义状态: 1:激活 , 2:中止")
    private int suspensionState;

    @ApiModelProperty("描述")
    private String description;

//    @ApiModelProperty("部署时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private Date deploymentTime;

    // 静态方法用于转换 ProcessDefinition 到 FlowProcDefDto
    public static FlowProcDefDto fromProcessDefinition(ProcessDefinition processDefinition) {
        FlowProcDefDto dto = new FlowProcDefDto();
        dto.setId(processDefinition.getId());
        dto.setName(processDefinition.getName());
        dto.setFlowKey(processDefinition.getKey());
        dto.setCategory(processDefinition.getCategory());
        dto.setVersion(processDefinition.getVersion());
        dto.setDeploymentId(processDefinition.getDeploymentId());
        dto.setSuspensionState(processDefinition.isSuspended() ? 2 : 1);
        dto.setDescription(processDefinition.getDescription());
        // 假设部署时间可以从部署信息中获取
        // dto.setDeploymentTime(...);
        // 处理 formName 和 formId 的获取逻辑

        return dto;
    }
}
