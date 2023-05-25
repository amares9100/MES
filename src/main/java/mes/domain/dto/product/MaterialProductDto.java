package mes.domain.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mes.domain.dto.material.MaterialDto;
import mes.domain.entity.material.MaterialEntity;
import mes.domain.entity.product.MaterialProductEntity;
import mes.domain.entity.product.ProductEntity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialProductDto {
    private int mpno;


    private MaterialDto materialDto;


    private ProductDto productDto;

    private int referencesValue;

    //등록용 생성자
    public MaterialProductDto(ProductDto productDto, MaterialEntity materialEntity) {
    }

    //저장용
    public MaterialProductEntity toEntity(){
        return MaterialProductEntity.builder()
                .productEntity(this.productDto.toEntity())
                .referencesValue(this.referencesValue)
                .build();
    }
}
