package com.yhh.springboot_hbase_ifc.flowable.model.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
//不能添加Serializable

@Data
public class FlowCommentDto implements Serializable {

    /**
     * 意见类别 0 正常意见  1 退回意见 2 驳回意见
     */
    private String type;

    /**
     * 意见内容
     */
    private String comment;
}