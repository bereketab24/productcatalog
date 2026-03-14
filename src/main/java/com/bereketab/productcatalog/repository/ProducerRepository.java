package com.bereketab.productcatalog.repository;

import com.bereketab.productcatalog.model.Producer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProducerRepository extends JpaRepository<Producer, Long> {
}
