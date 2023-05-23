package ru.lessonsvtb.lesson14.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.lessonsvtb.lesson14.entities.ProductDetails;
import java.util.List;

@Repository
public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Long> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE ProductDetails pd SET pd.views = pd.views + 1 WHERE pd.productId =:id")
    void incrementView(@Param("id") Long id);

    List<ProductDetails> findByOrderByViewsDesc();

}
