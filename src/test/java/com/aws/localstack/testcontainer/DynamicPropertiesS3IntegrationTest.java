package com.aws.localstack.testcontainer;

import com.aws.localstack.services.AmazonS3Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;

import java.nio.file.Path;


@SpringBootTest
public class DynamicPropertiesS3IntegrationTest {
    private static final LocalStackContainer localStackContainer = new LocalStackContainer()
            .withServices(LocalStackContainer.Service.S3)
            .withExposedPorts(4572)
            .withAccessToHost(true);

    static {
        localStackContainer.start();
    }

    @DynamicPropertySource()
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("aws.localstack.s3Uri",
                localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.S3)::getServiceEndpoint);
        registry.add("aws.localstack.accessKey", localStackContainer::getAccessKey);
        registry.add("aws.localstack.secretKey", localStackContainer::getSecretKey);
        registry.add("aws.localstack.region", localStackContainer::getRegion);
    }

    @Autowired
    private AmazonS3Service amazonS3Service;

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
}
