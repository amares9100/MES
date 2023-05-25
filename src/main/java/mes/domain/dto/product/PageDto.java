package mes.domain.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageDto {
    //1. 전체 게시물 수
    private long totalCount;
    //2. 전체 페이지수
    private int totalPage;

    //3. 현재 페이지의 제품 dto들

    List<ProductDto> productDtoList;

    //3. 현재 페이지의 제품 지시 dto 들
    @JsonIgnore
    List<ProductPlanDto> productPlanDtoList;

    @JsonIgnore
    //3. 현재 페이지의 제품 공정들어간 dto 들
    List<ProductProcessDto> productProcessDtoList;

    //4. 현재 페이지번호
    private int page;
    //5. 선택한 key
    private String key;
    //6. 검색한 keyword
    private String keyword;

}
