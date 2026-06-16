package com.luanpaiva.observador_de_precos.modules.products.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.luanpaiva.observador_de_precos.modules.products.dto.ProductFilterDTO;
import com.luanpaiva.observador_de_precos.modules.products.entity.Product;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public final class ProductSpecification {

        private ProductSpecification() {
                throw new IllegalStateException("Utility class");
        }

        public static Specification<Product> filter(
                        UUID userId,
                        ProductFilterDTO filter) {

                return (root, query, cb) -> {

                        List<Predicate> predicates = new ArrayList<>();

                        addUserFilter(predicates, root, cb, userId);
                        addStringFilters(predicates, root, cb, filter);
                        addBooleanFilters(predicates, root, cb, filter);
                        addPriceFilters(predicates, root, cb, filter);

                        return cb.and(predicates.toArray(new Predicate[0]));
                };
        }

        private static void addUserFilter(
                        List<Predicate> predicates,
                        Root<Product> root,
                        CriteriaBuilder cb,
                        UUID userId) {
                predicates.add(
                                cb.equal(
                                                root.get("user").get("id"),
                                                userId));
        }

        private static void addStringFilters(
                        List<Predicate> predicates,
                        Root<Product> root,
                        CriteriaBuilder cb,
                        ProductFilterDTO filter) {

                addLikePredicate(
                                predicates,
                                root,
                                cb,
                                "title",
                                filter.title());

                addLikePredicate(
                                predicates,
                                root,
                                cb,
                                "url",
                                filter.url());

                addEqualStringPredicate(
                                predicates,
                                root,
                                cb,
                                "store",
                                filter.store());

                addEqualStringPredicate(
                                predicates,
                                root,
                                cb,
                                "sku",
                                filter.sku());
        }

        private static void addBooleanFilters(
                        List<Predicate> predicates,
                        Root<Product> root,
                        CriteriaBuilder cb,
                        ProductFilterDTO filter) {

                if (filter.active() != null) {
                        predicates.add(
                                        cb.equal(
                                                        root.get("active"),
                                                        filter.active()));
                }

                if (filter.available() != null) {
                        predicates.add(
                                        cb.equal(
                                                        root.get("available"),
                                                        filter.available()));
                }
        }

        private static void addPriceFilters(
                        List<Predicate> predicates,
                        Root<Product> root,
                        CriteriaBuilder cb,
                        ProductFilterDTO filter) {

                if (filter.currentPriceEq() != null) {
                        predicates.add(
                                        cb.equal(
                                                        root.get("currentPrice"),
                                                        filter.currentPriceEq()));
                }

                if (filter.currentPriceGt() != null) {
                        predicates.add(
                                        cb.greaterThan(
                                                        root.get("currentPrice"),
                                                        filter.currentPriceGt()));
                }

                if (filter.currentPriceLt() != null) {
                        predicates.add(
                                        cb.lessThan(
                                                        root.get("currentPrice"),
                                                        filter.currentPriceLt()));
                }
        }

        private static void addLikePredicate(
                        List<Predicate> predicates,
                        Root<Product> root,
                        CriteriaBuilder cb,
                        String field,
                        String value) {

                if (value != null && !value.isBlank()) {
                        predicates.add(
                                        cb.like(
                                                        cb.lower(root.get(field)),
                                                        "%" + value.toLowerCase() + "%"));
                }
        }

        private static void addEqualStringPredicate(
                        List<Predicate> predicates,
                        Root<Product> root,
                        CriteriaBuilder cb,
                        String field,
                        String value) {

                if (value != null && !value.isBlank()) {
                        predicates.add(
                                        cb.equal(
                                                        root.get(field),
                                                        value));
                }
        }
}