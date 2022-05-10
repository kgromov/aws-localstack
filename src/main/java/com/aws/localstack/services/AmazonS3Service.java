package com.aws.localstack.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.Permission;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.nio.file.Path;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonS3Service {
    private final S3Client s3Client;

    public void createBucket(String bucketName) {
        CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                .bucket(bucketName)
                .grantFullControl(Permission.FULL_CONTROL.toString())
                .build();
        CreateBucketResponse response = s3Client.createBucket(bucketRequest);
        log.info("Create bucket {}: {}", bucketName, response.toString());
    }

    public void uploadFile(String bucketName, Path pathToFile) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(pathToFile.toFile().getName())
                .build();
        PutObjectResponse response = s3Client.putObject(objectRequest, RequestBody.fromFile(pathToFile));
        log.info("Upload file: {} on bucket {}, response: {}", pathToFile, bucketName, response.toString());
    }

    public byte[] readFile(String bucketName, String key) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        try(ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(request)) {
            return responseInputStream.readAllBytes();
        } catch (IOException e) {
            log.error("Failed to read content by bucket {} and key {}", bucketName, key, e);
            throw new RuntimeException(e);
        }
    }
}
