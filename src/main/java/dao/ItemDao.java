package dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import models.Comics;
import models.Item;
import models.Movie;
import models.Music;

import java.util.UUID;

@Dao
public interface ItemDao {

    @Insert
    void create(Item item);

    @Query("SELECT * FROM mediastore.items_by_id WHERE item_id = :id")
    Item read(@CqlName("id") UUID id);

    @Update
    void update(Item item);

    @Query("DELETE FROM mediastore.items_by_id WHERE item_id = :id")
    void deleteById(@CqlName("id") UUID id);

    @Query("SELECT * FROM mediastore.items_by_id WHERE item_id = :id")
    Music readMusic(@CqlName("id") UUID id);

    @Query("SELECT * FROM mediastore.items_by_id WHERE item_id = :id AND item_type = 'movie'")
    Movie readMovie(@CqlName("id") UUID id);

    @Query("SELECT * FROM mediastore.items_by_id WHERE item_id = :id AND item_type = 'comics'")
    Comics readComics(@CqlName("id") UUID id);
}

