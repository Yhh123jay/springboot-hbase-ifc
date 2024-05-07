package com.yhh.springboot_hbase_ifc.service.clickhouse;

import com.yhh.springboot_hbase_ifc.mapper.clickhouse.CHStrainMapper;
import com.yhh.springboot_hbase_ifc.model.entity.CHStrain;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CHStrainServiceImpl {
    private final CHStrainMapper chStrainMapper;
    //构造方法注入
    public CHStrainServiceImpl(CHStrainMapper chStrainMapper) {
        this.chStrainMapper = chStrainMapper;
    }

    public List<CHStrain> selectLast10()
    {
        return chStrainMapper.selectLast10();
    }

    public List<CHStrain> selectBySensing(String sensing)
    {
        return chStrainMapper.selectBySensing(sensing);
    }
}
