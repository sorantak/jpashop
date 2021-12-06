package jpabook.jpashop.config;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 3
@Configuration
public class AwsS3Config {

    @Autowired
    private AwsS3Properties awsS3Properties;

    @Bean
    public AWSSecurityTokenService awsSecurityTokenService() {
        return AWSSecurityTokenServiceClientBuilder.standard()
                .withCredentials(new ProfileCredentialsProvider(awsS3Properties.getCredentials().getProfileName()))
                .withRegion(awsS3Properties.getRegion())
                .build();
    }
}
