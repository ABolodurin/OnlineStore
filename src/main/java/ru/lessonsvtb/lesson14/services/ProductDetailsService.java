package ru.lessonsvtb.lesson14.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lessonsvtb.lesson14.entities.ProductDetails;
import ru.lessonsvtb.lesson14.repositories.ProductDetailsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDetailsService {
    private final ProductDetailsRepository productDetailsRepository;

    @Transactional
    public void add(ProductDetails productDetails) {
        productDetailsRepository.save(productDetails);
    }

    public List<ProductDetails> findMostViewed() {
        return productDetailsRepository.findTop3ByOrderByViewsDesc();
    }

}
