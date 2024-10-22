package com.tk.gg.post.domain.model;

import com.tk.gg.common.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "post_files")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    // Soft delete 처리 메서드
    public void softDelete() {
        this.isDeleted = true;
    }

    @Builder
    public PostFile(Post post, String fileUrl) {
        this.post = post;
        this.fileUrl = fileUrl;
    }
}