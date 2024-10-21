package com.tk.gg.multimedia.domain.model;

import com.tk.gg.common.jpa.BaseEntity;
import com.tk.gg.multimedia.domain.type.FileType;
import com.tk.gg.multimedia.domain.type.FileUploadType;
import com.tk.gg.security.user.AuthUserInfo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_multimedias")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Multimedia extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "multi_media_id", updatable = false, nullable = false)
    private UUID multiMediaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "upload_type", nullable = false)
    private FileUploadType uploadType;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "multi_media_url", nullable = false)
    private String multiMediaUrl;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_type", nullable = false)
    private FileType fileType;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    public static Multimedia create(FileUploadType fileUploadType, Long userId, String multiMediaUrl, String fileName, Long fileSize, FileType fileType) {
        return Multimedia.builder()
                .uploadType(fileUploadType)
                .userId(userId)
                .multiMediaUrl(multiMediaUrl)
                .fileName(fileName)
                .fileSize(fileSize)
                .fileType(fileType)
                .build();
    }

    //소프트 삭제 메서드
    public void softDelete(AuthUserInfo authUserInfo){
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = String.valueOf(authUserInfo.getId());
    }
}
