package com.induwara.store.mappers;

import com.induwara.store.dtos.RegisterUserRequest;
import com.induwara.store.dtos.UpdateUserRequest;
import com.induwara.store.dtos.UserDto;
import com.induwara.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
//    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);
    void update(UpdateUserRequest request, @MappingTarget User user);
}
