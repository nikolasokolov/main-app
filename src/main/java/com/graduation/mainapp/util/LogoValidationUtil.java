package com.graduation.mainapp.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class LogoValidationUtil {
    public static void validateLogoFormat(MultipartFile logo) throws Exception {
        String fileName = logo.getOriginalFilename();
        int dotIndex = Objects.requireNonNull(fileName).lastIndexOf('.');
        String extension = fileName.substring(dotIndex + 1);
        if (!extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("png")) {
            throw new Exception("Invalid image format");
        }
    }

}
