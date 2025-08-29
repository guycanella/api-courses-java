package com.guycanella.api_courses_java.web;

import com.guycanella.api_courses_java.domain.Course;
import com.guycanella.api_courses_java.domain.CourseRepository;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseRepository repo;

    public CourseController(CourseRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity <PagedResponse<Course>> getAll(
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit
    ) {
        if (page < 0) { page = 0; }
        if (limit <= 0 || limit > 100) { limit = 10; }

        var pageable = org.springframework.data.domain.PageRequest.of(
                page, limit, org.springframework.data.domain.Sort.by("createdAt").descending()
        );

        var pageResult = (q == null || q.isBlank())
                ? repo.findAll(pageable)
                : repo.findByTitleContainingIgnoreCase(q.trim(), pageable);

        var body = new PagedResponse<>(
                pageResult.getContent(),
                pageResult.getTotalElements(),
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.hasNext(),
                pageResult.hasPrevious()
        );

        return ResponseEntity.ok(body);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseById(@PathVariable UUID courseId) {
        if (courseId == null) {
            throw new CustomBadRequestException("courseId is required");
        }

        var aux = ResponseEntity.of(repo.findById(courseId));
        return ResponseEntity.of(repo.findById(courseId));
    }

    @PostMapping
    public ResponseEntity<CourseCreatedResponse> createCourse(@Valid @RequestBody CourseRequest req) {
        String normalizedTitle = req.getTitle().trim();
        String normalizedDescription = req.getDescription() != null ? req.getDescription().trim() : null;

        if (normalizedTitle.length() < 3) {
            throw new CustomBadRequestException("invalid JSON body");
        }

        if  (normalizedDescription != null && normalizedDescription.length() < 3) {
            return ResponseEntity.badRequest()
                    .body(new CourseCreatedResponse(null, "invalid JSON body"));
        }

        if (repo.existsByTitle(normalizedTitle)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new CourseCreatedResponse(null, "title already exists"));
        }

        Course course = new Course();

        course.setTitle(normalizedTitle);
        course.setDescription(normalizedDescription);

        Course createdCourse = repo.save(course);

        return ResponseEntity
                .created(URI.create("/courses/" + createdCourse.getId()))
                .body(new CourseCreatedResponse(createdCourse.getId(), null));
    }
}
