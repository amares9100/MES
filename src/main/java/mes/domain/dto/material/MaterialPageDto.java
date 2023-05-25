package mes.domain.dto.material;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialPageDto {


    private long totalCount;
    private int totalPage;

    private int page;
    private int matID;
    private String keyword;
    private List<MaterialDto> materialList;




}
