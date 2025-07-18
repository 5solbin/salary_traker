package solbin.project.salary.dto.user.join;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import solbin.project.salary.domain.user.User;
import solbin.project.salary.domain.user.UserEnum;

/**
 *  JoinReqDto - 회원가입 요청 DTO
 *  Validation 포함
 */

@Getter @Setter
public class JoinReqDto {

    @NotEmpty
    @Pattern(regexp = "^[a-zA-z0-9]{2,6}@[a-zA-z0-9]{2,6}\\.[a-zA-Z]{2,3}$", message = "이메일 형식으로 작성해 주세요")
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    @Pattern(regexp = "^[a-zA-z가-힣]{1,20}$", message = "한글/영문 1~20자 이내로 작성해주세요")
    private String name;

    public User toEntity(BCryptPasswordEncoder encoder) {
        return User.builder()
                .email(email)
                .password(encoder.encode(password))
                .name(name)
                .role(UserEnum.USER)
                .build();
    }
}
