package com.courseflow.aiquiz.controller;

import com.courseflow.aiquiz.entity.Course;
import com.courseflow.aiquiz.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        try {
            List<Course> courses = courseService.getAllCourses();
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        try {
            Course course = courseService.getCourseById(id);
            return ResponseEntity.ok(course);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404)
                    .body(Map.of("message", "Course not found"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
