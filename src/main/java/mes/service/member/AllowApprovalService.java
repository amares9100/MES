package mes.service.member;

import lombok.extern.slf4j.Slf4j;
import mes.domain.Repository.product.ProductProcessRepository;
import mes.domain.entity.product.ProductProcessEntity;
import mes.webSocket.ChattingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mes.domain.dto.sales.SalesDto;
import mes.domain.dto.material.MaterialInOutDto;
import mes.domain.dto.product.ProductPlanDto;
import mes.domain.entity.member.MemberEntity;
import mes.domain.entity.material.MaterialInOutEntity;
import mes.domain.entity.member.AllowApprovalEntity;
import mes.domain.entity.member.AllowApprovalRepository;
import mes.domain.entity.member.PermissionDeniedException;
import mes.domain.entity.product.ProductPlanEntity;
import mes.domain.entity.sales.SalesEntity;
import mes.domain.entity.material.MaterialInOutEntityRepository;
import mes.domain.entity.sales.SalesRepository;
import mes.domain.Repository.product.ProductPlanRepository;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpSession;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
@Service @Slf4j
public class AllowApprovalService {

    @Autowired ProductPlanRepository productPlanRepository;
    @Autowired MaterialInOutEntityRepository meterialRepository;
    @Autowired SalesRepository salesRepository;
    @Autowired AllowApprovalRepository allowApprovalRepository;
    @Autowired ProductProcessRepository productProcessRepository;
    @Autowired private ChattingHandler chattingHandler; //소켓

    // 0. 제네릭 사용하기 위해 생성
    public List<?> getEntityListByType(int type) {
        if (type == 1) {
                log.info( "AllowApprovalService getEntityListByType(material)" + meterialRepository.findAll().toString());
            return meterialRepository.findAll();
        } else if (type == 2) {
                log.info( "AllowApprovalService getEntityListByType(product)" + productPlanRepository.findAll().toString());
            return productPlanRepository.findAll();
        } else if (type == 3) {
                log.info( "AllowApprovalService getEntityListByType(sales)" + salesRepository.findAll().toString());
            return salesRepository.findAll();
        } else{
            throw new IllegalArgumentException("알 수 없는 요청");
        }
    }

    // 1. 승인 요청 데이터 출력 [type: 1 - 자재, 2 - 제품, 3 - 판매 ]
    // *프론트 처리 필요 사항: option: 1 - 미승인, 2 - 승인, 3 - 전체 출력 (Back에서 관리하면 로직 복잡해짐)
    public List<?> printAllowApproval( int type ){

        // 1. 승인 리스트 가져오기 [제네릭 사용 시도 - 3가지 타입 한번에 받기 위함]
        List<?> approvalList = getEntityListByType(type);

        // 2. 승인 리스트 저장소 생성
        List<Object> result;
        // 의견: Object 물음표로 변경해도 문제 없는지 테스트 필요
        // 확인 결과: 문제 없음

        // 3. 결재권자인 경우, 아래 내용 출력 [type 별로 List 저장 후 출력]
        if( type == 1) { // 자재
            result = new ArrayList<>();
            for (Object obj : approvalList) {
                MaterialInOutEntity entity = (MaterialInOutEntity) obj;

                MaterialInOutDto dto = new MaterialInOutDto(entity.getMat_in_outid(), entity.getMat_in_type(), entity.getMat_st_stock(),
                        entity.cdate.toLocalDate(), entity.udate.toLocalDate(), entity.getAllowApprovalEntity().toInDto(),
                        entity.getMaterialEntity().toDto(), entity.getMemberEntity().toDto()
                );
                result.add(dto);
            }
        } else if ( type == 2) { // 제품
            result = new ArrayList<>();
            for (Object obj : approvalList) {
                ProductPlanEntity entity = (ProductPlanEntity) obj;
                ProductPlanDto dto = new ProductPlanDto(
                        entity.getProdPlanNo(), entity.getProdPlanCount(), entity.getProdPlanDate(),
                        entity.getProductEntity().toDto(), entity.getAllowApprovalEntity().toInDto());
                result.add(dto);
            }
        } else if ( type == 3) { // 판매
            result = new ArrayList<>();
            for (Object obj : approvalList) {
                SalesEntity entity = (SalesEntity) obj;
                System.out.println(entity);
                SalesDto dto = new SalesDto(
                        entity.getOrder_id(), entity.cdate.toLocalDate() , entity.udate.toLocalDate(),
                        entity.getOrderCount(), entity.getOrder_status(), entity.getSalesPrice(),
                        entity.getAllowApprovalEntity().toInDto(), entity.getCompanyEntity().toDto(), entity.getProductEntity().toDto(), entity.getMemberEntity().toDto());
                result.add(dto);
            }
        } else{ // 예외 처리(PermissionDeniedException 클래스 공용 사용)
            throw new PermissionDeniedException("알 수 없는 요청");
        }
            log.info("AllowApprovalService printAllowApproval: " + result);
        return result;
    }
    // 예상되는 문제점: repository null이면 에러 발생할 거 같음
    // 확인 완료: 이상 없음. [23.05.15, th]

