package com.aws.localstack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AmazonS3Config {

    @Bean
    public S3Client s3Client(AmazonLocalstackProperties properties) {
        Region region = Region.US_EAST_1;
        /*S3Presigner presigner = S3Presigner.builder()
                .region(region)
                .build();*/
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create("aaa", "bbb");
        AwsCredentialsProviderChain credentialsProvider = AwsCredentialsProviderChain.of(StaticCredentialsProvider.create(awsCredentials));

        return S3Client.builder()
                .endpointOverride(properties.getUri())
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();
    }
}
