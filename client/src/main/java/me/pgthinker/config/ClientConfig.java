package me.pgthinker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.UUID;

/**
 * @Project: me.pgthinker.config
 * @Author: De Ning
 * @Date: 2024/10/7 18:57
 * @Description:
 */
@Configuration("clientConfig")
@ConfigurationProperties(prefix = "client")
@Data
public class ClientConfig {
    private List<ProxyConfig> proxies;
    private String serverHost;
    private Integer serverPort;
    private String password;
    private String clientId = UUID.randomUUID().toString();
}
