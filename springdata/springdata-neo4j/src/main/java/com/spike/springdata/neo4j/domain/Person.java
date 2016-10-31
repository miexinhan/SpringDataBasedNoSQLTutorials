package com.spike.springdata.neo4j.domain;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.spike.springdata.neo4j.anno.OnlineResource;

@OnlineResource(referenceUrls = { "http://spring.io/guides/gs/accessing-data-neo4j/" })
@NodeEntity
public class Person {
  @GraphId
  private Long id;

  private String name;

  /**
   * {@link @Fetch} is important
   */
  @RelatedTo(type = "TEAMMATE", direction = Direction.BOTH)
  @Fetch
  private Set<Person> teammates;

  public Person() {
  }

  public Person(String name) {
    this.name = name;
  }

  public void worksWith(Person person) {
    if (teammates == null) {
      teammates = new HashSet<Person>();
    }
    teammates.add(person);
  }

  @Override
  public String toString() {
    String results = name + "'s teammates include\n";
    if (teammates != null) {
      for (Person person : teammates) {
        results += "\t- " + person.name + "\n";
      }
    }
    return results;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<Person> getTeammates() {
    return teammates;
  }

  public void setTeammates(Set<Person> teammates) {
    this.teammates = teammates;
  }

}
