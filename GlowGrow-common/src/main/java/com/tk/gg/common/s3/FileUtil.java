package com.tk.gg.common.s3;

/**
 * 파일 유틸리티 클래스.
 * 파일 이름에서 확장자를 추출하는 기능을 제공합니다.
 */
public class FileUtil {
    public static String[] splitFileName(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return new String[]{fileName, ""}; // 확장자가 없는 경우
        }

        String name = fileName.substring(0, lastDotIndex);
        String extension = fileName.substring(lastDotIndex + 1);
        return new String[]{name, extension};
    }
}
