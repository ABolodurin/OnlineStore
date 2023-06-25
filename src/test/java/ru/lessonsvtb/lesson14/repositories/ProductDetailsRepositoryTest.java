package ru.lessonsvtb.lesson14.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.lessonsvtb.lesson14.entities.Product;
import ru.lessonsvtb.lesson14.entities.ProductDetails;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest //tests doesn't work without loading full context
class ProductDetailsRepositoryTest {

    @Autowired
    private ProductDetailsRepository productDetailsRepository;
    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void clear() {
        productDetailsRepository.deleteAll();
    }

    @Test
    void itShouldIncrementView() {
        Product product = new Product();
        productRepository.save(product);
        product = productRepository.findAll().stream().findAny().get();
        Long id = product.getId();
        ProductDetails productDetails =
                new ProductDetails(id, 0L, product);
        productDetailsRepository.save(productDetails);

        productDetailsRepository.incrementView(id);

        ProductDetails actual = productDetailsRepository.findById(id).get();
        assertThat(actual.getViews()).isEqualTo(1);
    }

    @Test
    void itShouldFindTop3ByOrderByViewsDesc() {
        for (int i = 0; i < 4; i++) {
            ProductDetails pd = new ProductDetails();
            pd.setViews((long) i);
            pd.setProduct(new Product());
            productDetailsRepository.save(pd);
        }

        List<ProductDetails> actual = productDetailsRepository.findTop3ByOrderByViewsDesc();
        boolean result = actual.size() == 3;    //top3 check
        for (int i = 0; i < 2; i++) {
            Long thisItemViews = actual.get(i).getViews();
            Long nextItemViews = actual.get(i + 1).getViews();
            result &= thisItemViews > nextItemViews;
        }

        assertThat(result).isTrue();
    }

}
