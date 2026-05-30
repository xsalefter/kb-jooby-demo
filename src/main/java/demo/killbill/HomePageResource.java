package demo.killbill;

import org.jooby.MediaType;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.mvc.GET;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

@Path("/")
@Produces("text/html")
public class HomePageResource {

    @GET
    public Result index() {
        return Results.with(
                        "<!DOCTYPE html><html><head><title>Kill Bill Jooby Demo</title></head>"
                                + "<body><h1>Kill Bill Jooby Demo</h1>"
                                + "<p>This page is served by the ported killbill-jooby module.</p>"
                                + "</body></html>")
                .type(MediaType.html);
    }
}
