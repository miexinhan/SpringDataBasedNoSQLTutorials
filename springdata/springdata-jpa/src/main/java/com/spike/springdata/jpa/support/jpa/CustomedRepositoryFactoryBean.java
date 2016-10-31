package com.spike.springdata.jpa.support.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 * Customed repository factory bean<br>
 * REF: http://docs.spring.io/spring-data/jpa/docs/1.8.0.RELEASE/reference/html/
 * @author zhoujiagen
 * @see @EnableJpaRepositories(repositoryFactoryBeanClass)
 */
public class CustomedRepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable>
    extends JpaRepositoryFactoryBean<R, T, I> {

  protected RepositoryFactorySupport createRepositoryFactory(EntityManager em) {
    return new CustomedRepositoryFactory<T, I>(em);
  }

  /**
   * Customed repository factory
   * @author zhoujiagen
   */
  private static class CustomedRepositoryFactory<T, I extends Serializable> extends
      JpaRepositoryFactory {

    private final EntityManager entityManager;

    public CustomedRepositoryFactory(EntityManager em) {
      super(em);

      this.entityManager = em;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Object getTargetRepository(RepositoryMetadata metadata) {
      return new CustomedRepositoryImpl<T, I>((Class<T>) metadata.getDomainType(), entityManager);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
      return CustomedRepositoryImpl.class;
    }
  }
}