package mes.domain.Repository.product;

import mes.domain.entity.product.MaterialProductEntity;
import mes.domain.entity.product.ProductEntity;
import mes.domain.entity.product.ProductPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialProductRepository extends JpaRepository<MaterialProductEntity, Integer> {

    @Query(value="select * from material_product" +
            " where prod_id = :prod_id", nativeQuery = true)
    List<MaterialProductEntity> findByMaterial(int prod_id);


}
