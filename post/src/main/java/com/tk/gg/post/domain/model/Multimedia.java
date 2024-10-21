package com.tk.gg.post.domain.model;

import com.tk.gg.common.jpa.BaseEntity;
import com.tk.gg.post.domain.type.FileType;
import com.tk.gg.security.user.AuthUserInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_multimedias_remove")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Multimedia extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "multi_media_id", updatable = false, nullable = false)
    private UUID multiMediaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "multi_media_url", nullable = false)
    private String multiMediaUrl;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_type", nullable = false)
    private FileType fileType;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    //소프트 삭제 메서드
    public void softDelete(AuthUserInfo authUserInfo){
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = String.valueOf(authUserInfo.getId());
    }

    @Builder(builderClassName = "CreateMultimediaBuilder", builderMethodName = "createMultimediaBuilder")
    public Multimedia(Post post, String multiMediaUrl, String fileName, Long fileSize, FileType fileType, AuthUserInfo authUserInfo) {
        this.post = post;
        this.userId = authUserInfo.getId();
        this.multiMediaUrl = multiMediaUrl;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
    }

}
