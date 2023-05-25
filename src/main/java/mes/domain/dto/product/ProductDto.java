package mes.domain.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mes.domain.BaseTime;
import mes.domain.dto.material.MaterialDto;
import mes.domain.dto.member.CompanyDto;
import mes.domain.entity.member.CompanyEntity;
import mes.domain.entity.product.ProductEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private int  prodId;// -- PK

    private String prodName;// 제품명

    private String prodCode;// -- 제품 구분 문자코드 (식별용)
    private String prodDate;// -- 생산일자
    private int prodPrice;// -- 제품 가격

    //등록용 자재 리스트
    private List<HashMap<String, Integer>> referencesValue; // mat_id : material개수

    private CompanyDto companyDto; // 회사명

    private int type; //1:제품 자체를 수정, 2: 제품의 자재 목록을 수정

    private List<MaterialDto> materialDtoList; //출력용 자재 리스트

    public ProductEntity toEntity(){
        return ProductEntity.builder()
                .prodCode(this.prodCode)
                .prodPrice(this.prodPrice)
                .prodName(this.prodName)
                .companyEntity(this.companyDto.toEntity())
                .build();

    }

    //plan 저장용 product
    public ProductEntity toPlanEntity(){
        return ProductEntity.builder()
                .prodId(this.prodId)
                .prodCode(this.prodCode)
                .prodDate(this.prodDate)
                .prodPrice(this.prodPrice)
                .prodName(this.prodName)
                .companyEntity(this.companyDto.toEntity())
                .build();
    }
}
