package com.courseflow.aiquiz.config;

import com.courseflow.aiquiz.entity.Course;
import com.courseflow.aiquiz.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CourseSeeder {

    @Bean
    CommandLineRunner initDatabase(CourseRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.saveAll(List.of(
                    new Course(null, "Python", "Coding", 0.0, "Free", "https://images.seeklogo.com/logo-png/33/1/python-logo-png_seeklogo-332789.png", "https://drive.google.com/file/d/1b05BssGPKNt7SUCz_bDqs62d75JnVhYh/preview"),
                    new Course(null, "Java", "Coding", 0.0, "Course", "https://logos-world.net/wp-content/uploads/2022/07/Java-Logo-700x394.png", "https://drive.google.com/file/d/1LDLQeWHiDMRQJzrIVU3wpcmD1COzD8bn/preview"),
                    new Course(null, "React", "Frontend", 0.0, "Free", "https://images.unsplash.com/photo-1670057046254-3b5095eb4b66?fm=jpg&q=60&w=3000&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTV8fHJlYWN0JTIwanN8ZW58MHx8MHx8fDA=", "https://drive.google.com/file/d/13RDsjbn9mS8ahKYGXdZh19FfpWVKZrZk/preview")
                ));
                System.out.println("Database seeded with sample courses.");
            }
        };
    }
}
