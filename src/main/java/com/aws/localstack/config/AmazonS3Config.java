package com.aws.localstack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Profile("!test")
@Configuration
public class AmazonS3Config {

    @Bean
    public S3Client s3Client(AmazonLocalstackProperties properties) {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(properties.getAccessKey(), properties.getSecretKey());
        AwsCredentialsProvider credentialsProvider = AwsCredentialsProviderChain.of(StaticCredentialsProvider.create(awsCredentials));

        return S3Client.builder()
                .endpointOverride(properties.getS3Uri())
                .region(Region.of(properties.getRegion().toUpperCase()))
                .credentialsProvider(credentialsProvider)
                .build();
    }
}
