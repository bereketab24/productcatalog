package com.bereketab.productcatalog.controller;

import com.bereketab.productcatalog.dto.PagedResponse;
import com.bereketab.productcatalog.dto.ProductCreateRequest;
import com.bereketab.productcatalog.dto.ProductResponse;
import com.bereketab.productcatalog.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * REST controller responsible for product lifecycle operations and product search.
 * Provides:
 * - pageable product listing with dynamic filtering
 * - product creation
 * - product update
 * - product deletion
 * Filtering logic is delegated to Specification layer via ProductService.
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product management and search APIs")
public class ProductController {

    private final ProductService productService;

    @Operation(
            summary = "List products",
            description = "Returns paginated products with optional filtering by name, producer name and dynamic JSON attributes"
    )
    @GetMapping
    public ResponseEntity<PagedResponse<ProductResponse>> listProducts(
            @Parameter(description = "Product name contains (case-insensitive)")
            @RequestParam(required = false) String name,

            @Parameter(description = "Producer name contains")
            @RequestParam(required = false) String producerName,

            @Parameter(description = "Dynamic attribute key (e.g. color, energy_rating)")
            @RequestParam(required = false) String attributeKey,

            @Parameter(description = "Dynamic attribute value")
            @RequestParam(required = false) String attributeValue,

            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(
                productService.getAllProducts(name, producerName, attributeKey, attributeValue, pageable)
        );
    }

    @Operation(summary = "Create product")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody @Valid ProductCreateRequest request
    ) {
        ProductResponse response = productService.createProduct(request);

        // Build Location header for newly created resource (REST best practice)
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }


    @Operation(summary = "Update product")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody @Valid ProductCreateRequest request
    ) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @Operation(summary = "Delete product")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
