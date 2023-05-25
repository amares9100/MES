package mes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication // 하위 폴더내 컴포넌트 스캔 빈 등록
@EnableJpaAuditing //BaseTime합의 @EntityListeners(AuditingEntityListener.class)와 세트
public class AppStart {
    public static void main(String[] args) {
        //05/25
        SpringApplication.run(AppStart.class);

    }
}
