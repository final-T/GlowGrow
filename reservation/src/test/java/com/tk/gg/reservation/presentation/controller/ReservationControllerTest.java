package com.tk.gg.reservation.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tk.gg.common.response.ResponseMessage;
import com.tk.gg.reservation.application.dto.CreateReservationDto;
import com.tk.gg.reservation.application.dto.ReservationDto;
import com.tk.gg.reservation.application.dto.TimeSlotDto;
import com.tk.gg.reservation.application.dto.UpdateReservationDto;
import com.tk.gg.reservation.application.service.ReservationService;
import com.tk.gg.reservation.domain.model.TimeSlot;
import com.tk.gg.reservation.domain.type.ReservationStatus;
import com.tk.gg.reservation.presentation.request.CreateReservationRequest;
import com.tk.gg.reservation.presentation.request.UpdateReservationRequest;
import com.tk.gg.security.user.AuthUserInfo;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[Reservation] - ReservationController")
@AutoConfigureMockMvc
@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("[POST] 예약 생성 - 정상 호출")
    @Test
    @WithMockUser(username = "user@example.com", roles = {"CUSTOMER"})
    void 예약_생성_성공() throws Exception {
        LocalDate localDate = LocalDate.parse("2024-10-07");
        CreateReservationRequest request = new CreateReservationRequest(
                UUID.randomUUID(), 1L, 2L, localDate, 20, 0);
        TimeSlotDto timeSlotDto = createTimeSlotDto();
        ReservationDto reservationDto = createReservationDto(timeSlotDto);
        given(reservationService.createReservation(any(CreateReservationDto.class)))
                .willReturn(reservationDto);

        // POST 요청 수행 및 결과 검증
        mvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                        .with(user("user@example.com").roles("CUSTOMER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value(RESERVATION_CREATE_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data.id").value(reservationDto.id().toString()))
                .andDo(print());

        then(reservationService).should().createReservation(any(CreateReservationDto.class));
    }

    @DisplayName("[GET] 예약 목록 조회 (페이징, 정렬) - 정상 호출")
    @Test
    @WithMockUser(username = "user@example.com", roles = {"CUSTOMER"})
    void 예약_목록_조회_성공() throws Exception {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("reservationDate").ascending());
        TimeSlotDto timeSlotDto = createTimeSlotDto();
        ReservationDto reservationDto = createReservationDto(timeSlotDto);

        given(reservationService.searchReservations(null, null, null, pageable))
                .willReturn(new PageImpl<>(List.of(reservationDto), pageable, 1));

        mvc.perform(get("/api/reservations")
                        .queryParam("page", "0")
                        .queryParam("size", "5")
                        .queryParam("sort", "reservationDate,asc")
                        .with(csrf())
                        .with(user("user@example.com").roles("CUSTOMER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value(RESERVATION_RETRIEVE_SUCCESS.getMessage()))
                .andDo(print());

        then(reservationService).should().searchReservations(null, null, null, pageable);
    }

    @DisplayName("[GET] 예약 단건 조회 - 정상 호출")
    @Test
    @WithMockUser(username = "user@example.com", roles = {"CUSTOMER"})
    void 예약_단건_조회_성공() throws Exception {
        UUID reservationId = UUID.randomUUID();
        TimeSlotDto timeSlotDto = createTimeSlotDto();
        ReservationDto reservationDto = createReservationDto(timeSlotDto);
        given(reservationService.getOneReservation(reservationId)).willReturn(reservationDto);

        mvc.perform(get("/api/reservations/{reservationId}", reservationId)
                        .with(csrf())
                        .with(user("user@example.com").roles("CUSTOMER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.reservationDate").value("2024-10-07"))
                .andExpect(jsonPath("$.data.reservationStatus").value("CHECK"))
                .andExpect(jsonPath("$.message").value(RESERVATION_RETRIEVE_SUCCESS.getMessage()))
                .andDo(print());

        then(reservationService).should().getOneReservation(reservationId);
    }

    @DisplayName("[PUT] 예약 수정 - 정상 호출")
    @Test
    @WithMockUser(username = "user@example.com", roles = {"CUSTOMER"})
    void 예약_수정_성공() throws Exception {
        UUID reservationId = UUID.randomUUID();
        LocalDate localDate = LocalDate.parse("2024-10-07");
        UpdateReservationRequest request = new UpdateReservationRequest(
                UUID.randomUUID(), 1L, 2L, ReservationStatus.CHECK, localDate, 20, 0);
        willDoNothing().given(reservationService)
                .updateReservation(eq(reservationId), any(UpdateReservationDto.class), any(AuthUserInfo.class));

        mvc.perform(put("/api/reservations/{reservationId}", reservationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                        .with(user("user@example.com").roles("CUSTOMER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value(RESERVATION_UPDATE_SUCCESS.getMessage()))
                .andDo(print());

        then(reservationService).should().updateReservation(eq(reservationId), any(UpdateReservationDto.class), any(AuthUserInfo.class));
    }

    @DisplayName("[DELETE] 예약 삭제 - 정상 호출")
    @Test
    @WithMockUser(username = "user@example.com", roles = {"CUSTOMER"})
    void 예약_삭제_성공() throws Exception {
        UUID reservationId = UUID.randomUUID();
        willDoNothing().given(reservationService)
                .deleteReservation(eq(reservationId), any(AuthUserInfo.class));

        mvc.perform(delete("/api/reservations/{reservationId}", reservationId)
                        .with(csrf())
                        .with(user("user@example.com").roles("CUSTOMER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value(RESERVATION_DELETE_SUCCESS.getMessage()))
                .andDo(print());

        then(reservationService).should().deleteReservation(eq(reservationId), any(AuthUserInfo.class));
    }

    private ReservationDto createReservationDto(TimeSlotDto timeSlotDto) {
        return ReservationDto.builder()
                .id(UUID.randomUUID())
                .timeSlotDto(timeSlotDto)
                .reservationDate(LocalDate.parse("2024-10-07"))
                .reservationTime(20)
                .customerId(1L)
                .serviceProviderId(2L)
                .reservationStatus(ReservationStatus.CHECK)
                .price(0)
                .build();
    }

    private TimeSlotDto createTimeSlotDto() {
        return TimeSlotDto.builder()
                .id(UUID.randomUUID())
                .availableDate(LocalDate.parse("2024-10-07"))
                .serviceProviderId(2L)
                .isReserved(false)
                .availableTime(20).build();
    }
}