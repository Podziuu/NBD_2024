package dao;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.annotations.*;
import models.Rent;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Dao
public interface RentDao {
    @Query("INSERT INTO mediastore.rents_by_rent_id (rent_id, client_id, begin_time, " +
            "end_time, rent_cost, archive, item_id) VALUES (:rent_id, :client_id," +
            ":begin_time, :end_time, :rent_cost, :archive, :item_id)")
    void createRentByRentId(@CqlName("rent_id") UUID rentId,
                            @CqlName("client_id") UUID clientId,
                            @CqlName("begin_time")Instant beginTime,
                            @CqlName("end_time") Instant endTime,
                            @CqlName("rent_cost") int rentCost,
                            @CqlName("archive") boolean archive,
                            @CqlName("item_id") UUID itemId);

    @Query("INSERT INTO mediastore.rents_by_client_id (client_id, rent_id, begin_time, " +
            "end_time, rent_cost, archive, item_id) VALUES (:client_id, :rent_id," +
            ":begin_time, :end_time, :rent_cost, :archive, :item_id)")
    void createRentByClientId(@CqlName("client_id") UUID clientId,
                              @CqlName("rent_id") UUID rentId,
                              @CqlName("begin_time")Instant beginTime,
                              @CqlName("end_time") Instant endTime,
                              @CqlName("rent_cost") int rentCost,
                              @CqlName("archive") boolean archive,
                              @CqlName("item_id") UUID itemId);

    @Query("SELECT * FROM mediastore.rents_by_rent_id WHERE rent_id = :rent_id")
    Row readByRentIdRaw(@CqlName("rent_id") UUID rentId);

    @Query("SELECT * FROM mediastore.rents_by_client_id WHERE client_id = :client_id")
    ResultSet readByClientIdRaw(@CqlName("client_id") UUID clientId);

    default Rent mapRowToRent(Row row) {
        Rent rent = new Rent();
        rent.setId(row.getUuid("rent_id"));
        rent.setClientId(row.getUuid("client_id"));
        rent.setBeginTime(row.getInstant("begin_time"));
        rent.setEndTime(row.getInstant("end_time"));
        rent.setRentCost(row.getInt("rent_cost"));
        rent.setArchive(row.getBoolean("archive"));
        rent.setItemId(row.getUuid("item_id"));
        return rent;
    }

    default Rent readByRentId(UUID rentId) {
        Row row = readByRentIdRaw(rentId);
        if (row != null) {
            return mapRowToRent(row);
        }
        return null;
    }

    default List<Rent> readByClientId(UUID clientId) {
        ResultSet resultSet = readByClientIdRaw(clientId);
        return resultSet.all().stream().map(this::mapRowToRent).toList();
    }

    @Query("UPDATE mediastore.rents_by_rent_id SET begin_time = :begin_time, end_time = :end_time, " +
            "rent_cost = :rent_cost, archive = :archive, item_id = :item_id " +
            "WHERE rent_id = :rent_id")
    void updateRentByRentId(@CqlName("rent_id") UUID rentId,
                            @CqlName("begin_time") Instant beginTime,
                            @CqlName("end_time") Instant endTime,
                            @CqlName("rent_cost") int rentCost,
                            @CqlName("archive") boolean archive,
                            @CqlName("item_id") UUID itemId);

//    @Query("UPDATE mediastore.rents_by_client_id SET begin_time = :begin_time, end_time = :end_time, " +
//            "rent_cost = :rent_cost, archive = :archive, item_id = :item_id " +
//            "WHERE client_id = :client_id AND rent_id = :rent_id")
//
//    void updateRentByClientId(@CqlName("client_id") UUID clientId,
//                              @CqlName("rent_id") UUID rentId,
//                              @CqlName("begin_time") Instant beginTime,
//                              @CqlName("end_time") Instant endTime,
//                              @CqlName("rent_cost") int rentCost,
//                              @CqlName("archive") boolean archive,
//                              @CqlName("item_id") UUID itemId);


    @Query("DELETE FROM mediastore.rents_by_rent_id WHERE rent_id = :rent_id")
    void deleteByRentId(@CqlName("rent_id") UUID rentId);

    @Query("DELETE FROM mediastore.rents_by_client_id WHERE client_id = :client_id AND rent_id = :rent_id")
    void deleteByClientId(@CqlName("client_id") UUID clientId, @CqlName("rent_id") UUID rentId);
}