package com.yhh.springboot_hbase_ifc.service.phoenix;

import com.yhh.springboot_hbase_ifc.mapper.phoenix.StrainMapper;
import com.yhh.springboot_hbase_ifc.model.entity.Strain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StrainServiceImpl {
    @Autowired
    private StrainMapper strainMapper;

    // 查询数据
    public List<Strain> selectLast10() {
        return strainMapper.selectLast10();
    }

}
