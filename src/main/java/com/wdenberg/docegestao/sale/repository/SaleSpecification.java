package com.wdenberg.docegestao.sale.repository;

import com.wdenberg.docegestao.sale.entity.Sale;
import com.wdenberg.docegestao.sale.entity.SaleStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

public class SaleSpecification {

    public static Specification<Sale> byFilters(UUID userId, SaleStatus status, LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            var predicate = cb.equal(root.get("user").get("id"), userId);

            if (status != null) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), status));
            }

            if (startDate != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("saleDate"), startDate));
            }

            if (endDate != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("saleDate"), endDate));
            }

            return predicate;
        };
    }
}