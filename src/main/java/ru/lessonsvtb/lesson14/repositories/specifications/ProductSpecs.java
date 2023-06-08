package ru.lessonsvtb.lesson14.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.lessonsvtb.lesson14.entities.Product;

public class ProductSpecs {
    public static Specification<Product> titleContains(String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<Product> priceGreaterOrEqualTo(int price) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), price);
    }

    public static Specification<Product> priceLessOrEqualTo(int price) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), price);
    }

}
