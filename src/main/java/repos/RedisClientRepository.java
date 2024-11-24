package repos;

import config.AbstractRedisRepository;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import models.Client;
import org.bson.types.ObjectId;

public class RedisClientRepository extends AbstractRedisRepository {
    private final ClientRepository clientRepository;
    private Jsonb jsonb = JsonbBuilder.create();

    public RedisClientRepository() {
        initDbConnection();
        clientRepository = new ClientRepository();
    }

    public void cacheClient(String key, String clientJson) {
        pool.set(key, clientJson);
    }

    public String getCachedClient(String key) {
        return pool.get(key);
    }

    public void removeCachedClient(String key) {
        pool.del(key);
    }

//    public String getClient(String key) {
//        return pool.run(jedis -> jedis.get(key));
//    }
//
//    public void updateClient(String key, String value) {
//        pool.run(jedis -> jedis.set(key, value));
//    }
//
//    public void removeClient(String key) {
//        pool.run(jedis -> jedis.del(key));
//    }
}
