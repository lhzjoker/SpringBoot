package com.atguigu.srpingboot.Controller;


import com.atguigu.srpingboot.bean.User;
import com.atguigu.srpingboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable("id") Integer id){
        User user= userRepository.getOne(id);       //getOne方法使用的懒加载，获取到的只是代理对象，转换为json时会报错
        return user;
    }

    @GetMapping("/user")
    public User insertUser(User user){
        User save = userRepository.save(user);      //插入一条数据
        return save;
    }

}
