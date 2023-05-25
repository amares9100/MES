package mes.domain.entity.material;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialEntityRepository extends JpaRepository<MaterialEntity , Integer> {

    @Query(value = "SELECT * FROM material WHERE IF(:keyword = '', TRUE, mat_name LIKE %:keyword%)", nativeQuery = true)
    Page<MaterialEntity> findByPage(String keyword , Pageable pageable);
}
