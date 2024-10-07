package me.pgthinker.config;

import lombok.*;
import me.pgthinker.enums.ProtocolEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Project: me.pgthinker.config
 * @Author: De Ning
 * @Date: 2024/10/7 18:44
 * @Description:
 */
@Configuration
@ConfigurationProperties(prefix = "proxy")
@Data
public class ProxyConfig {
    /**
     * 内网需要代理的Host
     */
    private String host;
    /**
     * 内网需要代理的Host端口
     */
    private Integer port;
    /**
     * 内网需要代理的协议
     */
    private ProtocolEnum protocol;
    /**
     * 代理后需要在服务端映射的公网端口
     */
    private Integer openPort;
}
