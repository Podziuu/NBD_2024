package repos;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import config.CassandraConfig;
import dao.ClientDao;
import mapper.ClientMapper;
import mapper.ClientMapperBuilder;
import models.*;

import java.util.UUID;

public class ClientRepository extends CassandraConfig implements IClientRepository {
    private final CqlSession session;
    private final ClientMapper clientMapper;
    private final ClientDao clientDao;

    public ClientRepository(CqlSession session) {
        this.session = session;
        createTable();
        this.clientMapper = new ClientMapperBuilder(session).build();
        this.clientDao = clientMapper.clientDao();
    }

    public void createTable() {
        SimpleStatement createClients = SchemaBuilder.createTable(CqlIdentifier.fromCql("clients_by_id"))
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql("client_id"), DataTypes.UUID)
                .withClusteringColumn(CqlIdentifier.fromCql("client_type"), DataTypes.TEXT)
                .withColumn("personal_id", DataTypes.BIGINT)
                .withColumn("first_name", DataTypes.TEXT)
                .withColumn("last_name", DataTypes.TEXT)
                .withColumn("archive", DataTypes.BOOLEAN)
                .withColumn("rents", DataTypes.listOf(DataTypes.UUID))
                .withColumn("discriminator", DataTypes.TEXT)
                .build();
        session.execute(createClients);
    }

    public void addClient(Client client) {
        clientDao.create(client);
    }

    public Client getClient(UUID id) {
        return clientDao.read(id);
    }

    public void updateClient(Client client) {
        clientDao.update(client);
    }

    public void removeClient(UUID id) {
        clientDao.deleteById(id);
    }

    @Override
    public void close() throws Exception {
        if (session != null) {
            session.close();
        }
    }
}