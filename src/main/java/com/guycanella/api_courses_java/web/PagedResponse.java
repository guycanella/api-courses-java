package com.guycanella.api_courses_java.web;

import java.util.List;

public record PagedResponse<T>(
        List<T> items,
        long total,
        int page,
        int limit,
        boolean hasNext,
        boolean hasPrevious
) {}
