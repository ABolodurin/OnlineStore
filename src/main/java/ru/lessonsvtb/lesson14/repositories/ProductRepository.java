package ru.lessonsvtb.lesson14.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.lessonsvtb.lesson14.entities.Product;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long>,
        JpaSpecificationExecutor<Product>, JpaRepository<Product, Long> {

}
