package com.spring_class.dscommerce.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring_class.dscommerce.dto.product.ProductDTO;
import com.spring_class.dscommerce.dto.product.ProductMinDTO;
import com.spring_class.dscommerce.dto.product.ProductRequestDTO;
import com.spring_class.dscommerce.entities.Category;
import com.spring_class.dscommerce.entities.Product;
import com.spring_class.dscommerce.mapper.ProductMapper;
import com.spring_class.dscommerce.repositories.CategoryRepository;
import com.spring_class.dscommerce.repositories.ProductRepository;
import com.spring_class.dscommerce.service.exceptions.DataBaseIntegrityViolationException;
import com.spring_class.dscommerce.service.exceptions.ProductNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper mapper;

    @Transactional(readOnly = true)
    public Page<ProductMinDTO> findAll(String name, Pageable pageable) {
        String searchName = (name == null) ? "" : name;
        return repository.findByNameContainingIgnoreCase(searchName, pageable).map(mapper::toMinDTO);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Transactional
    public ProductDTO insert(ProductRequestDTO requestDTO) {
        Product product = mapper.toEntity(requestDTO);
        product.getCategories().addAll(resolveCategories(requestDTO.categories()));
        return mapper.toDTO(repository.save(product));
    }

    @Transactional
    public ProductDTO update(Long id, ProductRequestDTO requestDTO) {
        Product product = repository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
        mapper.updateEntityFromDTO(requestDTO, product);
        product.getCategories().clear();
        product.getCategories().addAll(resolveCategories(requestDTO.categories()));
        return mapper.toDTO(repository.save(product));
    }

    @Transactional
    public void delete(Long id) {
        Product product = repository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
        try {
            repository.delete(product);
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseIntegrityViolationException("Referential integrity violation");
        }
    }

    private Set<Category> resolveCategories(Set<Category> categories) {
        return categories.stream()
                .map(category -> categoryRepository.getReferenceById(category.getId()))
                .collect(Collectors.toSet());
    }

}
