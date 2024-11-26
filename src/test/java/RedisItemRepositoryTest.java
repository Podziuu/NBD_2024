import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import models.Item;
import models.Movie;
import models.MusicGenre;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import repos.RedisItemRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RedisItemRepositoryTest {
    private RedisItemRepository redisItemRepository = new RedisItemRepository();
    private final Jsonb jsonb = JsonbBuilder.create();

    @Test
    void cacheItemTest() {
        Movie item = new Movie(123, "Kizo", 120, true);

        redisItemRepository.cacheItem("test", jsonb.toJson(item));
        String cachedItemJson = redisItemRepository.getCachedItem("test");
        Item cachedItem = jsonb.fromJson(cachedItemJson, Item.class);
        assertNotNull(cachedItem);
        assertEquals(item.getId(), cachedItem.getId());
        assertEquals(item.getItemName(), cachedItem.getItemName());
    }

    @Test
    void shouldReturnNullTest() {
        String cachedItemJson = redisItemRepository.getCachedItem("nonexistent");
        assertEquals(null, cachedItemJson);
    }
}
