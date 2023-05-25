package mes.domain.dto.member;

import lombok.*;
import mes.domain.entity.member.AllowApprovalEntity;
import mes.domain.entity.member.MemberEntity;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllowApprovalDto {
    private int al_app_no; //-- PK
    private boolean al_app_whether;//  -- 결재자 승인 여부
    private String al_app_date; // 승인 일자
    private int al_app_role; // 승인 처리인지 승인 요청인지

    private MemberEntity memberEntity; // 승인 요청 or 처리한 사람

    private AllowApprovalDto allowApprovalDto;

    public AllowApprovalEntity toInEntity(){
        return AllowApprovalEntity.builder()
                .al_app_whether(this.al_app_whether)
                .build();
    }

    //출고용 승인
    public AllowApprovalEntity toOutEntity(){
        return AllowApprovalEntity.builder()
                .al_app_whether(true)
                .build();
    }

    public AllowApprovalEntity toPlanInEntity(){
        return AllowApprovalEntity.builder()
                .al_app_whether(false)
                .memberEntity(this.memberEntity)
                .build();
    }

    //저장용
    public AllowApprovalEntity toSaveEntity(){
        return AllowApprovalEntity.builder()
                .al_app_no(this.al_app_no)
                .al_app_whether(this.al_app_whether)
                .al_app_date(this.al_app_date)
                .memberEntity(this.memberEntity)
                .build();
    }

}
