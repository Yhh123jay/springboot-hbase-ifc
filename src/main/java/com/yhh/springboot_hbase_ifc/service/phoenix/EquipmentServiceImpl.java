package com.yhh.springboot_hbase_ifc.service.phoenix;

import com.yhh.springboot_hbase_ifc.mapper.phoenix.EquipmentMapper;
import com.yhh.springboot_hbase_ifc.model.entity.Equipment;

import java.util.List;

public class EquipmentServiceImpl {

    private EquipmentMapper equipmentMapper;
    //通过构造方法注入
    public EquipmentServiceImpl(EquipmentMapper equipmentMapper) {
        this.equipmentMapper = equipmentMapper;
    }
    //查询所有设备
    public List<Equipment> selectAllEquipment() {
        return equipmentMapper.selectAllEquipment();
    }
    //根据设备id查询设备
    public Equipment selectEquipmentById(String equipment_id) {
        return equipmentMapper.selectEquipmentById(equipment_id);
    }
    //根据设备类型查询设备
    public List<Equipment> selectEquipmentByType(String equipment_type) {
        return equipmentMapper.selectEquipmentByType(equipment_type);
    }

}
