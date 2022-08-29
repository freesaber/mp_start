package com.mp;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mp.dao.UserMapper;
import com.mp.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleTest {
    @Autowired
    private UserMapper userMapper;

    private QueryWrapper<User> queryWrapper;

    @Test
    public void selectById(){
        User user = userMapper.selectById(1094590409767661570L);
        System.out.println(user);
    }

    @Test
    public void selectByIds(){
        List<Long> ids = Arrays.asList(1088248166370832385L, 1094592041087729666L, 1559063787270647810L);
        List<User> users = userMapper.selectBatchIds(ids);
        users.forEach(System.out::println);
    }

    @Test
    public void selectByMap(){
        Map<String,Object> columnMap = new HashMap<>();
        //columnMap.put("name", "王天风");
        columnMap.put("age", 25);
        List<User> users = userMapper.selectByMap(columnMap);
        users.forEach(System.out::println);
    }

    private void printQueryWrapper(){
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /*
        名字中包含雨并且年龄小于40
     */
    @Test
    public void selectByWrapper(){
        queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "雨")
                .lt("age", 40);
        printQueryWrapper();
    }

    /*
       名字中包含雨，并且年龄大于等于20，且小于等于40，并且email不为空
    */
    @Test
    public void selectByWrapper2(){
        queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "雨")
                .between("age", 20, 40)
                .isNotNull("email");
        printQueryWrapper();
    }

    /*
       名字为王姓或者年龄大于等于25，按照年龄降序排列。年龄相同按照id升序排列
    */
    @Test
    public void selectByWrapper3() {
        queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("name","王")
                .or()
                .ge("age",25)
                .orderByDesc("age").orderByAsc("id");
        printQueryWrapper();
    }
}
