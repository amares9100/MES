package mes.domain.entity.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllowApprovalRepository extends JpaRepository<AllowApprovalEntity, Integer> {
    @Query(value="select * from allow_approval where al_app_date like CONCAT('%', :findId, '%');", nativeQuery = true)
    List<AllowApprovalEntity> findByAllow(String findId);

}
