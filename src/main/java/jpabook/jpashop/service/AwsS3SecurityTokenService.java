package jpabook.jpashop.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleResult;
import com.amazonaws.services.securitytoken.model.Credentials;
import jpabook.jpashop.config.AwsS3Properties;
import jpabook.jpashop.config.AwsS3RoleProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

// 4
@Slf4j
@RequiredArgsConstructor
@Service
public class AwsS3SecurityTokenService {

    private final AwsS3RoleProperties awsS3RoleProperties;
    private final AwsS3Properties awsS3Properties;
    private final AWSSecurityTokenService awsSecurityTokenService;
    private AmazonS3 amazonS3;
    private Credentials credentials;

    /**
     *
     * @return AmazonS3
     */
    public AmazonS3 connect() {

        Date now = new Date();

        if (credentials == null || credentials.getExpiration().getTime() <= now.getTime()) {
            AssumeRoleRequest roleRequest = new AssumeRoleRequest()
                    .withRoleArn(awsS3RoleProperties.getArn())
                    .withRoleSessionName(awsS3RoleProperties.getSessionName());

            AssumeRoleResult roleResponse = awsSecurityTokenService.assumeRole(roleRequest);
            roleRequest.setDurationSeconds(awsS3Properties.getRaonk() * 60);

            this.credentials = roleResponse.getCredentials();
            this.amazonS3 = getAmazonS3();

            Date roleTime = new Date(credentials.getExpiration().getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            log.info("********** Amazon 신규 세션 생성 완료 ::: {} 까지 유효", dateFormat.format(roleTime));

        }
        return this.amazonS3;
    }

    private AmazonS3 getAmazonS3() {
        log.info("********** Amazon 신규 세션 생성 시작 **********");
        BasicSessionCredentials basicSessionCredentials = new BasicSessionCredentials(
                this.credentials.getAccessKeyId(),
                this.credentials.getSecretAccessKey(),
                this.credentials.getSessionToken()
        );

        log.info("AccessKey ::: {}", this.credentials.getAccessKeyId());
        log.info("SecretKey ::: {}", this.credentials.getSecretAccessKey());

        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicSessionCredentials))
                .build();
    }
}
