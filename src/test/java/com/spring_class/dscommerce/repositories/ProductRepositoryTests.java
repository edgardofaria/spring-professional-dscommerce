package com.spring_class.dscommerce.repositories;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import com.spring_class.dscommerce.entities.Product;

import jakarta.validation.ConstraintViolationException;

@DataJpaTest
class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;

    @Test
    public void saveShouldPersistProduct() {
        Product product = new Product("Laptop", "High-performance laptop", new BigDecimal("1999.90"),"https://example.com/laptop.jpg");

        Product saved = repository.save(product);
        assertNotNull(saved.getId());
    }

    @Test
    public void deleteShouldRemoveProductWhenIdExists() {
        Product product = new Product("Keyboard", "Mechanical keyboard", new BigDecimal("199.90"),"https://example.com/keyboard.jpg");

        Product saved = repository.saveAndFlush(product);

        repository.deleteById(saved.getId());
        assertFalse(repository.findById(saved.getId()).isPresent());
    }

    @Test
    public void deleteShouldDoNothingWhenIdDoesNotExist() {
        Long nonExistingId = 999L;

        repository.deleteById(nonExistingId);
        assertFalse(repository.findById(nonExistingId).isPresent());
    }

    @Test
    void findByIdShouldReturnProductWhenExists() {
        Product product = new Product("Mouse", "Wireless mouse", new BigDecimal("129.90"),"https://example.com/mouse.jpg");

        Product saved = repository.saveAndFlush(product);
        assertTrue(repository.findById(saved.getId()).isPresent());
    }

    @Test
    void saveShouldFailWhenRequiredFieldsAreBlank() {
        Product product = new Product("", "   ", new BigDecimal("0.00"), " ");

        Throwable throwable = assertThrows(ConstraintViolationException.class, () -> repository.saveAndFlush(product));
        assertNotNull(throwable);
    }

    @Test 
    void findByNameContainingIgnoreCaseShouldReturnProductsWhenMatchExists() {
        Product product = new Product("Monitor", "4K monitor", new BigDecimal("799.90"), "https://example.com/monitor.jpg");

        repository.saveAndFlush(product);

        assertNotNull(repository.findByNameContainingIgnoreCase("monitor", Pageable.ofSize(1)));
    }
}
