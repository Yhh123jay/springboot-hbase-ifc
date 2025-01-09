package com.yhh.springboot_hbase_ifc.controller.flowable;

import com.yhh.springboot_hbase_ifc.flowable.model.dto.FlowCommentDto;
import com.yhh.springboot_hbase_ifc.flowable.model.dto.FlowFormDto;
import com.yhh.springboot_hbase_ifc.flowable.model.dto.FlowProcDefDto;
import com.yhh.springboot_hbase_ifc.flowable.model.vo.FlowCommentVo;
import com.yhh.springboot_hbase_ifc.flowable.model.vo.FlowProcInstVo;
import com.yhh.springboot_hbase_ifc.flowable.model.vo.FlowTaskVo;
import com.yhh.springboot_hbase_ifc.flowable.model.vo.TaskNodeInfo;
import com.yhh.springboot_hbase_ifc.flowable.service.FlowDefinitionServiceImpl;
import com.yhh.springboot_hbase_ifc.flowable.service.MaintainServiceImpl;
import com.yhh.springboot_hbase_ifc.model.vo.Result;
import com.yhh.springboot_hbase_ifc.model.vo.ResultCodeEnum;
import lombok.AllArgsConstructor;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/maintain")
@AllArgsConstructor
public class MaintainController {

    private MaintainServiceImpl maintainService;

    private FlowDefinitionServiceImpl flowDefinitionService;

    private TaskService taskService;
    /**
     * 部署流程
     * @return 部署id
     */
    @PostMapping("/deploy")
    public Result<String> createProcess() {
        // 创建流程定义
        String processId = maintainService.createProcess();
        // 返回部署id
        return Result.build(processId, 200, "部署流程成功");
    }

    /**
     * 获取所有流程定义列表
     * @param name：流程定义名称
     */
    @GetMapping("/listProcDef/{name}")
    public Result<List<FlowProcDefDto>> getProcDef(@PathVariable("name") String name) {
        List<FlowProcDefDto> list = maintainService.getProcDef(name);
        return Result.build(list, ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMessage());
    }

    /**
     * 获取所有流程定义列表,使用Mybatis方式，需要注意表格名称的大小写问题
     */
    public Result<List<FlowProcDefDto>> getProc(@PathVariable("name") String name) {
        List<FlowProcDefDto> list = flowDefinitionService.getProcDefDto(name);
        return Result.build(list, 200, "查询成功");
    }

    /**
     * 启动流程
     * @param processId
     * @return 流程实例id
     */
    @PostMapping("/startProc/{processId}")
    public Result<String> startProcess(@PathVariable("processId") String processId, @RequestBody FlowFormDto flowFormDto) {
        Map<String, Object> flowFormDtoMap = flowFormDto.toMap();
        String processInstanceId = maintainService.startProcess(processId, flowFormDtoMap);
        // 查询下一个任务的id，并完成它
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        String taskId = task.getId();
        taskService.complete(taskId, flowFormDtoMap);
        return Result.build(processInstanceId, 200, "启动流程并完成第一个用户任务成功");
    }

    /**
     * 查询所有流程实例
     */
    @GetMapping("/listProcInst")
    public Result<List<FlowProcInstVo>> getProcInst() {
        List<FlowProcInstVo> voList = maintainService.getProcInst();
        return Result.build(voList, 200, "查询流程实例成功");
    }
    /**
     * 获取流程实例Line
     */
    @GetMapping("/getProcessLine/{processInstanceId}")
    public Result<List<TaskNodeInfo>> getProcessLine(@PathVariable("processInstanceId") String processInstanceId) {
        List<TaskNodeInfo> taskNodeInfoList = maintainService.getProcessLine(processInstanceId);
        return Result.build(taskNodeInfoList, 200, "查询流程实例Line成功");
    }
    /**
     * 查询指定用户的待办任务
     *
     * @param user
     * @return 待办任务id列表
     */
    @GetMapping("/getTask/{user}")
    public Result<List<FlowTaskVo>> getTask(@PathVariable("user") String user) {
        List<FlowTaskVo> taskList = maintainService.getTask(user);
        return Result.build(taskList, 200, "查询待办任务成功");
    }

