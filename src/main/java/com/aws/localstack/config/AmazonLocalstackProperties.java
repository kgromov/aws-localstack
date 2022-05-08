package com.aws.localstack.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@Data
@ConfigurationProperties(prefix = "aws.localstack.s3")
public class AmazonLocalstackProperties {
    private URI uri;
}
