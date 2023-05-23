package ru.lessonsvtb.lesson14.entities;

import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "product_details")
public class ProductDetails {
    @Id
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "views")
    private Long views;

    @OneToOne
    @MapsId
    private Product product;

    public ProductDetails(Long productId, Long views, Product product) {
        this.productId = productId;
        this.views = views;
        this.product = product;
    }

    public ProductDetails() {
    }

}
