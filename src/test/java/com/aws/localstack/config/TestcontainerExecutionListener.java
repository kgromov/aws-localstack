package com.aws.localstack.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.testcontainers.containers.localstack.LocalStackContainer;

@Slf4j
public class TestcontainerExecutionListener implements TestExecutionListener, Ordered {

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        LocalStackContainer localStackContainer = testContext.getApplicationContext().getBean(LocalStackContainer.class);
        log.info("Local container info {}", localStackContainer.toString());
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        LocalStackContainer localStackContainer = testContext.getApplicationContext().getBean(LocalStackContainer.class);
        localStackContainer.stop();
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
