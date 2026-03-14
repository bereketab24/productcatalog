package com.bereketab.productcatalog.mapper;

import com.bereketab.productcatalog.dto.ProducerResponse;
import com.bereketab.productcatalog.dto.ProductCreateRequest;
import com.bereketab.productcatalog.dto.ProductResponse;
import com.bereketab.productcatalog.dto.PagedResponse;
import com.bereketab.productcatalog.model.Product;
import com.bereketab.productcatalog.model.Producer;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;


import java.util.HashMap;

@Component
public class ProductMapper {

    public Product toEntity(ProductCreateRequest request, Producer producer) {
        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setProducer(producer);
        product.setAttributes(request.attributes() != null ? request.attributes() : new HashMap<>());
        return product;
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getProducer().getName(),
                product.getAttributes()
        );
    }

    public ProducerResponse toProducerResponse(Producer producer) {
        return new ProducerResponse(producer.getId(), producer.getName());
    }

    public <T> PagedResponse<T> toPagedResponse(Page<T> page) {
        return new PagedResponse<>(
                page.getContent(), // This is your List<ProductResponse>
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
