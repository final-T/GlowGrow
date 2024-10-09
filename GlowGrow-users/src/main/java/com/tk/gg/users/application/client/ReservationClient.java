package com.tk.gg.users.application.client;

import com.tk.gg.common.response.GlobalResponse;
import com.tk.gg.users.presenation.response.ResultGradeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "reservation-service")
public interface ReservationClient {

    @GetMapping("api/grades")
    GlobalResponse<ResultGradeDto> getTotalGradeForUser();
}
