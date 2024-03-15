package com.yhh.springboot_hbase_ifc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhh.springboot_hbase_ifc.model.entity.User;
import com.yhh.springboot_hbase_ifc.mapper.UserMapper;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> selectAll() {
        return userMapper.selectAll();
    }

    @Override
    public boolean saveUser(User user) {
        return saveOrUpdate(user);
    }

    @Override
    public List<User> queryUserWithPage(Integer pageNum, Integer pageSize) {
       return userMapper.selectUserByPage((pageNum-1)*pageSize,pageSize);
    }

    // 实现分页查询,并返回总数和查询到的数据,mybatis-plus方式
    public IPage<User> queryUserWithPage(Integer pageNum, Integer pageSize, String userName, String email, String address){
        IPage<User> page = new Page<>(pageNum,pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        if(!"".equals(userName)){
//            queryWrapper.like("username",userName);
//        }
        queryWrapper.like(Strings.isNotEmpty(userName),"username",userName);
        queryWrapper.like(Strings.isNotEmpty(email),"email",email);
        queryWrapper.like(Strings.isNotEmpty(address),"address",address);
        return page(page,queryWrapper);
    }

    @Override
    public User login(User user) {
        return null;
    }

}
