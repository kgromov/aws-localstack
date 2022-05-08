package com.aws.localstack;

import com.aws.localstack.config.AmazonLocalstackProperties;
import com.aws.localstack.services.AmazonS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.nio.file.Path;

@EnableConfigurationProperties(AmazonLocalstackProperties.class)
@SpringBootApplication
public class AwsLocalstackApplication implements CommandLineRunner {
    @Autowired private AmazonS3Service amazonS3Service;

    public static void main(String[] args) {
        SpringApplication.run(AwsLocalstackApplication.class, args);
    }

    @Override
    public void run(String... args) {
        amazonS3Service.createBucket("my-test");
        amazonS3Service.uploadFile("my-test", Path.of("~/Desktop/hello-s3.txt"));
    }
}
