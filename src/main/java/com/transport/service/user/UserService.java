package com.transport.service.user;

import java.util.List;

import com.transport.dto.user.UserCreateRequest;
import com.transport.dto.user.UserDetailResponse;
import com.transport.dto.user.UserResponse;

public interface UserService {
    List<UserResponse> getAll();
    
    UserDetailResponse getById(Long id);

    UserDetailResponse create(UserCreateRequest request);

    UserDetailResponse update(Long id, UserCreateRequest request);

    void delete(Long id);
}
