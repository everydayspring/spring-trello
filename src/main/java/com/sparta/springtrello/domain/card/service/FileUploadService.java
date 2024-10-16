package com.sparta.springtrello.domain.card.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class FileUploadService {

    private final S3Client s3Client;
    private final String bucketName = "spring-trello-project-team-spring"; // S3 버킷 이름

    public FileUploadService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        // 파일 검증 (크기와 형식)
        validateFile(file);

        // 고유한 파일 이름 생성
        String fileName = generateFileName(file);

        try {
            // S3에 파일 업로드
            s3Client.putObject(
                    PutObjectRequest.builder().bucket(bucketName).key(fileName).build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (S3Exception e) {
            throw new IllegalStateException("Failed to upload the file to S3", e);
        }

        return fileName;
    }

    // 파일 검증 로직
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        // 파일 크기 제한 (5MB 이하)
        if (file.getSize() > 5 * 1024 * 1024) { // 5MB 제한
            throw new IllegalArgumentException("파일 크기는 최대 5MB입니다.");
        }

        // 지원되는 MIME 타입 목록
        String contentType = file.getContentType();
        if (!isSupportedContentType(contentType)) {
            throw new IllegalArgumentException("지원되지 않는 파일 형식입니다.");
        }
    }

    // 지원되는 파일 형식 확인
    private boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/jpeg")
                || contentType.equals("image/png")
                || contentType.equals("application/pdf")
                || contentType.equals("text/csv");
    }

    // 고유한 파일 이름 생성
    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
    }
}
