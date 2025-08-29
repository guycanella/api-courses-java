package com.guycanella.api_courses_java.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {
    boolean existsByTitle(String title);

    Page<Course> findByTitleContainingIgnoreCase(String q, Pageable pageable);

//    boolean existsByCourseId(UUID courseId);
}

