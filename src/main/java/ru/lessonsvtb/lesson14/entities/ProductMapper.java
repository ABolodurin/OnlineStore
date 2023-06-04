package ru.lessonsvtb.lesson14.entities;

import org.springframework.stereotype.Service;
import java.util.function.Function;

@Service
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
