package demo.killbill;

import org.jooby.Jooby;
import org.jooby.MediaType;
import org.jooby.Request;
import org.jooby.Sse;

public class ItemEventsEndpoint {

  public void install(final Jooby app) {
    app.sse("/api/items/events", new ItemEventsHandler()).produces(MediaType.sse.name());
  }

  private static final class ItemEventsHandler implements Sse.Handler {

    @Override
    public void handle(final Request request, final Sse sse) throws Exception {
      sse.event("stream-started").name("status").id("1").send().get();
      sse.event("items-endpoint-ready").name("message").id("2").send().get();
      sse.close();
    }
  }
}
