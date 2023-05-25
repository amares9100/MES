package mes.domain.entity.product;

import lombok.*;
import mes.domain.BaseTime;
import mes.domain.dto.product.ProductProcessDto;

import javax.persistence.*;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "productProcess")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductProcessEntity extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int prodProcNo;// -- PK
    @Column private String prodProcDate;// -- 공정 일자 (삭제해도 무방 혹은 udate사용하는게 좋을거 같음)
    @Column private int prodProcStatus;// -- 재고 상태 (1: 판매가능, 2: 재고부족 // 완제품 재고가 0보다 크면 판매가능, 작으면 재고 부족)
    @Column private int prodStock;// -- 제품 재고(누적) (승인 완료되면, 기존 재고 +=으로 재고 추가 // 반대로 판매되면 +-으로 재고 차감)
    // ProductProcessEntity 사용 방향 [23.05.15, th]
    // 1) Repository에서 productEntity pno로 조회하는 메소드 생성 (제품별로 그룹핑되어야 함)
    // 2) 조회된 데이터의 prodStock값 변경 (생산 승인 완료: 기존재고 +=, 판매 승인 완료: 기존재고 -=)

    @ManyToOne
    @JoinColumn(name = "prodId")
    @ToString.Exclude
    public ProductEntity productEntity;// -- 마스터 제품 테이블 fk

    public ProductProcessDto toDto() { // 판매 쪽 제품공정 출력용
        return  ProductProcessDto.builder()
                .prodProcNo(this.prodProcNo)
                .prodProcDate(this.prodProcDate)
                .prodStock(this.prodStock)
                .prodId(this.productEntity.getProdId())
                .prodName(this.productEntity.getProdName())
                .build();
    }

    public ProductProcessEntity toSaveEntity(){
        return ProductProcessEntity.builder()
                .prodProcDate(this.cdate.minusMinutes(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .prodStock(this.prodStock)
                .productEntity(this.productEntity)
                .prodProcStatus(1)
                .build();
    }


}
