package demo.killbill;

import java.util.List;

import org.jooby.Request;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.Status;
import org.jooby.mvc.Body;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.DELETE;
import org.jooby.mvc.GET;
import org.jooby.mvc.POST;
import org.jooby.mvc.PUT;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
@Path("/jaxrs/items")
@jakarta.ws.rs.Path("/jaxrs/items")
@Produces("application/json")
@jakarta.ws.rs.Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
public class JaxRsItemsResource {

  private final ItemService itemService;

  @Inject
  public JaxRsItemsResource(final ItemService itemService) {
    this.itemService = itemService;
  }

  @GET
  @jakarta.ws.rs.GET
  public List<Item> list() {
    return itemService.list();
  }

  @POST
  @jakarta.ws.rs.POST
  @Consumes("application/json")
  @jakarta.ws.rs.Consumes(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
  public Result create(final @Body ItemPayload payload) {
    return Results.with(itemService.create(payload)).status(Status.CREATED);
  }

  @GET
  @jakarta.ws.rs.GET
  @Path("/{id}")
  @jakarta.ws.rs.Path("/{id}")
  public Item get(final Request request) {
    long id = request.param("id").longValue();
    return itemService.get(id);
  }

  @PUT
  @jakarta.ws.rs.PUT
  @Path("/{id}")
  @jakarta.ws.rs.Path("/{id}")
  @Consumes("application/json")
  @jakarta.ws.rs.Consumes(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
  public Item update(final Request request, final @Body ItemPayload payload) {
    long id = request.param("id").longValue();
    return itemService.update(id, payload);
  }

  @DELETE
  @jakarta.ws.rs.DELETE
  @Path("/{id}")
  @jakarta.ws.rs.Path("/{id}")
  public Result delete(final Request request) {
    long id = request.param("id").longValue();
    itemService.delete(id);
    return Results.with(Status.NO_CONTENT);
  }
}
