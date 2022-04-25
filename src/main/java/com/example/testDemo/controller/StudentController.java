package com.example.testDemo.controller;

import com.example.testDemo.model.Student;
import com.example.testDemo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController // root => \
@RequestMapping("api/student")
public class StudentController {

    @GetMapping
    @RequestMapping("/hello1")
    public String helloWorld() {
        return "helloWorld";
    }

    @GetMapping
    @RequestMapping("/hello2")
    public String helloWorld2() {
        return "helloWorld2";
    }

    private StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping
    public String addStudent(@RequestBody Student student) {
        int res = studentService.insertStudent(student);
        if (res == 1) {
            return "added Student!";
        } else {
            return "fail Student!";
        }
    }

    @PutMapping
    public String updateStudent(@RequestBody Student student) {
        int res = studentService.updateStudent(student);
        if (res == 1) {
            return "updated Student!";
        } else {
            return "fail Student!";
        }
    }

    @DeleteMapping(path = "{id}")
    public String deleteStudent(@PathVariable("id") UUID id) {
        int res = studentService.deleteStudent(id);
        if (res == 1) {
            return "deleted Student!";
        } else {
            return "fail Student!";
        }
    }
}
