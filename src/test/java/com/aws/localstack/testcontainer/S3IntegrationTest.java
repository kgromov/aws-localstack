package com.aws.localstack.testcontainer;

import com.aws.localstack.config.LocalstackConfig;
import com.aws.localstack.config.AmazonS3Config;
import com.aws.localstack.services.AmazonS3Service;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.localstack.LocalStackContainer;

@SpringBootTest
@Import({LocalstackConfig.class, AmazonS3Config.class})
public class S3IntegrationTest {
    @Autowired private AmazonS3Service amazonS3Service;
    @Autowired private LocalStackContainer localStackContainer;

    @Test
    void testS3Service_createBucket() {
        amazonS3Service.createBucket("test-with-tc");
        int a = 1;
    }

    @AfterEach
    void tearDown() {
        localStackContainer.stop();
    }
}
