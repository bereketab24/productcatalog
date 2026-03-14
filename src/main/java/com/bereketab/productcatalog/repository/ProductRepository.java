package com.bereketab.productcatalog.repository;

import com.bereketab.productcatalog.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

// Supports dynamic filtering and pagination via JpaSpecificationExecutor
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

}
