package config;

import java.net.InetSocketAddress;
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
                .withAuthCredentials("cassandra", "cassandra")
                .withLocalDatacenter("dc1")
                .build();

        if (session != null) {
            System.out.println("cql session zostało zainicjowane.");
        } else {
            System.out.println("cqlsession nie dziala");
        }

        createKeyspace();
        createClientsTableIfNotExists();
        createComicsItemsTableIfNotExists();
        createMoviesTableIfNotExists();
        createMusicItemsTableIfNotExists();
        createRentsTableIfNotExists();
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

    private void createClientsTableIfNotExists() {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS mediastore.clients ("
                + "client_id UUID PRIMARY KEY, "
                + "personal_id BIGINT, "
                + "first_name TEXT, "
                + "last_name TEXT, "
                + "archive BOOLEAN, "
                + "client_type TEXT, "
                + "rents LIST<UUID>"
                + ");";

        try {
            SimpleStatement statement = SimpleStatement.newInstance(createTableQuery);
            session.execute(statement);
        } catch (Exception e) {
            System.err.println("Błąd podczas tworzenia tabeli 'clients': " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createComicsItemsTableIfNotExists() {
        String createComicsItemsTableQuery = "CREATE TABLE IF NOT EXISTS mediastore.comics_items ("
                + "item_id UUID PRIMARY KEY, "
                + "base_price INT, "
                + "item_name TEXT, "
                + "available BOOLEAN, "
                + "item_type TEXT, "
                + "page_number INT"
                + ");";

        try {
            SimpleStatement statement = SimpleStatement.newInstance(createComicsItemsTableQuery);
            session.execute(statement);
        } catch (Exception e) {
            System.err.println("Błąd podczas tworzenia tabeli 'comics_items': " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createMoviesTableIfNotExists() {
        String createMoviesTableQuery = "CREATE TABLE IF NOT EXISTS mediastore.movies ("
                + "item_id UUID PRIMARY KEY, "
                + "base_price INT, "
                + "item_name TEXT, "
                + "available BOOLEAN, "
                + "item_type TEXT, "
                + "minutes INT, "
                + "casette BOOLEAN"
                + ");";

        try {
            SimpleStatement statement = SimpleStatement.newInstance(createMoviesTableQuery);
            session.execute(statement);
        } catch (Exception e) {
            System.err.println("Błąd podczas tworzenia tabeli 'movies': " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createRentsTableIfNotExists() {
        String createRentsTableQuery = "CREATE TABLE IF NOT EXISTS mediastore.rents ("
                + "rent_id UUID PRIMARY KEY, "
                + "begin_time TIMESTAMP, "
                + "end_time TIMESTAMP, "
                + "rent_cost INT, "
                + "archive BOOLEAN, "
                + "client_id UUID, "
                + "item_id UUID"
                + ");";

        try {
            SimpleStatement statement = SimpleStatement.newInstance(createRentsTableQuery);
            session.execute(statement);
        } catch (Exception e) {
            System.err.println("Błąd podczas tworzenia tabeli 'rents': " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void createMusicItemsTableIfNotExists() {
        String createMusicItemsTableQuery = "CREATE TABLE IF NOT EXISTS mediastore.music_items ("
                + "item_id UUID PRIMARY KEY, "
                + "base_price INT, "
                + "item_name TEXT, "
                + "available BOOLEAN, "
                + "item_type TEXT, "
                + "genre INT, "
                + "vinyl BOOLEAN"
                + ");";

        try {
            SimpleStatement statement = SimpleStatement.newInstance(createMusicItemsTableQuery);
            session.execute(statement);
        } catch (Exception e) {
            System.err.println("błąd podczas tworzenia tabeli 'music_items': " + e.getMessage());
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
