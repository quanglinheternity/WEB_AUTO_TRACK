package com.transport.repository.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.transport.dto.user.UserResponse;
import com.transport.dto.user.UserSearchRequest;
import com.transport.entity.domain.User;

public interface UserRepositoryCustom {
    boolean existsByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, Long id);
    
    Page<UserResponse> searchUsers(UserSearchRequest request, Pageable pageable);

    Optional<User> findByName(String name);
}
