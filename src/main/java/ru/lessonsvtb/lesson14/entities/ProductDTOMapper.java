package ru.lessonsvtb.lesson14.entities;

import org.springframework.stereotype.Service;
import java.util.function.Function;

@Service
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
