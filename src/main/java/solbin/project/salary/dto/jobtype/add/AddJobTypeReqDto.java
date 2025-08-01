package solbin.project.salary.dto.jobtype.add;

import lombok.Getter;
import lombok.Setter;
import solbin.project.salary.domain.jobtype.JobType;
import solbin.project.salary.domain.user.User;

@Getter
@Setter
public class AddJobTypeReqDto {

    private String name;
    private Long payRate;

    public JobType toEntity(User user) {
        return JobType.builder()
                .payRate(payRate)
                .name(name)
                .user(user)
                .build();
    }
}
