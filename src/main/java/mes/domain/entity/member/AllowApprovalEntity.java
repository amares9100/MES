package mes.domain.entity.member;

import lombok.*;
import mes.domain.dto.member.AllowApprovalDto;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "allowApproval")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllowApprovalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int al_app_no; //-- PK
    @Column private boolean al_app_whether;//  -- 결재자 승인 여부
    @Column private String al_app_date; // 승인 일자


    @ManyToOne
    @JoinColumn(name = "mno")
    @ToString.Exclude
    private MemberEntity memberEntity; // 승인 요청 or 처리한 사람

    // ----------------------------n:n or n:1 관계 설정 필요
    public AllowApprovalDto toInDto(){
        return AllowApprovalDto.builder()
                .al_app_no(this.al_app_no)
                .al_app_whether(this.al_app_whether)
                .al_app_date(this.al_app_date)
                .memberEntity(this.memberEntity)
                .build();
    }

    public AllowApprovalDto toPlanDto(){
        return AllowApprovalDto.builder()
                .al_app_no(this.al_app_no)
                .al_app_whether(this.al_app_whether)
                .al_app_date(this.al_app_date)
                .memberEntity(this.memberEntity)
                .build();
    }

    // 추가 메소드 생성: updateApproval [23-05-15, th]
    // 기능: 승인/반려 및 승인일자 초기화
    public void updateApproval(boolean approval) {
        this.setAl_app_whether(approval);
        this.setAl_app_date(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }
}
