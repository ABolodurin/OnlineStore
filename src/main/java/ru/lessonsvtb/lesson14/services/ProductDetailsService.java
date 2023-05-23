package ru.lessonsvtb.lesson14.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lessonsvtb.lesson14.entities.ProductDetails;
import ru.lessonsvtb.lesson14.repositories.ProductDetailsRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductDetailsService {
    private ProductDetailsRepository productDetailsRepository;

    @Autowired
    public void setProductDetailsRepository(ProductDetailsRepository productDetailsRepository) {
        this.productDetailsRepository = productDetailsRepository;
    }

    public void countView(Long id){
        productDetailsRepository.incrementView(id);
    }

    @Transactional
    public void add(ProductDetails productDetails){
        productDetailsRepository.save(productDetails);
    }

    public List<ProductDetails> findMostViewed(int limit){
        return productDetailsRepository.findByOrderByViewsDesc().stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

}
