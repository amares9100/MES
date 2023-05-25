package mes.controller.performance;

import lombok.extern.slf4j.Slf4j;
import mes.domain.entity.member.MemberEntity;
import mes.domain.entity.member.PermissionDeniedException;
import mes.service.performance.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/perform")
public class PerformanceController {
    @Autowired private PerformanceService performanceService;

    // 1. 실적 출력 (type: 1 - 생산 실적, 2 - 판매실적) 코드 단순화 적용
    @GetMapping("")
    public List<?> printPerformance(@RequestParam int type, HttpSession session){
            log.info("printProduction type (1: production, 2: sales):"+type);

        //checkLogin(session);

        try{
            return performanceService.getPerformanceDto(type);
        } catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    public boolean checkLogin(HttpSession session){
        MemberEntity member = (MemberEntity) session.getAttribute("memeber");
        if(member==null){
            throw new PermissionDeniedException("권한이 없습니다.");
        }else{ return true; }
    }
}
