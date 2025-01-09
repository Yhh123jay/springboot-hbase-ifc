package com.yhh.springboot_hbase_ifc.flowable.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.flowable.task.api.Task;

import java.util.List;
import java.util.Map;

@Data
@ApiModel("工作流任务相关--请求参数")
public class FlowTaskVo {

    @ApiModelProperty("任务Id")
    private String taskId;

    @ApiModelProperty("任务名称")
    private String name;

    @ApiModelProperty("审批人")
    private String assignee;

    @ApiModelProperty("流程实例Id")
    private String instanceId;

    @ApiModelProperty("流程分类")
    private String category;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("流程描述")
    private String description;


    // 静态方法用于转换 Task 到 FlowTaskVo
    public static FlowTaskVo fromTask(Task task) {
        FlowTaskVo flowTaskVo = new FlowTaskVo();
        flowTaskVo.setTaskId(task.getId());
        flowTaskVo.setName(task.getName());
        flowTaskVo.setAssignee(task.getAssignee());
        flowTaskVo.setCategory(task.getCategory());
        flowTaskVo.setCreateTime(task.getCreateTime().toString());
        flowTaskVo.setInstanceId(task.getProcessInstanceId());
        flowTaskVo.setDescription(task.getDescription());
        return flowTaskVo;
    }
}
