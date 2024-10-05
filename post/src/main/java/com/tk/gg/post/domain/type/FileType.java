package com.tk.gg.post.domain.type;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
public enum FileType {
    JPG(List.of("jpg", "jpeg"), "image/jpeg"),
    PNG(List.of("png"), "image/png"),
    MP4(List.of("mp4"), "video/mp4"),
    AVI(List.of("avi"), "video/x-msvideo"),
    MOV(List.of("mov"), "video/quicktime");

    final List<String> key;
    final String contentType;

    FileType(List<String> key, String contentType) {
        this.key = key;
        this.contentType = contentType;
    }

    public static Optional<FileType> findByKey(String key) {
        return Arrays.stream(FileType.values())
                .filter(extension -> extension.key.contains(key))
                .findAny();
    }

    public String getKey() {
        return this.key.get(0);
    }

    public boolean isVideo() {
        return this == MP4 || this == AVI || this == MOV;
    }

    public boolean isImage() {
        return this == JPG || this == PNG;
    }
}
