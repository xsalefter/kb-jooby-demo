package demo.killbill;

import org.jooby.Request;
import org.jooby.mvc.GET;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

import jakarta.inject.Inject;

@Path("/api/items/{id}")
@Produces("application/json")
public class ItemGetEndpoint {

    private final ItemService itemService;

    @Inject
    public ItemGetEndpoint(final ItemService itemService) {
        this.itemService = itemService;
    }

    @GET
    public Item get(final Request request) {
        long id = request.param("id").longValue();
        return itemService.get(id);
    }
}
