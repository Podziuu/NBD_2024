import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import managers.ClientManager;
import models.Client;
import models.ClientType;
import org.junit.jupiter.api.Test;
import repos.CachedClientRepository;
import repos.ClientRepository;
import repos.RedisClientRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RedisClientRepositoryTest {
    private final RedisClientRepository redisClientRepository = new RedisClientRepository();
    private final Jsonb jsonb = JsonbBuilder.create();

    @Test
    void cacheClientTest() {
        Client client = new Client(123456789, "Jan", "Kowalski", ClientType.createDiamondMembership());
        redisClientRepository.cacheClient("test", jsonb.toJson(client));
        String cachedClientJson = redisClientRepository.getCachedClient("test");
        Client cachedClient = jsonb.fromJson(cachedClientJson, Client.class);
        assertNotNull(cachedClient);
        assertEquals(cachedClient.getPersonalId(), client.getPersonalId());
        assertEquals(cachedClient.getFirstName(), client.getFirstName());
        assertEquals(cachedClient.getLastName(), client.getLastName());
        assertEquals(cachedClient.getClientType().getMaxArticles(), client.getClientType().getMaxArticles());
    }

    @Test
    void shouldReturnNullTest() {
        String cachedClientJson = redisClientRepository.getCachedClient("nonexistent");
        assertEquals(null, cachedClientJson);
    }
}
