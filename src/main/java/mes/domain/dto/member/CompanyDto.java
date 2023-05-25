package mes.domain.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mes.domain.entity.member.CompanyEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDto {


    private int cno; // 회사 고유 pk

    private String cname; // 회사명

    private int ctype; // 구매자 or 판매자 식별(0: 구매자, 1: 판매자)

    public CompanyEntity toEntity(){
        return CompanyEntity.builder()
                .cname(this.cname)
                .ctype(this.ctype)
                .build();
    }

}
