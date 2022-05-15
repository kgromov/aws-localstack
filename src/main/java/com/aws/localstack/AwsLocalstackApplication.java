package com.aws.localstack;

import com.aws.localstack.config.AmazonLocalstackProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AmazonLocalstackProperties.class)
@SpringBootApplication
public class AwsLocalstackApplication {

    public static void main(String[] args) {
        SpringApplication.run(AwsLocalstackApplication.class, args);
    }

}
