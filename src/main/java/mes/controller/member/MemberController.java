package mes.controller.member;

import lombok.extern.slf4j.Slf4j;
import mes.domain.dto.member.MemberDto;
import mes.domain.entity.member.MemberEntity;
import mes.domain.entity.member.MemberRepository;
import mes.service.member.MemberSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequestMapping("/home")
public class MemberController {
    @Autowired MemberSerivce memberService;
    @Autowired MemberRepository memberRepository;

    @PostMapping("/login")
    public boolean login( @RequestBody MemberDto memberDto, HttpSession session) {
        boolean result = memberService.login(memberDto.getMname(), memberDto.getMpassword());
            log.info("login result: " + result);
        if (result) {
            MemberEntity member = memberRepository.findByMnameAndMpassword(memberDto.getMname(), memberDto.getMpassword());
            session.setAttribute("member", member);
                log.info(session.getAttribute("member").toString());
            return result;
        }
        return result;
    }
    @GetMapping("/logout")
    public void logout(HttpSession session) {
            log.info("logout");
        memberService.logout(session);
    }

    // 로그인 정보 출력
    @GetMapping("/loginInfo")
    public MemberEntity loginInfo(HttpSession session) {
        MemberEntity member = memberService.getLoginInfo(session);
            log.info("loginInfo" + member.toString());;
        return member;
    }
}
