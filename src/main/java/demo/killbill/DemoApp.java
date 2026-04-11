package demo.killbill;

import org.jooby.Jooby;
import org.jooby.json.Jackson;

public class DemoApp extends Jooby {

  public DemoApp() {
    use(new Jackson());
    new ItemEventsEndpoint().install(this);

    use(HomePageResource.class);
    use(ItemListEndpoint.class);
    use(ItemCreateEndpoint.class);
    use(ItemGetEndpoint.class);
    use(ItemUpdateEndpoint.class);
    use(ItemDeleteEndpoint.class);
    use(JaxRsItemsResource.class);
  }

  public static void main(final String[] args) {
    run(DemoApp::new, args);
  }
}
