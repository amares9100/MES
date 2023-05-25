package mes.domain.dto.member;

import lombok.*;
import mes.domain.entity.member.CompanyEntity;
import mes.domain.entity.member.MemberEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    private int mno;
    private String mname;
    private String mpassword;
    private String position;
    private CompanyEntity companyEntity;

    public MemberDto(String mname, String mpassword) {
        this.mname = mname;
        this.mpassword = mpassword;
    }

    public MemberEntity toEntity() {
        return MemberEntity.builder()
                .mno(mno)
                .mname(mname)
                .mpassword(mpassword)
                .position(position)
                .companyEntity(companyEntity)
                .build();
    }
}
