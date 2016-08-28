package com.spike.mybatis.mappers;

import java.util.List;

import com.spike.mybatis.domain.Student;

/**
 * {@link Student}映射器定义
 * 
 * (1) 该类为StudentMapper.xml中的命名空间
 * 
 * (2) 方法名称与StudentMapper.xml中SQL映射定义名相同
 * */
public interface StudentMapper {
	List<Student> findAllStudents();

	Student findStudentById(Integer id);

	void insertStudent(Student student);
}
