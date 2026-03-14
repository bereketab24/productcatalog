package com.bereketab.productcatalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Map;

@Schema(description = "Product response with producer name")
public record ProductResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Samsung Galaxy S25") String name,
        @Schema(example = "Next-gen Android flagship") String description,
        @Schema(example = "899.99") BigDecimal price,
        @Schema(example = "Samsung") String producerName,
        @Schema(example = "{\"color\":\"Titanium\"}") Map<String, Object> attributes
) {}
