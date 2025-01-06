package config;

import java.net.InetSocketAddress;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;

public class CassandraConfig implements AutoCloseable {
    private static CqlSession session;

    public CassandraConfig() {
        try {
            initSession();
            if (session == null) {
                throw new IllegalStateException("CqlSession nie zostało poprawnie zainicjowane.");
            }
        } catch (Exception e) {
            System.err.println("Błąd podczas inicjalizacji sesji Cassandra: " + e.getMessage());
            e.printStackTrace();
            throw new IllegalStateException("CqlSession nie mogło zostać zainicjowane.", e);
        }
    }

    private void initSession() throws Exception {
        session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("cassandra1", 9042))
                .addContactPoint(new InetSocketAddress("cassandra2", 9043))
                .addTypeCodecs(new ClientTypeCodec())
                .addTypeCodecs(new MusicGenreCodec())
                .withAuthCredentials("cassandra", "cassandra")
                .withLocalDatacenter("dc1")
                .withKeyspace(CqlIdentifier.fromCql("mediastore"))
                .build();

        if (session != null) {
            System.out.println("cql session zostało zainicjowane.");
        } else {
            System.out.println("cqlsession nie dziala");
        }

        createKeyspace();
    }

    private void createKeyspace() {
        try {
            CreateKeyspace keyspace = SchemaBuilder.createKeyspace("mediastore")
                    .ifNotExists()
                    .withSimpleStrategy(2)
                    .withDurableWrites(true);

            SimpleStatement createKeySpaceStatement = keyspace.build();
            session.execute(createKeySpaceStatement);
        } catch (Exception e) {
            System.err.println("Błąd podczas tworzenia Keyspace: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public CqlSession getSession() {
        return session;
    }

    @Override
    public void close() throws Exception {
        if (session != null) {
            session.close();
        }
    }
}
