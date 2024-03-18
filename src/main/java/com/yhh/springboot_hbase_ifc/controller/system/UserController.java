package com.yhh.springboot_hbase_ifc.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yhh.springboot_hbase_ifc.model.dto.LoginDto;
import com.yhh.springboot_hbase_ifc.model.entity.User;
import com.yhh.springboot_hbase_ifc.model.vo.LoginVo;
import com.yhh.springboot_hbase_ifc.model.vo.Result;
import com.yhh.springboot_hbase_ifc.model.vo.ResultCodeEnum;
import com.yhh.springboot_hbase_ifc.service.system.UserService;
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
    public Result login(@RequestBody LoginDto loginDto) {
        LoginVo loginVo = userService.login(loginDto);
        return Result.build(loginVo, ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMessage());
    }

    //获取用户信息
    @GetMapping("/userInfo")
    public Result getUserInfo(@RequestHeader("token") String token){
        // 从请求头中获取token
        // 根据token查询redis获取用户信息
        User user = userService.getUserInfo(token);
        // 返回用户信息
        return Result.build(user, ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMessage());
    }
    //退出登录
    @GetMapping("/logout")
    public Result logout(@RequestHeader("token") String token){
        // 从请求头中获取token
        // 删除redis中的token
        userService.logout(token);
        return Result.build(null, ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMessage());
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
