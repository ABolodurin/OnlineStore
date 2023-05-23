package ru.lessonsvtb.lesson14.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.lessonsvtb.lesson14.entities.Product;
import ru.lessonsvtb.lesson14.entities.ProductDTO;
import ru.lessonsvtb.lesson14.entities.ProductDTOMapper;
import ru.lessonsvtb.lesson14.repositories.ProductRepository;
import ru.lessonsvtb.lesson14.repositories.specifications.ProductSpecs;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private ProductRepository productRepository;
    private ProductDetailsService productDetailsService;
    private ProductDTOMapper productDTOMapper;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    public void setProductDetailsService(ProductDetailsService productDetailsService) {
        this.productDetailsService = productDetailsService;
    }

    @Autowired
    public void setProductDTOMapper(ProductDTOMapper productDTOMapper) {
        this.productDTOMapper = productDTOMapper;
    }

    public Product getById(Long id) {
        return productRepository.findById(id).get();
    }

    public void deleteProduct(Long id) {
        productRepository.delete(getById(id));
    }

    public Page<ProductDTO> productPage(Specification<Product> specifications, Pageable pageable) {
        List<ProductDTO> productList = productRepository.findAll(specifications).stream()
                .map(productDTOMapper).collect(Collectors.toList());
        return new PageImpl<>(productList, pageable, productList.size());
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public void updateProduct(Long id, Product updatedProduct) {
        productRepository.updateById(id, updatedProduct.getTitle(), updatedProduct.getPrice());
    }

    public void add(Product product) {
        productRepository.save(product);
    }

    public Specification<Product> addFilters(String titleContains, Integer minPrice, Integer maxPrice) {
        Specification<Product> specs = Specification.where(null);
        if (titleContains != null) specs = specs.and(ProductSpecs.titleContains(titleContains));
        if (minPrice != null) specs = specs.and(ProductSpecs.priceGreaterOrEqualTo(minPrice));
        if (maxPrice != null) specs = specs.and(ProductSpecs.priceLessOrEqualTo(maxPrice));
        return specs;
    }

    public List<ProductDTO> getMostViewed(int limit) {
        List<Product> products = new ArrayList<>();
        productDetailsService.findMostViewed(limit).forEach(productDetails -> products.
                add(getById(productDetails.getProductId())));
        return products.stream()
                .map(productDTOMapper)
                .collect(Collectors.toList());
    }

    public List<Integer> getPageNumbers(Page<ProductDTO> products) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < products.getTotalPages(); i++) {
            result.add(i);
        }
        return result;
    }

}
