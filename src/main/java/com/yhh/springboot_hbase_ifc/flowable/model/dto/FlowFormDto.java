package com.yhh.springboot_hbase_ifc.flowable.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@ApiModel("工作流流程实例相关-流程变量")
public class FlowFormDto implements Serializable {
    //流程实例名称
    private String processInstanceName;
    //流程发起人
    private String initiator;
    //原因描述
    private String reasonDescription;
    //养护部位
    private String maintainPart;
    //损伤图片
    private String damagePicture;
    //养护工作人员
    private String maintainer;
    //养护内容
    private String maintainContent;
    //养护完成图片
    private String maintainPicture;

//    //审批人1
//    private String approver1;
//    //审批意见1
//    private String comment1;
//    //审批人2
//    private String approver2;
//    //审批意见2
//    private String comment2;


    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("processInstanceName",processInstanceName);
        map.put("initiator",initiator);
        map.put("reasonDescription",reasonDescription);
        map.put("maintainPart",maintainPart);
        map.put("damagePicture", damagePicture);

        map.put("maintainer",maintainer);
        map.put("maintainContent",maintainContent);
        map.put("maintainPicture",maintainPicture);
        return map;
    }
    public static FlowFormDto fromMap(Map<String,Object> map){
        FlowFormDto flowFormDto = new FlowFormDto();
        //使用Objects.toString方法来处理 null 值
        flowFormDto.setProcessInstanceName(Objects.toString(map.get("processInstanceName"), ""));
        flowFormDto.setInitiator(Objects.toString(map.get("initiator"), ""));
        flowFormDto.setReasonDescription(Objects.toString(map.get("reasonDescription"), ""));
        flowFormDto.setMaintainPart(Objects.toString(map.get("maintainPart"), ""));
        flowFormDto.setDamagePicture(Objects.toString(map.get("damagePicture"), ""));
        flowFormDto.setMaintainer(Objects.toString(map.get("maintainer"), ""));
        flowFormDto.setMaintainContent(Objects.toString(map.get("maintainContent"), ""));
        flowFormDto.setMaintainPicture(Objects.toString(map.get("maintainPicture"), ""));
        return flowFormDto;
    }
}
