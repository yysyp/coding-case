package ps.demo.commonlibx.common;

import cn.hutool.http.ssl.TrustAnyHostnameVerifier;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import ps.demo.commonlibx.config.LoggingRequestInterceptor;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class RestTemplateTool {

    private RestTemplate restTemplate;

    private RestTemplateTool() {

        try {
            this.restTemplate = initRestTemplate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static class RestTemplateToolHolder {
        private static final RestTemplateTool INSTANCE = new RestTemplateTool();
    }

    public static RestTemplateTool getInstance() {
        return RestTemplateToolHolder.INSTANCE;
    }

    public static RestTemplate getRestTemplate() {
        return RestTemplateToolHolder.INSTANCE.restTemplate;
    }

    private RestTemplate initRestTemplate() throws Exception {

        final SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustAllStrategy()).build();
        final SSLConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactoryBuilder.create().setSslContext(sslcontext)
                .setHostnameVerifier(new TrustAnyHostnameVerifier()).build();
        final HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create().setSSLSocketFactory(sslSocketFactory).build();
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).evictExpiredConnections().build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        requestFactory.setConnectionRequestTimeout(5000);
        requestFactory.setConnectTimeout(10000);
        RestTemplate restTemplate = initRestTemplate(new BufferingClientHttpRequestFactory(requestFactory));

        setCharset(restTemplate);
        return restTemplate;
    }

    private RestTemplate initRestTemplate(BufferingClientHttpRequestFactory requestFactory) {
        RestTemplate restTemplate = new RestTemplate(requestFactory); //getRequestFactory());

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new LoggingRequestInterceptor());
        restTemplate.setInterceptors(interceptors);
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                //do nothing
            }
        });
        return restTemplate;
    }

    private void setCharset(RestTemplate restTemplate) {
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        for (HttpMessageConverter messageConverter : messageConverters) {
            if (messageConverter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) messageConverter).setDefaultCharset(StandardCharsets.UTF_8);
            }
        }
    }

    //Config SSL
    public HttpComponentsClientHttpRequestFactory getRequestFactory() {
        TrustStrategy trustStrategy = (x509Certificates, s) -> true;
        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, trustStrategy).build();
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
            HttpClient httpClient = HttpClients.custom().setConnectionManager(PoolingHttpClientConnectionManagerBuilder
                    .create().setSSLSocketFactory(socketFactory).build()).build();
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);
            requestFactory.setConnectionRequestTimeout(5000);
            requestFactory.setConnectTimeout(10000);
            return requestFactory;
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new RuntimeException(e);
        }
    }


    public ResponseEntity<String> postSubmitFormMultiValueMapForStr(String url, HttpHeaders headers, MultiValueMap<String, Object> formMap) {
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {
        };
        ResponseEntity<String> responseEntity = postSubmitFormMultiValueMapForT(url, headers, formMap, responseType);
        return responseEntity;
    }

    public <T> ResponseEntity<T> postSubmitFormMultiValueMapForT(String url, HttpHeaders headers, MultiValueMap<String, Object> formMap, ParameterizedTypeReference<T> responseType) {
        if (null == headers.getContentType()) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(formMap, headers);
        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, responseType);
            return responseEntity;
        } catch (Exception e) {
            log.info("Rest call submitFormForObject error, url={}, message={}", url, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    public <T> ResponseEntity<T> postSubmitFormMultiValueMapWithUriVariableObjectsForT(String url, HttpHeaders headers, MultiValueMap<String, Object> formMap, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        if (null == headers.getContentType()) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(formMap, headers);
        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, responseType, uriVariables);
            return responseEntity;
        } catch (Exception e) {
            log.info("Rest call submitFormForObject error, url={}, message={}", url, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<String> postJsonStrForStr(String url, String jsonStr) {
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {
        };
        return postJsonStrForT(url, jsonStr, responseType);
    }

    public <T> ResponseEntity<T> postJsonStrForT(String url, String jsonStr, ParameterizedTypeReference<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        return postJsonStrForT(url, headers, jsonStr, responseType);
    }

    public <T> ResponseEntity<T> postJsonStrForT(String url, HttpHeaders headers, String jsonStr, ParameterizedTypeReference<T> responseType) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonStr, headers);
        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, responseType);
            return responseEntity;
        } catch (Exception e) {
            log.info("Rest call postJsonObjectForObject error, url={}, message={}", url, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<String> getWithUriVariableObjectsForStr(String url, Object... uriVariables) {
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {
        };
        return getWithUriVariableObjectsForT(url, responseType, uriVariables);
    }

    public <T> ResponseEntity<T> getWithUriVariableObjectsForT(String url, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        HttpHeaders headers = new HttpHeaders();
        return getWithUriVariableObjectsForT(url, headers, responseType, uriVariables);
    }

    /**
     * NOTE!!! Current RestTemplate settings has conflict with this get method, if you use
     * "RestTemplate restTemplate = new RestTemplate();" to set the "restTemplate" attribute, it will work.
     * otherwise it will throw:
     * Caused by: java.lang.UnsupportedOperationException: getBody not supported
     * at org.springframework.http.client.HttpComponentsStreamingClientHttpRequest.getBodyInternal(HttpComponentsStreamingClientHttpRequest.java:86)
     * at org.springframework.http.client.AbstractClientHttpRequest.getBody(AbstractClientHttpRequest.java:47)
     * at org.springframework.http.client.BufferingClientHttpRequestWrapper.executeInternal(BufferingClientHttpRequestWrapper.java:62)
     * at org.springframework.http.client.AbstractBufferingClientHttpRequest.executeInternal(AbstractBufferingClientHttpRequest.java:48)
     * at org.springframework.http.client.AbstractClientHttpRequest.execute(AbstractClientHttpRequest.java:53)
     * at org.springframework.http.client.InterceptingClientHttpRequest$InterceptingRequestExecution.execute(InterceptingClientHttpRequest.java:109)
     * at p.y.demo.restconfig.LogClientHttpRequestInterceptor.intercept(LogClientHttpRequestInterceptor.java:28)
     * at org.springframework.http.client.InterceptingClientHttpRequest$InterceptingRequestExecution.execute(InterceptingClientHttpRequest.java:93)
     * at org.springframework.http.client.InterceptingClientHttpRequest.executeInternal(InterceptingClientHttpRequest.java:77)
     * at org.springframework.http.client.AbstractBufferingClientHttpRequest.executeInternal(AbstractBufferingClientHttpRequest.java:48)
     * at org.springframework.http.client.AbstractClientHttpRequest.execute(AbstractClientHttpRequest.java:53)
     * at org.springframework.web.client.RestTemplate.doExecute(RestTemplate.java:739)
     * at org.springframework.web.client.RestTemplate.execute(RestTemplate.java:674)
     * at org.springframework.web.client.RestTemplate.exchange(RestTemplate.java:612)
     * at p.y.demo.restconfig.RestTemplateUtil.getForObject(RestTemplateUtil.java:85)
     *
     * @param url
     * @param headers
     * @param responseType
     * @param uriVariables
     * @param <T>
     * @return
     */
    public <T> ResponseEntity<T> getWithUriVariableObjectsForT(String url, HttpHeaders headers, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        HttpEntity<String> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    responseType, uriVariables
            );
            return responseEntity;
        } catch (Exception e) {
            log.info("Rest call getForObject error, url={}, message={}", url, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public <T> ResponseEntity<T> getWithUriVariableObjectMapForT(String url, HttpHeaders headers, ParameterizedTypeReference<T> responseType, Map uriVariableMap) {
        HttpEntity<String> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    responseType, uriVariableMap
            );
            return responseEntity;
        } catch (Exception e) {
            log.info("Rest call getForObject error, url={}, message={}", url, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<String> putBodyObjectForStr(String url, Object requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                String.class
        );

        return response;
    }

    public ResponseEntity<String> patchBodyObjectForStr(String url, Object requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<>(requestBody, headers);

        return restTemplate.exchange(
                url,
                HttpMethod.PATCH,
                request,
                String.class
        );
    }



}
