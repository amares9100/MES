package mes.domain.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.query.AbstractJpaQuery;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoProduceDto {
    private int prodID; //제품 PK

    private double avgOrderCount; //평균 판매량

    private double avgProdPlanCount; //평균 생산량

    private double prodCurrentStock; //제품 현재 재고

    private double prodSafeStock; //안전 재고

    public AutoProduceDto(int prodID, double orderCountAVG, double prodSafeStock) {
        this.prodID = prodID;
        this.avgOrderCount = orderCountAVG;
        this.prodSafeStock = prodSafeStock;
    }


}
