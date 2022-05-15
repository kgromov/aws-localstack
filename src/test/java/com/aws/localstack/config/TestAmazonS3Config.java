package com.aws.localstack.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.localstack.LocalStackContainer;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Profile("test")
@TestConfiguration
public class TestAmazonS3Config {

    @Bean
    public S3Client s3Client(LocalStackContainer localStackContainer) {
        String s3Uri = localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.S3).getServiceEndpoint();
        String accessKey = localStackContainer.getAccessKey();
        String secretKey = localStackContainer.getSecretKey();
        String region = localStackContainer.getRegion();

        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        AwsCredentialsProvider credentialsProvider = AwsCredentialsProviderChain.of(StaticCredentialsProvider.create(awsCredentials));

        return S3Client.builder()
                .endpointOverride(URI.create(s3Uri))
                .region(Region.of(region.toUpperCase()))
                .credentialsProvider(credentialsProvider)
                .build();
    }
}
