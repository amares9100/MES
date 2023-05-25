package mes.domain.dto.performance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@NoArgsConstructor@AllArgsConstructor@Builder
public class SalesByProductDto {
    private String prodName; // 제품명
    private long prodPrice; // 제품 원가
    private long averageSalesPrice; // 평균 판매 가격
    private long totalOrderCount; // 총 주문 수량
    private long totalSalesAmount; // 총 판매 금액
    private int profit; // 수익금
    private long profitMargin;


}
