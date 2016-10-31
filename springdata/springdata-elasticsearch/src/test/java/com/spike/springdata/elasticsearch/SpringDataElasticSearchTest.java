package com.spike.springdata.elasticsearch;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.spike.springdata.elasticsearch.domain.BookDoc;
import com.spike.springdata.elasticsearch.repository.BookDocRepository;

/**
 * Spring Data ElasticSearch单元测试
 * @author zhoujiagen
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringDataElasticSearchConfig.class })
// 不要回滚 @TransactionConfiguration(defaultRollback = false)
@Rollback(false)
// 从4.2开始使用@Rollback
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class SpringDataElasticSearchTest {

  // @Autowired
  // private ElasticsearchTemplate elasticsearchTemplate;

  @Autowired
  private BookDocRepository bookRepo;

  @Test
  public void resources() {
    Assert.assertNotNull(bookRepo);
  }

  // @Test
  // public void testMapping() {
  // @SuppressWarnings("rawtypes")
  // Map mapping = elasticsearchTemplate.getMapping(BookDoc.class);
  // System.out.println(mapping);
  // }

  @Test
  public void testCreate() throws ParseException {

    List<BookDoc> docs =
        //
        Arrays.asList(new BookDoc(1L, "中国新闻", "中国驻洛杉矶领事馆遭亚裔男子枪击,嫌犯已自首", new Double(10.3),
            new Date()), //
          new BookDoc(2L, "北京新闻", "北京校车将享最高路权", new Double(11.3), new DateTime().plusDays(1)
              .toDate()), //
          new BookDoc(3L, "中国新闻", "公安部：昆明砍人事件是外疆独干的", new Double(12.3), new DateTime().plusDays(2)
              .toDate()), //
          new BookDoc(4L, "国际新闻", "美国留给伊拉克的是个烂摊子吗", new Double(13.3), new DateTime().plusDays(3)
              .toDate()), //
          new BookDoc(5L, "国际新闻", "中韩渔警冲突调查：韩警平均每天扣1艘中国渔船", new Double(14.3), new DateTime()
              .plusDays(4).toDate()));

    bookRepo.save(docs);
  }

  @Test
  public void testSearch() {
    Iterable<BookDoc> docs = bookRepo.findAll();
    Iterator<BookDoc> iterator = docs.iterator();
    while (iterator.hasNext()) {
      System.out.println(iterator.next());
    }

  }
}
