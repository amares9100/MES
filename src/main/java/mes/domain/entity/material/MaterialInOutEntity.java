package mes.domain.entity.material;

import lombok.*;
import mes.domain.BaseTime;
import mes.domain.dto.material.MaterialInOutDto;
import mes.domain.entity.member.AllowApprovalEntity;
import mes.domain.entity.member.MemberEntity;
import javax.persistence.*;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "materialInOut")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialInOutEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mat_in_outid;// -- 원자재 입출고 PK

    @Column private int mat_in_type;// -- + -
    @Column private int mat_st_stock;// -- 남은 재고
    @Column private int mat_in_code; // 재고증가 최종확인용

    @ManyToOne // 다수가 하나에게 [fk ---> pk]
    @JoinColumn(name = "al_app_no") //pk 이름 정하기
    @ToString.Exclude
    private AllowApprovalEntity allowApprovalEntity;

    @ManyToOne // 다수가 하나에게 [fk ---> pk]
    @JoinColumn(name = "MatID") //pk 이름 정하기
    @ToString.Exclude
    private MaterialEntity materialEntity;// -- 마스터 원자재 테이블 fk

    @ManyToOne // 다수가 하나에게 [fk ---> pk]
    @JoinColumn(name = "mno") //pk 이름 정하기
    @ToString.Exclude
    private MemberEntity memberEntity;

    public MaterialInOutDto toDto() {
        return MaterialInOutDto.builder()
                .mat_in_outid(this.mat_in_outid)
                .materialDto(this.materialEntity.toDto())
                .allowApprovalDto(this.allowApprovalEntity.toInDto())
                .mat_in_type(this.mat_in_type)
                .mat_st_stock(this.mat_st_stock)
                .cdate(this.cdate.minusMinutes(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .udate(this.udate.minusMinutes(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .mat_in_code(this.mat_in_code)
                .memberdto(this.memberEntity.toDto())
                .build();
    }

    //재고 취소 entity
    public MaterialInOutEntity toCancelEntity(){
        return MaterialInOutEntity.builder()
                .mat_st_stock(this.mat_st_stock)
                .mat_in_code(this.mat_in_code)
                .allowApprovalEntity(this.allowApprovalEntity)
                .mat_in_type(this.mat_in_type)
                .materialEntity(this.materialEntity)
                .memberEntity(this.memberEntity)
                .build();
    }

}
