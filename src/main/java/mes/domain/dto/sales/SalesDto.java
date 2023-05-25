package mes.domain.dto.sales;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mes.domain.BaseTime;
import mes.domain.dto.member.AllowApprovalDto;
import mes.domain.dto.member.CompanyDto;
import mes.domain.dto.member.MemberDto;
import mes.domain.dto.product.ProductDto;
import mes.domain.entity.member.AllowApprovalEntity;
import mes.domain.entity.member.CompanyEntity;
import mes.domain.entity.member.MemberEntity;
import mes.domain.entity.product.ProductEntity;
import mes.domain.entity.product.ProductPlanEntity;
import mes.domain.entity.product.ProductProcessEntity;
import mes.domain.entity.sales.SalesEntity;

import javax.persistence.Column;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesDto extends BaseTime {

    private int order_id;// -- 주문 ID

    private String orderDate;   // 주문 일자
    private int orderCount;     // 주문 수량
    private int order_status;   // 주문 상태
    private int salesPrice;     // 판매가
    private AllowApprovalDto allowApprovalDto; // 결제 승인여부
    private int al_app_no;

    private CompanyDto companyDto; // 판매회사
    private int cno;

    private ProductDto productDto;  // 주문 제품 ( 이름 )    * ProductEntity -> ProductDto로 변경
    private String prodName;        // 완재품 이름
    private int prodId;             // 완제품 id
    // private ProductProcessEntity productProcessEntity; // 주문 제품 ( 상태, 개수 )
    private int prodProcStatus;     // 완재품 상태
    private int prodStock;          // 완재품 개수

    private MemberDto memberDto;    // 판매등록자(판매원) * MemberEntity -> MemberDto로 변경
    private int mname;
    private String cdate;
    private String udate;

    public SalesEntity toEntity(){ // 저장용
        return SalesEntity.builder()
                .orderCount(this.orderCount)
                .order_status(this.order_status)
                .salesPrice(this.salesPrice)
                .memberEntity(this.memberDto.toEntity())
                .build();
    }

    public SalesDto(int order_id, LocalDate cdate , LocalDate udate , int orderCount, int order_status, int salesPrice, AllowApprovalDto allowApprovalDto, CompanyDto companyDto, ProductDto productDto, MemberDto memberDto) {
        this.order_id = order_id;
        this.cdate = cdate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.udate = udate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.orderCount = orderCount;
        this.order_status = order_status;
        this.salesPrice = salesPrice;
        this.allowApprovalDto = allowApprovalDto;
        this.companyDto = companyDto;
        this.productDto = productDto;
        this.memberDto = memberDto;
    }

}
