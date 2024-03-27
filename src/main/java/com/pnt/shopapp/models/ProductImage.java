package com.pnt.shopapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "product_images")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    @ManyToMany
    @JoinColumn (name = "product_id")
    private Product product;
    @Column(name = "image_url", length = 355)
    private String imageUrl;
}
