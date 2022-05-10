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
        String bucketName = "my-test";
        amazonS3Service.createBucket(bucketName);
        Path pathToFile = Path.of("/home/konstantin/Desktop/hello-s3.txt");
        amazonS3Service.uploadFile(bucketName, pathToFile);
        byte[] content = amazonS3Service.readFile(bucketName, pathToFile.toFile().getName());
        System.out.println(new String(content));
    }
}
