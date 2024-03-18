package com.yhh.springboot_hbase_ifc.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yhh.springboot_hbase_ifc.model.dto.LoginDto;
import com.yhh.springboot_hbase_ifc.model.entity.User;
import com.yhh.springboot_hbase_ifc.model.vo.LoginVo;

import java.util.List;

// Service层接口
public interface UserService extends IService<User> {
    // 查询所有用户
    public  List<User> selectAll();

    // 新增和修改
    public  boolean saveUser(User user);

    // 分页查询
    public  List<User> queryUserWithPage(Integer pageNum, Integer pageSize);

    // 分页查询
    public  IPage<User> queryUserWithPage(Integer pageNum, Integer pageSize, String userName, String email, String address);

    // 登录
    LoginVo login(LoginDto loginDto);

    // 获取当前用户信息
    User getUserInfo(String token);

    void logout(String token);
}
