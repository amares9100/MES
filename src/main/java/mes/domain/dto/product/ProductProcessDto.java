package mes.domain.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mes.domain.entity.product.ProductEntity;
import mes.domain.entity.product.ProductPlanEntity;
import mes.domain.entity.product.ProductProcessEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductProcessDto {

    private int prodProcNo;// -- PK

    private String prodProcDate;// -- 공정 일자
    private int prodProcStatus;// -- 공정 상태
    private int prodStock;// -- 완제품 재고 (+, - 아니고 = 으로)

    private int prodId;         // 완제품 id
    private String prodName;    // 완제품 물품명

    private ProductEntity productEntity;// -- 마스터 제품 테이블 fk

    public ProductProcessEntity toEntity(){
        return ProductProcessEntity.builder()
                .prodProcNo(this.prodProcNo)
                .prodProcDate(this.prodProcDate)
                .prodProcStatus(this.prodProcStatus)
                .prodStock(this.prodStock)
                .productEntity(this.productEntity)
                .build();
    }

}
