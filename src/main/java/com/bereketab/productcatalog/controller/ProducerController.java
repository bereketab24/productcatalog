package com.bereketab.productcatalog.controller;

import com.bereketab.productcatalog.dto.ProducerResponse;
import com.bereketab.productcatalog.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for producer lookup operations.
 * Currently, supports:
 * - listing all producers
 * - retrieving producer details by ID
 * Producer creation/update can be added here in future if required.
 */
@RestController
@RequestMapping("/api/v1/producers")
@RequiredArgsConstructor
@Tag(name = "Producers", description = "Producer lookup APIs")
public class ProducerController {

    private final ProductService productService;

    @Operation(summary = "List all producers")
    @GetMapping
    public ResponseEntity<List<ProducerResponse>> listProducers() {
        return ResponseEntity.ok(productService.getAllProducers());
    }

    @Operation(summary = "Get producer by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProducerResponse> getProducerById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProducerById(id));
    }
}
