package com.example.testDemo.Dao;

import com.example.testDemo.model.Student;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FakeStudentDao implements StudentDao {
    private static List<Student> Database = new ArrayList<>();

    @Override
    public List<Student> selectAllStudents() {
        return Database;
    }


    // Optional 型態用在 可能回傳 null 的方法
    @Override
    public Optional<Student> selectStudentById(UUID id) {
        for (Student s : Database) {
            if (s.getId().equals(id)) {
                // 使用Optional.of(type) 確保輸入參數type不可為null
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }

    @Override
    public int insertStudent(Student student) {
        UUID id = UUID.randomUUID();
        Database.add(new Student(id, student.getName()));
        return 1;
    }

    @Override
    public int updateStudent(Student student) {
        // 真正實作資料庫要 先驗證 資料庫 是否有此筆 id 存在 => selectStudentById

        // 找到此筆資料在 Database 中的 index 位置

        // 找 index
        int indexToUpdate = -1;
        for (int i = 0; i < Database.size(); i++) {
            if (student.getId().equals(Database.get(i).getId())) {
                indexToUpdate = i;
                break;
            }
        }

        // 更新 Database
        if (indexToUpdate != -1) {
            Database.set(indexToUpdate, student);
            return  1;
        } else {
            return -1;
        }
    }

    @Override
    public int deleteStudent(UUID id) {
        Optional<Student> optionalStudent = selectStudentById(id);
        // isPresent 判斷存入的值是否為空，值不存在 返回 false，值存在返回 true
        if (!optionalStudent.isPresent()) {
            return -1;
        }

        // remove() 吃物件，但 optionalStudent 不是物件，因此要用 get 取出裡面的 Student 物件
        Database.remove(optionalStudent.get());
        return 1;
    }
}
