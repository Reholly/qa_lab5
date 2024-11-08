package com.example.demo;

import com.example.demo.model.Gender;
import com.example.demo.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Task2 {
    @Autowired
    private RestTemplate client;
    @LocalServerPort
    private int port;

    @Test
    public void getUndefinedUserWithNotFoundStatusCode() {
        ResponseEntity<String> responseEntity =
                client.exchange(
                        "http://localhost:"+ port +"/api/v1/students/10000",
                        HttpMethod.GET,
                        null,
                        String.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void getDefinedUser() {
        var testStudent = new Student("Test", "test@test.com", Gender.MALE);
        client.postForEntity(
                "http://localhost:"+ port +"/api/v1/students",
                testStudent,
                void.class);
        ResponseEntity<String> responseEntity = client.exchange(
                "http://localhost:"+ port +"/api/v1/students/1",
                HttpMethod.GET,
                null,
                String.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

}
