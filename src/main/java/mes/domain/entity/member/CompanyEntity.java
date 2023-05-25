package mes.domain.entity.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mes.domain.dto.member.CompanyDto;

import javax.persistence.*;

@Entity
@Table(name = "company")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cno; // 회사 고유 pk
    @Column
    private String cname; // 회사명
    @Column
    private int ctype; // 구매자 or 판매자 식별(0: 구매자, 1: 판매자)

    public CompanyDto toDto() {
        return CompanyDto.builder()
                .cno(this.cno)
                .cname(this.cname)
                .ctype(this.ctype)
                .build();


    }
}
