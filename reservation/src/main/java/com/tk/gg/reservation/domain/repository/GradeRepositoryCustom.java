package com.tk.gg.reservation.domain.repository;

import com.tk.gg.common.enums.UserRole;
import com.tk.gg.reservation.application.dto.CustomerGradeDto;
import com.tk.gg.reservation.application.dto.ProviderGradeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GradeRepositoryCustom {
    Page<CustomerGradeDto> getCustomerGradesByUserInfo(Long userId, Pageable pageable);
    Page<ProviderGradeDto> getProviderGradesByUserInfo(Long userId, Pageable pageable);
}
