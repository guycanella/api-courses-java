package com.guycanella.api_courses_java.web;

import com.guycanella.api_courses_java.domain.Course;
import com.guycanella.api_courses_java.domain.CourseRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseRepository repo;

    public CourseController(CourseRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Course> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable UUID id) {
        return ResponseEntity.of(repo.findById(id));
    }

    @PostMapping
    public ResponseEntity<CourseCreatedResponse> createCourse(@Valid @RequestBody CourseRequest req) {
        String normalizedTitle = req.getTitle().trim();

        if (repo.existsByTitle(normalizedTitle)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new CourseCreatedResponse(null, "title already exists."));
        }

        Course course = new Course();
        course.setTitle(normalizedTitle);
        course.setDescription(req.getDescription());

        Course createdCourse = repo.save(course);
        return ResponseEntity
                .created(URI.create("/courses/" + createdCourse.getId()))
                .body(new CourseCreatedResponse(createdCourse.getId(), null));
    }
}
