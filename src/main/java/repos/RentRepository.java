package repos;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import config.CassandraConfig;
import models.Rent;

import java.util.UUID;

public class RentRepository extends CassandraConfig implements IRentRepository {
    private final CqlSession session;

    public RentRepository() {
        this.session = getSession();
    }

    @Override
    public UUID addRent(Rent rent) {
        UUID rentId = UUID.randomUUID();
        String cql = "INSERT INTO mediastore.rents (rent_id, begin_time, end_time, rent_cost, archive, client_id, item_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        SimpleStatement statement = SimpleStatement.newInstance(
                cql,
                rentId,
                rent.getBeginTime(),
                rent.getEndTime(),
                rent.getRentCost(),
                rent.isArchive(),
                rent.getClientId(),
                rent.getItemId()
        );
        session.execute(statement);
        rent.setId(rentId);
        return rentId;
    }

    @Override
    public Rent getRent(UUID id) {
        String cql = "SELECT * FROM mediastore.rents WHERE rent_id = ?";
        SimpleStatement statement = SimpleStatement.newInstance(cql, id);
        var resultSet = session.execute(statement);
        var row = resultSet.one();

        if (row != null) {
            Rent rent = new Rent();
            rent.setId(row.getUuid("rent_id"));
            rent.setBeginTime(row.getInstant("begin_time"));
            rent.setEndTime(row.getInstant("end_time"));
            rent.setRentCost(row.getInt("rent_cost"));
            rent.setArchive(row.getBoolean("archive"));
            rent.setClientId(row.getUuid("client_id"));
            rent.setItemId(row.getUuid("item_id"));
            return rent;
        }
        return null;
    }

    @Override
    public void updateRent(Rent rent) {
        String cql = "UPDATE mediastore.rents SET begin_time = ?, end_time = ?, rent_cost = ?, archive = ?, " +
                "client_id = ?, item_id = ? WHERE rent_id = ?";
        SimpleStatement statement = SimpleStatement.newInstance(
                cql,
                rent.getBeginTime(),
                rent.getEndTime(),
                rent.getRentCost(),
                rent.isArchive(),
                rent.getClientId(),
                rent.getItemId(),
                rent.getId()
        );
        session.execute(statement);
    }

    @Override
    public void removeRent(UUID id) {
        String cql = "DELETE FROM mediastore.rents WHERE rent_id = ?";
        SimpleStatement statement = SimpleStatement.newInstance(cql, id);
        session.execute(statement);
    }

    public void close() throws Exception {
        if (session != null) {
            session.close();
        }
    }
}