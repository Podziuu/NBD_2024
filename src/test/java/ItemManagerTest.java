import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import config.MongoEntity;
import managers.ItemManager;
import models.*;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repos.ItemRepository;

public class ItemManagerTest {
    private static ItemRepository itemRepository;
    private static ItemManager itemManager;

    @BeforeAll
    static void setUp() {
        itemRepository = new ItemRepository();
        itemManager = new ItemManager(itemRepository);
    }

    @Test
    void addMusicTest() {
        ObjectId itemId = itemManager.addMusic(100, "Album", MusicGenre.Metal, true);
        Assertions.assertNotNull(itemManager.getItem(itemId));
    }

    @Test
    void addMovieTest() {
        ObjectId itemId = itemManager.addMovie(100, "Movie", 120, true);
        Assertions.assertNotNull(itemManager.getItem(itemId));
    }

    @Test
    void addComicsTest() {
        ObjectId itemId = itemManager.addComics(100, "Comics", 100);
        Assertions.assertNotNull(itemManager.getItem(itemId));
    }

    @Test
    void removeItemTest() {
        ObjectId itemId = itemManager.addMovie(100, "Movie", 120, true);
        itemManager.removeItem(itemId);
        Assertions.assertNull(itemManager.getItem(itemId));
    }

    @Test
    void removeItemInvalidIdTest() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            itemManager.removeItem(new ObjectId());
        });
    }

    @Test
    void updateItemTest() {
        ObjectId itemId = itemManager.addComics(100, "Spiderman", 100);
        itemManager.updateItem(itemId, 150, "Batman");
        Item item = itemManager.getItem(itemId);
        Assertions.assertEquals("Batman", item.getItemName());
        Assertions.assertEquals(150, item.getBasePrice());
    }

    @Test
    void updateItemInvalidIdTest() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            itemManager.updateItem(new ObjectId(), 150, "New Album");
        });
    }

    @Test
    void setUnavailableTest() {
        ObjectId itemId = itemManager.addComics(100, "Superman", 100);
        itemManager.setUnavailable(itemId);
        Item item = itemManager.getItem(itemId);
        Assertions.assertFalse(item.isAvailable());
    }

    @Test
    void setAvailableTest() {
        ObjectId itemId = itemManager.addComics(100, "Superman", 100);
        itemManager.setUnavailable(itemId);
        itemManager.setAvailable(itemId);
        Item item = itemManager.getItem(itemId);
        Assertions.assertTrue(item.isAvailable());
    }

    @Test
    void setAvailableInvalidIdTest() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            itemManager.setAvailable(new ObjectId());
        });
    }

    @Test
    void setUnavailableInvalidIdTest() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            itemManager.setUnavailable(new ObjectId());
        });
    }
}