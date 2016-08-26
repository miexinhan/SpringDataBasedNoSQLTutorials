package com.spike.mybatis.mappers;

import java.util.List;

import com.spike.mybatis.domain.Student;

public interface StudentMapper {
	List<Student> findAllStudents();

	Student findStudentById(Integer id);

	void insertStudent(Student student);
}
