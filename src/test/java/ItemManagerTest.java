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

    private static MongoEntity mongoEntity;
    private static MongoDatabase database;
    private static MongoCollection<Item> itemCollection;
    private static ItemRepository itemRepository;
    private static ItemManager itemManager;

    @BeforeAll
    static void setUp() {
        mongoEntity = new MongoEntity();
        database = mongoEntity.getDatabase();
        itemCollection = database.getCollection("items", Item.class);
        itemRepository = new ItemRepository(itemCollection);
        itemManager = new ItemManager(itemRepository);
    }

    @AfterAll
    static void tearDown() throws Exception {
        mongoEntity.close();
    }

    @Test
    void addItemTest() {
        ObjectId itemId = itemManager.addItem(100, "Album", "Music");
        Assertions.assertNotNull(itemManager.getItem(itemId));
    }

    @Test
    void addItemInvalidTypeTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            itemManager.addItem(100, "Movie", "InvalidType");
        });
    }

    @Test
    void removeItemTest() {
        ObjectId itemId = itemManager.addItem(100, "Album", "Music");
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
        ObjectId itemId = itemManager.addItem(100, "Album", "Music");
        itemManager.updateItem(itemId, 150, "New Album", "Music");
        Item item = itemManager.getItem(itemId);
        Assertions.assertEquals("New Album", item.getItemName());
        Assertions.assertEquals(150, item.getBasePrice());
    }

    @Test
    void updateItemInvalidIdTest() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            itemManager.updateItem(new ObjectId(), 150, "New Album", "Music");
        });
    }

    @Test
    void setAvailableTest() {
        ObjectId itemId = itemManager.addItem(100, "Album", "Music");
        itemManager.setAvailable(itemId);
        Item item = itemManager.getItem(itemId);
        Assertions.assertTrue(item.isAvailable());
    }

    @Test
    void setUnavailableTest() {
        ObjectId itemId = itemManager.addItem(100, "Album", "Music");
        itemManager.setUnavailable(itemId);
        Item item = itemManager.getItem(itemId);
        Assertions.assertFalse(item.isAvailable());
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
