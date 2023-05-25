package mes.service.product;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import mes.domain.Repository.product.MaterialProductRepository;
import mes.domain.Repository.product.ProductProcessRepository;
import mes.domain.Repository.product.ProductRepository;
import mes.domain.dto.material.MaterialDto;

import mes.domain.dto.product.PageDto;
import mes.domain.dto.product.ProductDto;
import mes.domain.entity.material.MaterialEntity;
import mes.domain.entity.material.MaterialEntityRepository;
import mes.domain.entity.member.CompanyEntity;
import mes.domain.entity.member.CompanyRepository;
import mes.domain.entity.product.MaterialProductEntity;
import mes.domain.entity.product.ProductEntity;
import mes.domain.entity.product.ProductProcessEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Slf4j
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    MaterialProductRepository materialProductRepository;

    @Autowired
    MaterialEntityRepository materialEntityRepository;

    @Autowired
    ProductProcessRepository productProcessRepository; //제품 삭제 확인용


    @Transactional
    //제품 출력 => 제품 지시, 제품 관리 페이지에서 수행할 예정
    public PageDto getProductList(PageDto pageDto){

        Pageable pageable = PageRequest.of(pageDto.getPage()-1, 5, Sort.by(Sort.Direction.DESC, "prod_id") );

        try {
            //페이징 처리를 위해 해당 키와 키워드 페이지 5개씩 지정
            Page<ProductEntity> pageEntity = productRepository.findBySearch(pageable, pageDto.getKey(), pageDto.getKeyword());
            List<ProductDto> productDto = new ArrayList<ProductDto>();

            //해당 pageEntity는 필요한 정보값(제품)을 가지고 있는 리스트이다.
            pageEntity.forEach((p) -> {
                productDto.add(p.toDto());
            });


            for (int i = 0; i < productDto.size(); i++) { //회사 정보 담아서 내보내기 위해
                productDto.get(i).setCompanyDto(productRepository.findById(productDto.get(i).getProdId()).get().getCompanyEntity().toDto());

                //제품 PK로 자재를 찾기 위해 materialProduct를 찾는다
                List<MaterialProductEntity> materialProductEntity = materialProductRepository.findByMaterial(productDto.get(i).getProdId());

                List<MaterialDto> materialDtoList = new ArrayList<>(); //제품에 담을 자재 목록

                System.out.println(materialDtoList);
                for (int j = 0; j < materialProductEntity.size(); j++) {
                    materialDtoList.add(materialProductEntity.get(j).getMaterialEntity().toDto());
                }

                productDto.get(i).setMaterialDtoList(materialDtoList);

            }
            pageDto.setProductDtoList(productDto);
            pageDto.setTotalPage(pageEntity.getTotalPages());
            pageDto.setTotalCount(pageEntity.getTotalElements());

            return pageDto;

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Transactional
    //제품 등록 => 제품 생산페이지에서 수행 할 예정
    public boolean postProduct(ProductDto productDto) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<MaterialEntity> materialEntityList = new ArrayList<>(); //자재 리스트(제품마다 가지고 있는)

        List<MaterialProductEntity> materialProductEntityList = new ArrayList<>(); //제품-자재 리스트

        try {

            for (HashMap<String, Integer> item : productDto.getReferencesValue()) {
                int matId = (int) item.get("matId");

                materialEntityList.add(materialEntityRepository.findById(matId).get());
            }


            Optional<CompanyEntity> companyEntity = companyRepository.findById(productDto.getCompanyDto().getCno());
            ProductEntity productEntity;

            if(companyEntity.isPresent()){
                productEntity = productRepository.save(productDto.toEntity());
                productEntity.setCompanyEntity(companyEntity.get());
                //date를 여기서 처리하는 이유는 수정할때 바꾸지 않을 려고!
                productEntity.setProdDate(simpleDateFormat.format(new Date()));

                if (productEntity.getProdId() < 1) { //앞부분 등록 실패시 (제품 등록 실패시 - 자재 제외하고 온랴 제품테이블만)
                    return false;
                }
                List<MaterialProductEntity> resultMaterialProductEntity = new ArrayList<>();

                for (int i = 0; i < materialEntityList.size(); i++) {
                    MaterialProductEntity materialProductEntity = new MaterialProductEntity(materialEntityList.get(i), productEntity);
                    //제품-재고 테이블에 필요한 정보를 set으로 다 넣었다면 save
                    for (HashMap<String, Integer> item : productDto.getReferencesValue()) {
                        if (materialEntityList.get(i).getMatID() == (int) item.get("matId")){
                            materialProductEntity.setReferencesValue((int) item.get("matRate"));
                        }
                    }

                    resultMaterialProductEntity.add(materialProductRepository.save(materialProductEntity));
                }

                for (int j = 0; j < resultMaterialProductEntity.size(); j++) {//새로 추가하는 mpno
                    materialProductEntityList.add(resultMaterialProductEntity.get(j));
                }

                for (int i = 0; i < materialEntityList.size(); i++) { //재료 테이블에 mp 테이블 정보 넣어준다.
                    materialEntityList.get(i).setMaterialProductEntityList(materialProductEntityList);
                }

                if (resultMaterialProductEntity.get(resultMaterialProductEntity.size() - 1).getMpno() >= 1) { //등록 성공시
                    return true;
                }
            }
            return false;

        }catch (Exception e){
            System.err.println(e.getMessage());
            return false;
        }

    }

    @Transactional
    //제품 수정 => 제품 관리 페이지에서 수행할 예정 (제품 자체를 수정)
    public boolean putProduct(ProductDto productDto) throws Exception{
        System.out.println(productDto);
        Optional<ProductEntity> putProductEntity = productRepository.findById(productDto.getProdId());

        System.out.println("찾음 : " +putProductEntity.get());
        CompanyEntity companyEntity;

        if(putProductEntity.isPresent() && companyRepository.findById(productDto.getCompanyDto().getCno()).isPresent()){
            ProductEntity productEntity = putProductEntity.get();
            companyEntity = companyRepository.findById(productDto.getCompanyDto().getCno()).get();

            productEntity.setProdPrice(productDto.getProdPrice());
            productEntity.setCompanyEntity(companyEntity);

            productEntity.setProdName(productDto.getProdName());

            return true;
        }

        return false;
    }

    // 제품의 자재 목록을 수정 -> 제품 관리 페이지의 모달로 수행할 예정
    @Transactional
    public boolean putProdcuctMaterialList(ProductDto productDto) throws IllegalStateException{
        System.out.println(productDto);

        ProductEntity productEntity;

        if(productRepository.findById(productDto.getProdId()).isPresent()){ //존재할 경우

            productEntity = productRepository.findById(productDto.getProdId()).get();

            List<MaterialEntity> materialEntityList = new ArrayList<>();

            for (HashMap<String, Integer> item : productDto.getReferencesValue()) {
                int matId = (int) item.get("matId");

                materialEntityList.add(materialEntityRepository.findById(matId).get());
            }

            //prod_id(product PK로 관련 MaterialProductEntity 리스트를 전부 가져와서 삭제 후 => 수정 한다.
            //수정할 자재의 수가 같을 수 없고 => 적거나 많을 수 있기 떄문에.
            List<MaterialProductEntity> materialProductEntityList = materialProductRepository.findByMaterial(productDto.getProdId());

            for(int i = 0; i < materialProductEntityList.size(); i++){ //삭제 작업
                materialProductRepository.delete(materialProductEntityList.get(i));
            }

            List<MaterialProductEntity> resultMaterialProductEntity = new ArrayList<>();

            //기존 값을 삭제했다면 다시 (제품 - 자재목록)을 넣어준다(material_product)
            for (int i = 0; i < materialEntityList.size(); i++) {
                MaterialProductEntity materialProductEntity = new MaterialProductEntity(materialEntityList.get(i), productEntity);
                //제품-재고 테이블에 필요한 정보를 set으로 다 넣었다면 save
                for (HashMap<String, Integer> item : productDto.getReferencesValue()) {
                    if (materialEntityList.get(i).getMatID() == (int) item.get("matId")){
                        materialProductEntity.setReferencesValue((int) item.get("matRate"));
                    }
                }
                resultMaterialProductEntity.add(materialProductRepository.save(materialProductEntity));
            }

            //--- 양방향을 위해
            for (int j = 0; j < resultMaterialProductEntity.size(); j++) {//새로 추가하는 mpno
                materialProductEntityList.add(resultMaterialProductEntity.get(j));
            }

            for (int i = 0; i < materialEntityList.size(); i++) { //재료 테이블에 mp 테이블 정보 넣어준다.
                materialEntityList.get(i).setMaterialProductEntityList(materialProductEntityList);
            }

            if (resultMaterialProductEntity.get(resultMaterialProductEntity.size() - 1).getMpno() >= 1) { //수정 성공시
                return true;
            }
        }
        return false;
    }
    @Transactional
    //제품 삭제 => 제품 관리 페이지에서 수행할 예정
    public String deleteProduct(int prodId){
        System.out.println("delte product : " + prodId);
        Optional<ProductEntity> productEntity = productRepository.findById(prodId);

        if(productEntity.isPresent()){
            List<ProductProcessEntity> productProcessList = productProcessRepository.findByProductEntity(prodId);

            if(productProcessList.size() > 0){
                return "[알림]"+prodId+"번 제품은 삭제할 수 없습니다.(공정에서 사용하는 제품)";
            }else{
                //materialsProduct도 삭제를 해줘야 삭제가됨..
                List<MaterialProductEntity> materialProductEntities = materialProductRepository.findByMaterial(prodId);
                for(int i = 0; i < materialProductEntities.size(); i++){ //materialsProduct에 있는 prodId 모두 삭제
                    materialProductRepository.delete(materialProductEntities.get(i));
                }
                // materialProduct모두 삭제했으면 그제서야 제품을 삭제한다..
                productRepository.delete(productEntity.get());
            }

        }else{
            return "[에러]"+prodId+"번 제품은 이미 삭제되었거나 삭제할 수 없는 제품입니다.";
        }

        return "[성공]"+prodId+"번 제품이 삭제되었습니다.";
    }

    //제품이 가진 자재리스트 가져오기
    public ProductDto getproductMaterialsList(int prodId){
        ProductDto productDto = new ProductDto(); //반환용

        List<MaterialProductEntity> materialProductEntityList = materialProductRepository.findByMaterial(prodId);

        List<HashMap<String, Integer>> returnMaterialsList = new ArrayList<>();


       for(int i = 0; i < materialProductEntityList.size(); i++){
           HashMap<String, Integer> materialInfo = new HashMap<>();

           materialInfo.put("matId", materialProductEntityList.get(i).getMaterialEntity().getMatID());
           materialInfo.put("matRate", materialProductEntityList.get(i).getReferencesValue());

           returnMaterialsList.add(materialInfo);

           System.out.println(i+"번째 장착 : "+returnMaterialsList.get(i));
       }

       productDto.setReferencesValue(returnMaterialsList);

       return productDto;
    }


    //선택한 자재의 합 가격(원가) 구하기
    public int getTotalPrice(List<HashMap<String, Integer>> matIDList){
        int totalPrice = 0;

        for (HashMap<String, Integer> item : matIDList) {
            int matId = (int) item.get("matId");

            totalPrice += materialEntityRepository.findById(matId).get().getMat_price();
        }

        return totalPrice;
    }
}
