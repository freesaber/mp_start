package com.mp;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
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

    /**
     * 名字中包含雨并且年龄小于40
     */
    @Test
    public void selectByWrapper(){
        queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "雨")
                .lt("age", 40);
        printQueryWrapper();
    }

    /**
     * 名字中包含雨，并且年龄大于等于20，且小于等于40，并且email不为空
    */
    @Test
    public void selectByWrapper2(){
        queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "雨")
                .between("age", 20, 40)
                .isNotNull("email");
        printQueryWrapper();
    }

    /**
     * 名字为王姓或者年龄大于等于25，按照年龄降序排列。年龄相同按照id升序排列
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

    /**
     * 创建日期为2019年2月14日，并且直属上级为名字为王姓
     * date_format(create_time,'%Y-%m-%d')='2019-02-14' and manager_id in (select id from user where name like '王%')
    */
    @Test
    public void selectByWrapper4() {
        queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("date_format(create_time,'%Y-%m-%d') = {0}","2019-02-14")
                .inSql("manager_id","select id from user where name like '王%'");
        printQueryWrapper();
    }

    /**
     * 名字为王性并且（年龄小于40或邮箱不为空）
     * name like '王%' and (age<40 or email is not null)
    */
    @Test
    public void selectByWrapper5() {
        queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("name","王")
                .and(qw -> qw.lt("age", 40).or().isNotNull("email"));
        printQueryWrapper();
    }

    /**
     * 名字为王性或者（年龄小于40兵器年龄大于20并且邮箱不为空）
     * name like '王%' or (age<40 and age>20 and email is not null)
    */
    @Test
    public void selectByWrapper6() {
        queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("name","王")
                .or(qw -> qw.gt("age", 20).lt("age", 40).isNotNull("email"));
        printQueryWrapper();
    }

    /**
     * (年龄小于40或邮箱不为空)并且名字为王姓名
     * (age<40 or email is not null) and name like '王$'
    */
    @Test
    public void selectByWrapper7() {
        queryWrapper = new QueryWrapper<>();
        // 正常嵌套 不带 AND 或者 OR
        // nested换成and也可以正常执行
        queryWrapper.nested(qw -> qw.lt("age", 40).or().isNotNull("email"))
                .likeRight("name","王");
        printQueryWrapper();
    }

    /**
     * 年龄为30、31、34、35
     * age in (30、31、34、35)
    */
    @Test
    public void selectByWrapper8() {
        queryWrapper = new QueryWrapper<>();
        queryWrapper.in("age",Arrays.asList(30,31,34,35));
        printQueryWrapper();
    }

    /**
     * 只返回满足条件的其中一条语句即可
    */
    @Test
    public void selectByWrapper9() {
        queryWrapper = new QueryWrapper<>();
        queryWrapper.in("age",Arrays.asList(30,31,34,35)).last("limit 1");
        printQueryWrapper();
    }

    /**
     * 查询指定字段
     */
    @Test
    public void selectByWrapperSupper(){
        queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","name")
                .like("name", "雨")
                .lt("age", 40);
        printQueryWrapper();
    }

    /**
     * 排除指定字段
     */
    @Test
    public void selectByWrapperSupper2(){
        queryWrapper = new QueryWrapper<>();
        queryWrapper.select(User.class, info -> !info.getColumn().equals("create_time") && !info.getColumn().equals("manager_id"))
                .like("name", "雨")
                .lt("age", 40);
        printQueryWrapper();
    }

    /**
     * 控制条件是否加入
     */
    @Test
    public void testCondition(){
        String name = "王";
        String email = "";
        queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), "name", name)
                .like(StringUtils.isNotEmpty(email), "email", email);
        printQueryWrapper();
    }

    /**
     * 实体作为条件构造器
     */
    @Test
    public void selectByWrapperEntity(){
        User user = new User();
        user.setName("刘红雨");
        user.setAge(32);

        queryWrapper = new QueryWrapper<User>(user);
        printQueryWrapper();
    }

    /**
     * 类似于userMapper.selectByMap
     * null 查询变成 is null
     * queryWrapper.allEq(params, false)第二个参数，忽略null值条件
     */
    @Test
    public void selectByWrapperAllEq(){
        queryWrapper = new QueryWrapper<>();
        Map<String,Object> params = new HashMap<>();
        params.put("name", "王天风");
        params.put("age",25);
        queryWrapper.allEq(params);
        // 对条件进行过滤
        // queryWrapper.allEq((k,v) -> !k.equals("name"),params);

        printQueryWrapper();
    }

    /**
     * 实体类，未设置值，会返回null默认值
     */
    @Test
    public void selectByWrapperMaps(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","name").like("name","雨");

        List<Map<String, Object>> userList = userMapper.selectMaps(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 按照直属上级分组，查询每组的平均年龄、按最大年龄、最小年龄。并且只取年龄总和小于500的组
     * select avg(age) avg_age,min(age) min_age,max(age) max_age
     * from user
     * group by manager_id
     * having sum(age) < 500
     */
    @Test
    public void selectByWrapperMaps2(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("avg(age) avg_age","min(age) min_age","max(age) max_age")
                .groupBy("manager_id")
                .having("sum(age) < {0}",500);

        List<Map<String, Object>> userList = userMapper.selectMaps(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 只返回1列
     */
    @Test
    public void selectByWrapperObjs(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","name").like("name","雨");

        List<Object> objects = userMapper.selectObjs(queryWrapper);
        objects.forEach(System.out::println);
    }

    /**
     * 返回记录数
     */
    @Test
    public void selectByWrapperCount(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name","雨");

        Integer integer = userMapper.selectCount(queryWrapper);
        System.out.println(integer);
    }

    /**
     * selectOne，没有结果，或者一条。多条符合结果会报错
     */
}
