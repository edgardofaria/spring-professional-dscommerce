package com.spring_class.dscommerce.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.spring_class.dscommerce.dto.product.ProductMinDTO;
import com.spring_class.dscommerce.service.ProductService;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService service;

    private ProductMinDTO productMinDTO;

    private PageImpl<ProductMinDTO> page;

    private final Long EXISTING_ID = 1L;
    private final Long NON_EXISTING_ID = 2L;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        productMinDTO = new ProductMinDTO(EXISTING_ID, "Laptop", new BigDecimal("1999.90"),
                "https://example.com/laptop.jpg");
        page = new PageImpl<>(List.of(productMinDTO));
    }

    @Test
    void findAllShouldReturnPage() throws Exception {
        doReturn(page).when(service).findAll(any(), any());

        mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findByIdShouldReturnProductWhenIdExists() throws Exception {
        doReturn(productMinDTO).when(service).findById(EXISTING_ID);

        mockMvc.perform(get("/products/{id}", EXISTING_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(EXISTING_ID))
                .andExpect(jsonPath("$.name").value(productMinDTO.name()))
                .andExpect(jsonPath("$.price").value(productMinDTO.price()))
                .andExpect(jsonPath("$.imgUrl").value(productMinDTO.imgUrl()));
    }

    @Test
    void findByIdShouldReturnProductNotFoundWhenIdDoesNotExist() throws Exception {
        doReturn(null).when(service).findById(NON_EXISTING_ID);

        mockMvc.perform(get("/products/{id}", NON_EXISTING_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
