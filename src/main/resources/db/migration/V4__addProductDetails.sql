ALTER TABLE boot.products
    ADD CONSTRAINT products_pk PRIMARY KEY (id);

CREATE TABLE product_details
(
    product_id bigint PRIMARY KEY,
    views      bigint DEFAULT 0,
    CONSTRAINT product_id_fk FOREIGN KEY (product_id)
        REFERENCES boot.products (id)
);
