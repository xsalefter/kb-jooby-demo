package demo.killbill;

import org.jooby.Request;
import org.jooby.mvc.Body;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.PUT;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

import jakarta.inject.Inject;

@Path("/api/items/{id}")
@Produces("application/json")
public class ItemUpdateEndpoint {

  private final ItemService itemService;

  @Inject
  public ItemUpdateEndpoint(final ItemService itemService) {
    this.itemService = itemService;
  }

  @PUT
  @Consumes("application/json")
  public Item update(final Request request, final @Body ItemPayload payload) {
    long id = request.param("id").longValue();
    return itemService.update(id, payload);
  }
}
