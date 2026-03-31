package com.wdenberg.docegestao.sale.repository;

import com.wdenberg.docegestao.sale.entity.Sale;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface SaleRepository extends JpaRepository<Sale, UUID>, JpaSpecificationExecutor<Sale> {

    @EntityGraph(attributePaths = {"client", "items", "items.recipe"})
    Optional<Sale> findByIdAndUser_Id(UUID id, UUID userId);
}
