package com.bereketab.productcatalog.service;

import com.bereketab.productcatalog.dto.PagedResponse;
import com.bereketab.productcatalog.dto.ProducerResponse;
import com.bereketab.productcatalog.dto.ProductCreateRequest;
import com.bereketab.productcatalog.dto.ProductResponse;
import com.bereketab.productcatalog.mapper.ProductMapper;
import com.bereketab.productcatalog.model.Product;
import com.bereketab.productcatalog.model.Producer;
import com.bereketab.productcatalog.repository.ProducerRepository;
import com.bereketab.productcatalog.repository.ProductRepository;
import com.bereketab.productcatalog.repository.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProducerRepository producerRepository;
    private final ProductMapper mapper;

    /**
     * Retrieves paginated products using dynamic filtering parameters.
     * Delegates filtering logic to Specification layer and maps entities to response DTOs.
     */
    @Transactional(readOnly = true)
    public PagedResponse<ProductResponse> getAllProducts(String name, String producerName, String attrKey, String attrValue, Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(
                ProductSpecification.filterProducts(name, producerName, attrKey, attrValue),
                pageable
        );
        Page<ProductResponse> responsePage = productPage.map(mapper::toResponse);

        return mapper.toPagedResponse(responsePage);
    }

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        Producer producer = producerRepository.findById(request.producerId())
                .orElseThrow(() -> new IllegalArgumentException("Producer not found with id: " + request.producerId()));

        Product product = mapper.toEntity(request, producer);
        Product saved = productRepository.save(product);
        return mapper.toResponse(saved);
    }

    // Update main product fields fully.
    // Attributes (JSONB) are updated only if provided to prevent overwriting existing dynamic keys.
    @Transactional
    public ProductResponse updateProduct(Long id, ProductCreateRequest request) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        Producer producer = producerRepository.findById(request.producerId())
                .orElseThrow(() -> new IllegalArgumentException("Producer not found"));

        existing.setName(request.name());
        existing.setDescription(request.description());
        existing.setPrice(request.price());
        existing.setProducer(producer);

        if (request.attributes() != null) {
            existing.setAttributes(request.attributes());
        }

        //No need to call productRepository.save(existing)
        //Because of @Transactional, Hibernate's "Dirty Checking" automatically detects
        //the changes and writes the UPDATE statement when the method finishes.
        return mapper.toResponse(existing);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

//    Bonus Producer End points


    @Transactional(readOnly = true)
    public List<ProducerResponse> getAllProducers() {
        return producerRepository.findAll().stream()
                .map(mapper::toProducerResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProducerResponse getProducerById(Long id) {
        Producer producer = producerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producer not found with id: " + id));
        return mapper.toProducerResponse(producer);
    }
}
