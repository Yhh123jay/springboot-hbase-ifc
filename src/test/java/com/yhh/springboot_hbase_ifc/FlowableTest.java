package com.yhh.springboot_hbase_ifc;

import com.sun.codemodel.internal.JVar;
import com.yhh.springboot_hbase_ifc.controller.flowable.MaintainController;
import com.yhh.springboot_hbase_ifc.flowable.model.dto.FlowFormDto;
import com.yhh.springboot_hbase_ifc.flowable.model.dto.FlowProcDefDto;
import com.yhh.springboot_hbase_ifc.flowable.model.vo.FlowCommentVo;
import com.yhh.springboot_hbase_ifc.flowable.model.vo.FlowProcInstVo;
import com.yhh.springboot_hbase_ifc.flowable.model.vo.FlowTaskVo;
import com.yhh.springboot_hbase_ifc.flowable.model.vo.TaskNodeInfo;
import com.yhh.springboot_hbase_ifc.flowable.service.FlowDefinitionServiceImpl;
import com.yhh.springboot_hbase_ifc.flowable.service.MaintainServiceImpl;
import com.yhh.springboot_hbase_ifc.model.vo.Result;
import org.flowable.engine.*;
import org.flowable.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class FlowableTest {

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private MaintainController maintainController;

    @Autowired
    private MaintainServiceImpl maintainService;

    @Autowired
    private FlowDefinitionServiceImpl flowDefinitionService;
    @Autowired
    private HistoryService historyService;

    //部署流程
    @Test
    void deployProcess(){
        String processId = maintainService.createProcess();
        System.out.println("部署成功" + processId);
    }

    //发起流程
    @Test
    void startProcess(){
        //2.通过流程部署id查询流程定义id
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId("2a0b732d-3431-11ef-87c4-005056c00001").singleResult();

        String processInstanceId = maintainService.startProcess(processDefinition.getId());
        System.out.println("流程发起成功" + processInstanceId);//f78305d7-2f9b-11ef-8fd2-005056c00001
    }

    //查询
    @Test
    void getTask(){
        String user = "zhangsan";
        List<FlowTaskVo> task = maintainService.getTask(user);
        task.forEach(System.out::println);
    }

    //完成任务
    @Test
    void finishTask(){
        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", true);
        taskService.complete("b2659c34-3431-11ef-8b28-005056c00001",variables);
    }

    //查询任务表单
    @Test
    void getTaskVars(){
        FlowFormDto taskVars = maintainService.getTaskVars("d33b8627-3431-11ef-acec-005056c00001");
        System.out.println(taskVars.toString());
    }

    //删除流程
    @Test
    void deleteProcess(){
        runtimeService.deleteProcessInstance("d33b8627-3431-11ef-acec-005056c00001", "手动结束");
    }

    //删除流程定义
    @Test
    void deleteProcessDefinition(){
        repositoryService.deleteDeployment("c4b8de8e-2f00-11ef-b761-005056c00001", true);
    }

    @Test
    //获取流程定义列表
    void getProcessDefList(){
        List<FlowProcDefDto> list = flowDefinitionService.getProcDefDto("桥梁养护流程");
        System.out.println(list.toString());
    }
    //添加流程comments
    @Test
    void addComment(){
        maintainService.addComment("de9cd3eb-34fb-11ef-a145-005056c00001", "777", "777");
    }
    @Test
    //获取流程comments
    void getComments(){
        List<FlowCommentVo> comments = maintainService.getComments("d84ec0b1-34fb-11ef-a145-005056c00001");
        comments.forEach(System.out::println);
    }
    //获取流程线
    @Test
    void getProcessLine(){
        List<TaskNodeInfo> processLine = maintainService.getProcessLine("9b38fe3c-351a-11ef-b5c4-005056c00001");
        processLine.forEach(System.out::println);
    }
    //测试historyService
    @Test
    void historyService(){
        System.out.println(historyService.createHistoricActivityInstanceQuery()
                .processInstanceId("9b38fe3c-351a-11ef-b5c4-005056c00001"));
    }
}
