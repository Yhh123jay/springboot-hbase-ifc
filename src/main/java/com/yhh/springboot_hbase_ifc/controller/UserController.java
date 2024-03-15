package com.yhh.springboot_hbase_ifc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yhh.springboot_hbase_ifc.model.entity.User;
import com.yhh.springboot_hbase_ifc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    //查询所有数据
    @GetMapping("/")
    public List<User> index() {
        return userService.selectAll();
    }
    // 登录
    @PostMapping("/login")
    // 前端传递的数据是json格式的，所以需要使用@RequestBody来接收
    public User login(@RequestBody User user) {
        return userService.login(user);
    }

    //新增和修改
    @PostMapping
    public boolean saveUser(@RequestBody User user){
        return userService.saveUser(user);
    }

    //删除
    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable("id") Integer id){
        return userService.removeById(id);
    }

    //批量删除
    @DeleteMapping("/delete/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return userService.removeByIds(ids);
    }

    //分页查询,通过sql语句来实现
    //接口路径：/user/page?pageNum=1&pageSize=10
    //使用RequestParam接受参数
    //(pageNum-1)*pageSize  pageSize
    @GetMapping("/page")
    public IPage<User> queryUserWithPage(@RequestParam("pageNum") Integer pageNum,
                                         @RequestParam(value = "pageSize") Integer pageSize,
                                         @RequestParam(value = "username",defaultValue = "") String userName,
                                         @RequestParam(value = "email",defaultValue = "") String email,
                                         @RequestParam(value = "address",defaultValue = "") String address){
        return userService.queryUserWithPage(pageNum,pageSize,userName,email,address);
    }
}
