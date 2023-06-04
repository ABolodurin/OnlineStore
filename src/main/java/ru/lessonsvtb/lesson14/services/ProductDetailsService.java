package ru.lessonsvtb.lesson14.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lessonsvtb.lesson14.entities.ProductDetails;
import ru.lessonsvtb.lesson14.repositories.ProductDetailsRepository;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductDetailsService {
    private ProductDetailsRepository productDetailsRepository;

    @Transactional
    public void add(ProductDetails productDetails){
        productDetailsRepository.save(productDetails);
    }

    public List<ProductDetails> findMostViewed(){
        return productDetailsRepository.findTop3ByOrderByViewsDesc();
    }

}
