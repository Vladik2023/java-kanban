package http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String url;
    private final HttpClient httpClient;
    private final String apiToken;

    public KVTaskClient(String url) {
        this.httpClient = HttpClient.newHttpClient();
        this.url = url;
        this.apiToken = register(url);
    }

    private String register(String url) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url + "/register"))
                    .build();

            HttpResponse<String> httpResponse = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 200) {
                return httpResponse.body();
            } else {
                throw new RequestFailedException("Запрос не обработан, код состояния - " + httpResponse.statusCode());
            }
        } catch (InterruptedException | IOException e) {
            throw new RequestFailedException("Ошибка в обработке запроса" + e.getMessage());
        }
    }

    public void save(String key, String value) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(value))
                    .uri(URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken))
                    .build();

            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() != 200) {
                throw new RequestFailedException("Запрос не обработан, код состояния - " + httpResponse.statusCode());
            }
        } catch (InterruptedException | IOException e) {
            throw new RequestFailedException("Ошибка в обработке запроса" + e.getMessage());
        }
    }

    public String load(String key) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url + "/load/" + key + "?API_TOKEN=" + apiToken))
                    .build();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() == 200) {
                return httpResponse.body();
            } else {
                throw new RequestFailedException("Запрос не обработан, код состояния - " + httpResponse.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RequestFailedException("Ошибка в обработке запроса" + e.getMessage());
        }
    }

}
