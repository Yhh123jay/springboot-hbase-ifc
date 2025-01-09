package com.yhh.springboot_hbase_ifc.controller.sensor;

import com.yhh.springboot_hbase_ifc.model.entity.CHStrain;
import com.yhh.springboot_hbase_ifc.model.vo.Result;
import com.yhh.springboot_hbase_ifc.model.vo.ResultCodeEnum;
import com.yhh.springboot_hbase_ifc.service.clickhouse.CHStrainServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sensor")
public class SensorController {
    private final CHStrainServiceImpl strainService;

    //通过构造函数注入
    public SensorController(CHStrainServiceImpl strainService) {
        this.strainService = strainService;
    }

    @GetMapping("/strain/{sensing}")
    public Result getStrainData(@PathVariable("sensing") String sensing) {
        //返回给前端特定格式的数据
        List<CHStrain> strain_data = strainService.selectBySensing(sensing);
        return Result.build(strain_data, ResultCodeEnum.SUCCESS);
    }
}
