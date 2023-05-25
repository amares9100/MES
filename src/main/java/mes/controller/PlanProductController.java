package mes.controller;

import lombok.extern.slf4j.Slf4j;
import mes.controller.member.MemberController;
import mes.domain.dto.material.MaterialDto;
import mes.domain.dto.product.PageDto;
import mes.domain.dto.product.ProductDto;
import mes.domain.dto.product.ProductPlanDto;
import mes.service.product.ProductPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/planProduct")
public class PlanProductController {

    @Autowired
    private ProductPlanService productPlanService; // 제품 지시관련 기능 수행

    //생산 지시 목록 가져오기
    @GetMapping("")
    public List<ProductPlanDto> getPlanProductList() throws IllegalStateException{
        return productPlanService.getPlanProductList();
    }

    //생산 지시
    @PostMapping("")
    public List<String> postProduct(@RequestBody ProductPlanDto productPlanDto) throws IOException {
        System.out.println("생산 지시 : " + productPlanDto.toString());

        return productPlanService.postProduct(productPlanDto);
    }

    //생산 지시 수정 => 승인이 안되었을 경우만
    @PutMapping("")
    public boolean putProduct(@RequestBody ProductDto productDto) throws Exception{
        return false;
    }

    //생산 지시 삭제 => 승인이 안되었을 경우만
    @DeleteMapping("")
    public String deleteProduct(@RequestParam int prodPlanNo){
        return productPlanService.deleteProduct(prodPlanNo);
    }

    //생산 지시를 내릴 자재객체리스트를 넣는다.
    @GetMapping("/existMaterialsList")
    public List<MaterialDto> getExistMaterialList(@RequestParam int prodId){
        System.out.println("생산 지시 목록 : " + prodId);
        return productPlanService.getExistMaterialList(prodId);
    }

}
