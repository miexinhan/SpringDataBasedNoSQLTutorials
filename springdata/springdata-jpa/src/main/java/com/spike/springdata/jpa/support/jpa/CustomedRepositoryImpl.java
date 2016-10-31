package com.spike.springdata.jpa.support.jpa;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;

import org.apache.log4j.Logger;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.Assert;

/**
 * Customed repository implementation
 * @author zhoujiagen
 */
public class CustomedRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
    implements CustomedRepository<T, ID> {

  private static final Logger LOG = Logger.getLogger(CustomedRepositoryImpl.class);

  private final EntityManager entityManager;

  /**
   * Creates a new {@link CustomedRepositoryImpl} to manage objects of the given
   * {@link JpaEntityInformation}.
   * @param entityInformation must not be {@literal null}.
   * @param entityManager must not be {@literal null}.
   */
  public CustomedRepositoryImpl(JpaEntityInformation<T, ?> entityInformation,
      EntityManager entityManager) {
    super(entityInformation, entityManager);

    // Keep the EntityManager around to
    // used from the newly introduced methods.
    this.entityManager = entityManager;
  }

  /**
   * Creates a new {@link CustomedRepositoryImpl} to manage objects of the given domain type.
   * @param domainClass must not be {@literal null}.
   * @param em must not be {@literal null}.
   */
  public CustomedRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
    super(JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager),
        entityManager);

    this.entityManager = entityManager;
  }

  @Override
  public void sharedCustomMethod(ID id) {
    Assert.notNull(entityManager);

    // implementation goes here
  }

  @Override
  public void showRepositoryInfo() {
    Assert.notNull(entityManager);

    StringBuilder sb = new StringBuilder();
    Map<String, Object> porperties = entityManager.getProperties();
    sb.append("porperties:");
    sb.append(System.lineSeparator());
    sb.append(porperties);

    Metamodel metaModel = entityManager.getMetamodel();
    sb.append("metaModel:");
    sb.append(System.lineSeparator());

    Set<EntityType<?>> entityTypes = metaModel.getEntities();
    sb.append("entities: ");
    sb.append(renderer(entityTypes));
    sb.append(System.lineSeparator());

    Set<ManagedType<?>> managedTypes = metaModel.getManagedTypes();
    sb.append("managed types: ");
    sb.append(renderer2(managedTypes));
    sb.append(System.lineSeparator());

    LOG.info(sb);
  }

  private static String renderer(Set<EntityType<?>> set) {
    StringBuilder sb = new StringBuilder();
    for (EntityType<?> t : set) {
      sb.append(t.getName());
      sb.append(" ");
    }
    return sb.toString();
  }

  private static String renderer2(Set<ManagedType<?>> set) {
    StringBuilder sb = new StringBuilder();
    for (ManagedType<?> t : set) {
      sb.append(t.getJavaType().getSimpleName());
      sb.append(" ");
    }
    return sb.toString();
  }
}