package com.courseflow.aiquiz.controller;

import com.courseflow.aiquiz.dto.QuizResponse;
import com.courseflow.aiquiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PostMapping("/api/quiz")
    public ResponseEntity<?> generateQuiz(@RequestParam("file") MultipartFile file,
            @RequestParam(value = "level", defaultValue = "Medium") String level) {
        try {
            System.out.println("📝 Quiz generation request received");
            System.out.println("File: " + file.getOriginalFilename() + " (" + file.getSize() + " bytes)");
            System.out.println("Level: " + level);

            if (file.isEmpty()) {
                System.out.println("❌ File is empty");
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "No file uploaded"));
            }

            QuizResponse quiz = quizService.generateQuiz(file, level);
            System.out.println("✅ Quiz generated successfully with " + quiz.getQuestions().size() + " questions");
            return ResponseEntity.ok(quiz);
        } catch (Exception e) {
            System.err.println("❌ Error generating quiz: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", "An unexpected error occurred on the server.", "details", e.getMessage()));
        }
    }

    @PostMapping("/quiz/evaluate")
    public ResponseEntity<?> evaluateQuiz(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<QuizResponse.Question> questions = (List<QuizResponse.Question>) request.get("questions");
            @SuppressWarnings("unchecked")
            List<String> answers = (List<String>) request.get("answers");

            if (questions == null || answers == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Questions or answers missing"));
            }

            Map<String, Integer> result = quizService.evaluateQuiz(questions, answers);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
