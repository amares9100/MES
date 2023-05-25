package mes.domain.dto.product;

import lombok.*;
import mes.domain.dto.member.AllowApprovalDto;
import mes.domain.dto.member.MemberDto;
import mes.domain.entity.member.AllowApprovalEntity;
import mes.domain.entity.product.ProductEntity;
import mes.domain.entity.product.ProductPlanEntity;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPlanDto {

    private int prodPlanNo;// -- PK

    private String prodPlanCount;// -- 제품 생산 수량

    private String prodPlanDate;// -- 생산 예정 일자

    private ProductDto productDto;// -- 마스터 제품 테이블 fk

    private AllowApprovalDto allowApprovalDto;// -- 결제 승인 여부 테이블 fk

    //자재와 자재 inout과 연결할 때 사용하기 위해..
    private MemberDto memberDto; //로그인한 사람의 정보

    //제품-승인쪽
    public ProductPlanDto(int prodPlanNo, String prodPlanCount, String prodPlanDate, ProductDto productDto, AllowApprovalDto allowApprovalDto) {
        this.prodPlanNo = prodPlanNo;
        this.prodPlanCount = prodPlanCount;
        this.prodPlanDate = prodPlanDate;
        this.productDto = productDto;
        this.allowApprovalDto = allowApprovalDto;
    }

    public ProductPlanEntity toEntity(){
        return ProductPlanEntity.builder()
                .prodPlanCount(this.prodPlanCount)
                .productEntity(this.productDto.toPlanEntity())
                .allowApprovalEntity(this.allowApprovalDto.toSaveEntity())
                .build();
    }

    //저장용 엔티티
    public ProductPlanEntity toSaveEntity(){
        return ProductPlanEntity.builder()
                .allowApprovalEntity(this.allowApprovalDto.toPlanInEntity())
                .build();
    }

}
