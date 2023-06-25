package ru.lessonsvtb.lesson14.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.lessonsvtb.lesson14.entities.Product;
import ru.lessonsvtb.lesson14.entities.ProductDTO;
import ru.lessonsvtb.lesson14.entities.ProductDTOMapper;
import ru.lessonsvtb.lesson14.entities.ProductDetails;
import ru.lessonsvtb.lesson14.entities.ProductMapper;
import ru.lessonsvtb.lesson14.repositories.ProductRepository;
import ru.lessonsvtb.lesson14.repositories.specifications.ProductSpecs;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductDetailsService productDetailsService;
    private final ProductDTOMapper productDTOMapper;
    private final ProductMapper productMapper;

    public ProductDTO getById(Long id) {
        return productRepository
                .findById(id)
                .map(productDTOMapper)
                .orElse(new ProductDTO(id, null, 0, new ProductDetails()));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Page<ProductDTO> productPage(Pageable pageable,
                                        String titleContains, Integer minPrice, Integer maxPrice) {
        return productRepository
                .findAll(this.getSpecFrom(titleContains, minPrice, maxPrice), pageable)
                .map(productDTOMapper);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public void updateProduct(Long id, ProductDTO updatedProduct) {
        Product product = productRepository.findById(id)
                .orElse(new Product(id, null, 0, updatedProduct.getProductDetails()));
        product.setPrice(updatedProduct.getPrice());
        product.setTitle(updatedProduct.getTitle());
        productRepository.save(product);
    }

    public void add(ProductDTO productDTO) {
        Product product = productMapper.apply(productDTO);
        productRepository.save(product);
    }

    private Specification<Product> getSpecFrom(String titleContains, Integer minPrice, Integer maxPrice) {
        Specification<Product> specs = Specification.where(null);
        if (titleContains != null) specs = specs.and(ProductSpecs.titleContains(titleContains));
        if (minPrice != null) specs = specs.and(ProductSpecs.priceGreaterOrEqualTo(minPrice));
        if (maxPrice != null) specs = specs.and(ProductSpecs.priceLessOrEqualTo(maxPrice));
        return specs;
    }

    public List<ProductDTO> getMostViewed() {
        List<ProductDTO> products = new ArrayList<>();
        productDetailsService
                .findMostViewed()
                .forEach(productDetails -> products.add(getById(productDetails.getProductId())));
        return products;
    }

    public Product map(ProductDTO productDTO) {
        return productMapper.apply(productDTO);
    }

    public void updateDetails() {
        findAll().forEach(product -> {
            if (product.getProductDetails() == null) {
                product.setProductDetails(new ProductDetails(product.getId(), 0L, product));
                updateProduct(product.getId(), productDTOMapper.apply(product));
            }
        });
    }

}
