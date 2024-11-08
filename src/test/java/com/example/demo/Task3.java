package com.example.demo;

import com.example.demo.core.StudentRepository;
import com.example.demo.core.StudentService;
import com.example.demo.integration.BookingClient;
import com.example.demo.integration.ChuckClient;
import com.example.demo.model.ChuckResponse;
import com.example.demo.model.Gender;
import com.example.demo.model.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Task3 {
    // Task 3
    @Autowired
    private ChuckClient chuckClient;

    @Autowired
    StudentRepository sr;

    @Autowired
    StudentService service;

    // Проверяем, что студент создастся нормально
    @Test
    public void studentCreatedRight() throws JsonProcessingException {
        sr = mock(StudentRepository.class);
        when(sr.selectExistsEmail(anyString())).thenReturn(false);

        var chuckMock = mock(ChuckClient.class);
        var bookMock = mock(BookingClient.class);

        when(chuckMock.getJoke()).thenReturn(new ChuckResponse(""));
        when(bookMock.createBooking(anyString())).thenReturn(1);
        var service = new StudentService(sr, chuckMock, bookMock);

        var testStudent = new Student("lol!", "lol@gmail.com", Gender.MALE);
        service.addStudent(testStudent);

        assertEquals(testStudent.getBookingId(), 1);
    }
}
