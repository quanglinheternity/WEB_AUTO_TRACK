package com.transport.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.transport.dto.user.UserResponse;

public interface UserRepositoryCustom {
    boolean existsByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, Long id);
    
    Page<UserResponse> searchUsers(String keyword, Pageable pageable);
}
