package com.aws.localstack.testcontainer;

import com.aws.localstack.services.AmazonS3Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.lifecycle.Startables;

import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
@ContextConfiguration(initializers = TestContainerViaInitialializer.TestInitializer.class)
public class TestContainerViaInitialializer {
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

    static class TestInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        static LocalStackContainer container = new LocalStackContainer()
                .withServices(LocalStackContainer.Service.S3)
                .withExposedPorts(4572)
                .withAccessToHost(true);

        // not stable - smth works/smth not
        /*static GenericContainer<?> container = new GenericContainer<>("localstack/localstack")
                .withExposedPorts(4572)
                .withAccessToHost(true);*/

        // does not work - container stops
        /*static DockerComposeContainer container = new DockerComposeContainer(Paths.get("docker-compose.yml").toFile())
                .withLocalCompose(true)
                .withExposedService("s3", 4572);*/


        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            container.start();
//            Startables.deepStart(containers);     // to run more than 1 containers in parallel

            TestPropertyValues.of(
                    "aws.localstack.s3Uri=" + container.getEndpointConfiguration(LocalStackContainer.Service.S3).getServiceEndpoint(),
                    "aws.localstack.accessKey=" + container.getAccessKey(),
                    "aws.localstack.secretKey=" + container.getSecretKey(),
                    "aws.localstack.region=" + container.getRegion()
            ).applyTo(applicationContext);
        }
    }
}
