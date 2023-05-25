package mes.domain.dto.product;

public interface AutoProdctDto {
    Integer getProdID(); // 제품 PK

    Double getAvgOrderCount(); // 평균 판매량

    Double getAvgProdPlanCount(); // 평균 생산량

    Double getProdCurrentStock(); // 제품 현재 재고

    Double getProdSafeStock(); // 안전 재고
}

