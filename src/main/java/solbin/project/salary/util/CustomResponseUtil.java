package solbin.project.salary.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import solbin.project.salary.dto.ResponseDto;

/**
 * 로그인 상황에서의 반환을 제어하는 클래스
 * 예외를 백엔드 측에서 직접 제어해서 문제점을 보다 쉽게 찾을 수 있도록 한다.
 */
public class CustomResponseUtil {

    private static final Logger log = LoggerFactory.getLogger(CustomResponseUtil.class);

    public static void success(HttpServletResponse response, Object dto){

        try{
            ObjectMapper om = new ObjectMapper();
            ResponseDto<?> responseDto = new ResponseDto<>(1, "로그인성공", dto);
            String responseBody = om.writeValueAsString(responseDto);
            response.setContentType("application/jsion; charset=utf-8");
            response.setStatus(200);
            response.getWriter().println(responseBody); // 예쁘게 메시지를 포장한느 공통적인 응답 DTO를 만들어 보자!

        }catch (Exception e) {
            log.error("서버 파싱 에러");
        }
    }

    public static void fail(HttpServletResponse response, String msg, HttpStatus httpStatus){

        try{
            ObjectMapper om = new ObjectMapper();
            ResponseDto<?> responseDto = new ResponseDto<>(-1, msg, null);
            String responseBody = om.writeValueAsString(responseDto);
            response.setContentType("application/jsion; charset=utf-8");
            response.setStatus(httpStatus.value());
            response.getWriter().println(responseBody); // 예쁘게 메시지를 포장한느 공통적인 응답 DTO를 만들어 보자!

        }catch (Exception e) {
            log.error("서버 파싱 에러");
        }
    }
}
