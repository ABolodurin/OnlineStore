package ru.lessonsvtb.lesson14.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.lessonsvtb.lesson14.entities.Product;
import ru.lessonsvtb.lesson14.entities.ProductDetails;
import ru.lessonsvtb.lesson14.repositories.ProductDetailsRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class ProductDetailsServiceTest {

    @Mock
    private ProductDetailsRepository productDetailsRepository;

    private ProductDetailsService productDetailsService;

    @BeforeEach
    void setUp() {
        productDetailsService = new ProductDetailsService(productDetailsRepository);
    }


    @Test
    void canAddProductDetailsInDB() {
        ProductDetails expected = new ProductDetails();
        expected.setProduct(new Product());
        expected.setViews(0L);

        productDetailsService.add(expected);

        ArgumentCaptor<ProductDetails> productDetailsArgumentCaptor =
                ArgumentCaptor.forClass(ProductDetails.class);
        verify(productDetailsRepository)
                .save(productDetailsArgumentCaptor.capture());
        ProductDetails actual = productDetailsArgumentCaptor.getValue();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void canFindMostViewed() {
        productDetailsService.findMostViewed();

        verify(productDetailsRepository).findTop3ByOrderByViewsDesc();
    }

}