    // 2. 승인/반려 처리
    // 특이사항: 코드 변경함 [23.05.15, th]
    // 이유: approvalEntity에서 승인/반려 처리하고 있었기 때문에, id값을 제대로 인식하지 못하는 문제점 확인
    // 해결: 연결되어 있는 entity 별로 승인/반려 처리하고, approvalEntity에 승인/반려 처리 메소드 생성하여 공통 사용
    private void updateAllowApproval(AllowApprovalEntity entity, boolean approval, HttpSession session) {

        entity.updateApproval(approval);
        MemberEntity member = (MemberEntity) session.getAttribute("member");
        entity.setMemberEntity(member);
        try {
            allowApprovalRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("AllowApprovalService updateAllowApproval failed to save");
        }
    }

    // 3. 자재, 제품, 판매 승인/반려 처리 [23.05.15, th]
    // 메소드 역할: 1)type에 맞게 연결된 repository에서 승인/반려 처리, 2)updateAllowApproval 메소드에 내용 전달
    // 특이사항-1: repository에서 승인/반려 처리 기능 추가 ('2.승인/반려 처리 메소드 변경 이유와 동일)
    // 특이사항-2: @Transactional 어노테이션 사용하지 않았기 때문에 set으로 데이터 초기화 후 save하여 db에 저장
    // 승인 후 소켓 처리 하는 부분 메소드르 빼기 [23.05.20, 수정 완료 th]
    public boolean approveMaterialInOut(List<Integer> MatInOutIDs, HttpSession session) {
        for (int id : MatInOutIDs) {
            Optional<MaterialInOutEntity> materialInOutEntity = meterialRepository.findById(id);
            materialInOutEntity.ifPresent(entity -> updateAllowApproval(entity.getAllowApprovalEntity(), true, session));
        }
        sendMessageToChattingHandler("11");
        return true;
    }
    public boolean rejectMaterialInOut(List<Integer> MatInOutIDs, HttpSession session) {
        for (int id : MatInOutIDs) {
            Optional<MaterialInOutEntity> materialInOutEntity = meterialRepository.findById(id);
            materialInOutEntity.ifPresent(entity -> updateAllowApproval(entity.getAllowApprovalEntity(), false, session));
        } return true;
    }
    // 제품 재고 로직 변경 [23.05.18, th]
    // 기존: 제품 구분 없이 재고 추가 (DB에서 집계처리하여 제품 재고를 사용하고자 함)
    // 변경: 제품 구분 하여 재고 추가 (1회 이상 재고 등록된 제품의 경우, 기재고에 누적 더하기 & 신규 제품은 행 추가)
    public boolean approveProductInOut(List<Integer> ProdInOutIDs, HttpSession session) {
        for (int id : ProdInOutIDs) {
            Optional<ProductPlanEntity> productPlanEntity = productPlanRepository.findById(id);
            productPlanEntity.ifPresent(entity -> {
                updateAllowApproval(entity.getAllowApprovalEntity(), true, session);

                ProductProcessEntity productProcessEntity = productProcessRepository.findByProductEntity(entity.getProductEntity());

                if (productProcessEntity != null) {
                    // 동일한 정보가 이미 존재하는 경우
                    int existingStock = productProcessEntity.getProdStock();
                    int newStock = existingStock + Integer.parseInt(entity.getProdPlanCount());
                    productProcessEntity.setProdStock(newStock);
                } else {
                    // 새로운 정보를 생성하는 경우
                    productProcessEntity = new ProductProcessEntity();
                    productProcessEntity.setProductEntity(entity.getProductEntity());
                    productProcessEntity.setProdStock(Integer.parseInt(entity.getProdPlanCount()));
                    productProcessEntity.setProdProcDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }

                productProcessRepository.save(productProcessEntity);
            });
        }
        sendMessageToChattingHandler("21");
        return true;
    }
    public boolean rejectProductInOut(List<Integer> ProdInOutIDs, HttpSession session) {
        for (int id : ProdInOutIDs) {
            Optional<ProductPlanEntity> productPlanEntity = productPlanRepository.findById(id);
            productPlanEntity.ifPresent(entity -> updateAllowApproval(entity.getAllowApprovalEntity(), false, session));

        } return true;
    }
    
    // 판매 승인&반려 처리 후 orderState 상태 변경 추가 적용 [23.05.15, th]
    public boolean approveSales(List<Integer> OrderIds, HttpSession session) {
        for (int id : OrderIds) {
            Optional<SalesEntity> salesEntity = salesRepository.findById(id);

            salesEntity.ifPresent(entity -> {
                updateAllowApproval(entity.getAllowApprovalEntity(), true, session);
                entity.setOrder_status(entity.getAllowApprovalEntity().isAl_app_whether() ? 1 : 0);
                salesRepository.save(entity); // 변경 내용 저장
            });

        }
        sendMessageToChattingHandler("31");
        return true;
    }
    public boolean rejectSales(List<Integer> OrderIds, HttpSession session) {
        for (int id : OrderIds) {
            Optional<SalesEntity> salesEntity = salesRepository.findById(id);

            salesEntity.ifPresent(entity -> {
                updateAllowApproval(entity.getAllowApprovalEntity(), false, session);
                entity.setOrder_status(entity.getAllowApprovalEntity().isAl_app_whether() ? 1 : 0);
                salesRepository.save(entity); // 변경 내용 저장
            });
        } return true;
    }
    
    // 소켓 사용 메소드 생성 [23.05.20, th]
    // num: 11 - 자재, 21 - 생산, 31 - 판매
    private void sendMessageToChattingHandler(String message) {
        try {
            chattingHandler.handleMessage(null, new TextMessage(message));
        } catch (Exception e) {
            System.err.println("sendMessageToChattingHandler error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}