package repos;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import config.CassandraConfig;
import models.*;

import java.util.List;
import java.util.UUID;

public class ClientRepository extends CassandraConfig implements IClientRepository {
    private final CqlSession session;

    public ClientRepository() {
        this.session = getSession();
    }

    @Override
    public UUID addClient(Client client) {
        UUID id = UUID.randomUUID();
        String clientTypeString = client.getClientType().toString();

        String cql = "INSERT INTO mediastore.clients (client_id, personal_id, first_name, last_name, archive, client_type, rents) VALUES (?, ?, ?, ?, ?, ?, ?)";

        SimpleStatement statement = SimpleStatement.newInstance(
                cql,
                id,
                client.getPersonalId(),
                client.getFirstName(),
                client.getLastName(),
                client.isArchive(),
                clientTypeString,
                client.getRents()
        );

        session.execute(statement);
        client.setId(id);
        return id;
    }


    @Override
    public Client getClient(UUID id) {
        String cql = "SELECT * FROM mediastore.clients WHERE client_id = ?";
        SimpleStatement statement = SimpleStatement.newInstance(cql, id);
        var resultSet = session.execute(statement);
        var row = resultSet.one();

        if (row != null) {
            Client client = new Client();
            client.setId(row.getUuid("client_id"));
            client.setPersonalId(row.getLong("personal_id"));
            client.setFirstName(row.getString("first_name"));
            client.setLastName(row.getString("last_name"));
            client.setArchive(row.getBoolean("archive"));
            String clientTypeString = row.getString("client_type");
            client.setClientType(ClientType.fromString(clientTypeString));
            client.setRents(row.getList("rents", UUID.class));
            return client;
        }
        return null;
    }

    @Override
    public void updateClient(Client client) {
        String cql = "UPDATE mediastore.clients SET personal_id = ?, first_name = ?, last_name = ?, archive = ?, client_type = ?, rents = ? WHERE client_id = ?";
        SimpleStatement statement = SimpleStatement.newInstance(
                cql,
                client.getPersonalId(),
                client.getFirstName(),
                client.getLastName(),
                client.isArchive(),
                client.getClientType(),
                client.getRents(),
                client.getId()
        );
        session.execute(statement);
    }

    @Override
    public void removeClient(UUID id) {
        String cql = "DELETE FROM mediastore.clients WHERE client_id = ?";
        SimpleStatement statement = SimpleStatement.newInstance(cql, id);
        session.execute(statement);
    }

    public void addRentToClient(UUID clientId, UUID rentId) {
        String cql = "UPDATE mediastore.clients SET rents = rents + ? WHERE client_id = ?";
        List<UUID> rents = List.of(rentId);
        SimpleStatement statement = SimpleStatement.newInstance(cql, rents, clientId);
        session.execute(statement);
    }

    @Override
    public void close() throws Exception {
        if (session != null) {
            session.close();
        }
    }
}