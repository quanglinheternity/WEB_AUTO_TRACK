package com.transport.repository.user;

public interface UserRepositoryCustom {
    boolean existsByUsername(String username);
    boolean existsByUsernameAndIdNot(String username, Long id);
}
