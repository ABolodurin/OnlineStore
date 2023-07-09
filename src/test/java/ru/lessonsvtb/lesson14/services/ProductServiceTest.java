package ru.lessonsvtb.lesson14.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.lessonsvtb.lesson14.entities.Product;
import ru.lessonsvtb.lesson14.entities.ProductDTO;
import ru.lessonsvtb.lesson14.entities.ProductDTOMapper;
import ru.lessonsvtb.lesson14.entities.ProductDetails;
import ru.lessonsvtb.lesson14.entities.ProductMapper;
import ru.lessonsvtb.lesson14.repositories.ProductRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductDetailsService productDetailsService;
    private ProductDTOMapper productDTOMapper;

    @BeforeEach
    void setUp() {
        productDTOMapper = new ProductDTOMapper();
        productService =
                new ProductService(productRepository, productDetailsService, productDTOMapper, new ProductMapper());
    }

    @Test
    void canGetProductDTOById() {
        Long expected = 8L;
        productService.getById(expected);

        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(productRepository).findById(longArgumentCaptor.capture());

        Long actual = longArgumentCaptor.getValue();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void canDeleteProductById() {
        Long expected = 15L;
        productService.deleteProduct(expected);

        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(productRepository).deleteById(longArgumentCaptor.capture());

        Long actual = longArgumentCaptor.getValue();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void canGetPageOfProductDTOsWithSpecs() {
        String titleContains = "word";
        int minPrice = 10;
        int maxPrice = 99;
        int page = 1;
        int size = 2;
        Pageable pageableExpected = PageRequest.of(page, size);

        try {
            productService.productPage(pageableExpected, titleContains, minPrice, maxPrice);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        ArgumentCaptor<Object> specificationArgumentCaptor = ArgumentCaptor.forClass(Object.class);
        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(productRepository).findAll(
                (Specification<Product>) specificationArgumentCaptor.capture(),
                pageableArgumentCaptor.capture());

        assertThat(pageableArgumentCaptor.getValue()).isEqualTo(pageableExpected);
    }

    @Test
    void canFindAll() {
        productService.findAll();

        verify(productRepository).findAll();
    }

    @Test
    void canUpdateProduct() {
        ProductDTO expected = new ProductDTO(134L, "someProduct", 135, new ProductDetails());

        productService.updateProduct(expected.getId(), expected);

        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productArgumentCaptor.capture());

        Product actual = productArgumentCaptor.getValue();

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
        assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
        assertThat(actual.getProductDetails()).isEqualTo(expected.getProductDetails());
    }

    @Test
    void canAddProduct() {
        ProductDTO expected =
                new ProductDTO(12L, "someProduct", 135, new ProductDetails());

        productService.add(expected);

        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productArgumentCaptor.capture());

        Product actual = productArgumentCaptor.getValue();

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
        assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
        assertThat(actual.getProductDetails()).isEqualTo(expected.getProductDetails());
    }

    @Test
    void canGet3MostViewedProductDTO() {
        List<ProductDetails> given = List.of(new ProductDetails(1L, 23L, new Product()),
                new ProductDetails(2L, 22L, new Product()));
        given(productDetailsService.findMostViewed()).willReturn(given);

        List<ProductDTO> actual = productService.getMostViewed();

        verify(productDetailsService).findMostViewed();

        for (int i = 0; i < given.size(); i++) {
            ProductDetails productDetails = given.get(i);
            ProductDTO productFromDetails = actual.get(i);

            assertThat(productFromDetails.getId()).isEqualTo(productDetails.getProductId());
        }
    }

    @Test
    void canMapToDTO() {
        Product expected =
                new Product(12L, "someProduct", 135, new ProductDetails());

        ProductDTO actual = productDTOMapper.apply(expected);

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
        assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
        assertThat(actual.getProductDetails()).isEqualTo(expected.getProductDetails());
    }

    @Test
    void canMapToProduct() {
        ProductDTO expected =
                new ProductDTO(12L, "someProduct", 135, new ProductDetails());

        Product actual = productService.map(expected);

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
        assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
        assertThat(actual.getProductDetails()).isEqualTo(expected.getProductDetails());
    }

    @Test
    void updateDetails() {
        List<Product> given = List.of(new Product(1L, "someProduct", 151436, null),
                new Product(2L, "someProduct1", 5325, new ProductDetails()));
        given(productRepository.findAll()).willReturn(given);

        productService.updateDetails();

        verify(productRepository).findAll();

        given.forEach(product ->

            assertThat(product.getProductDetails()).isNotNull());
    }

}
