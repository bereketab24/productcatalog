package com.bereketab.productcatalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Map;

@Schema(description = "Request to create or update a product")
public record ProductCreateRequest(
        @Schema(example = "Samsung Galaxy S25") @NotBlank String name,
        @Schema(example = "Next-gen Android flagship") String description,
        @Schema(example = "899.99") @Positive BigDecimal price,
        @Schema(example = "1") Long producerId,
        @Schema(example = "{\"color\":\"Titanium\",\"warranty_period\":24}") Map<String, Object> attributes
) {}
