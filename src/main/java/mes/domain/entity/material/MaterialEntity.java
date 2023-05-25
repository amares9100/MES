package mes.domain.entity.material;

import lombok.*;
import mes.domain.BaseTime;
import mes.domain.dto.material.MaterialDto;
import mes.domain.entity.member.CompanyEntity;
import mes.domain.entity.product.MaterialProductEntity;
import javax.persistence.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "material")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialEntity extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int MatID;// -- 원자재 ID (PK)
    @Column
    private String mat_code;// -- 자재 구분 문자코드[ 입출고로 넘어갈 때 B로 바꾸기] (식별용)
    @Column private String mat_name;// -- 원자재명
    @Column private String mat_unit;// -- 자재 단위
    @Column private String mat_st_exp;// -- 유통기한
    @Column private int mat_price;// -- 단가

    @ManyToOne // 다수가 하나에게 [fk ---> pk]
    @JoinColumn(name = "cno") //pk 이름 정하기
    @ToString.Exclude
    private CompanyEntity companyEntity;// -- 제조사


    @OneToMany(mappedBy = "materialEntity")
    @Builder.Default
    @ToString.Exclude
    private List<MaterialProductEntity> materialProductEntityList = new ArrayList<>();



    public MaterialDto toDto() { // 반환용
        return MaterialDto.builder()
                .MatID(this.MatID)
                .mat_st_exp(this.mat_st_exp)
                .mat_name(this.mat_name)
                .mat_code(this.mat_code)
                .mat_unit(this.mat_unit)
                .mat_price(this.mat_price)
                .companyDto(this.companyEntity.toDto())
                .mdate(this.cdate.minusMinutes(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();
    }
}
