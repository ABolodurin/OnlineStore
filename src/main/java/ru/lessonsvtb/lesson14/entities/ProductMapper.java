package ru.lessonsvtb.lesson14.entities;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ProductMapper implements Function<ProductDTO, Product> {
    @Override
    public Product apply(ProductDTO productDTO) {
        return new Product(
                productDTO.getId(),
                productDTO.getTitle(),
                productDTO.getPrice(),
                productDTO.getProductDetails());
    }

}
