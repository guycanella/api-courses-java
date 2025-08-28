package com.guycanella.api_courses_java.web;

import java.util.UUID;

public record CourseCreatedResponse(UUID courseId, String error) {}
