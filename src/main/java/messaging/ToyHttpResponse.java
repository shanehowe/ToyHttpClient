package messaging;

public record ToyHttpResponse(
    int statusCode, String statusText, ToyHttpHeaders headers, String body) {}
