package com.wanted.preonboardingbackend.global.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableEncryptableProperties
public class JasyptConfig {

    // VM options에서 추가한 Secret Key 설정 값을 읽어서 변수로 사용
    @Value("${jasypt.encryptor.password}")
    private String key;

    @Bean
    public PooledPBEStringEncryptor jasyptStringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setPoolSize(8); // 코어 수
        encryptor.setPassword(key);
        encryptor.setAlgorithm("PBEWithMD5AndDES"); // 암호화 알고리즘

        return encryptor;
    }
}
