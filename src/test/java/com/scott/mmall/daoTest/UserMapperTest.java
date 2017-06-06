package com.scott.mmall.daoTest;

import com.scott.mmall.base.BaseJunitTest;
import com.scott.mmall.dao.UserMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Ubuntu on 2017/5/25.
 */
public class UserMapperTest extends BaseJunitTest {
    @Autowired
    private UserMapper userMapper;

    @Override
    @Before
    public void before() {

    }

    @Test
    public void TestObjectIsNull() {
        Assert.assertNotNull(userMapper);
    }

    @Override
    @After
    public void after() {

    }
}
