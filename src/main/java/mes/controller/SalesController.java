package mes.controller;

import lombok.extern.slf4j.Slf4j;
import mes.domain.dto.member.CompanyDto;
import mes.domain.dto.product.ProductDto;
import mes.domain.dto.product.ProductProcessDto;
import mes.domain.dto.sales.ProductProcessPageDto;
import mes.domain.dto.sales.SalesDto;
import mes.domain.dto.sales.SalesPageDto;
import mes.service.sales.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/sales")
public class SalesController {

    @Autowired
    private SalesService salesService;

    // 0. 판매 쪽 product_process 출력
    @GetMapping("/getproduct_process")
    public ProductProcessPageDto getProductProcess(ProductProcessPageDto productProcessPageDto){
        return salesService.getProductProcess(productProcessPageDto);
    }

    // 1. 판매 등록 ( 승인이 되었을 경우, 제품 재고량이 줄어들어야함 )
    @PostMapping("/salesCreate")
    public boolean salesCreate( @RequestBody SalesDto salesDto ){
        log.info("salesCreate : " + salesDto);

        return salesService.salesCreate( salesDto );
    }

    // [등록 조건1] 회사 호출 -> * ctype 2인 회사 조건수정해야함 *
    @GetMapping("/getcompany")
    public List<CompanyDto> getCompany(){
        log.info("getCompany");
        List<CompanyDto> list = salesService.getCompany();
        log.info("getCompany list : " + list );
        return list;
    }

    // [등록 조건2] 물품 호출
    @GetMapping("/getproduct")
    public List<ProductDto> getProduct(){
        log.info("getProduct");
        List<ProductDto> list = salesService.getProduct();
        log.info("getProduct list : " + list );
        return list;
    }

    // 2. 판매 출력 [ 출력창 2개 필요 , 승인 전 = 수정, 삭제 가능 , 승인 후 = 수정 삭제 불가 ]
    @GetMapping("/salesView")
    public SalesPageDto salesView(SalesPageDto salesPageDto){
        log.info( salesPageDto.toString() );
        return salesService.salesView(salesPageDto);

    }

    // 3. 판매 수정 ( 판매 수정했을 경우, 제품 재고량이 다시 늘어나야 함 ) / 결제자 승인 허락 전
    @PutMapping("/SalesUpdate")
    public boolean SalesUpdate(@RequestBody SalesDto salesDto){
        return salesService.SalesUpdate(salesDto);
    }

    // 4. 판매 삭제  / 결제자 승인 허락 전 -> order_status가 0인 경우 가능
    @DeleteMapping("/delete")
    public boolean SalesDelete(@RequestParam int order_id){
        return salesService.SalesDelete(order_id);
    }

    // 5. 판매 확정 / order_status 변경 -> stock 변경
    @PutMapping("/SalesStock")
    public boolean SalesStock(@RequestBody SalesDto salesDto){
        return salesService.SalesStock(salesDto);
    }

    
}
