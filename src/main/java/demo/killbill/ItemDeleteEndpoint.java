package demo.killbill;

import org.jooby.Request;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.Status;
import org.jooby.mvc.DELETE;
import org.jooby.mvc.Path;

import jakarta.inject.Inject;

@Path("/api/items/{id}")
public class ItemDeleteEndpoint {

    private final ItemService itemService;

    @Inject
    public ItemDeleteEndpoint(final ItemService itemService) {
        this.itemService = itemService;
    }

    @DELETE
    public Result delete(final Request request) {
        long id = request.param("id").longValue();
        itemService.delete(id);
        return Results.with(Status.NO_CONTENT);
    }
}
