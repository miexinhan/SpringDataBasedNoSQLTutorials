package com.spike.mybatis.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.mybatis.domain.Student;
import com.spike.mybatis.mappers.StudentMapper;
import com.spike.mybatis.util.MyBatisSqlSessionFactory;

public class StudentService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * <pre>
   * SqlSession不是线程安全的。
   * 
   * 或者使用
   * Student student = (Student)sqlSession.selectOne(
   * "com.mybatis3.mappers.StudentMapper.findStudentById", studId);
   * 
   * </pre>
   */
  public List<Student> findAllStudents() {
    SqlSession sqlSession = MyBatisSqlSessionFactory.openSession();
    try {
      StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);
      return studentMapper.findAllStudents();
    } finally {
      // If sqlSession is not closed
      // then database Connection associated this sqlSession will not be returned to pool
      // and application may run out of connections.
      sqlSession.close();
    }
  }

  public Student findStudentById(Integer studId) {
    logger.debug("Select Student By ID :{}", studId);
    SqlSession sqlSession = MyBatisSqlSessionFactory.openSession();

    try {
      StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);
      return studentMapper.findStudentById(studId);
    } finally {
      sqlSession.close();
    }
  }

  public void createStudent(Student student) {
    SqlSession sqlSession = MyBatisSqlSessionFactory.openSession();
    try {
      StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);
      studentMapper.insertStudent(student);
      sqlSession.commit();
    } finally {
      sqlSession.close();
    }
  }

}
