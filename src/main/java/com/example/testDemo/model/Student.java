package com.example.testDemo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public class Student {
    private UUID id;
    private String name;


    public Student(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

//    @JsonIgnore
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
