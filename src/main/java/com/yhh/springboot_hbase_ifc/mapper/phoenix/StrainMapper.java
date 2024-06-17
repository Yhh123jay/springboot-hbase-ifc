package com.yhh.springboot_hbase_ifc.mapper.phoenix;

import com.yhh.springboot_hbase_ifc.model.entity.Strain;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StrainMapper {
    @Insert("UPSERT INTO \"strain_sensing\" VALUES(#{id}, #{time}, #{sensing}, #{value})")
    int insertStrain(Strain strain);

    // 查询最后10行数据
    @Select("SELECT * FROM \"strain_sensing\" ORDER BY \"id\" DESC LIMIT 100")
    List<Strain> selectLast10();

    // 查询最后win_size行数据
    @Select("SELECT * FROM \"strain_sensing\" ORDER BY \"id\" DESC LIMIT #{win_size}")
    List<Strain> selectLastWinSize(int win_size);

}
