package com.yhh.springboot_hbase_ifc.mapper.phoenix;

import com.yhh.springboot_hbase_ifc.model.entity.Equipment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EquipmentMapper {
    // 查询所有设备信息
    // SELECT * FROM equipment_info
    @Select("SELECT * FROM bridge.equipment_info")
    List<Equipment> selectAllEquipment();

    // 根据设备id查询设备信息
    // SELECT * FROM equipment_info WHERE equipment_id = #{equipment_id}
    @Select("SELECT * FROM bridge.equipment_info WHERE equipment_id = #{equipment_id}")
    Equipment selectEquipmentById(String equipment_id);

    //根据设备类型查询设备信息
    @Select("SELECT * FROM bridge.equipment_info WHERE equipment_type = #{equipment_type}")
    List<Equipment> selectEquipmentByType(String equipment_type);
}
