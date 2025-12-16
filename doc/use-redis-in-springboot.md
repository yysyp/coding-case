without cert, you can just confing in the spring.data.redis.xxx
otherwise you need to customize the config to load ca-cert.pem as below:

public class RedisConfiguration {
public RedisConnectionFactory jedisConnectionFactory()
RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
config. setHostName(host);
config. setPort(port);
config. setDatabase(database);
config. setPassword(password);
if (isSsl) {
log. info(" enabled ssl for redis: {}, port: {}, isSsl: {} ", host, port, isSsL);
CertificateFactory cf = CertificateFactory. getInstance(type: "X.509");
InputStream inputStream = new FileInputStream(certPath);
X509Certificate x509Certificate = (X509Certificate) cf. generateCertificate(inputStream);
TrustManagerFactory tmf = TrustManagerFactory. getInstance(TrustManagerFactory. getDefavLtAlgorithm
KeyStore ks = KeyStore. getInstance(KeyStore. getDefavltType());
ks. load(param: null);
ks. setCertificateEntry(alias: ca", x509Certificate);
tmf. init(ks);
SSLContext sslContext = SSLContext. getInstance(protocol: "TLS");
sslContext. init(km: null, tmf. getTrustManagers(), random: null);
JedisClientConfiguration. JedisClientConfigurationBuilder builder = JedisClientConfiguration. builder(). useSsl(). sslSocketFactory(sslContext. getSocketFactory())
. and();
return new JedisConnectionFactory(config, builder. build());
}
return new JedisConnectionFactory(config);
