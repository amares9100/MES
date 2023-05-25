package mes.domain.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocketMessage { //재고부족시 메시지 보내기
    private int type; //재고 부족임을 알리는
    private String prodName; //제품명
    private String matName; //자재명

}
