package com.spring_class.dscommerce.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.spring_class.dscommerce.entities.Order;
import com.spring_class.dscommerce.entities.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Override
    @EntityGraph(attributePaths = {"client", "items", "items.id.product"})
    Optional<Order> findById(Long id);

    boolean existsByClientIdAndStatusIn(Long clientId, List<OrderStatus> statuses);

}
