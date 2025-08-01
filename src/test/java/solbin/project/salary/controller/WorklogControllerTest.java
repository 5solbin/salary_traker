package solbin.project.salary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import solbin.project.salary.domain.jobtype.JobType;
import solbin.project.salary.domain.user.User;
import solbin.project.salary.domain.worklog.Worklog;
import solbin.project.salary.dto.worklog.add.AddWorklogReqDto;
import solbin.project.salary.dto.worklog.monthly.GetMonthlyReqDto;
import solbin.project.salary.dto.worklog.update.UpdateReqDto;
import solbin.project.salary.dummy.DummyObject;
import solbin.project.salary.repository.JobTypeRepository;
import solbin.project.salary.repository.UserRepository;
import solbin.project.salary.repository.WorklogRepository;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("classpath:db/teardown.sql")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class WorklogControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private WorklogRepository worklogRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private EntityManager em;
    @Autowired
    private JobTypeRepository jobTypeRepository;

    @BeforeEach
    public void setup() {
        dataSetting();
        em.clear();
    }

    @WithUserDetails(value = "aaa@naver.com" , setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void 근무기록_추가() throws Exception {
        //given
        AddWorklogReqDto addReqDto = new AddWorklogReqDto();
        addReqDto.setStartTime(LocalDateTime.of(2025, 7, 10, 9, 0, 0));
        addReqDto.setEndTime(LocalDateTime.of(2025, 7, 10, 18, 0, 0));
        addReqDto.setJobTypeId(1L);

        String requestBody = om.writeValueAsString(addReqDto);

        //when
        ResultActions resultActions = mvc.perform(post("/api/s/worklog")
                .contentType(MediaType.APPLICATION_JSON).content(requestBody));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + responseBody);

        //then
        resultActions.andExpect(status().isCreated());
    }

    @WithUserDetails(value = "aaa@naver.com" , setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void 근무기록_삭제() throws Exception{
        //given
        Long worklogId = 1L;
        //when
        ResultActions resultActions = mvc.perform(delete("/api/s/worklog/" + worklogId));
        String  responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + responseBody);

        //then
        resultActions.andExpect(status().isOk());
    }

    @WithUserDetails(value = "aaa@naver.com" , setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void 근무기록_변경() throws Exception{
        //given
        Long worklogId = 1L;

        UpdateReqDto updateReqDto = new UpdateReqDto();
        updateReqDto.setStartTime(LocalDateTime.of(2025, 7, 9, 9, 0, 0));
        updateReqDto.setEndTime(LocalDateTime.of(2025, 7, 9, 18, 0, 0));
        String requestBody = om.writeValueAsString(updateReqDto);

        //when
        ResultActions resultActions = mvc.perform(put("/api/s/worklog/" + worklogId)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + responseBody);

        //then
        resultActions.andExpect(status().isOk());
    }

    @WithUserDetails(value = "aaa@naver.com" , setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void 근무기록_조회() throws Exception{
        //given
        GetMonthlyReqDto getMonthlyReqDto = new GetMonthlyReqDto();
        getMonthlyReqDto.setMonth("7");
        getMonthlyReqDto.setYear("2025");

        //when
        ResultActions resultActions = mvc.perform(get("/api/s/worklog/getMonthlyInfo?" + "year=" + getMonthlyReqDto.getYear() + "&month=" + getMonthlyReqDto.getMonth())
        .contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + responseBody);

        //then
        resultActions.andExpect(status().isOk());
    }



    private void dataSetting() {
        User aaa = userRepository.save(newUser("aaa@naver.com", "aaa"));

        JobType jobType = JobType.builder()
                .name("치킨집")
                .payRate(10000L)
                .build();
        jobTypeRepository.save(jobType);

        LocalDateTime start1 = LocalDateTime.of(2025, 7, 8, 9, 0, 0);
        LocalDateTime end1 = LocalDateTime.of(2025, 7, 8, 18, 0, 0);
        Worklog worklog1 = worklogRepository.save(newWorklog(aaa, start1, end1, jobType));

        LocalDateTime start2 = LocalDateTime.of(2025, 7, 9, 9, 0, 0);
        LocalDateTime end2 = LocalDateTime.of(2025, 7, 9, 18, 0, 0);
        Worklog worklog2 = worklogRepository.save(newWorklog(aaa, start2, end2, jobType));

    }

}