package com.bereketab.productcatalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Producer response")
public record ProducerResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Samsung") String name
) {}
