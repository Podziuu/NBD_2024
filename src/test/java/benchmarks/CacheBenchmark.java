package benchmarks;

import jakarta.json.bind.Jsonb;
import models.Client;
import models.ClientType;
import org.openjdk.jmh.annotations.*;
import repos.CachedClientRepository;
import repos.RedisClientRepository;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime) // Mierzymy średni czas wykonania
@OutputTimeUnit(TimeUnit.MILLISECONDS) // Wyniki w milisekundach
@State(Scope.Thread) // Jeden obiekt na wątek
public class CacheBenchmark {
    private RedisClientRepository redisClientRepository;
    private CachedClientRepository cachedClientRepository;
    private Jsonb jsonb;

    @Setup
    public void setup() {
        redisClientRepository = new RedisClientRepository();
        cachedClientRepository = new CachedClientRepository();
        jsonb = jakarta.json.bind.JsonbBuilder.create();
    }

    @Benchmark
    public void cacheHit() {
        Client client = new Client(123456789, "Jan", "Kowalski", ClientType.createDiamondMembership());
        cachedClientRepository.addClient(client);
        cachedClientRepository.getClient(client.getId());
    }

    @Benchmark
    public void noCacheHit() {
        Client client = new Client(123456789, "Jan", "Kowalski", ClientType.createDiamondMembership());
        cachedClientRepository.addClient(client);
        redisClientRepository.removeCachedClient("client:" + client.getId());
        cachedClientRepository.getClient(client.getId());
    }
}
