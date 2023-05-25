package mes.service.product;

import lombok.extern.slf4j.Slf4j;
import mes.domain.Repository.product.MaterialProductRepository;
import mes.domain.Repository.product.ProductPlanRepository;
import mes.domain.Repository.product.ProductProcessRepository;
import mes.domain.Repository.product.ProductRepository;
import mes.domain.dto.product.AutoProdctDto;
import mes.domain.dto.product.AutoProduceDto;
import mes.domain.dto.product.ProductPlanDto;
import mes.domain.entity.material.MaterialEntityRepository;
import mes.domain.entity.material.MaterialInOutEntityRepository;
import mes.domain.entity.member.MemberEntity;
import mes.domain.entity.member.MemberRepository;
import mes.domain.entity.product.ProductEntity;
import mes.domain.entity.product.ProductPlanEntity;
import mes.webSocket.ChattingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AutoProduceService {
    @Autowired MaterialProductRepository materialProductRepository; //제품별 자재세트 구하기
    @Autowired MaterialEntityRepository materialEntityRepository; //자재
    @Autowired ProductRepository productRepository; //제품
    @Autowired MaterialInOutEntityRepository materialInOutEntityRepository; //자재 입출고
    @Autowired ProductPlanRepository productPlanRepository; //생산 지시
    @Autowired ProductProcessRepository productProcessRepository; //생산 공정
    @Autowired ProductPlanService productPlanService; //생산 공정 쓰기 위해
    @Autowired MemberRepository memberRepository; //승인권자 memberEntity를 가져오기 위함.

    @Transactional
    public void addAutoProduceService(){
        //1) 제품별 조회: 제품명, 평균 판매량, 안전재고량(현재고*1.2)
        List<AutoProdctDto> produceInfo1 = productProcessRepository.getAverageSalesAndCurrentStock();

        //2) 제품별 조회: 제품명, 평균 판매량, 평균 생산량
        List<AutoProdctDto> produceInfo2 = productProcessRepository.getProductSalesAndProduction();

        //3) 실질적으로 처리할 데이터 ; 제품명, 제품 현재 재고, 제품 평균 판매량
        List<AutoProdctDto> inputProductInfo = productProcessRepository.getCurrentStockAndAverageSales();

        //승인권자는 무조건 mno = 1이라고 가정한다.
        Optional<MemberEntity> memberEntity = memberRepository.findById(1);

        boolean produceOk = false; //생산 해야할지 말아야할지 정하는 유효성 검사 변수

        for(int i = 0; i < produceInfo1.size(); i++){ //제품별로 생산여부를 체크하고 생산한다.

            /* ------- 생산 여부 체크 ------- */

            //(1) 평균 판매량 > 현재 재고*1.2(안전율 적용) : true 자동생산 / false 대상 X
            if(produceInfo1.get(i).getAvgOrderCount() > produceInfo1.get(i).getProdSafeStock()){
                produceOk = true;
            }

            for(int j = 0; j < produceInfo2.size(); j++){
                //pk값이 같을 경우만 처리
                if(produceInfo2.get(j).getProdID() == produceInfo1.get(i).getProdID()){
                    //(2) 평균 판매량 / 평균 생산량 > 1 : true 자동생산 / false 대상 X
                    if(produceInfo2.get(j).getAvgOrderCount()/produceInfo2.get(j).getAvgProdPlanCount() > 1){
                        produceOk = true;
                        continue; //같은 것을 찾았다면 가장 가까운 for문 나가기
                    }
                }
            }

            if(produceOk && memberEntity.isPresent()){ //승인권자가 있고, 생산을 해야하면.
                //생산 지시를 넘기기 위한 dto를 만든다.
                ProductPlanDto productPlanDto = new ProductPlanDto();
                productPlanDto.setMemberDto(memberEntity.get().toDto());

                for(int j = 0; j < inputProductInfo.size(); j++){
                    //pk값이 같으면
                    if(produceInfo1.get(i).getProdID() == inputProductInfo.get(j).getProdID()){
                        //productEntity 찾기
                        Optional<ProductEntity> productEntity = productRepository.findById(inputProductInfo.get(j).getProdID());

                        if(productEntity.isPresent()){ //productEntity가 존재하면
                            //필요한 데이터를 담는다; productDto, 제품 생산 수량
                            productPlanDto.setProductDto(productEntity.get().toDto());
                            String prodPlanCount = String.valueOf(Math.floor(1.2*inputProductInfo.get(j).getAvgOrderCount()) - (inputProductInfo.get(j).getProdCurrentStock()/inputProductInfo.get(j).getAvgProdPlanCount()));
                            productPlanDto.setProdPlanCount(prodPlanCount);
                            System.out.println("자동 생산 지시 : " + productPlanDto);

                            productPlanService.postProduct(productPlanDto);

                        }
                    }
                }
            }
        }
    }

    @Transactional
    public void AutoProduce(int prodID){

        //제품별 조회: 제품명, 제품 현재 재고, 제품 평균 판매량, 제품 평균 생산량,안전재고량(현재고*1.2) 리스트
        List<AutoProdctDto> autoProdctDtoList = productProcessRepository.getCurrentStockAndAverageSales();

        System.out.println(autoProdctDtoList);

        //유효성 검사 변수
        boolean produceOk = false;

        //승인권자는 무조건 mno = 1이라고 가정한다.
        Optional<MemberEntity> memberEntity = memberRepository.findById(1);

        for(int i = 0; i < autoProdctDtoList.size(); i++){
            System.out.println("-----------------------------------------------");
            System.out.println(i + " : "+ autoProdctDtoList.get(i).toString());

            if(prodID == autoProdctDtoList.get(i).getProdID()){

                //(1) 평균 판매량 > 현재 재고*1.2(안전율 적용) : true 자동생산 / false 대상 X
                if(autoProdctDtoList.get(i).getAvgOrderCount() > autoProdctDtoList.get(i).getProdSafeStock()){
                    produceOk = true;
                }

                //(2) 평균 판매량 / 평균 생산량 > 1 : true 자동생산 / false 대상 X
                if(autoProdctDtoList.get(i).getAvgOrderCount()/autoProdctDtoList.get(i).getAvgProdPlanCount() > 1){
                    produceOk = true;
                }

                //만약 (1), (2) 유효성 검사 중 하나라도 걸리면
                if(produceOk){
                    //생산 지시를 넘기기 위한 dto를 만든다.
                    ProductPlanDto productPlanDto = new ProductPlanDto();
                    productPlanDto.setMemberDto(memberEntity.get().toDto());

                    //productEntity를 찾는다.
                    Optional<ProductEntity> productEntity = productRepository.findById(autoProdctDtoList.get(i).getProdID());

                    if(productEntity.isPresent()){ //productEntity가 존재하면
                        //필요한 데이터를 담는다; productDto, 제품 생산 수량
                        productPlanDto.setProductDto(productEntity.get().toDto());
                        String prodPlanCount =
                                String.valueOf(
                                        Math.round(
                                                (1.2*autoProdctDtoList.get(i).getAvgOrderCount()) -
                                                (autoProdctDtoList.get(i).getProdCurrentStock()/autoProdctDtoList.get(i).getAvgProdPlanCount())));
                        productPlanDto.setProdPlanCount(prodPlanCount);
                        System.out.println("자동 생산 지시 : " + productPlanDto);

                        productPlanService.postProduct(productPlanDto);

                    }
                }
            }

        }

    }
}
