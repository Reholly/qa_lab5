package com.example.demo;


import com.example.demo.core.StudentRepository;
import com.example.demo.core.StudentService;
import com.example.demo.exception.StudentNotFoundException;
import com.example.demo.integration.ChuckClient;
import com.example.demo.model.ChuckResponse;
import com.example.demo.model.Gender;
import com.example.demo.model.Student;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.status;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@WireMockTest(httpPort = 8000)

public class ChuckJokeTest {
    @Autowired
    private TestRestTemplate client;

    @Autowired
    private ChuckClient chuckClient;

    @Autowired
    StudentRepository sr;

    @Autowired
    StudentService service;

    // Task 1
    @Test
    public void saveWithInternalServerErrorStatusCode() {
        var response = new ChuckResponse("Random joke!");
        stubFor(WireMock.get("/jokes/random").willReturn(status(500)));

        var testStudent = new Student("Test", "test@test.com", Gender.MALE);
        client.postForEntity("/api/v1/students", testStudent, void.class);
        var testStudentFromService = client.getForObject("/api/v1/students/1", Student.class);

        assertEquals(response.getValue(), testStudentFromService.getJoke());
    }

    // Task 2
    @Test
    public void getUndefinedUserWithNotFoundStatusCode() {
        var service = new StudentService(sr, null, null);
        ResponseEntity<String> responseEntity = client.exchange("/api/v1/students/100", HttpMethod.GET, null, String.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void getDefinedUserWithOkStatusCode() {
        var testStudent = new Student(null, null, null);
        client.postForEntity("/api/v1/students", testStudent, void.class);
        Student expected = client.getForObject("/api/v1/students/1", Student.class);

        assertEquals(expected, testStudent);
    }

    // Task 3


}
