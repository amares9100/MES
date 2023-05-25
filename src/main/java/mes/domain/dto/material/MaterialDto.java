package mes.domain.dto.material;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mes.domain.dto.member.CompanyDto;
import mes.domain.dto.product.MaterialProductDto;
import mes.domain.entity.material.MaterialEntity;
import mes.domain.entity.member.CompanyEntity;
import mes.domain.entity.product.MaterialProductEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialDto{

    private int MatID;// -- 원자재 ID (PK)

    private String mat_code;// -- 자재 구분 문자코드[ 입출고로 넘어갈 때 B로 바꾸기] (식별용)
    private String mat_name;// -- 원자재명
    private String mat_unit;// -- 자재 단위
    private String mat_st_exp;// -- 유통기한
    private int mat_price;// -- 단가
    private CompanyDto companyDto;// -- 제조사
    private String mdate; // 등록날짜
    private int cno;
    private List<MaterialProductDto> MaterialProductDtoList = new ArrayList<>();
    private int m_stock; // 스톡 출력용

    private int ratio; //비율 값(plan에서 사용할)

    public MaterialEntity toEntity() { // 저장용 추후 추가할것있음
        return  MaterialEntity.builder()
                .mat_name(this.mat_name)
                .mat_unit(this.mat_unit)
                .mat_code(this.mat_code)
                .mat_st_exp(this.mat_st_exp)
                .mat_price(this.mat_price)
                .build();
    }

    //출고용 엔티티
    public MaterialEntity toOutEntity() { // 저장용 추후 추가할것있음
        return  MaterialEntity.builder()
                .MatID(this.MatID)
                .mat_name(this.mat_name)
                .mat_unit(this.mat_unit)
                .mat_code(this.mat_code)
                .mat_st_exp(this.mat_st_exp)
                .mat_price(this.mat_price)
                .build();
    }

}
