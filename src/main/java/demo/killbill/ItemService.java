package demo.killbill;

import java.util.List;

import org.jooby.Err;
import org.jooby.Status;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class ItemService {

  private final ItemStore itemStore;

  @Inject
  public ItemService(final ItemStore itemStore) {
    this.itemStore = itemStore;
  }

  public List<Item> list() {
    return itemStore.list();
  }

  public Item get(final long id) {
    Item item = itemStore.get(id);
    if (item == null) {
      throw new Err(Status.NOT_FOUND, "Item not found: " + id);
    }
    return item;
  }

  public Item create(final ItemPayload payload) {
    return itemStore.create(requirePayload(payload));
  }

  public Item update(final long id, final ItemPayload payload) {
    return itemStore.update(id, requirePayload(payload));
  }

  public void delete(final long id) {
    if (!itemStore.delete(id)) {
      throw new Err(Status.NOT_FOUND, "Item not found: " + id);
    }
  }

  private ItemPayload requirePayload(final ItemPayload payload) {
    if (payload == null || isBlank(payload.getName())) {
      throw new Err(Status.BAD_REQUEST, "Item name is required");
    }
    return payload;
  }

  private boolean isBlank(final String value) {
    return value == null || value.trim().isEmpty();
  }
}
