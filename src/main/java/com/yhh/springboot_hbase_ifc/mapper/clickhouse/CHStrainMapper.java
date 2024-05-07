package com.yhh.springboot_hbase_ifc.mapper.clickhouse;

import com.yhh.springboot_hbase_ifc.model.entity.CHStrain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CHStrainMapper {
    // TODO
    // 获取sensing通道为？的最新的100条数据
    @Select("SELECT * FROM sensing_data WHERE sensing=#{sensing} ORDER BY id DESC LIMIT 100")
    List<CHStrain> selectBySensing(String sensing);

    @Select("SELECT * FROM sensing_data ORDER BY id DESC LIMIT 10")
    List<CHStrain> selectLast10();

}
