package ru.lessonsvtb.lesson14.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String title;
    private int price;
    private ProductDetails productDetails;

}
