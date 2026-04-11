package demo.killbill;

import org.jooby.Result;
import org.jooby.Results;
import org.jooby.Status;
import org.jooby.mvc.Body;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

import jakarta.inject.Inject;

@Path("/api/items")
@Produces("application/json")
public class ItemCreateEndpoint {

  private final ItemService itemService;

  @Inject
  public ItemCreateEndpoint(final ItemService itemService) {
    this.itemService = itemService;
  }

  @POST
  @Consumes("application/json")
  public Result create(final @Body ItemPayload payload) {
    return Results.with(itemService.create(payload)).status(Status.CREATED);
  }
}
