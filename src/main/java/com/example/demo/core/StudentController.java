package com.example.demo.core;

import com.example.demo.exception.StudentNotFoundException;
import com.example.demo.model.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/students")
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping(path = "{studentId}")
    public Optional<Student> getStudent(
            @PathVariable("studentId") Long studentId
    ) {
        try{
            return Optional.ofNullable(studentService.getStudent(studentId));
        } catch (StudentNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "STUDENT NOT FOUND!");
        }
    }

    @PostMapping
    public void addStudent(@Valid @RequestBody Student student) throws JsonProcessingException {
        studentService.addStudent(student);
    }

    @DeleteMapping(path = "{studentId}")
    public void deleteStudent(
            @PathVariable("studentId") Long studentId) {
        studentService.deleteStudent(studentId);
    }
}
