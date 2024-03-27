package name.lattuada.trading.tests;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;
@Component
public final class RestUtility {

    private final HttpHeaders headers;
    private final String baseUrl;
    private final RestTemplate restTemplate;

    public RestUtility(@Value("${base.url}") String baseUrl) {
        this.baseUrl = baseUrl;
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate = new RestTemplate();
    }

    protected <T> T get(String uri, Class<T> valueType) {
        return restTemplate.getForObject(getUrl(uri), valueType);
    }

    protected <T, B> T post(String uri, B body, Class<T> bodyType) {
        HttpEntity<B> httpEntity = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(getUrl(uri), httpEntity, bodyType);
    }

    private String getUrl(String uri) {
        return baseUrl + (uri.startsWith("/") ? "" : '/') + uri;
    }

}
