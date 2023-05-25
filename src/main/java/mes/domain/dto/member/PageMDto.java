package mes.domain.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PageMDto {
    private int type; // PageDto 클래스 활용을 위해 type 생성
    private long totalCount;
    private int totalPage;
    List<AllowApprovalDto> allowApprovalDtos;
    private int page;
    private String key;
    private String keyword;

}
