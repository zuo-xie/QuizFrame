package com.quiz.controller;

import com.quiz.mapper.StudentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/student")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StudentController {

    private StudentMapper studentMapper;

}
