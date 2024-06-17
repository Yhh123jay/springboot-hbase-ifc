package com.yhh.springboot_hbase_ifc.controller.sensor;

import com.yhh.springboot_hbase_ifc.model.entity.Equipment;
import com.yhh.springboot_hbase_ifc.model.vo.Result;
import com.yhh.springboot_hbase_ifc.service.phoenix.EquipmentServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/equipment")
public class EquipmentController {
    private final EquipmentServiceImpl equipmentService;

    public EquipmentController(EquipmentServiceImpl equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping("/all")
    public Result<List<Equipment>> getAllEquipment(){
        return Result.build(equipmentService.selectAllEquipment(),200,"success");
    }
    @GetMapping("/{equipment_id}")
    public Result<Equipment> getEquipmentById(@PathVariable("equipment_id") String equipment_id){
        return Result.build(equipmentService.selectEquipmentById(equipment_id),200,"success");
    }

    @GetMapping("/{equipment_type}")
    public Result<List<Equipment>> getEquipmentByType(@PathVariable("equipment_type") String equipment_type){
        return Result.build(equipmentService.selectEquipmentByType(equipment_type),200,"success");
    }
}
