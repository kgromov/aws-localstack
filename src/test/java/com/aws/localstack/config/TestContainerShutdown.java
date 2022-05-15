package com.aws.localstack.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.localstack.LocalStackContainer;

import javax.annotation.PreDestroy;

@Profile("test")
@TestConfiguration
@RequiredArgsConstructor
public class TestContainerShutdown {
    private final LocalStackContainer localStackContainer;

    @PreDestroy
    public void stopTestContainer(){
        localStackContainer.stop();
    }
}
