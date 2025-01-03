package repos;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import config.CassandraConfig;
import models.*;

import java.util.UUID;

public class ItemRepository extends CassandraConfig implements IItemRepository {

    private final CqlSession session;

    public ItemRepository() {
        this.session = getSession();
    }

    @Override
    public UUID addItem(Item item) {
        UUID id = UUID.randomUUID();
        String cql = "";

        if (item instanceof Music) {
            Music music = (Music) item;
            cql = "INSERT INTO mediastore.music_items (item_id, base_price, item_name, available, item_type, genre, vinyl) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            SimpleStatement statement = SimpleStatement.newInstance(cql, id, music.getBasePrice(), music.getItemName(),
                    music.isAvailable(), "Music", music.getGenreValue(), music.isVinyl());
            session.execute(statement);
        } else if (item instanceof Movie) {
            Movie movie = (Movie) item;
            cql = "INSERT INTO mediastore.movies (item_id, base_price, item_name, available, item_type, minutes, casette) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            SimpleStatement statement = SimpleStatement.newInstance(cql, id, movie.getBasePrice(), movie.getItemName(),
                    movie.isAvailable(), "Movie", movie.getMinutes(), movie.isCasette());
            session.execute(statement);
        } else if (item instanceof Comics) {
            Comics comics = (Comics) item;
            cql = "INSERT INTO mediastore.comics_items (item_id, base_price, item_name, available, item_type, page_number) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            SimpleStatement statement = SimpleStatement.newInstance(cql, id, comics.getBasePrice(), comics.getItemName(),
                    comics.isAvailable(), "Comics", comics.getPageNumber());
            session.execute(statement);
        }

        return id;
    }

    @Override
    public Item getItem(UUID item_id) {
        String cql = "SELECT * FROM mediastore.music_items WHERE item_id = ?";
        SimpleStatement statement = SimpleStatement.newInstance(cql, item_id);
        var resultSet = session.execute(statement);
        var row = resultSet.one();

        if (row != null) {
            Music music = new Music();
            music.setId(row.getUuid("item_id"));
            music.setBasePrice(row.getInt("base_price"));
            music.setItemName(row.getString("item_name"));
            music.setAvailable(row.getBoolean("available"));
            int genreValue = row.getInt("genre");
            MusicGenre genre = MusicGenre.fromValue(genreValue);
            music.setGenre(genre);

            music.setVinyl(row.getBoolean("vinyl"));
            return music;
        }

        cql = "SELECT * FROM mediastore.movies WHERE item_id = ?";
        statement = SimpleStatement.newInstance(cql, item_id);
        resultSet = session.execute(statement);
        row = resultSet.one();

        if (row != null) {
            Movie movie = new Movie();
            movie.setId(row.getUuid("item_id"));
            movie.setBasePrice(row.getInt("base_price"));
            movie.setItemName(row.getString("item_name"));
            movie.setAvailable(row.getBoolean("available"));
            movie.setMinutes(row.getInt("minutes"));
            movie.setCasette(row.getBoolean("casette"));
            return movie;
        }

        cql = "SELECT * FROM mediastore.comics_items WHERE item_id = ?";
        statement = SimpleStatement.newInstance(cql, item_id);
        resultSet = session.execute(statement);
        row = resultSet.one();

        if (row != null) {
            Comics comics = new Comics();
            comics.setId(row.getUuid("item_id"));
            comics.setBasePrice(row.getInt("base_price"));
            comics.setItemName(row.getString("item_name"));
            comics.setAvailable(row.getBoolean("available"));
            comics.setPageNumber(row.getInt("page_number"));
            return comics;
        }

        return null;
    }

    @Override
    public void updateItem(Item item) {
        String cql = "";

        if (item instanceof Music) {
            Music music = (Music) item;
            cql = "UPDATE mediastore.music_items SET base_price = ?, item_name = ?, available = ?, genre = ?, vinyl = ? WHERE item_id = ?";
            SimpleStatement statement = SimpleStatement.newInstance(cql, music.getBasePrice(), music.getItemName(),
                    music.isAvailable(), music.getGenreValue(), music.isVinyl(), item.getId());
            session.execute(statement);
        }
        else if (item instanceof Movie) {
            Movie movie = (Movie) item;
            cql = "UPDATE mediastore.movies SET base_price = ?, item_name = ?, available = ?, minutes = ?, casette = ? WHERE item_id = ?";
            SimpleStatement statement = SimpleStatement.newInstance(cql, movie.getBasePrice(), movie.getItemName(),
                    movie.isAvailable(), movie.getMinutes(), movie.isCasette(), item.getId());
            session.execute(statement);
        }
        else if (item instanceof Comics) {
            Comics comics = (Comics) item;
            cql = "UPDATE mediastore.comics_items SET base_price = ?, item_name = ?, available = ?, page_number = ? WHERE item_id = ?";
            SimpleStatement statement = SimpleStatement.newInstance(cql, comics.getBasePrice(), comics.getItemName(),
                    comics.isAvailable(), comics.getPageNumber(), item.getId());
            session.execute(statement);
        }
    }

    @Override
    public void removeItem(UUID item_id) {
        String cql = "SELECT item_id FROM mediastore.music_items WHERE item_id = ?";
        SimpleStatement statement = SimpleStatement.newInstance(cql, item_id);
        var resultSet = session.execute(statement);
        var row = resultSet.one();

        if (row != null) {
            cql = "DELETE FROM mediastore.music_items WHERE item_id = ?";
            statement = SimpleStatement.newInstance(cql, item_id);
            session.execute(statement);
            System.out.println("Przedmiot usunięty z tabeli music_items.");
            return;
        }

        cql = "SELECT item_id FROM mediastore.movies WHERE item_id = ?";
        statement = SimpleStatement.newInstance(cql, item_id);
        resultSet = session.execute(statement);
        row = resultSet.one();

        if (row != null) {
            cql = "DELETE FROM mediastore.movies WHERE item_id = ?";
            statement = SimpleStatement.newInstance(cql, item_id);
            session.execute(statement);
            System.out.println("Przedmiot usunięty z tabeli movies.");
            return;
        }

        cql = "SELECT item_id FROM mediastore.comics_items WHERE item_id = ?";
        statement = SimpleStatement.newInstance(cql, item_id);
        resultSet = session.execute(statement);
        row = resultSet.one();

        if (row != null) {
            cql = "DELETE FROM mediastore.comics_items WHERE item_id = ?";
            statement = SimpleStatement.newInstance(cql, item_id);
            session.execute(statement);
            System.out.println("Przedmiot usunięty z tabeli comics_items.");
            return;
        }

        System.out.println("Nie znaleziono przedmiotu o podanym UUID.");
    }

    public void close() throws Exception {
        if (session != null) {
            session.close();
        }
    }
}