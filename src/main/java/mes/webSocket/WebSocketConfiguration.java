package mes.webSocket;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket // 웹소켓 프로토콜의 url 매핑 설정
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Autowired
    private ChattingHandler chattingHandler;

    @Override // 서버소켓으로 사용되고있는 클래스 등록
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 서버소캣 등록함수
        registry.addHandler(chattingHandler , "/chat").setAllowedOrigins("*");
        //registry.addHandler(소켓객체 , 경로)
        // .setAllowedOrigins("*") : 요청할수있는 url설정
    }
}
