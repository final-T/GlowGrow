package com.tk.gg.users.application.datainitializer;

import com.tk.gg.users.domain.model.Grade;
import com.tk.gg.users.domain.repository.grade.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GradeDataInitializer implements CommandLineRunner {

    private final GradeRepository gradeRepository;

    @Override
    public void run(String... args) throws Exception {
        saveGrade();
    }

    private void saveGrade(){
        if (gradeRepository.count() == 0) { // 이미 데이터가 있으면 실행 안 함
            gradeRepository.save(Grade.of(UUID.randomUUID(), "씨앗", 0.0, 20.0, "/images/seed.png", "초기 단계"));
            gradeRepository.save(Grade.of(UUID.randomUUID(), "새싹", 21.0, 40.0, "/images/sprout.png", "성장 중인 단계"));
            gradeRepository.save(Grade.of(UUID.randomUUID(), "꽃봉오리", 41.0, 60.0, "/images/bud.png", "중간 단계"));
            gradeRepository.save(Grade.of(UUID.randomUUID(), "반쯤 핀 꽃", 61.0, 80.0, "/images/half_bloom.png", "거의 만개한 단계"));
            gradeRepository.save(Grade.of(UUID.randomUUID(), "만개한 꽃", 81.0, 100.0, "/images/full_bloom.png", "최종 단계"));
        }
    }

}
