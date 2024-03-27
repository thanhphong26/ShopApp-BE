package com.pnt.shopapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Table(name = "tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "token", length = 255)
    private String token;
    @Column(name = "toke_types", length=100)
    private String tokenType;
    @Column(name = "expiration_date")
    private Timestamp expirationDate;
    private Byte revoked;
    private Byte expired;
    @ManyToOne
    @JoinColumn(name ="user_id")
    private User user;

}
