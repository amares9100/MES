package mes.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) //AppStart의 @EnableJpaAuditing와 세트
public class BaseTime {
    @CreatedDate
    public LocalDateTime cdate;

    @LastModifiedDate
    public LocalDateTime udate;


}
