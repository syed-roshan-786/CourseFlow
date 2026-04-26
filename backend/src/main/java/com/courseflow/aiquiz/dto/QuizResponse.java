package com.courseflow.aiquiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizResponse {
    private List<Question> questions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Question {
        private String question;
        private List<String> options;
        private Integer answer;
    }
}
