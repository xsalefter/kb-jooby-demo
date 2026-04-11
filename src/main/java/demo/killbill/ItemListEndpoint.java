package demo.killbill;

import java.util.List;

import org.jooby.mvc.GET;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

import jakarta.inject.Inject;

@Path("/api/items")
@Produces("application/json")
public class ItemListEndpoint {

  private final ItemService itemService;

  @Inject
  public ItemListEndpoint(final ItemService itemService) {
    this.itemService = itemService;
  }

  @GET
  public List<Item> list() {
    return itemService.list();
  }
}
