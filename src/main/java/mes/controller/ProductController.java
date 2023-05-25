package mes.controller;

import lombok.extern.slf4j.Slf4j;
import mes.domain.dto.material.MaterialDto;
import mes.domain.dto.product.PageDto;
import mes.domain.dto.product.ProductDto;
import mes.domain.entity.material.MaterialEntity;
import mes.service.product.ProductPlanService;
import mes.service.product.ProductProcessService;
import mes.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService; //간단한 제품 CRUD 기능 수행
    
    @Autowired
    private ProductPlanService productPlanService; // 제품 지시관련 기능 수행
    
    @Autowired
    private ProductProcessService productProcessService; //제품 공정 관련 기능 수행

    
    //전체 제품 출력 => 제품 지시, 제품 관리에서 출력 => 제품쪽에서는 재고 출력
    @GetMapping("")
    public PageDto getProductList(PageDto pageDto) throws IllegalStateException{
        System.out.println(pageDto.toString());
        PageDto page = productService.getProductList(pageDto);
        System.out.println("제품 가져왔다 controller : " + page);
        return page;
    }
    
    //제품 등록
    @PostMapping("")
    public boolean postProduct(@RequestBody ProductDto productDto) throws IOException {
        System.out.println(productDto.toString());
        return productService.postProduct(productDto);
    }

    //재품 수정
    @PutMapping("")
    public boolean putProduct(@RequestBody ProductDto productDto) throws Exception{
        if(productDto.getType() == 1) { //제품 자체를 수정
            return productService.putProduct(productDto);
        }else if(productDto.getType() == 2){
            return productService.putProdcuctMaterialList(productDto);
        }
        return false;
    }

    //제품 삭제
    @DeleteMapping("")
    public String deleteProduct(@RequestParam int prodId){
        return productService.deleteProduct(prodId);
    }

    //제품이 가지고 있는 materialList 반환
    @GetMapping("/existMaterials")
    public ProductDto getExistsMaterials(@RequestParam int prodId){
        //js에서 인식하기 쉽게 변환해서 보내준다.
        return productService.getproductMaterialsList(prodId);
    }

    //제품의 총 가격(원가)
    @GetMapping("totalPrice")
    public int getTotalPrice( List<HashMap<String, Integer>> matIDList){
        return productService.getTotalPrice(matIDList);
    }
}
