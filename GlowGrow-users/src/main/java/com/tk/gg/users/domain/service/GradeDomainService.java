package com.tk.gg.users.domain.service;

import com.tk.gg.common.response.exception.GlowGlowError;
import com.tk.gg.common.response.exception.GlowGlowException;
import com.tk.gg.users.application.dto.CustomerGradeEvaluationDto;
import com.tk.gg.users.application.dto.ProviderGradeEvaluationDto;
import com.tk.gg.users.application.dto.UserDto;
import com.tk.gg.users.domain.model.Grade;
import com.tk.gg.users.domain.model.User;
import com.tk.gg.users.domain.model.UserGrade;
import com.tk.gg.users.domain.repository.grade.GradeRepository;
import com.tk.gg.users.domain.repository.grade.UserGradeRepository;
import com.tk.gg.users.domain.type.UserGradeType;
import com.tk.gg.users.presenation.response.GradeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

import static com.tk.gg.common.response.exception.GlowGlowError.USER_GRADE_NOT_AVAILABLE;

@Service
@RequiredArgsConstructor
public class GradeDomainService {

    private final GradeRepository gradeRepository;
    private final UserGradeRepository userGradeRepository;
    private final UserDomainService userDomainService;

    private final static Double SEED_PER = 0.5;
    private final static Double SPROUT_PER = 0.3;
    private final static Double BUD_PER = 0.2;
    private final static Double HALF_BLOOMED_FLOWER_PER = 0.1;
    private final static Double FULLY_BLOOMED_FLOWER_PER = 0.1;


    public void updateUserGradeByReservation(GradeResponse response) {
        UserDto userDto = userDomainService.findById(response.userId());
        if (userDto == null) {
            throw new GlowGlowException(GlowGlowError.USER_NO_EXIST);
        }


        UserGrade userGrade = userGradeRepository.findByUserUserId(userDto.userId())
                .orElse(UserGrade.of(User.of(userDto), UserGradeType.SEED, 0.0));

        Double updateScore = calculateScoreByReservation(userGrade.getUserGradeType(), userGrade.getScore());
        userGrade.updateGrade(checkUserGrantType(userGrade.getUserGradeType(), updateScore), updateScore);
    }

    public void updateUserGradeByReview(GradeResponse response) {
        UserDto userDto = userDomainService.findById(response.userId());
        if (userDto == null) {
            throw new GlowGlowException(GlowGlowError.USER_NO_EXIST);
        }

        UserGrade userGrade = userGradeRepository.findByUserUserId(userDto.userId())
                .orElse(UserGrade.of(User.of(userDto), UserGradeType.SEED, 0.0));
        Double updateScore = userGrade.getScore();

        switch (response.userType()){
            case CUSTOMER -> {
                updateScore = calculateScoreForReviewByCustomer(userGrade, response.toCustomerGradeEvaluationDto());
            }
            case PROVIDER -> {
                updateScore = calculateScoreForReviewByProvider(userGrade, response.toProviderGradeEvaluationDto());
            }
        }

        userGrade.updateGrade(checkUserGrantType(userGrade.getUserGradeType(), updateScore), updateScore);
    }

    // 예약 완료에 따른 점수 계산
    private Double calculateScoreByReservation(UserGradeType userGradeType, Double score) {
        switch (userGradeType) {
            case SEED -> {
                return score + 1;
            }
            case SPROUT -> {
                return score + 0.5;
            }
            case BUD, HALF_BLOOMED_FLOWER, FULLY_BLOOMED_FLOWER -> {
                return score + 0.1;
            }
            default -> {
                throw new GlowGlowException(USER_GRADE_NOT_AVAILABLE);
            }
        }
    }

    // 리뷰 완료에 따른 점수 계산
    private Double calculateScoreForReviewByProvider(UserGrade userGrade, ProviderGradeEvaluationDto dto) {
        Integer totalScore = dto.providerPriceSatisfaction() + dto.providerCommunication() + dto.providerProfessionalism()
                + dto.providerServiceQuality() + dto.providerPunctuality();

        return calculateScoreByReview(userGrade, totalScore);
    }


    private Double calculateScoreForReviewByCustomer(UserGrade userGrade, CustomerGradeEvaluationDto dto) {

        Integer totalScore = dto.customerCommunication() + dto.customerPunctuality() + dto.customerManners()
                + dto.customerPaymentPromptness();

        return calculateScoreByReview(userGrade, totalScore);
    }

    private Double calculateScoreByReview(UserGrade userGrade, Integer totalScore) {
        switch (userGrade.getUserGradeType()){
            case SEED -> {
                return totalScore * SEED_PER;
            }
            case SPROUT -> {
                return totalScore * SPROUT_PER;
            }
            case BUD -> {
                return totalScore * BUD_PER;
            }
            case HALF_BLOOMED_FLOWER -> {
                return totalScore * HALF_BLOOMED_FLOWER_PER;
            }
            case FULLY_BLOOMED_FLOWER -> {
                return totalScore * FULLY_BLOOMED_FLOWER_PER;
            }
            default -> {
                throw new GlowGlowException(USER_GRADE_NOT_AVAILABLE);
            }
        }
    }

    private UserGradeType checkUserGrantType(UserGradeType userGradeType, Double score) {
        Map<UserGradeType, Grade> gradeMap = gradeRepository.findAll().stream()
                .collect(Collectors.toMap(Grade::getUserGradeType, grade -> grade));

        switch (userGradeType) {
            case SEED -> {
                if (score > gradeMap.get(UserGradeType.SEED).getGradeMaxScore()) {
                    return UserGradeType.SPROUT;
                }
            }
            case SPROUT -> {
                if (score > gradeMap.get(UserGradeType.SPROUT).getGradeMaxScore()) {
                    return UserGradeType.BUD;
                } else if (score < gradeMap.get(UserGradeType.SPROUT).getGradeMinScore()) {
                    return UserGradeType.SEED;
                }
            }
            case BUD -> {
                if (score > gradeMap.get(UserGradeType.BUD).getGradeMaxScore()) {
                    return UserGradeType.HALF_BLOOMED_FLOWER;
                } else if (score < gradeMap.get(UserGradeType.BUD).getGradeMinScore()) {
                    return UserGradeType.SPROUT;
                }
            }
            case HALF_BLOOMED_FLOWER -> {
                if (score > gradeMap.get(UserGradeType.HALF_BLOOMED_FLOWER).getGradeMaxScore()) {
                    return UserGradeType.FULLY_BLOOMED_FLOWER;
                } else if (score < gradeMap.get(UserGradeType.HALF_BLOOMED_FLOWER).getGradeMinScore()) {
                    return UserGradeType.BUD;
                }
            }
            case FULLY_BLOOMED_FLOWER -> {
                if (score < gradeMap.get(UserGradeType.FULLY_BLOOMED_FLOWER).getGradeMinScore()) {
                    return UserGradeType.HALF_BLOOMED_FLOWER;
                }
            }
            default -> {
                throw new GlowGlowException(USER_GRADE_NOT_AVAILABLE);
            }
        }

        return userGradeType;
    }
}
