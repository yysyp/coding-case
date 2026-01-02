package ps.demo.jpademo.config;

import com.alibaba.excel.util.StringUtils;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.net.*;
import java.util.List;

@Slf4j
@Data
@Profile({"poxy"})
@Configuration
@ConfigurationProperties(prefix = "http")
public class ProxyConfig {

    private String proxyHost;
    private String proxyPort;

    @PostConstruct
    public void setHttpProxy() {
        ProxySelector.setDefault(new ProxySelector() {
            @Override
            public List<Proxy> select(URI uri) {

                return List.of(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, Integer.parseInt(proxyPort))));
                //return List.of(Proxy.NO_PROXY);
            }

            @Override
            public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
                throw new RuntimeException("Proxy connecting failed");
            }
        });
    }
}
