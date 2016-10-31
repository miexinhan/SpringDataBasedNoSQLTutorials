package com.spike.querydsl;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.mysema.query.jpa.impl.JPAQuery;
import com.spike.querydsl.domain.Customer;
import com.spike.querydsl.domain.QCustomer;
import com.spike.querydsl.repository.CustomerRepository;

/**
 * <pre>
 *  Querydsl Spike单元测试
 *  
 *  http://www.querydsl.com/static/querydsl/3.7.3/reference/html/
 * 
 * </pre>
 * @author zhoujiagen
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class })
// 不要回滚
@TransactionConfiguration(defaultRollback = false)
public class QuerydslAppTest {

  @Autowired
  private EntityManager entityManager;

  // @Autowired private JPAQueryFactory jpaQueryFactory;

  @Autowired
  private CustomerRepository customerRepo;

  @Test
  public void resources() {
    Assert.assertNotNull(entityManager);
    Assert.assertNotNull(customerRepo);

    // Assert.assertNotNull(jpaQueryFactory);
  }

  @Transactional
  @Test
  public void prepareData() {
    Customer customer = new Customer();
    customer.setFirstName("Jia Gen");
    customer.setLastName("Zhou");

    customerRepo.save(customer);

    customer = new Customer();
    customer.setFirstName("Eric");
    customer.setLastName("Cartman");
    customerRepo.save(customer);
  }

  /** 简单查询, 使用apt-maven-plugin插件 */
  @Test
  public void query() {
    QCustomer qcustomer = QCustomer.customer;
    JPAQuery query = new JPAQuery(entityManager);
    Customer customer =
        query.from(qcustomer).where(qcustomer.firstName.eq("Eric")).uniqueResult(qcustomer);

    System.out.println(customer);
  }

  /** 使用querydsl-maven-plugin插件 */
  // @Test
  // public void nativeSQL() {
  // SQLTemplates sqlTemplates = new MySQLTemplates();
  //
  // JPASQLQuery query = new JPASQLQuery(entityManager, sqlTemplates);
  //
  // QCustomer qCustomer = QCustomer.customer;
  // List<String> names = query.from(qCustomer).list(qCustomer.firstName);
  // System.out.println(names);
  // }

}
