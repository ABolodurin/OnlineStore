package ru.lessonsvtb.lesson14.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.lessonsvtb.lesson14.entities.ProductDTO;
import ru.lessonsvtb.lesson14.entities.ProductDetails;
import ru.lessonsvtb.lesson14.services.ProductDetailsService;
import ru.lessonsvtb.lesson14.services.ProductService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsController {
    private final ProductService productsService;
    private final ProductDetailsService productDetailsService;

    @GetMapping
    public String showProductsList(Model model,
                                   @RequestParam(value = "title_contains", required = false) String titleContains,
                                   @RequestParam(value = "from", required = false) Integer minPrice,
                                   @RequestParam(value = "to", required = false) Integer maxPrice,
                                   @RequestParam(value = "page", required = false) Integer page) {
        ProductDTO product = new ProductDTO();
        model.addAttribute("product", product);

        if (page == null) page = 0;
        model.addAttribute("page", page);
        model.addAttribute("title_contains", titleContains);
        model.addAttribute("from", minPrice);
        model.addAttribute("to", maxPrice);
        Page<ProductDTO> products = productsService.productPage(
                PageRequest.of(page, 10),
                titleContains, minPrice, maxPrice);
        model.addAttribute("products", products);

        model.addAttribute("mostViewed", productsService.getMostViewed());

        int totalPages = products.getTotalPages();
        model.addAttribute("totalPages", totalPages);
        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 0; i < totalPages; i++) {
            pageNumbers.add(i);
        }
        model.addAttribute("pageNumbers", pageNumbers);

        return "products";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute(value = "product") ProductDTO product) {
        productDetailsService.add(new ProductDetails(
                product.getId(), 0L, productsService.map(product)));
        return "redirect:/products";
    }

    @GetMapping("/show/{id}")
    public String showOneProduct(Model model, @PathVariable(value = "id") Long id) {
        ProductDTO product = productsService.getById(id);
        ProductDTO editProduct = new ProductDTO();
        model.addAttribute("product", product);
        model.addAttribute("editProduct", editProduct);
        return "product-page";
    }

    @PostMapping("show/{id}/delete")
    public String deleteProduct(@PathVariable(value = "id") Long id) {
        productsService.deleteProduct(id);
        return "redirect:/products";
    }

    @PostMapping("show/{id}/update")
    public String updateProduct(@PathVariable(value = "id") Long id, ProductDTO updatedProduct) {
        productsService.updateProduct(id, updatedProduct);
        return "redirect:/products";
    }

    @GetMapping("/init")
    public String initViews() {
        productsService.updateDetails();
        return "redirect:/products";
    }

}
