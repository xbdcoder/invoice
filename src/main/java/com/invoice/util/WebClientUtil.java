package com.invoice.util;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class WebClientUtil {

    private final WebClient webClient;

    public WebClientUtil(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    /**
     * Makes an HTTP request using WebClient with headers and body.
     *
     * @param method       HTTP method (GET, POST, etc.)
     * @param url          Target URL
     * @param queryParams  Map of query parameters
     * @param headers      Map of request headers
     * @param body         Request body (JSON payload)
     * @param responseType Response type class
     * @return Mono of the specified response type
     */
    public <T> Mono<T> makeRequest(
            String method,
            String url,
            Map<String, String> queryParams,
            Map<String, String> headers,
            Object body,
            Class<T> responseType
    ) {
        WebClient.RequestBodySpec request = webClient.method(HttpMethod.valueOf(method.toUpperCase())).uri(uriBuilder -> {
            uriBuilder.path(url);
            if (queryParams != null) {
                queryParams.forEach(uriBuilder::queryParam);
            }
            return uriBuilder.build();
        });

        // Add headers
        if (headers != null) {
            headers.forEach(request::header);
        }

        // Add body if not null
        if (body != null) {
            request.bodyValue(body);
        }

        return request
                .retrieve()
                .bodyToMono(responseType);
    }
}
