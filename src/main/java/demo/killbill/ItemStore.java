package demo.killbill;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import jakarta.inject.Singleton;

@Singleton
public class ItemStore {

    private final AtomicLong sequence = new AtomicLong(0);
    private final Map<Long, Item> items = new ConcurrentHashMap<>();

    public List<Item> list() {
        List<Item> result = new ArrayList<>(items.values());
        result.sort(Comparator.comparingLong(Item::getId));
        return result;
    }

    public Item get(final long id) {
        return items.get(id);
    }

    public Item create(final ItemPayload payload) {
        long id = sequence.incrementAndGet();
        Item item = new Item(id, payload.getName(), payload.getDescription());
        items.put(id, item);
        return item;
    }

    public Item update(final long id, final ItemPayload payload) {
        Item item = new Item(id, payload.getName(), payload.getDescription());
        items.put(id, item);
        return item;
    }

    public boolean delete(final long id) {
        return items.remove(id) != null;
    }
}
