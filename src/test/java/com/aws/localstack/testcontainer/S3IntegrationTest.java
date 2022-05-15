package com.aws.localstack.testcontainer;

import com.aws.localstack.config.LocalstackConfig;
import com.aws.localstack.config.TestAmazonS3Config;
import com.aws.localstack.config.TestContainerShutdown;
import com.aws.localstack.services.AmazonS3Service;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.testcontainers.containers.localstack.LocalStackContainer;

import java.nio.file.Path;

@ActiveProfiles("test")
@SpringBootTest
@Import({LocalstackConfig.class, TestAmazonS3Config.class, TestContainerShutdown.class})
public class S3IntegrationTest {
    @Autowired private AmazonS3Service amazonS3Service;
    @Autowired private LocalStackContainer localStackContainer;

    @Test
    void testS3Service_createBucket() {
        String bucketName = "test-with-tc";
        amazonS3Service.createBucket(bucketName);
    }

    @Test
    void testS3Service_uploadFile() {
        String bucketName = "test-with-tc";
        Path pathToFile = Path.of("/home/konstantin/Desktop/hello-s3.txt");
        String fileKey = pathToFile.toFile().getName();
        amazonS3Service.uploadFile(bucketName, pathToFile);
        byte[] content = amazonS3Service.readFile(bucketName, fileKey);
        System.out.println(new String(content));
    }

//    @AfterTestClass
}
