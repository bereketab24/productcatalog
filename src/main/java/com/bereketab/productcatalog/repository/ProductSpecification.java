package com.bereketab.productcatalog.repository;

import com.bereketab.productcatalog.model.Product;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Dynamic specification builder for Product search.
 * Supports filtering by name, producer and flexible JSON attributes.
 * Designed for pageable search endpoints with optional parameters.
 */
public class ProductSpecification {

    public static Specification<Product> filterProducts(String name, String producerName, String attrKey, String attrValue) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Prevent fetch join during count query used for pagination
            if (!Long.class.equals(query.getResultType()) && !long.class.equals(query.getResultType())) {
                root.fetch("producer", JoinType.INNER);
            }

            if (StringUtils.hasText(name)) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (StringUtils.hasText(producerName)) {
                predicates.add(cb.like(cb.lower(root.get("producer").get("name")), "%" + producerName.toLowerCase() + "%"));
            }

            if (StringUtils.hasText(attrKey) && StringUtils.hasText(attrValue)) {

                // PostgreSQL JSONB attribute extraction for flexible product filtering
                Expression<String> extractedJsonAttribute =
                        cb.function("jsonb_extract_path_text",
                                String.class,
                                root.get("attributes"),
                                cb.literal(attrKey));

                predicates.add(cb.equal(cb.lower(extractedJsonAttribute), attrValue.toLowerCase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
