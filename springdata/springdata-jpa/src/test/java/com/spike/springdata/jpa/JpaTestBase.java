package com.spike.springdata.jpa;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * 测试基类
 * @author zhoujiagen
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class })
// 不要回滚
@TransactionConfiguration(defaultRollback = false)
public abstract class JpaTestBase {
  // put common utilities for test here.

}
