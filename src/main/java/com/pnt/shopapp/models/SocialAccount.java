package com.pnt.shopapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "social_accounts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "provider", length = 100, nullable = false)
    private String provider;
    @Column(name = "provider_id", length = 100, nullable = false)
    private String providerId;
    @Column(name = "email", length = 100)
    private String email;
    @Column(name = "name", length = 100)
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
