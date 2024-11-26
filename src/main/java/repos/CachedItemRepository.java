package repos;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import models.Item;
import org.bson.types.ObjectId;

public class CachedItemRepository implements IItemRepository {
    private final ItemRepository itemRepository;
    private final RedisItemRepository redisItemRepository;
    private final Jsonb jsonb = JsonbBuilder.create();

    public CachedItemRepository() {
        itemRepository = new ItemRepository();
        redisItemRepository = new RedisItemRepository();
    }

    @Override
    public ObjectId addItem(Item item) {
        ObjectId itemId = itemRepository.addItem(item);

        redisItemRepository.cacheItem(generateCecheKey(itemId), jsonb.toJson(item));
        return itemId;
    }

    @Override
    public Item getItem(ObjectId itemId) {
        String cachedItem = redisItemRepository.getCachedItem(generateCecheKey(itemId));
        if (cachedItem != null) {
            return jsonb.fromJson(cachedItem, Item.class);
        }

        Item item = itemRepository.getItem(itemId);
        if (item != null) {
            redisItemRepository.cacheItem(generateCecheKey(itemId), jsonb.toJson(item));
        }
        return item;
    }

    @Override
    public void updateItem(Item item) {
        itemRepository.updateItem(item);
        redisItemRepository.cacheItem(generateCecheKey(item.getId()), jsonb.toJson(item));
    }

    @Override
    public void removeItem(ObjectId itemId) {
        itemRepository.removeItem(itemId);
        redisItemRepository.removeCachedItem(generateCecheKey(itemId));
    }

    private String generateCecheKey(ObjectId id) {
        return "item:" + id.toString();
    }
}
