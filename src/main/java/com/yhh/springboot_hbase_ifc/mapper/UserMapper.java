package com.yhh.springboot_hbase_ifc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhh.springboot_hbase_ifc.model.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    //查询所有用户
    @Select("select * from user_info")
    List<User> selectAll();

    //查询用户总数
    @Select("select count(*) from user_info")
    Integer selectAllCount();

    //增加用户
    @Insert("INSERT INTO user_info(username, PASSWORD, nickname, email, phone, address) VALUES(#{username},#{password},#{nickname},#{email},#{phone},#{address}")
    int insertUser(User user);

    //使用mapper.xml来写动态sql更新数据
    int updateUser(User user);

    //删除用户
    @Delete("delete from user_info where id = #{id}")
    Integer deleteUserById(@Param("id") Integer id);

    //分页查询数据
    @Select("select * from user_info limit #{pageNum},#{pageSize}")
    List<User> selectUserByPage(@Param("pageNum") Integer pageNum,@Param("pageSize") Integer pageSize);
}
