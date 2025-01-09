package com.yhh.springboot_hbase_ifc.flowable.service;

import com.yhh.springboot_hbase_ifc.flowable.mapper.FlowDeployMapper;
import com.yhh.springboot_hbase_ifc.flowable.model.dto.FlowProcDefDto;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlowDefinitionServiceImpl {
    @Autowired
    private FlowDeployMapper flowDeployMapper;

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 获取所有流程定义
     *
     * @param name: 流程定义名称
     * @return List<FlowProcDefDto> 流程定义列表
     */
    public List<ProcessDefinition> getProcDef(String name) {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        return list;
        //return flowDeployMapper.selectDeployList(name);
    }

    public List<FlowProcDefDto> getProcDefDto(String name) {
        List<FlowProcDefDto> list = flowDeployMapper.selectDeployList(name);
        return list;
    }
}
