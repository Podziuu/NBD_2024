package dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import models.Client;
//import providers.ClientProvider;

import java.util.UUID;

@Dao
public interface ClientDao {
    @Insert
    void create(Client client);

    @Query("SELECT * FROM mediastore.clients WHERE client_id = :client_id")
    Client read(@CqlName("client_id") UUID client_id);

    @Insert
    void update(Client client);

    @Query("DELETE FROM mediastore.clients WHERE client_id = :id")
    void deleteById(@CqlName("id") UUID id);
}
