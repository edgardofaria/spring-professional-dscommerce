package com.spring_class.dscommerce.service;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring_class.dscommerce.dto.user.UserDTO;
import com.spring_class.dscommerce.dto.user.UserRequestDTO;
import com.spring_class.dscommerce.dto.user.UserUpdateDTO;
import com.spring_class.dscommerce.entities.Authority;
import com.spring_class.dscommerce.entities.OrderStatus;
import com.spring_class.dscommerce.entities.Role;
import com.spring_class.dscommerce.entities.User;
import com.spring_class.dscommerce.mapper.UserMapper;
import com.spring_class.dscommerce.repositories.OrderRepository;
import com.spring_class.dscommerce.repositories.RoleRepository;
import com.spring_class.dscommerce.repositories.UserRepository;
import com.spring_class.dscommerce.service.exceptions.DataBaseIntegrityViolationException;
import com.spring_class.dscommerce.service.exceptions.EmailAlreadyExistsException;
import com.spring_class.dscommerce.service.exceptions.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final OrderRepository orderRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return repository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public UserDTO getCurrentUser() {
        User user = authenticate();
        return mapper.toDTO(user);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllOrByName(String name, Pageable pageable) {
        Page<User> page;
        String findName = (name == null) ? "" : name;
        page = repository.findByNameContainingIgnoreCase(findName, pageable);
        return page.map(mapper::toDTO);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return mapper.toDTO(user);
    }

    @Transactional
    public UserDTO insert(UserRequestDTO dto) {
        if (repository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException("Email already in use");
        }
        User user = mapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));
        Role role = roleRepository.findByAuthority(Authority.ROLE_CLIENT).get();
        user.addRole(role);
        user = repository.save(user);
        return mapper.toDTO(user);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        mapper.updateEntityFromDTO(dto, user);
        user.setPassword(passwordEncoder.encode(dto.password()));
        user = repository.save(user);
        return mapper.toDTO(user);
    }

    @Transactional
    public void delete(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (hasOpenOrders(id)) {
            throw new DataBaseIntegrityViolationException("Cannot delete user with open orders");
        }

        removeUserData(user);
        repository.save(user);
    }

    public User authenticate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getClaim("username");
        return repository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private boolean hasOpenOrders(Long userId) {
        return orderRepository.existsByClientIdAndStatusIn(userId, List.of(OrderStatus.WAITING_PAYMENT, OrderStatus.PAID, OrderStatus.SHIPPED));
    }

    private void removeUserData(User user) {
        user.setActive(false);
        user.setDeletedAt(Instant.now());
        user.setName("User Removed");
        user.setEmail("removed_" + user.getId() + "@deleted.invalid");
        user.setPhone(null);
        user.setBirthDate(null);
        user.setPassword("[REMOVED]");
    }
}
