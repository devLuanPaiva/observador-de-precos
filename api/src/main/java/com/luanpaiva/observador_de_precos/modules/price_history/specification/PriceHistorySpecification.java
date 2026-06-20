package com.luanpaiva.observador_de_precos.modules.price_history.specification;

import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.luanpaiva.observador_de_precos.modules.price_history.dto.PriceHistoryFilterDTO;
import com.luanpaiva.observador_de_precos.modules.price_history.entity.PriceHistory;

import org.springframework.data.jpa.domain.Specification;

public final class PriceHistorySpecification {

    private PriceHistorySpecification() {
    }

    public static Specification<PriceHistory> filter(
            UUID userId,
            PriceHistoryFilterDTO filter) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(
                    cb.equal(
                            root.get("monitoring")
                                    .get("user")
                                    .get("id"),
                            userId));

            if (filter.monitoringId() != null) {

                predicates.add(
                        cb.equal(
                                root.get("monitoring")
                                        .get("id"),
                                filter.monitoringId()));
            }

            return cb.and(
                    predicates.toArray(
                            Predicate[]::new));
        };
    }
}
