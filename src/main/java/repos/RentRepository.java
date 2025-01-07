package repos;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import config.CassandraConfig;
import dao.RentDao;
import mapper.RentMapper;
import mapper.RentMapperBuilder;
import models.Rent;

import java.util.List;
import java.util.UUID;

public class RentRepository extends CassandraConfig {
    private final CqlSession session;
    private final RentMapper rentMapper;
    private final RentDao rentDao;

    public RentRepository(CqlSession session) {
        this.session = session;
        createTable();
        this.rentMapper = new RentMapperBuilder(session).build();
        this.rentDao = rentMapper.rentDao();
    }

    public void createTable() {
        SimpleStatement createRentsByClientId = SchemaBuilder.createTable(CqlIdentifier.fromCql("rents_by_client_id"))
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql("client_id"), DataTypes.UUID)
                .withClusteringColumn(CqlIdentifier.fromCql("rent_id"), DataTypes.UUID)
                .withColumn("begin_time", DataTypes.TIMESTAMP)
                .withColumn("end_time", DataTypes.TIMESTAMP)
                .withColumn("rent_cost", DataTypes.INT)
                .withColumn("archive", DataTypes.BOOLEAN)
                .withColumn("item_id", DataTypes.UUID)
                .build();

        SimpleStatement createRentsByRentId = SchemaBuilder.createTable(CqlIdentifier.fromCql("rents_by_rent_id"))
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql("rent_id"), DataTypes.UUID)
                .withColumn("client_id", DataTypes.UUID)
                .withColumn("begin_time", DataTypes.TIMESTAMP)
                .withColumn("end_time", DataTypes.TIMESTAMP)
                .withColumn("rent_cost", DataTypes.INT)
                .withColumn("archive", DataTypes.BOOLEAN)
                .withColumn("item_id", DataTypes.UUID)
                .build();
        session.execute(createRentsByClientId);
        session.execute(createRentsByRentId);
    }

    public void addRent(Rent rent) {
        rentDao.createRentByRentId(rent.getId(),
                rent.getClientId(), rent.getBeginTime(), rent.getEndTime(),
                rent.getRentCost(), rent. isArchive(), rent.getItemId());
        rentDao.createRentByClientId(rent.getClientId(), rent.getId(),
                rent.getBeginTime(), rent.getEndTime(), rent.getRentCost(),
                rent.isArchive(), rent.getItemId());
    }

    public Rent getRentByRentId(UUID id) {
        return rentDao.readByRentId(id);
    }

    public List<Rent> getRentByClientId(UUID id) {
        return rentDao.readByClientId(id);
    }

    public void updateRentByRentId(Rent rent) {
        rentDao.updateRentByRentId(rent.getId(),
                rent.getBeginTime(), rent.getEndTime(),
                rent.getRentCost(), rent.isArchive(), rent.getItemId());
    }

//    public void updateRentByClientId(UUID clientId, UUID rentId, Instant beginTime, Instant endTime, int rentCost, boolean archive, UUID itemId) {
//        rentDao.updateRentByClientId(clientId, rentId, beginTime, endTime, rentCost, archive, itemId);
//    }

    public void removeRent(UUID id) {
        rentDao.deleteByRentId(id);
    }

    public void deleteRentByClientId(UUID clientId, UUID rentId) {
        rentDao.deleteByClientId(clientId, rentId);
    }

    public void close() throws Exception {
        if (session != null) {
            session.close();
        }
    }
}