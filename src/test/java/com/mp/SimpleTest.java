package com.mp;

import com.mp.dao.UserMapper;
import com.mp.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jws.soap.SOAPBinding;
import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void Select(){
        List<User> users = userMapper.selectList(null);
        Assert.assertEquals(5,users.size());
        users.forEach(System.out::println);
    }

    @Test
    public void insert(){
        User user = new User();
        user.setName("向北");
        user.setAge(26);
        user.setManagerId(1088248166370832385L);
        user.setCreateTime(LocalDateTime.now());
        int rows = userMapper.insert(user);
        System.out.println("影响记录数：" + rows);
    }
}
