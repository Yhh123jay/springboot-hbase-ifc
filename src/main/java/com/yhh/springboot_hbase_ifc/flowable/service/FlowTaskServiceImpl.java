package com.yhh.springboot_hbase_ifc.flowable.service;

import org.flowable.engine.*;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FlowTaskServiceImpl {
    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    /**
     * 完成任务
     */
    public boolean completeTask(String taskId, Map<String, Object> variables) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        taskService.complete(taskId, variables);
        return true;
    }

    /**
     * 根据人员id获取任务列表
     */
    public List<String> getTask(String user) {
        List<String> taskIds = taskService.createTaskQuery()
                .taskAssignee(user).list().
                stream().map(task -> task.getId()).collect(Collectors.toList());
        return taskIds;
    }

    /**
     * 获取流程任务关联变量
     * @param taskId
     */
    public Map<String, Object> getTaskVariables(String taskId) {
        return taskService.getVariables(taskId);
    }


}
