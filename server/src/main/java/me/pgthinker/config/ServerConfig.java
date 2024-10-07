package me.pgthinker.config;

import cn.hutool.crypto.digest.BCrypt;
import jakarta.annotation.PostConstruct;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Project: me.pgthinker.config
 * @Author: De Ning
 * @Date: 2024/10/7 19:37
 * @Description:
 */
@Configuration
@ConfigurationProperties(prefix = "server")
@Data
public class ServerConfig {
    private Integer port;
    private String password;

    @Setter(AccessLevel.NONE)
    private String encryptedPassword;

    @PostConstruct
    public void encryptPassword() {
        this.encryptedPassword = BCrypt.hashpw(this.password,BCrypt.gensalt());
    }
}
