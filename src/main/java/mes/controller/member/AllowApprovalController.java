package mes.controller.member;

import lombok.extern.slf4j.Slf4j;
import mes.domain.entity.member.MemberEntity;
import mes.domain.entity.member.PermissionDeniedException;
import mes.service.Material.MaterialService;
import mes.service.member.AllowApprovalService;
import mes.service.product.ProductService;
import mes.service.sales.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/allowApproval")
public class AllowApprovalController {

    @Autowired private AllowApprovalService allowApprovalService;

    // 1. 승인 리스트 출력
    @GetMapping("")
    public List<?> printAllowApproval(@RequestParam int type, HttpSession session) {

        // 1. 승인권자 권한 확인 (제어)
        MemberEntity member = (MemberEntity) session.getAttribute("member");
        if (!member.getPosition().equals("임원")) { // 아니면 던지기 처리 (PermissionDeniedException 예외 클래스 추가 생성)
            throw new PermissionDeniedException("권한이 없습니다.");
        } else{
            try { log.info(allowApprovalService.printAllowApproval(type).toString());
                    log.info(allowApprovalService.printAllowApproval(type).toString());
                return allowApprovalService.printAllowApproval(type);
            } catch (PermissionDeniedException e) {
                // 권한 없음 예외 처리
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
            } catch (Exception e) {
                // 그 외 예외 처리
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
            }
        }
    }

    // 2. 승인/반려 처리
    @PutMapping("")
    public boolean updateAllowApproval(@RequestBody Map<String, Object> requestBody, HttpSession session ){
        int type = (int) requestBody.get("type"); // type 1: 자재, 2: 제품 , 3: 판매
        int approve = (int) requestBody.get("approve"); // approve 1: 승인, 2: 반려
        List<Integer> ids = (List<Integer>) requestBody.get("id");
        // id List로 받음(선택된 항목 전체 처리 적용하기 위함)
            // 아래 log.info로 들어오는 데이터 확인 완료(결과: 이상 없음, 확인일자: 23.05.15, th)
            // log.info("AllowApprovalController updateAllowApproval(type: 1-자재, 2-제품, 3-판매):" + type);
            // log.info("AllowApprovalController updateAllowApproval(1-승인, 2-반려):" + approve);
            // log.info("AllowApprovalController updateAllowApproval(id list)" + ids);
        try{
            if(type == 1){ // 자재
                if( approve == 1 ){ // 승인 or 반려
                    return allowApprovalService.approveMaterialInOut(ids, session);
                } else{ return allowApprovalService.rejectMaterialInOut(ids, session); }
            } else if(type == 2){ // 제품
                if( approve == 1 ){ // 승인 or 반려
                    return allowApprovalService.approveProductInOut(ids, session);
                } else{ return allowApprovalService.rejectProductInOut(ids, session); }
            } else if(type == 3){ // 판매
                if( approve == 1 ){ // 승인 or 반려
                    return allowApprovalService.approveSales(ids, session);
                } else{ return allowApprovalService.rejectSales(ids, session); }
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
        return false;
    }

}
