package com.spring_class.dscommerce.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.spring_class.dscommerce.dto.product.ProductDTO;
import com.spring_class.dscommerce.dto.product.ProductMinDTO;
import com.spring_class.dscommerce.service.ProductService;
import com.spring_class.dscommerce.service.exceptions.ProductNotFoundException;

@WebMvcTest(value = ProductController.class, excludeAutoConfiguration = { SecurityAutoConfiguration.class })
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService service;

    private ProductDTO productDTO;

    private PageImpl<ProductMinDTO> page;

    private final Long EXISTING_ID = 1L;
    private final Long NON_EXISTING_ID = 2L;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        productDTO = new ProductDTO(EXISTING_ID, "Laptop", "", new BigDecimal("1999.9"),
                "https://example.com/laptop.jpg", Set.of());
        page = new PageImpl<>(List
                .of(new ProductMinDTO(productDTO.id(), productDTO.name(), productDTO.price(), productDTO.imgUrl())));
    }

    @Test
    void findAllShouldReturnPage() throws Exception {
        doReturn(page).when(service).findAll(isNull(), any(Pageable.class));

        mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(productDTO.id()))
                .andExpect(jsonPath("$.content[0].name").value(productDTO.name()))
                .andExpect(jsonPath("$.content[0].price").value(productDTO.price()));

        verify(service).findAll(null, PageRequest.of(0, 20));
    }

    @Test
    void findByIdShouldReturnProductWhenIdExists() throws Exception {
        doReturn(productDTO).when(service).findById(EXISTING_ID);

        mockMvc.perform(get("/products/{id}", EXISTING_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(EXISTING_ID))
                .andExpect(jsonPath("$.name").value(productDTO.name()))
                .andExpect(jsonPath("$.description").value(productDTO.description()))
                .andExpect(jsonPath("$.price").value(productDTO.price()))
                .andExpect(jsonPath("$.imgUrl").value(productDTO.imgUrl()));
    }

    @Test
    void findByIdShouldReturnProductNotFoundWhenIdDoesNotExist() throws Exception {
        doThrow(new ProductNotFoundException("Product not found"))
                .when(service).findById(NON_EXISTING_ID);

        mockMvc.perform(get("/products/{id}", NON_EXISTING_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Product not found"))
                .andExpect(jsonPath("$.path").value("/products/" + NON_EXISTING_ID));
    }
}
