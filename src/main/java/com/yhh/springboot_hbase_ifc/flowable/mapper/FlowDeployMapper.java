package com.yhh.springboot_hbase_ifc.flowable.mapper;

import com.yhh.springboot_hbase_ifc.flowable.model.dto.FlowProcDefDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FlowDeployMapper {
    List<FlowProcDefDto> selectDeployList(String name);
}
