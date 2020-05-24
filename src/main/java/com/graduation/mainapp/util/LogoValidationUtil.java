package com.graduation.mainapp.util;

import com.graduation.mainapp.exception.InvalidLogoException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class LogoValidationUtil {

    public static void validateLogoFormat(MultipartFile logo) throws InvalidLogoException {
        String fileName = logo.getOriginalFilename();
        int dotIndex = Objects.requireNonNull(fileName).lastIndexOf('.');
        String extension = fileName.substring(dotIndex + 1);
        if (!extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("png")) {
            throw new InvalidLogoException("Invalid image format");
        }
    }

}
