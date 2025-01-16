package dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import models.Comics;
import models.Item;
import models.Movie;
import models.Music;

import java.util.UUID;

@Dao
public interface ItemDao {

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Insert
    void create(Music music);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Insert
    void create(Movie movie);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Insert
    void create(Comics comics);

    @StatementAttributes(consistencyLevel = "ONE", pageSize = 100)
    @Query("SELECT * FROM mediastore.items_by_id WHERE item_id = :id")
    Item read(@CqlName("id") UUID id);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Update
    void update(Item item);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Query("DELETE FROM mediastore.items_by_id WHERE item_id = :id")
    void deleteById(@CqlName("id") UUID id);

    @StatementAttributes(consistencyLevel = "ONE", pageSize = 100)
    @Query("SELECT * FROM mediastore.items_by_id WHERE item_id = :id")
    Music readMusic(@CqlName("id") UUID id);

    @StatementAttributes(consistencyLevel = "ONE", pageSize = 100)
    @Query("SELECT * FROM mediastore.items_by_id WHERE item_id = :id")
    Movie readMovie(@CqlName("id") UUID id);

    @StatementAttributes(consistencyLevel = "ONE", pageSize = 100)
    @Query("SELECT * FROM mediastore.items_by_id WHERE item_id = :id")
    Comics readComics(@CqlName("id") UUID id);
}