package ru.lessonsvtb.lesson14.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.lessonsvtb.lesson14.entities.Product;
import ru.lessonsvtb.lesson14.entities.ProductDTO;
import ru.lessonsvtb.lesson14.entities.ProductDetails;
import ru.lessonsvtb.lesson14.services.ProductDetailsService;
import ru.lessonsvtb.lesson14.services.ProductService;
import ru.lessonsvtb.lesson14.services.UserService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductsController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ProductsControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private ProductService productService;
    @MockBean
    private ProductDetailsService productDetailsService;
    @MockBean
    private UserService userService;
    private List<Product> givenProducts;
    List<ProductDTO> givenProductDTOs;

    @BeforeEach
    public void init() {
        givenProductDTOs = List.of(new ProductDTO(1L, "someProduct", 151436, null),
                new ProductDTO(2L, "someProduct1", 5325, new ProductDetails()));
    }


    @Test
    void whenValidInputPerforms200() throws Exception {
        given(productService
                .productPage(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .willReturn(new PageImpl<>(givenProductDTOs));
        String titleContainsExpected = "titleContains";
        int fromExpected = 1;
        int toExpected = 99;
        int pageExpected = 3;

        mvc.perform(get("/products")
                        .contentType(MediaType.TEXT_HTML)
                        .param("title_contains", titleContainsExpected)
                        .param("from", Integer.toString(fromExpected))
                        .param("to", Integer.toString(toExpected))
                        .param("page", Integer.toString(pageExpected)))
                .andExpect(status().isOk());
        ArgumentCaptor<String> stringArgumentCaptor =
                ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> integerArgumentCaptor =
                ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Pageable> pageableArgumentCaptor =
                ArgumentCaptor.forClass(Pageable.class);
        verify(productService).productPage(
                pageableArgumentCaptor.capture(),
                stringArgumentCaptor.capture(),
                integerArgumentCaptor.capture(),
                integerArgumentCaptor.capture());
        int pageActual = pageableArgumentCaptor.getValue().getPageNumber();
        String titleContainsActual = stringArgumentCaptor.getValue();
        int fromActual = integerArgumentCaptor.getAllValues().get(0);
        int toActual = integerArgumentCaptor.getAllValues().get(1);
        assertThat(pageActual).isEqualTo(pageExpected);
        assertThat(titleContainsActual).isEqualTo(titleContainsExpected);
        assertThat(fromActual).isEqualTo(fromExpected);
        assertThat(toActual).isEqualTo(toExpected);
        verify(productService).getMostViewed();
    }

    @Test
    @Disabled
    void sendProductToServiceAndReturns3xx() throws Exception {
        ProductDTO expected = new ProductDTO();
        String titleExpected = "someProduct";
        int priceExpected = 1234;
        expected.setPrice(priceExpected);
        expected.setTitle(titleExpected);

        mvc.perform(post("/products/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .content("&title=" + titleExpected + "&price=" + Integer.toString(priceExpected))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/products"));
        ArgumentCaptor<ProductDetails> productDetailsArgumentCaptor =
                ArgumentCaptor.forClass(ProductDetails.class);
        verify(productDetailsService).add(productDetailsArgumentCaptor.capture());
        ProductDetails actual = productDetailsArgumentCaptor.getValue();
        String titleActual = productDetailsArgumentCaptor
                .getValue()
                .getProduct()
                .getTitle();
        int priceActual = productDetailsArgumentCaptor
                .getValue()
                .getProduct()
                .getPrice();
        assertThat(titleActual).isEqualTo(titleExpected);
        assertThat(priceActual).isEqualTo(priceExpected);
    }

    @Test
    void showOneProduct() throws Exception {
        ProductDTO productDTO = givenProductDTOs.get(0);
        Long expected = productDTO.getId();
        given(productService.getById(ArgumentMatchers.any())).willReturn(productDTO);

        mvc.perform(get("/products/show/" + expected + "/")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
        verify(productService).getById(expected);
    }

    @Test
    void deleteProductById() throws Exception {
        Long expected = 3L;

        mvc.perform(post("/products/show/" + expected + "/delete")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/products"));
        verify(productService).deleteProduct(expected);
    }

    @Test
    void updateProduct() throws Exception {
        ProductDTO productDTO = givenProductDTOs.get(0);
        Long expected = productDTO.getId();

        mvc.perform(post("/products/show/" + expected + "/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/products"));
        verify(productService).updateProduct(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    void initViews() throws Exception {
        mvc.perform(get("/products/init")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/products"));
        verify(productService).updateDetails();
    }

}