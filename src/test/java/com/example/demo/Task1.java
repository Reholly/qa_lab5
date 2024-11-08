package com.example.demo;


import com.example.demo.core.StudentRepository;
import com.example.demo.core.StudentService;
import com.example.demo.integration.BookingClient;
import com.example.demo.integration.ChuckClient;
import com.example.demo.model.ChuckResponse;
import com.example.demo.model.Gender;
import com.example.demo.model.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@WireMockTest(httpPort = 8000)

public class Task1 {
    @Autowired
    private TestRestTemplate client;

    @Autowired
    StudentService service;

    // Task 1
    // Service test
    @Test
    public void saveWithInternalServerErrorStatusCode() {
        var response = new ChuckResponse("Random joke!");
        stubFor(WireMock.get("/jokes/random").willReturn(status(500)));

        var testStudent = new Student("Test", "test@gmail.com", Gender.MALE);
        client.postForEntity("/api/v1/students", testStudent, void.class);
        var testStudentFromService = client.getForObject("/api/v1/students/1", Student.class);

        assertEquals(response.getValue(), testStudentFromService.getJoke());
    }

    // Unit
    // Проверяем, что даже при плохом ответе от сервиса у нас не будет Exception.
    @Test
    public void testClientReturnsStatusCode200() {
        stubFor(WireMock.get("/jokes/random").willReturn(status(500)));

        assertDoesNotThrow(() ->
            service.addStudent(new Student("sd", "test@gmail.com", Gender.MALE))
        );
    }
}
