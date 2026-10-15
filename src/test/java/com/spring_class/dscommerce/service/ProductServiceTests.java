package com.spring_class.dscommerce.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.spring_class.dscommerce.dto.product.ProductDTO;
import com.spring_class.dscommerce.dto.product.ProductMinDTO;
import com.spring_class.dscommerce.dto.product.ProductRequestDTO;
import com.spring_class.dscommerce.entities.Product;
import com.spring_class.dscommerce.mapper.ProductMapper;
import com.spring_class.dscommerce.repositories.ProductRepository;
import com.spring_class.dscommerce.service.exceptions.DataBaseIntegrityViolationException;
import com.spring_class.dscommerce.service.exceptions.ProductNotFoundException;

@ExtendWith(MockitoExtension.class)
class ProductServiceTests {

    private final Long EXISTING_ID = 1L;
    private final Long NON_EXISTING_ID = 2L;
    private final Long DEPENDENT_ID = 3L;

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper mapper;

    private Product product;

    private ProductDTO productDTO;

    private final Pageable pageable = Pageable.ofSize(1);

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        product = new Product("Laptop", "High-performance laptop", new BigDecimal("1999.90"),
                "https://example.com/laptop.jpg");
        productDTO = new ProductDTO(EXISTING_ID, product.getName(), product.getDescription(), product.getPrice(),
                product.getImgUrl(), Set.of());
    }

    @Test
    void findAllShouldReturnPageOfProductsWithProductName() {
        ProductMinDTO productMinDTO = new ProductMinDTO(product.getId(), product.getName(), product.getPrice(),
                product.getImgUrl());

        doReturn(new PageImpl<>(List.of(product), pageable, 1)).when(repository)
                .findByNameContainingIgnoreCase("Laptop", pageable);

        doReturn(productMinDTO).when(mapper).toMinDTO(product);

        Page<ProductMinDTO> page = service.findAll("Laptop", pageable);

        assertEquals(1, page.getTotalElements());
        assertEquals(productMinDTO, page.getContent().get(0));
        verify(repository).findByNameContainingIgnoreCase("Laptop", pageable);
    }

    @Test
    void findAllShouldReturnPageOfProductsWhenNameIsNull() {
        ProductMinDTO productMinDTO = new ProductMinDTO(product.getId(), product.getName(), product.getPrice(),
                product.getImgUrl());

        doReturn(new PageImpl<>(List.of(product), pageable, 1)).when(repository)
                .findByNameContainingIgnoreCase("", pageable);

        doReturn(productMinDTO).when(mapper).toMinDTO(product);

        Page<ProductMinDTO> page = service.findAll(null, pageable);

        assertEquals(1, page.getTotalElements());
        assertEquals(productMinDTO, page.getContent().get(0));
        verify(repository).findByNameContainingIgnoreCase("", pageable);
    }

    @Test
    void findByIdShouldReturnProductDTOWhenIdExists() {
        doReturn(Optional.of(product)).when(repository).findById(EXISTING_ID);
        doReturn(productDTO).when(mapper).toDTO(product);

        ProductDTO result = service.findById(EXISTING_ID);

        assertEquals(productDTO, result);
        verify(repository).findById(EXISTING_ID);
        verify(mapper).toDTO(product);
    }

    @Test
    void findByIdShouldThrowProductNotFoundExceptionWhenIdDoesNotExist() {
        doReturn(Optional.empty()).when(repository).findById(NON_EXISTING_ID);

        assertNotNull(assertThrows(ProductNotFoundException.class, () -> service.findById(NON_EXISTING_ID)));
        verify(repository).findById(NON_EXISTING_ID);
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void updateShouldReturnProductDTOWhenIdExists() {
        ProductRequestDTO request = new ProductRequestDTO(product.getName(), product.getDescription(),
                product.getPrice(), product.getImgUrl(), Set.of());

        doReturn(Optional.of(product)).when(repository).findById(EXISTING_ID);
        doReturn(product).when(repository).save(product);
        doReturn(productDTO).when(mapper).toDTO(product);

        ProductDTO result = service.update(EXISTING_ID, request);

        assertEquals(productDTO, result);
        verify(mapper).updateEntityFromDTO(request, product);
        verify(repository).save(product);
    }

    @Test
    void updateShouldThrowProductNotFoundExceptionWhenIdDoesNotExist() {
        ProductRequestDTO request = new ProductRequestDTO(product.getName(), product.getDescription(),
                product.getPrice(), product.getImgUrl(), Set.of());

        doReturn(Optional.empty()).when(repository).findById(NON_EXISTING_ID);

        assertNotNull(assertThrows(ProductNotFoundException.class, () -> service.update(NON_EXISTING_ID, request)));
        verify(mapper, never()).updateEntityFromDTO(any(), any());
        verify(repository, never()).save(any());
    }

    @Test
    void deleteShouldDeleteProductWhenIdExists() {
        doReturn(Optional.of(product)).when(repository).findById(EXISTING_ID);

        assertDoesNotThrow(() -> service.delete(EXISTING_ID));
        verify(repository).findById(EXISTING_ID);
        verify(repository).delete(product);
        verify(repository).flush();
    }

    @Test
    void deleteShouldThrowProductNotFoundExceptionWhenIdDoesNotExist() {
        doReturn(Optional.empty()).when(repository).findById(NON_EXISTING_ID);

        assertNotNull(assertThrows(ProductNotFoundException.class, () -> service.delete(NON_EXISTING_ID)));
        verify(repository).findById(NON_EXISTING_ID);
        verify(repository, never()).delete(any());
        verify(repository, never()).flush();
    }

    @Test
    void deleteShouldThrowDataIntegrityViolationExceptionWhenIdIsDependent() {
        doReturn(Optional.of(product)).when(repository).findById(DEPENDENT_ID);
        doThrow(new DataIntegrityViolationException("Constraint violation")).when(repository).delete(product);

        DataBaseIntegrityViolationException exception = assertThrows(DataBaseIntegrityViolationException.class,
                () -> service.delete(DEPENDENT_ID));

        assertEquals("Referential integrity violation", exception.getMessage());
        verify(repository).findById(DEPENDENT_ID);
        verify(repository).delete(product);
        verify(repository, never()).flush();
    }

}
