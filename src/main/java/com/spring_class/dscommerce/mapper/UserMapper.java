package com.spring_class.dscommerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.spring_class.dscommerce.dto.user.UserDTO;
import com.spring_class.dscommerce.dto.user.UserMinDTO;
import com.spring_class.dscommerce.dto.user.UserRequestDTO;
import com.spring_class.dscommerce.dto.user.UserUpdateDTO;
import com.spring_class.dscommerce.entities.Role;
import com.spring_class.dscommerce.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    UserMinDTO toMinDTO(User user);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    User toEntity(UserRequestDTO request);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    User updateEntityFromDTO(UserUpdateDTO request, @MappingTarget User user);

    default String roleToString(Role role) {
        return role.getAuthority();
    }

}
