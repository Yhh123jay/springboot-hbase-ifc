package com.yhh.springboot_hbase_ifc.flowable.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.flowable.engine.task.Comment;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowCommentVo implements Serializable {
    private String id;
    private String type;
    private String time;
    private String userId;
    private String taskId;
    private String action;
    private String message;
    private String fullMessage;

    //通过Comment转为FlowCommentVo
    public static FlowCommentVo fromComment(Comment comment) {
        FlowCommentVo flowCommentVo = new FlowCommentVo();
        flowCommentVo.setId(comment.getId());
        flowCommentVo.setType(comment.getType());
        flowCommentVo.setTime(comment.getTime().toString());
        flowCommentVo.setUserId(comment.getUserId());
        flowCommentVo.setTaskId(comment.getTaskId());
        flowCommentVo.setMessage(comment.getFullMessage());
        flowCommentVo.setFullMessage(comment.getFullMessage());
        return flowCommentVo;
    }
}
