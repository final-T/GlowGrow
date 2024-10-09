package com.tk.gg.users.domain.service;

import com.tk.gg.common.kafka.grade.GradeForReservationEventDto;
import com.tk.gg.common.kafka.grade.GradeForReviewEventDto;
import com.tk.gg.users.domain.repository.grade.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GradeDomainService {

    private final GradeRepository gradeRepository;

    public void updateUserGradeByReservation(GradeForReservationEventDto event) {

    }

    public void updateUserGradeByReview(GradeForReviewEventDto event) {

    }
}
