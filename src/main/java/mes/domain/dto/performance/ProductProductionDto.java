package mes.domain.dto.performance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@NoArgsConstructor@AllArgsConstructor@Builder
public class ProductProductionDto {
    private String prodName;
    private int prodPrice;
    private long averageProductionCount;
    private int totalProductionCount;
    private long totalProductionAmount;
    private long productionPercentage;
}
