package com.spring_class.dscommerce.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring_class.dscommerce.entities.Authority;
import com.spring_class.dscommerce.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByAuthority(Authority authority);
}
