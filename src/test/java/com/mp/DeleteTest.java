package com.mp;

import com.mp.dao.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeleteTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void deleteById(){
        int rows = userMapper.deleteById(1560473873310339074L);
        System.out.println("删除条数:"+rows);
    }

    /**
     * deleteByMap
     * deleteBatchIds
     */
}
