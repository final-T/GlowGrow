package com.tk.gg.reservation.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tk.gg.reservation.application.dto.CreateTimeSlotRequestDto;
import com.tk.gg.reservation.application.dto.TimeSlotDto;
import com.tk.gg.reservation.application.dto.UpdateTimeSlotRequestDto;
import com.tk.gg.reservation.application.service.TimeSlotService;
import com.tk.gg.reservation.presentation.request.CreateTimeSlotRequest;
import com.tk.gg.reservation.presentation.request.UpdateTimeSlotRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.tk.gg.common.response.ResponseMessage.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("[Reservaition] - TimeSlot")
@AutoConfigureMockMvc
@WebMvcTest(TimeSlotController.class)
class TimeSlotControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TimeSlotService timeSlotService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("[POST] 예약 타임 슬롯 생성 - 정상 호출")
    @Test
    @WithMockUser(username = "user@example.com", roles = {"PROVIDER"})
    void 예약타임슬롯_생성_성공() throws Exception {
        // 테스트용 데이터 준비
        LocalDate localDate = LocalDate.parse("2024-10-02");
        CreateTimeSlotRequest request = new CreateTimeSlotRequest(1L, localDate, 20);
        TimeSlotDto timeSlotDto = createTimeSlotDto(localDate);
        given(timeSlotService.createTimeSlot(any(CreateTimeSlotRequestDto.class),any())).willReturn(timeSlotDto);

        // POST 요청 수행 및 결과 검증
        mvc.perform(post("/api/time-slots")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                        .with(user("user@example.com").roles("PROVIDER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value(TIMESLOT_CREATE_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data.id").value(timeSlotDto.id().toString()))
                .andExpect(jsonPath("$.data.serviceProviderId").value(1L))
                .andExpect(jsonPath("$.data.availableDate").value("2024-10-02"))
                .andExpect(jsonPath("$.data.availableTime").value(20))
                .andExpect(jsonPath("$.data.isReserved").value(false))
                .andDo(print());

        then(timeSlotService).should().createTimeSlot(any(CreateTimeSlotRequestDto.class), any());
    }


    @DisplayName("[GET] 예약타임슬롯 페이징, 정렬 조회 - 정상 호출")
    @Test
    @WithMockUser(username = "user@example.com", roles = {"PROVIDER"})
    void 예약타임슬롯_전체조회_페이징및정렬_성공() throws Exception {
        Sort sort = Sort.by(Sort.Order.desc("availableDate"));
        Pageable pageable = PageRequest.of(0, 5, sort);
        // 저장 메서드 모킹
        given(timeSlotService.getAllTimeSlot(eq(null),eq(null), eq(pageable)))
                .willReturn(new PageImpl<>(List.of(), pageable, 0));

        mvc.perform(get("/api/time-slots")
                    .queryParam("page", "0")
                    .queryParam("size", "5")
                    .queryParam("sort", "availableDate,desc")
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf())
                    .with(user("user@example.com").roles("PROVIDER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.page.size").value(5))
                .andExpect(jsonPath("$.message").value(TIMESLOT_RETRIEVE_SUCCESS.getMessage()))
                .andDo(print());

        then(timeSlotService).should().getAllTimeSlot(eq(null),eq(null), eq(pageable));
    }

    @DisplayName("[GET] 예약타임슬롯 단건 조회 - 정상 호출")
    @Test
    @WithMockUser(username = "user@example.com", roles = {"PROVIDER"})
    void 예약타임슬롯_단건조회_성공() throws Exception {
        // 저장 메서드 모킹
        LocalDate localDate = LocalDate.parse("2024-10-02");
        TimeSlotDto timeSlotDto = createTimeSlotDto(localDate);
        given(timeSlotService.getTimeSlotDetails(timeSlotDto.id())).willReturn(timeSlotDto);

        mvc.perform(get("/api/time-slots/{timeSlotId}", timeSlotDto.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(user("user@example.com").roles("PROVIDER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value(TIMESLOT_RETRIEVE_SUCCESS.getMessage()))
                .andDo(print());

        then(timeSlotService).should().getTimeSlotDetails(timeSlotDto.id());
    }

    @DisplayName("[POST] 예약 타임 슬롯 수정 - 정상 호출")
    @Test
    @WithMockUser(username = "user@example.com", roles = {"PROVIDER"})
    void 예약타임슬롯_수정_성공() throws Exception {
        // 테스트용 데이터 준비
        LocalDate localDate = LocalDate.parse("2024-10-02");
        UpdateTimeSlotRequest request = new UpdateTimeSlotRequest(1L,localDate,10,false);
        willDoNothing().given(timeSlotService)
                .updateTimeSlot(any(UUID.class),any(UpdateTimeSlotRequestDto.class),any());

        // POST 요청 수행 및 결과 검증
        mvc.perform(put("/api/time-slots/{timeSlotId}", UUID.randomUUID())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                        .with(user("user@example.com").roles("PROVIDER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value(TIMESLOT_UPDATE_SUCCESS.getMessage()))
                .andDo(print());

        then(timeSlotService).should().updateTimeSlot(any(UUID.class),any(UpdateTimeSlotRequestDto.class), any());
    }


    private TimeSlotDto createTimeSlotDto(LocalDate localDate){
        return TimeSlotDto.builder()
                .id(UUID.randomUUID())
                .availableDate(localDate)
                .serviceProviderId(1L)
                .isReserved(false)
                .availableTime(20).build();
    }
}