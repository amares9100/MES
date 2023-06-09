package mes.domain.entity.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository< MemberEntity, Integer > {

    MemberEntity findByMnameAndMpassword(String mname, String mpassword);
}
