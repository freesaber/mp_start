package com.mp;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mp.dao.UserMapper;
import com.mp.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdateTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void updateById(){
        User user = new User();
        user.setId(1088248166370832385L);
        user.setAge(26);
        user.setEmail("wtf@imooc.com");

        int rows = userMapper.updateById(user);
        System.out.println("影响记录数："+rows);
    }

    @Test
    public void updateByWrapper(){
        User user = new User();
        user.setAge(29);
        user.setEmail("lyw@imooc.com");

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("name","李艺伟").eq("age",28);

        int rows = userMapper.update(user, updateWrapper);
        System.out.println("影响记录数："+rows);
    }

    @Test
    public void updateByWrapper3(){
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("name","李艺伟").eq("age",29).set("age",28);

        int rows = userMapper.update(null, updateWrapper);
        System.out.println("影响记录数："+rows);
    }

    @Test
    public void updateByWrapperLambda(){
        User user = new User();
        user.setAge(29);

        LambdaUpdateWrapper<User> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(User::getName,"李艺伟").eq(User::getAge,28);

        int rows = userMapper.update(user, updateWrapper);
        System.out.println("影响记录数："+rows);
    }
}
