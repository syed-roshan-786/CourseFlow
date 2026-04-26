package com.courseflow.aiquiz.service;

import com.courseflow.aiquiz.dto.QuizResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuizService {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public QuizResponse generateQuiz(MultipartFile file, String level) throws IOException {
        // Extract text from PDF
        String text = extractTextFromPdf(file);

        if (text == null || text.trim().isEmpty()) {
            throw new RuntimeException("Could not extract text from the PDF. It may be a scanned image.");
        }

        // Generate quiz using OpenAI
        String prompt = String.format(
                """
                        You are a helpful assistant. Generate exactly 10 %s-level multiple-choice quiz questions from the following text.
                        Return ONLY valid JSON, nothing else, no explanations.
                        Format strictly as:
                        {
                          "questions": [
                            { "question": "Q1 text", "options": ["A","B","C","D"], "answer": 1 },
                            { "question": "Q2 text", "options": ["A","B","C","D"], "answer": 0 }
                          ]
                        }
                        The "answer" must be the zero-based index of the correct option. For example, if "B" is the correct answer, the value for "answer" must be 1.
                        Text:
                        %s
                        """,
                level, text);

        return callOpenAI(prompt);
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private QuizResponse callOpenAI(String prompt) {
        try {
            System.out.println("🔑 OpenAI API Key (first 10 chars): "
                    + openaiApiKey.substring(0, Math.min(10, openaiApiKey.length())));
            System.out.println("📡 Calling OpenAI API...");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openaiApiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-4o-mini");
            requestBody.put("messages", List.of(
                    Map.of("role", "user", "content", prompt)));
            requestBody.put("temperature", 0.5);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(
                    "https://api.openai.com/v1/chat/completions",
                    entity,
                    Map.class);

            System.out.println("✅ OpenAI API call successful");

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            @SuppressWarnings("unchecked")
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            String content = (String) message.get("content");

            // Clean potential markdown formatting
            String cleanedContent = content.replaceAll("```json\\n?|```", "");

            return objectMapper.readValue(cleanedContent, QuizResponse.class);
        } catch (Exception e) {
            System.err.println("❌ OpenAI API Error: " + e.getClass().getName());
            System.err.println("❌ Error message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to generate quiz: " + e.getMessage(), e);
        }
    }

    public Map<String, Integer> evaluateQuiz(List<QuizResponse.Question> questions, List<String> answers) {
        int correct = 0;

        for (int i = 0; i < questions.size(); i++) {
            QuizResponse.Question question = questions.get(i);
            String userAnswer = answers.get(i);
            String correctAnswer = question.getOptions().get(question.getAnswer());

            if (userAnswer.equals(correctAnswer)) {
                correct++;
            }
        }

        int total = questions.size();
        int wrong = total - correct;

        Map<String, Integer> result = new HashMap<>();
        result.put("total", total);
        result.put("correct", correct);
        result.put("wrong", wrong);

        return result;
    }
}
