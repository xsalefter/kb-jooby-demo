package demo.killbill;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.ServerSocket;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DemoAppTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  private DemoApp app;
  private HttpClient client;
  private String baseUrl;

  @Before
  public void setUp() {
    int port = findFreePort();
    app = new DemoApp();
    app.start(
        "application.host=127.0.0.1",
        "application.port=" + port,
        "server.join=false",
        "application.env=test");
    client = HttpClient.newHttpClient();
    baseUrl = "http://127.0.0.1:" + port;
  }

  @After
  public void tearDown() {
    if (app != null) {
      app.stop();
    }
  }

  @Test
  public void shouldServeHtmlPage() throws Exception {
    HttpResponse<String> response = send("GET", "/", null, "text/html");
    assertEquals(200, response.statusCode());
    assertTrue(response.body().contains("Kill Bill Jooby Demo"));
    assertTrue(response.body().contains("ported killbill-jooby module"));
  }

  @Test
  public void shouldHandleCrudOperations() throws Exception {
    verifyCrudFlow("/api/items");
  }

  @Test
  public void shouldHandleJaxRsLayeredCrudOperations() throws Exception {
    verifyCrudFlow("/jaxrs/items");
  }

  @Test
  public void shouldStreamServerSentEvents() throws Exception {
    HttpResponse<String> response = send("GET", "/api/items/events", null, "text/event-stream");
    assertEquals(200, response.statusCode());
    assertTrue(response.headers().firstValue("Content-Type").orElse("")
        .toLowerCase().contains("text/event-stream"));
    assertTrue(response.body().contains("event: status") || response.body().contains("event:status"));
    assertTrue(response.body().contains("id: 1") || response.body().contains("id:1"));
    assertTrue(response.body().contains("data: stream-started") || response.body().contains("data:stream-started"));
    assertTrue(response.body().contains("event: message") || response.body().contains("event:message"));
    assertTrue(response.body().contains("id: 2") || response.body().contains("id:2"));
    assertTrue(response.body().contains("data: items-endpoint-ready") || response.body().contains("data:items-endpoint-ready"));
  }

  private void verifyCrudFlow(final String basePath) throws Exception {
    long itemId;

    HttpResponse<String> createResponse = send(
        "POST",
        basePath,
        "{\"name\":\"first\",\"description\":\"created\"}");
    assertEquals(201, createResponse.statusCode());
    Map<String, Object> created = parseObject(createResponse.body());
    itemId = ((Number) created.get("id")).longValue();
    assertEquals("first", created.get("name"));
    assertEquals("created", created.get("description"));

    HttpResponse<String> getResponse = send("GET", basePath + "/" + itemId, null);
    assertEquals(200, getResponse.statusCode());
    Map<String, Object> item = parseObject(getResponse.body());
    assertEquals(itemId, ((Number) item.get("id")).longValue());
    assertEquals("first", item.get("name"));

    HttpResponse<String> updateResponse = send(
        "PUT",
        basePath + "/" + itemId,
        "{\"name\":\"updated\",\"description\":\"changed\"}");
    assertEquals(200, updateResponse.statusCode());
    Map<String, Object> updated = parseObject(updateResponse.body());
    assertEquals("updated", updated.get("name"));
    assertEquals("changed", updated.get("description"));

    HttpResponse<String> listResponse = send("GET", basePath, null);
    assertEquals(200, listResponse.statusCode());
    List<Map<String, Object>> items = parseList(listResponse.body());
    assertEquals(1, items.size());
    assertEquals("updated", items.get(0).get("name"));

    HttpResponse<String> deleteResponse = send("DELETE", basePath + "/" + itemId, null);
    assertEquals(204, deleteResponse.statusCode());

    HttpResponse<String> missingResponse = send("GET", basePath + "/" + itemId, null);
    assertEquals(404, missingResponse.statusCode());
  }

  private int findFreePort() {
    try (ServerSocket socket = new ServerSocket(0)) {
      return socket.getLocalPort();
    } catch (IOException e) {
      throw new IllegalStateException("Unable to allocate a test port", e);
    }
  }

  private Map<String, Object> parseObject(final String json) throws Exception {
    return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
    });
  }

  private List<Map<String, Object>> parseList(final String json) throws Exception {
    return objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {
    });
  }

  private HttpResponse<String> send(final String method, final String path, final String body)
      throws Exception {
    return send(method, path, body, "application/json");
  }

  private HttpResponse<String> send(final String method, final String path, final String body,
      final String accept)
      throws Exception {
    HttpRequest.Builder builder = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + path))
        .header("Accept", accept);

    if (body != null) {
      builder.header("Content-Type", "application/json");
    }

    if ("GET".equals(method)) {
      builder.GET();
    } else if ("POST".equals(method)) {
      builder.POST(HttpRequest.BodyPublishers.ofString(body));
    } else if ("PUT".equals(method)) {
      builder.PUT(HttpRequest.BodyPublishers.ofString(body));
    } else if ("DELETE".equals(method)) {
      builder.DELETE();
    } else {
      throw new IllegalArgumentException("Unsupported method: " + method);
    }

    return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
  }
}
