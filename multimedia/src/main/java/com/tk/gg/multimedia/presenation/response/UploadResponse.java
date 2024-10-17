package com.tk.gg.multimedia.presenation.response;

import com.tk.gg.multimedia.application.dto.MultimediaDto;

import java.util.UUID;

public record UploadResponse(
        UUID multimediaId,
        String uploadUrl
) {
    public static UploadResponse from(MultimediaDto dto){
        return new UploadResponse(dto.multiMediaId(), dto.multiMediaUrl());
    }
}
