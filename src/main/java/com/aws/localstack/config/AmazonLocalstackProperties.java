package com.aws.localstack.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.net.URI;

@Data
@ConfigurationProperties(prefix = "aws.localstack")
public class AmazonLocalstackProperties {
    private String accessKey;
    private String secretKey;
    private String region;
    private URI s3Uri;
    @Autowired private Environment environment;

    @PostConstruct
    public void init() {
        int a =1;
    }
}
