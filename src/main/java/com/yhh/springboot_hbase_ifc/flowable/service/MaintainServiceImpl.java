package com.yhh.springboot_hbase_ifc.flowable.service;

import com.yhh.springboot_hbase_ifc.flowable.model.dto.FlowFormDto;
import com.yhh.springboot_hbase_ifc.flowable.model.dto.FlowProcDefDto;
import com.yhh.springboot_hbase_ifc.flowable.model.vo.FlowCommentVo;
import com.yhh.springboot_hbase_ifc.flowable.model.vo.FlowProcInstVo;
import com.yhh.springboot_hbase_ifc.flowable.model.vo.FlowTaskVo;
import com.yhh.springboot_hbase_ifc.flowable.model.vo.TaskNodeInfo;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.ExecutionQueryProperty;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.flowable.identitylink.api.IdentityLinkType;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MaintainServiceImpl {
    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    /**
     * 获取所有流程定义
     *
     * @param name: 流程定义名称
     * @return List<FlowProcDefDto> 流程定义列表
     */
    public List<FlowProcDefDto> getProcDef(String name) {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        List<FlowProcDefDto> list1 = new ArrayList<>();
        list.forEach(item -> {
            list1.add(FlowProcDefDto.fromProcessDefinition(item));
        });
        return list1;
    }

    /**
     * 创建流程定义
     * @return deploymentId：部署id
     */
    public String createProcess() {
        // 创建流程定义
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/bridge-maintain.bpmn20.xml")
                .name("桥梁养护流程")
                .deploy();
        return deployment.getId();
    }

    /**
     * 启动流程并传入变量
     * @param processId
     * @return processInstanceId：流程实例id
     */
    public String startProcess(String processId, Map<String, Object> variables) {
        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processId, variables);
        return processInstance.getId();
    }

    /**
     * 启动流程（无变量传入）
     * @param processId
     * @return processInstanceId：流程实例id
     */
    public String startProcess(String processId) {
        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processId);
        return processInstance.getId();
    }

    /**
     * 查询所有流程实例
     * @return processInstanceIds：流程实例id集合
     */
    public List<FlowProcInstVo> getProcInst() {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
        List<FlowProcInstVo> list1 = new ArrayList<>();
        list.forEach(item -> {
            list1.add(FlowProcInstVo.fromProcessInstance(item));
        });
        return list1;
    }


    /**
     * 查询指定用户的待办任务
     * @param user: 用户名
     * @return taskIds：任务id集合
     *
     */
    public List<FlowTaskVo> getTask(String user) {
//        List<String> taskIds = taskService.createTaskQuery()
//                .taskAssignee(user).list().
//                stream().map(task -> task.getId()).collect(Collectors.toList());
        List<Task> list = taskService.createTaskQuery().taskAssignee(user).list();
        ArrayList<FlowTaskVo> list1 = new ArrayList<>();
        list.forEach(item -> {
            list1.add(FlowTaskVo.fromTask(item));
        });
        return list1;
    }

    /**
     * 完成任务
     * @param taskId
     * @param variables
     * @return
     */
    public boolean completeTask(String taskId, Map<String, Object> variables) {
        //查询taskId是否存在
        if (taskService.createTaskQuery().taskId(taskId).count() == 0) {
            return false;
        }
        taskService.complete(taskId, variables);
        return true;
    }

    /**
     * 添加comment
     * @param taskId
     * @param type
     * @param comment
     */
    public void addComment(String taskId, String type ,String comment) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
