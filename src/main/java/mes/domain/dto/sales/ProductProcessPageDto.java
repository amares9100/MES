package mes.domain.dto.sales;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mes.domain.dto.product.ProductProcessDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductProcessPageDto {
    private long totalCount;
    private int totalPage;

    private int page;
    private int orderStatus;
    private String keyword;

    private int prod_stock;
    List<ProductProcessDto> productProcessDtoList;
}