    /**
     * 完成任务
     * @param taskId
     * @return 状态
     */
    @PostMapping("/complete/{taskId}")
    public Result<String> completeTask(@PathVariable("taskId") String taskId, @RequestBody FlowFormDto flowFormDto) {
        Map<String, Object> flowFormDtoMap = flowFormDto.toMap();
        Boolean isFinished = maintainService.completeTask(taskId, flowFormDtoMap);
        if (!isFinished) {
            return Result.build(null, 500, "任务id不存在");
        }
        return Result.build(null, 200, "完成任务成功");
    }

    /**
     * 接受流程
     * @param taskId
     * @return 状态
     */
    @PostMapping("/accept/{taskId}")
    public Result<String> acceptTask(@PathVariable("taskId") String taskId, @RequestBody FlowFormDto flowFormDto) {
        Map<String, Object> flowFormDtoMap = flowFormDto.toMap();
        flowFormDtoMap.put("approved", true);
        Boolean isFinished = maintainService.completeTask(taskId, flowFormDtoMap);
        if (!isFinished) {
            return Result.build(null, 500, "任务id不存在");
        }
        return Result.build(null, 200, "完成任务成功");
    }

    /**
     * 拒绝流程
     * @param taskId
     * @return 状态
     */
    @PostMapping("/reject/{taskId}")
    public Result<String> rejectTask(@PathVariable("taskId") String taskId, @RequestBody FlowFormDto flowFormDto) {
        Map<String, Object> flowFormDtoMap = flowFormDto.toMap();
        flowFormDtoMap.put("approved", false);
        Boolean isFinished = maintainService.completeTask(taskId, flowFormDtoMap);
        if (!isFinished) {
            return Result.build(null, 500, "任务id不存在");
        }
        return Result.build(null, 200, "拒绝流程成功");
    }

    /**
     * 获取任务变量
     * @param taskId
     * @return
     */
    @GetMapping("/getTaskForm/{taskId}")
    public Result<FlowFormDto> getTaskForm(@PathVariable("taskId") String taskId) {
        FlowFormDto flowFormDto = maintainService.getTaskVars(taskId);
        return Result.build(flowFormDto, 200, "查询任务变量成功");
    }

    /**
     * 添加任务意见
     * @param taskId
     * @return
     */
    @PostMapping("/addComment/{taskId}")
    public Result<String> addComment(@PathVariable("taskId") String taskId, @RequestBody FlowCommentDto flowCommentDto) {
        maintainService.addComment(taskId, flowCommentDto.getType(), flowCommentDto.getComment());
        return Result.build(null, 200, "添加任务意见成功");
    }
    /**
     * 获取任务意见列表
     * @param processInstanceId
     * @return List<FlowCommentVo>
     */
    @GetMapping("/getComments/{processInstanceId}")
    public Result<List<FlowCommentVo>> getComments(@PathVariable("processInstanceId") String processInstanceId) {
        List<FlowCommentVo> flowCommentVoList = maintainService.getComments(processInstanceId);
        return Result.build(flowCommentVoList, 200, "查询任务意见成功");
    }
    /**
     * 生成流程图
     * @param processId
     * @return
     */
    @GetMapping("/genProcessDiagram/{processId}")
    public void genProcessDiagram(HttpServletResponse httpServletResponse, @PathVariable("processId") String processId) {
        httpServletResponse.setContentType("image/png"); // 或者是其他格式如"image/jpeg"，取决于生成的图片类型
        OutputStream os;
        try {
            os = httpServletResponse.getOutputStream();
            maintainService.genProcessDiagram(os, processId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
