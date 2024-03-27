package com.pnt.shopapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "reviews")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(name="content")
    private String content;
    @Column(name="rating")
    private Float rating;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @PrePersist
    protected  void onCreate(){
        createdAt=LocalDateTime.now();
    }
}
