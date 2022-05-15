package com.aws.localstack.testcontainer;


import com.aws.localstack.services.AmazonS3Service;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.localstack.LocalStackContainer;

// TODO: does not work - all properties from testcontainer are empty
@Slf4j
@SpringBootTest
@ContextConfiguration(initializers = BaseTest.TestContainerInitializer.class)
public class BaseTest {
    private static LocalStackContainer localStackContainer;

    static {
        localStackContainer = new LocalStackContainer()
                .withServices(LocalStackContainer.Service.S3)
                .withExposedPorts(4572, 4572)
//                .withAccessToHost(true)
                .withEnv("HOSTNAME_EXTERNAL", "localhost")
                .withEnv("PORT_WEB_UI", "8082")
                .withEnv("DOCKER_HOST", "unix:///var/run/docker.sock")
                .withEnv("AWS_DEFAULT_REGION", "us-east-1")
                .withEnv("AWS_ACCESS_KEY_ID", "MY-SECRET-KEY")
                .withEnv("AWS_SECRET_ACCESS_KEY", "MY-SECRET-KEY")
                .withPrivilegedMode(true);
        localStackContainer.start();
        log.info("s3 port = {}", localStackContainer.getMappedPort(4572));
    }

    @Autowired
    private AmazonS3Service amazonS3Service;

    @Test
    void testS3Service_createBucket() {
        amazonS3Service.createBucket("test-with-tc");
        int a = 1;
    }

    @AfterAll
    public static void tearDown() {
        localStackContainer.stop();
    }

    static class TestContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues testPropertyValues = TestPropertyValues.of(
                    "aws.localstack.s3Uri", localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.S3)
                            .getServiceEndpoint(),
                    "aws.localstack.accessKey", localStackContainer.getAccessKey(),
                    "aws.localstack.secretKey", localStackContainer.getSecretKey(),
                    "aws.localstack.region", localStackContainer.getRegion()
            );
            testPropertyValues.applyTo(applicationContext);
        }
    }
}
