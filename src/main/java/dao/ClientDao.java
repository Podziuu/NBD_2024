package dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import models.Client;

import java.util.UUID;

@Dao
public interface ClientDao {
    @StatementAttributes(consistencyLevel = "QUORUM")
    @Insert
    void create(Client client);

    @StatementAttributes(consistencyLevel = "ONE", pageSize = 100)
    @Query("SELECT * FROM mediastore.clients_by_id WHERE client_id = :client_id")
    Client read(@CqlName("client_id") UUID client_id);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Update
    void update(Client client);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Query("DELETE FROM mediastore.clients_by_id WHERE client_id = :client_id")
    void deleteById(@CqlName("client_id") UUID client_id);
}
