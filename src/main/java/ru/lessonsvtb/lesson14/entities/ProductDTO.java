package ru.lessonsvtb.lesson14.entities;

public class ProductDTO {
    private Long id;
    private String title;
    private int price;
    private ProductDetails productDetails;

    public ProductDTO(Long id, String title, int price, ProductDetails productDetails) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.productDetails = productDetails;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ProductDetails getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(ProductDetails productDetails) {
        this.productDetails = productDetails;
    }

}
