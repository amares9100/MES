package mes.domain.entity.material;

import mes.domain.dto.material.ApexChart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialInOutEntityRepository extends JpaRepository<MaterialInOutEntity , Integer> {


    @Query(value = "select * from material_in_out where matid=:matID" , nativeQuery = true)
    Page<MaterialInOutEntity> findByMatid(int matID, Pageable pageable);

    @Query(value = "select * from material_in_out where matid=:matID and mat_in_code = 1" , nativeQuery = true)
    List<MaterialInOutEntity> findByMaterial(int matID);

    @Query(value = "select * from material_in_out where matid=:matID and 1=mat_in_code order by udate DESC limit 1"  , nativeQuery = true)
    Optional<MaterialInOutEntity> findByUdate(int matID);

    @Query(value = "select * from material_in_out where al_app_no=:al_app_no"  , nativeQuery = true)
    MaterialInOutEntity findByAlid(int al_app_no);

    @Query(value = "SELECT date_format(udate ,'%Y-%m-%d') as udate , MAX(maxstock) AS stock " +
            "FROM ( " +
            "  SELECT date_format(udate ,'%Y-%m-%d') as udate, max(mat_st_stock) AS maxstock " +
            "  FROM material_in_out WHERE matid =:matID AND mat_in_code = 1 " +
            "  GROUP BY udate " +
            ") AS subquery " +
            "GROUP BY udate " +
            "ORDER BY udate;", nativeQuery = true)
    List<ApexChart> findByChart(@Param("matID") int matID);

    interface ApexChart{

        String getUdate();
        int getStock();
    }
}