//        taskService.addUserIdentityLink(taskId, task.getAssignee(),IdentityLinkType.PARTICIPANT);
        processEngine.getIdentityService().setAuthenticatedUserId(task.getAssignee());
        taskService.addComment(taskId, task.getProcessInstanceId(), type, comment);
    }

    /**
     * 获取comments
     * @param processInstanceId
     */
    public List<FlowCommentVo> getComments(String processInstanceId) {
        //根据流程实例id查询所有评论
        List<Comment> taskComments = taskService.getProcessInstanceComments(processInstanceId);
        List<FlowCommentVo> commentVos = new ArrayList<>();
        taskComments.forEach(item -> {
            FlowCommentVo flowCommentVo = FlowCommentVo.fromComment(item);
            commentVos.add(flowCommentVo);
        });
        return commentVos;
    }
    /**
     * 根据任务id查询任务名
     * @param taskId
     * @return taskName
     */
    public String getTaskName(String taskId) {
        return taskService.createTaskQuery().taskId(taskId).singleResult().getName();
    }

    /**
     * 根据任务id获取当前的全局变量
     */
    public FlowFormDto getTaskVars(String taskId) {
        Map<String,Object> map = taskService.getVariables(taskId);
        FlowFormDto flowFormDto = FlowFormDto.fromMap(map);
        return flowFormDto;
    }

    /**
     * 生成流程图
     *
     * @param processId 任务ID
     */
    public void genProcessDiagram(OutputStream out, String processId) throws Exception {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();

        //流程走完的不显示图
        if (pi == null) {
            return;
        }
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        String InstanceId = task.getProcessInstanceId();
        List<Execution> executions = runtimeService
                .createExecutionQuery()
                .processInstanceId(InstanceId)
                .list();

        //得到正在执行的Activity的Id
        List<String> activityIds = new ArrayList<>();
        List<String> flows = new ArrayList<>();
        for (Execution exe : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
            activityIds.addAll(ids);
        }

        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
        ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engconf.getProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png", activityIds, flows, engconf.getActivityFontName(), engconf.getLabelFontName(), engconf.getAnnotationFontName(), engconf.getClassLoader(), 1.0,true);
        byte[] buf = new byte[1024];
        int legth = 0;
        try {
            while ((legth = in.read(buf)) != -1) {
                out.write(buf, 0, legth);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    public List<TaskNodeInfo> getProcessLine(String processInstanceId) {
        String executionId = runtimeService.createExecutionQuery()
                .processInstanceId(processInstanceId)
                .orderBy(ExecutionQueryProperty.TENANT_ID).desc()  // 假设ID是按时间顺序递增的
                .listPage(0, 1)  // 获取第一个结果
                .stream()
                .findFirst()
                .map(Execution::getId)
                .orElse(null);
        List<HistoricActivityInstance> activities = historyService.createHistoricActivityInstanceQuery()
                .executionId(executionId)
                .orderByHistoricActivityInstanceStartTime().asc()
                .list();

        List<TaskNodeInfo> taskNodes = new ArrayList<>();

        for (HistoricActivityInstance activity : activities) {
            if ("userTask".equals(activity.getActivityType())) {
                TaskNodeInfo nodeInfo = new TaskNodeInfo();
                nodeInfo.setTaskName(activity.getActivityName());
                nodeInfo.setAssignee(activity.getAssignee());
                nodeInfo.setStartTime(activity.getStartTime().toString());
                nodeInfo.setEndTime(activity.getEndTime().toString());

                if (activity.getEndTime() != null) {
                    long duration = activity.getEndTime().getTime() - activity.getStartTime().getTime();
                    nodeInfo.setDuration(formatDuration(duration));
                } else {
                    // 对于未完成的任务，计算到当前时间的持续时间
                    long duration = new Date().getTime() - activity.getStartTime().getTime();
                    nodeInfo.setDuration(formatDuration(duration) + " (进行中)");
                }

                taskNodes.add(nodeInfo);
            }
        }

        // 检查是否有正在进行的任务
        List<Task> activeTasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .active()
                .list();

        for (Task activeTask : activeTasks) {
            // 检查是否已经添加了这个任务（可能在历史记录中已存在）
            boolean taskExists = taskNodes.stream()
                    .anyMatch(node -> node.getTaskName().equals(activeTask.getName()));

            if (!taskExists) {
                TaskNodeInfo nodeInfo = new TaskNodeInfo();
                nodeInfo.setTaskName(activeTask.getName());
                nodeInfo.setAssignee(activeTask.getAssignee());
                nodeInfo.setStartTime(activeTask.getCreateTime().toString());
                // 结束时间为null表示任务尚未完成
                nodeInfo.setEndTime(null);

                long duration = new Date().getTime() - activeTask.getCreateTime().getTime();
                nodeInfo.setDuration(formatDuration(duration) + " (进行中)");

                taskNodes.add(nodeInfo);
            }
        }

        return taskNodes;
    }

    private String formatDuration(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        seconds %= 60;
        minutes %= 60;
        return String.format("%d小时%d分%d秒", hours, minutes, seconds);
    }
}
