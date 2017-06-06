package com.scott.mmall.base;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Ubuntu on 2017/5/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class BaseJunitTest {
    private Logger logger = LoggerFactory.getLogger(BaseJunitTest.class);

    @Before
    public void before() {

    }

    @After
    public void after() {

    }
}
