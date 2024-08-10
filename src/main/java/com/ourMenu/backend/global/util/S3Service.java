package com.ourMenu.backend.global.util;

import com.ourMenu.backend.domain.menulist.exception.ImageLoadException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${spring.aws.s3.bucket-name}")
    private String bucketName;

    // 파일 해시 생성
    public String generateFileHash(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] fileBytes = file.getBytes();
        byte[] hashBytes = digest.digest(fileBytes);
        return Base64.getUrlEncoder().encodeToString(hashBytes);
    }

    //FileKey가 동일한 파일이 S3 내에 존재하는지 확인
    public boolean doesObjectExist(String fileKey) {
        try {
            s3Client.headObject(HeadObjectRequest.builder().bucket(bucketName).key(fileKey).build());
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    // S3 내에 존재하는 경우 해당 URL 반환
    public String getExistingFileUrl(String fileKey) {
        return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(fileKey)).toExternalForm();
    }


    // 이미지 업로드
    public String uploadFile(String fileKey, MultipartFile file) {
        try {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileKey)
                            .build(),
                    RequestBody.fromBytes(file.getBytes()));

            return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(fileKey)).toExternalForm();
        }catch (Exception e){
            throw new ImageLoadException();
        }
    }


    // 이미지 삭제
    public void deleteFile(String fileKey) {
        s3Client.deleteObject(builder -> builder.bucket(bucketName).key(fileKey));
    }
}
