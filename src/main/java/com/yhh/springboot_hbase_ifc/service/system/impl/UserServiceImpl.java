package com.yhh.springboot_hbase_ifc.service.system.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhh.springboot_hbase_ifc.exception.GuiguException;
import com.yhh.springboot_hbase_ifc.model.dto.LoginDto;
import com.yhh.springboot_hbase_ifc.model.entity.User;
import com.yhh.springboot_hbase_ifc.mapper.system.UserMapper;
import com.yhh.springboot_hbase_ifc.model.vo.LoginVo;
import com.yhh.springboot_hbase_ifc.model.vo.ResultCodeEnum;
import com.yhh.springboot_hbase_ifc.service.system.UserService;
import com.yhh.springboot_hbase_ifc.utils.JWTUtils;
import org.apache.hadoop.security.token.delegation.web.DelegationTokenManager;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    //注入RedisTemplate
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

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
    public LoginVo login(LoginDto loginDto) {
        // 登录逻辑
        // 获取前端传递的用户名和密码
        String username = loginDto.getUsername();
        // 根据用户名查询用户信息
        User user = userMapper.selectUserInfoByUsername(username);
        // 如果用户不存在,返回用户名或密码错误
        if(user == null){
            throw new GuiguException(ResultCodeEnum.LOGIN_FAIL);
        }
        // 如果用户存在,判断密码是否正确
        //String loginDtoPassword = DigestUtils.md5DigestAsHex(loginDto.getPassword().getBytes());
        String loginDtoPassword = loginDto.getPassword();
        String userPassword = user.getPassword();
        // 如果密码错误,返回用户名或密码错误
        if(!loginDtoPassword.equals(userPassword)){
            throw new GuiguException(ResultCodeEnum.LOGIN_FAIL);
        }
        // 如果密码正确,返回登录成功
        // 返回登录成功，生成token
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        String token = JWTUtils.generateToken(claims);
        // 将用户信息和token存入redis
        redisTemplate.opsForValue().set(token,JSON.toJSONString(user),30, TimeUnit.DAYS);

        // 返回登录成功的响应结果LoginVo对象
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        return loginVo;
    }

    @Override
    public User getUserInfo(String token) {
        String userJson = redisTemplate.opsForValue().get(token);
        User user = JSON.parseObject(userJson, User.class);
        return user;
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete(token);
    }
}
