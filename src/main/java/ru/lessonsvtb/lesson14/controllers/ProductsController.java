package ru.lessonsvtb.lesson14.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.lessonsvtb.lesson14.entities.Product;
import ru.lessonsvtb.lesson14.entities.ProductDTO;
import ru.lessonsvtb.lesson14.entities.ProductDetails;
import ru.lessonsvtb.lesson14.services.ProductDetailsService;
import ru.lessonsvtb.lesson14.services.ProductService;

@Controller
@RequestMapping("/products")
public class ProductsController {
    private ProductService productsService;
    private ProductDetailsService productDetailsService;

    @Autowired
    public void setProductDetailsService(ProductDetailsService productDetailsService) {
        this.productDetailsService = productDetailsService;
    }

    @Autowired
    public void setProductsService(ProductService productsService) {
        this.productsService = productsService;
    }

    @GetMapping
    public String showProductsList(Model model,
                                   @RequestParam(value = "title_contains", required = false) String titleContains,
                                   @RequestParam(value = "from", required = false) Integer minPrice,
                                   @RequestParam(value = "to", required = false) Integer maxPrice,
                                   @RequestParam(value = "page", required = false) Integer page) {
        Product product = new Product();
        model.addAttribute("product", product);

        if (page == null) page = 0;
        model.addAttribute("page", page);
        model.addAttribute("title_contains", titleContains);
        model.addAttribute("from", minPrice);
        model.addAttribute("to", maxPrice);
        Page<ProductDTO> products = productsService.productPage(
                productsService.addFilters(titleContains, minPrice, maxPrice),
                PageRequest.of(page, 10));
        model.addAttribute("products", products);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("pageNumbers", productsService.getPageNumbers(products));
        model.addAttribute("mostViewed", productsService.getMostViewed(3));

        return "products";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute(value = "product") Product product) {
        ProductDetails productDetails = new ProductDetails(product.getId(), 0L, product);
        product.setProductDetails(productDetails);
        productsService.add(product);
        productDetailsService.add(productDetails);
        return "redirect:/products";
    }

    @GetMapping("/show/{id}")
    public String showOneProduct(Model model, @PathVariable(value = "id") Long id) {
        Product product = productsService.getById(id);
        Product editProduct = new Product();
        model.addAttribute("product", product);
        model.addAttribute("editProduct", editProduct);
        return "product-page";
    }

    @PostMapping("show/{id}/delete")
    public String removeProduct(@PathVariable(value = "id") Long id) {
        productsService.deleteProduct(id);
        return "redirect:/products";
    }

    @PostMapping("show/{id}/update")
    public String updateProduct(@PathVariable(value = "id") Long id, Product updatedProduct) {
        productsService.updateProduct(id, updatedProduct);
        return "redirect:/products";
    }

    @GetMapping("/init")
    public String initViews() {
        productsService.findAll().forEach(product ->
        {
            if (product.getProductDetails() == null) {
                product.setProductDetails(new ProductDetails(product.getId(), 0L, product));
                productsService.updateProduct(product.getId(), product);
            }
        });
        return "redirect:/products";
    }

}
