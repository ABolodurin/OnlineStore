package ru.lessonsvtb.lesson14.entities;

import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_details")
public class ProductDetails {
    @Id
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "views")
    private Long views;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Product product;

}
