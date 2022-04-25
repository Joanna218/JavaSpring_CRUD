package com.example.testDemo.service;

import com.example.testDemo.Dao.StudentDao;
import com.example.testDemo.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StudentService {
    private StudentDao studentDao;

    @Autowired
    public StudentService(@Qualifier("fakeStudentDao") StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public List<Student> getAllStudents() {
        return studentDao.selectAllStudents();
    }

    public int insertStudent(Student student) {
        int res = studentDao.insertStudent(student);
        if (res == 1) {
            return 1;
        } else  {
            return 0;
        }
    }

    public int updateStudent(Student student) {
        int res = studentDao.updateStudent(student);
        if (res == 1) {
            return 1;
        } else  {
            return 0;
        }
    }

    public int deleteStudent(UUID id) {
        int res = studentDao.deleteStudent(id);
        if (res == 1) {
            return 1;
        } else  {
            return 0;
        }
    }
}
