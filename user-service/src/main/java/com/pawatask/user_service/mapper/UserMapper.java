package com.pawatask.user_service.mapper;

import com.pawatask.user_service.dto.UserDto;
import com.pawatask.user_service.model.User;

public class UserMapper {
    public static UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        return dto;
    }
}
