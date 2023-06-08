package ru.lessonsvtb.lesson14.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
