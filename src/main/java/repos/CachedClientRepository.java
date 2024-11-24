package repos;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import models.Client;
import org.bson.types.ObjectId;

public class CachedClientRepository implements IClientRepository {
    private final ClientRepository clientRepository;
    private final RedisClientRepository redisClientRepository;
    private final Jsonb jsonb = JsonbBuilder.create();

    public CachedClientRepository() {
        clientRepository = new ClientRepository();
        redisClientRepository = new RedisClientRepository();
    }

    @Override
    public ObjectId addClient(Client client) {
        ObjectId clientId = clientRepository.addClient(client);
        redisClientRepository.cacheClient(generateCacheKey(clientId), jsonb.toJson(client));
        return clientId;
    }

    @Override
    public Client getClient(ObjectId id) {
        String cachedClient = redisClientRepository.getCachedClient(generateCacheKey(id));
        if (cachedClient != null) {
            return jsonb.fromJson(cachedClient, Client.class);
        }

        Client client = clientRepository.getClient(id);
        if (client != null) {
            redisClientRepository.cacheClient(generateCacheKey(id), jsonb.toJson(client));
        }
        return client;
    }

    @Override
    public void updateClient(Client client) {
        clientRepository.updateClient(client);
        redisClientRepository.cacheClient(client.getId().toString(), jsonb.toJson(client));
    }

    @Override
    public void removeClient(ObjectId id) {
        clientRepository.removeClient(id);
        redisClientRepository.removeCachedClient(id.toString());
    }

    private String generateCacheKey(ObjectId clientId) {
        return "client:" + clientId.toString();
    }
}
