package jpabook.jpashop.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

// 2
@Getter
@Setter
@ConfigurationProperties("cloud.aws.s3.temp.session.role")
@ConstructorBinding
public class AwsS3RoleProperties {
    private String arn;
    private String sessionName;
}
