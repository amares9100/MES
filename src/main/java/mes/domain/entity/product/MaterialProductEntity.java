package mes.domain.entity.product;

import lombok.*;
import mes.domain.dto.product.MaterialProductDto;
import mes.domain.entity.material.MaterialEntity;
import mes.domain.entity.member.MemberEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "materialProduct")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int mpno;


   /* @OneToMany(mappedBy = "materialProductEntity")
    @Builder.Default
    @ToString.Exclude
    private List<MaterialEntity> materialEntityList = new ArrayList<>();
*/
    @ManyToOne
    @JoinColumn(name = "mat_id")
    @ToString.Exclude
    public  MaterialEntity materialEntity;

    @ManyToOne
    @JoinColumn(name = "prod_id")
    @ToString.Exclude
    public ProductEntity productEntity;

    @Column
    public int referencesValue; //기준량


    public MaterialProductEntity(MaterialEntity materialEntity, ProductEntity productEntity) {
        this.materialEntity = materialEntity;
        this.productEntity = productEntity;
    }


    //출력용
    public MaterialProductDto toDto(){
        return MaterialProductDto.builder()
                .mpno(this.mpno)
                .productDto(this.productEntity.toDto())
                .materialDto(this.materialEntity.toDto())
                .build();
    }

}