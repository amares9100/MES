package mes.service.Material;

import lombok.extern.slf4j.Slf4j;
import mes.domain.dto.material.MaterialDto;
import mes.domain.dto.material.MaterialPageDto;
import mes.domain.dto.member.CompanyDto;
import mes.domain.entity.material.MaterialEntity;
import mes.domain.entity.material.MaterialEntityRepository;
import mes.domain.entity.material.MaterialInOutEntity;
import mes.domain.entity.material.MaterialInOutEntityRepository;
import mes.domain.entity.member.CompanyEntity;
import mes.domain.entity.member.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class MaterialService {

    @Autowired
    private MaterialEntityRepository materialEntityRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private MaterialInOutEntityRepository materialInOutEntityRepository;


    // 자재 등록
    @Transactional
    public boolean materialCreate(MaterialDto dto){


        CompanyEntity companyEntity = companyRepository.findById(dto.getCno()).get();

        MaterialEntity entity = materialEntityRepository.save(dto.toEntity());
        entity.setCompanyEntity(companyEntity);


        log.info("Material entity"+ entity);
        if( entity.getMatID() >= 1 ){ return true; }

        return false;
    }

    // 자재 리스트 출력
    @Transactional
    public MaterialPageDto materialList(MaterialPageDto dto){
        List<MaterialDto> list = new ArrayList<>();

        // 전체출력
        if(dto.getMatID() == 0){
            Pageable pageable = PageRequest.of(dto.getPage()-1 , 5 , Sort.by(Sort.Direction.DESC , "matID"));
            System.out.println("Servicedto : " + dto);
            Page<MaterialEntity> entityPage = materialEntityRepository.findByPage(dto.getKeyword() , pageable);
            entityPage.forEach((e)->{
                // 검색된 자재로 마지막으로 update된 inout레코드 출력
                Optional<MaterialInOutEntity> lastInOut = materialInOutEntityRepository.findByUdate(e.toDto().getMatID());

                // Dto 객체 만들어서 나온 stock값 세팅
                MaterialDto materialDto = e.toDto();
                if(!lastInOut.isPresent()){ // 검색된 것이 없으면 재고 0
                    materialDto.setM_stock(0);
                }
                else{ // 검색된 것이 있으면 재고세팅
                    materialDto.setM_stock(lastInOut.get().toDto().getMat_st_stock());
                    System.out.println("materialDtoss : " + materialDto);
                }

                // 만들어진 materialDto를 리스트에 담기
                list.add(materialDto);
            });

           dto.setMaterialList(list);
           dto.setTotalPage(entityPage.getTotalPages());
           dto.setTotalCount(entityPage.getTotalElements());
        }
        // 선택한 자재 상세보기 출력
        else if(dto.getMatID() > 0){
            MaterialEntity entity = materialEntityRepository.findById(dto.getMatID()).get();
            list.add(entity.toDto());

            dto.setMaterialList(list);
        }
        System.out.println("Servicedto : " + dto);

        return dto;
    }


    // 회사불러오기
    @Transactional
    public List<CompanyDto> getCompany(){
        List<CompanyDto> dto = new ArrayList<>();
        List<CompanyEntity> entityOptional = companyRepository.findAll();

        entityOptional.forEach((e)->{
           dto.add(e.toDto());
        });
        return dto;
    }

    // 자재수정
    @Transactional
    public boolean materialUpdate(MaterialDto dto){

        MaterialEntity entity = materialEntityRepository.findById(dto.getMatID()).get();

        entity.setMat_name(dto.getMat_name());
        entity.setMat_price(dto.getMat_price());
        entity.setMat_st_exp(dto.getMat_st_exp());
        entity.setMat_unit(dto.getMat_unit());

        CompanyEntity companyEntity = companyRepository.findById(dto.getCno()).get();
        entity.setCompanyEntity(companyEntity);

        materialEntityRepository.save(entity);

        return true;
    }
}
