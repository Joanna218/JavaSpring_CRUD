package com.example.testDemo.Dao;

import com.example.testDemo.model.Student;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentDao {

    List<Student> selectAllStudents();

    Optional<Student> selectStudentById(UUID id);

    int insertStudent(Student student);

    int updateStudent(Student student);

    int deleteStudent(UUID id);

}
