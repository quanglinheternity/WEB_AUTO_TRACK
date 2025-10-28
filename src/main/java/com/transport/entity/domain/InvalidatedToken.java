package com.transport.entity.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "invalidated_tokens")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvalidatedToken {
    
    @Id
    @Column(name = "token_id", length = 36)
    private String id;
    
    @Column(name = "expiry_time", nullable = false)
    private LocalDateTime expiryTime;
}
