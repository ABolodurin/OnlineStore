package ru.lessonsvtb.lesson14.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.lessonsvtb.lesson14.entities.ProductDTO;
import ru.lessonsvtb.lesson14.entities.ProductDetails;
import ru.lessonsvtb.lesson14.services.ProductDetailsService;
import ru.lessonsvtb.lesson14.services.ProductService;
import ru.lessonsvtb.lesson14.services.UserService;

import java.nio.charset.StandardCharsets;
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
@MockBeans(@MockBean(UserService.class))
class ProductsControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private ProductService productService;
    @MockBean
    private ProductDetailsService productDetailsService;
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

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);

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
    void sendProductToServiceAndReturns3xx() throws Exception {
        String titleExpected = "someProduct";
        int priceExpected = 1234;

        MockHttpServletRequestBuilder requestBuilder = post("/products/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("title=" + titleExpected + "&price=" + priceExpected)
                .characterEncoding(StandardCharsets.UTF_8)
                .with(csrf());

        mvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/products"));

        ArgumentCaptor<ProductDTO> productArgumentCaptor = ArgumentCaptor.forClass(ProductDTO.class);

        verify(productDetailsService).add(ArgumentMatchers.any());
        verify(productService).map(productArgumentCaptor.capture());

        ProductDTO actual = productArgumentCaptor.getValue();

        assertThat(actual.getTitle()).isEqualTo(titleExpected);
        assertThat(actual.getPrice()).isEqualTo(priceExpected);
    }

    @Test
    void showOneProductAndReturns200() throws Exception {
        ProductDTO productDTO = givenProductDTOs.get(0);
        Long expected = productDTO.getId();
        given(productService.getById(ArgumentMatchers.any())).willReturn(productDTO);

        mvc.perform(get("/products/show/" + expected + "/")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk());

        verify(productService).getById(expected);
    }

    @Test
    void deleteProductByIdAndReturns3xx() throws Exception {
        Long expected = 3L;

        mvc.perform(post("/products/show/" + expected + "/delete")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/products"));

        verify(productService).deleteProduct(expected);
    }

    @Test
    void updateProductAndReturns3xx() throws Exception {
        ProductDTO expected = givenProductDTOs.get(0);

        mvc.perform(post("/products/show/" + expected.getId() + "/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .content("title=" + expected.getTitle() +
                                "&price=" + expected.getPrice())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/products"));
        ArgumentCaptor<ProductDTO> productDTOArgumentCaptor = ArgumentCaptor.forClass(ProductDTO.class);

        verify(productService).updateProduct(
                ArgumentMatchers.any(),
                productDTOArgumentCaptor.capture());

        ProductDTO actual = productDTOArgumentCaptor.getValue();

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
        assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
    }

    @Test
    void initViewsAndReturns3xx() throws Exception {
        mvc.perform(get("/products/init")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/products"));

        verify(productService).updateDetails();
    }

}
