package com.yhh.springboot_hbase_ifc.flowable.service;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class FlowInstanceServiceImpl {

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    /**
     * 根据流程定义ID启动流程实例
     */
    public String startProcessInstance(String processId, Map<String, Object> variables) {
        // 设置流程发起人的name到流程中,从登陆人获取
        //String user =
        ProcessInstance processInstanceId = runtimeService.startProcessInstanceById(processId, variables);
        return processInstanceId.getId();
    }


}
