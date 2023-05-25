package mes.domain.Repository.product;

import mes.domain.entity.product.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    @Query(value = "select * " +
    "from product p inner join company c " +
    "on p.cno = c.cno and if(:key = '', true, " +
    "if(:key = 'prodName', p.prod_name like %:keyword%, c.cname like %:keyword%))",
    nativeQuery = true)
    Page<ProductEntity> findBySearch(Pageable pageable, String key, String keyword);
}
