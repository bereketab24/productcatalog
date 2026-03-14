package com.bereketab.productcatalog.dto;

import java.util.List;

public record PagedResponse<T>(
        List<T> data,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean isLast
) {
}