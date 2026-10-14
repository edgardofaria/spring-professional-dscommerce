package com.spring_class.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring_class.dscommerce.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
