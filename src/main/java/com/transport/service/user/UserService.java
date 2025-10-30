package com.transport.service.user;


import org.springframework.data.domain.Pageable;

import com.transport.dto.page.PageResponse;
import com.transport.dto.user.UserCreateRequest;
import com.transport.dto.user.UserDetailResponse;
import com.transport.dto.user.UserResponse;

public interface UserService {
    PageResponse<UserResponse> getAll(String keyword, Pageable pageable);
    
    UserDetailResponse getById(Long id);

    UserDetailResponse create(UserCreateRequest request);

    UserDetailResponse update(Long id, UserCreateRequest request);

    void delete(Long id);
}
