package repos;

import config.AbstractRedisRepository;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

public class RedisItemRepository extends AbstractRedisRepository {
    private final ItemRepository itemRepository;
    private Jsonb jsonb = JsonbBuilder.create();

    public RedisItemRepository() {
        initDbConnection();
        itemRepository = new ItemRepository();
    }

    public void cacheItem(String key, String itemJson) {
        pool.setex(key, 900, itemJson);
    }

    public String getCachedItem(String key) {
        return pool.get(key);
    }

    public void removeCachedItem(String key) {
        pool.del(key);
    }
}