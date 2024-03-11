package com.emi.onlineshop.utils;

import org.springframework.data.jpa.domain.Specification;

public class Specifications {

    private Specifications() {
    }

    public static <T, S extends Comparable<S>> Specification<T> createSpecification(Filter input) {
        return switch (input.getOperation()) {
            case ":" -> ((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(input.getKey()), input.getValue()));
            case ">" -> ((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThan(root.get(input.getKey()), Double.valueOf(input.getValue())));
            case "<" -> ((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get(input.getKey()), Double.valueOf(input.getValue())));
            case ">=" -> ((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get(input.getKey()), Double.valueOf(input.getValue())));
            case "<=" -> ((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get(input.getKey()), Double.valueOf(input.getValue())));
            case "like" -> ((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(input.getKey()), "%" + input.getValue() + "%"));
            default -> throw new SpecificationException("Operation not supported yet");
        };
    }

}
