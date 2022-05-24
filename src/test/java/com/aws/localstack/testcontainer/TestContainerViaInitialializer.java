package com.aws.localstack.testcontainer;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.lifecycle.Startables;

import java.nio.file.Paths;

@SpringBootTest
@ContextConfiguration(initializers = TestContainerViaInitialializer.TestInitializer.class)
public class TestContainerViaInitialializer {


    static class TestInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        static LocalStackContainer localStackContainer = new LocalStackContainer()
                .withServices(LocalStackContainer.Service.S3)
                .withExposedPorts(4572)
                .withAccessToHost(true);

       /* GenericContainer<?> container = new GenericContainer<>("localstack/localstack")
                .withExposedPorts(4572)
                .withAccessToHost(true);*/

        DockerComposeContainer dockerComposeContainer = new DockerComposeContainer(Paths.get("docker-compose.yml").toFile())
                .withLocalCompose(true)
                .withExposedService("s3", 4572);

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            localStackContainer.start();
//            Startables.deepStart();     // to run more than 1 containers in parallel

            TestPropertyValues.of(
                "aws.localstack.s3Uri", localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.S3).getServiceEndpoint(),
                "aws.localstack.accessKey", localStackContainer.getAccessKey(),
                "aws.localstack.secretKey", localStackContainer.getSecretKey(),
                "aws.localstack.region", localStackContainer.getRegion()
            ).applyTo(applicationContext);
        }
    }
}
