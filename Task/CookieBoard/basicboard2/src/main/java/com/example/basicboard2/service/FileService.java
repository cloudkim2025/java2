package com.example.basicboard2.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * ✅ 파일 업로드를 처리하는 서비스 클래스.
 */
@Service // Spring의 Service 계층으로 등록
public class FileService {

    // ✅ 파일이 저장될 기본 경로 설정
    private final String UPLOADED_FOLDER = System.getProperty("user.home")
            + File.separator + "Desktop" + File.separator + "java2"
            + File.separator + "springboot" + File.separator + "uploads"
            + File.separator;

    /**
     * ✅ 파일 업로드 처리 메서드
     * @param file 업로드할 MultipartFile 객체
     * @return 저장된 파일의 전체 경로
     */
    public String fileUpLoad(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes(); // 파일 데이터를 바이트 배열로 변환
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename()); // 파일 경로 설정

            Files.write(path, bytes); // 파일 저장

            return UPLOADED_FOLDER + file.getOriginalFilename(); // 저장된 파일 경로 반환
        } catch (IOException e) {
            throw new RuntimeException(e); // 예외 발생 시 런타임 예외로 던짐
        }
    }
}
