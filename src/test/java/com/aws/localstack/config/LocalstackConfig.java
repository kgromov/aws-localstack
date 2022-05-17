package com.aws.localstack.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.localstack.LocalStackContainer;

@Slf4j
@Profile("test")
@TestConfiguration
public class LocalstackConfig {

    @Bean
    public LocalStackContainer localStackContainer() {
        LocalStackContainer localStackContainer = new LocalStackContainer()
                .withServices(LocalStackContainer.Service.S3)
                .withExposedPorts(4572, 4572)
                .withAccessToHost(true)
                .withEnv("HOSTNAME_EXTERNAL", "localhost")
                .withEnv("PORT_WEB_UI", "8082")
                .withEnv("DOCKER_HOST", "unix:///var/run/docker.sock")
                .withEnv("AWS_DEFAULT_REGION", "us-east-1")
                .withEnv("AWS_ACCESS_KEY_ID", "MY-SECRET-KEY")
                .withEnv("AWS_SECRET_ACCESS_KEY", "MY-SECRET-KEY")
                .withPrivilegedMode(true);
        localStackContainer.start();
        Integer mappedPort = localStackContainer.getMappedPort(4572);
        log.info("s3 port = {}", localStackContainer.getMappedPort(4572));
        return localStackContainer;
    }
}
