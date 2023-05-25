package mes.domain.entity.member;

import lombok.*;
import mes.domain.dto.member.MemberDto;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mno; // pk

    @Column
    private String mname; //이름

    @Column
    private String mpassword;

    @Column
    private String position; //회사 직급/포지션

    @ManyToOne
    @JoinColumn(name = "cno")
    @ToString.Exclude
    private CompanyEntity companyEntity; // 회사명

    public MemberDto toDto() {
        return MemberDto.builder()
              .mno(mno)
              .mname(mname)
              .mpassword(mpassword)
              .position(position)
              .build();
    }
}
