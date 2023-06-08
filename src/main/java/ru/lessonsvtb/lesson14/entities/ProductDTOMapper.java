package ru.lessonsvtb.lesson14.entities;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ProductDTOMapper implements Function<Product, ProductDTO> {
    @Override
    public ProductDTO apply(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getTitle(),
                product.getPrice(),
                product.getProductDetails()
        );
    }

}
