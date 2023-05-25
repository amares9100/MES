package mes.service.member;

import lombok.extern.slf4j.Slf4j;
import mes.domain.entity.member.MemberEntity;
import mes.domain.entity.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;


@Service @Slf4j
public class MemberSerivce {


    @Autowired private MemberRepository memberRepository;

    // 1. 로그인
    public boolean login(String mname, String password) { log.info("login:" + mname);
        MemberEntity member = memberRepository.findByMnameAndMpassword(mname, password);
        return member != null;
    }
    
    // 2. 로그아웃
    public void logout(HttpSession session) {
        session.invalidate();
    }

    // 3. 로그인 정보 반환[React Component에서 SessionStorage에 저장하기 위함]
    public MemberEntity getLoginInfo(HttpSession session) {
        MemberEntity member = (MemberEntity) session.getAttribute("member");
        return member;
    }
}

