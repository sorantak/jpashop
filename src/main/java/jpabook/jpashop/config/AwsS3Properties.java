package jpabook.jpashop.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Configuration;

// 1
@Getter
@AllArgsConstructor
@Configuration("cloud.aws")
@ConstructorBinding
public class AwsS3Properties {

    private Credential credentials;
    private S3 s3;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.credentials.session.durationMinute.raonk}")
    private int raonk;

    @Getter
    @AllArgsConstructor
    public static class Credential{
        private String profileName;
    }

    @Getter
    @AllArgsConstructor
    public static class S3{
        private String imgBucket;
        private String rootDirectory;
    }
}
