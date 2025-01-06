package managersTests;

import managers.ItemManager;
import models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.datastax.oss.driver.api.core.CqlSession;
import config.CassandraConfig;
import repos.ItemRepository;
import java.util.UUID;

public class ItemManagerTest {

    private ItemManager itemManager;
    private CassandraConfig cassandraConfig;
    private CqlSession session;
    private ItemRepository itemRepository;

    @BeforeEach
    public void setUp() throws Exception {
        cassandraConfig = new CassandraConfig();

        session = cassandraConfig.getSession();
        if (session == null) {
            throw new IllegalStateException("CqlSession jest null. Sprawdź inicjalizację w CassandraConfig.");
        }

        itemRepository = new ItemRepository(session);
        itemManager = new ItemManager(itemRepository);

//        session.execute("TRUNCATE mediastore.comics_items");
//        session.execute("TRUNCATE mediastore.movies");
//        session.execute("TRUNCATE mediastore.music_items");
        session.execute("TRUNCATE mediastore.items_by_id");
    }

//    @AfterEach
//    public void tearDown() throws Exception {
//        if (cassandraConfig != null) {
//            cassandraConfig.close();
//        }
//    }

    @Test
    public void testAddMusic() {
        String itemName = "Jazz Album";
        int basePrice = 100;
        MusicGenre genre = MusicGenre.Jazz;
        boolean vinyl = true;

        UUID itemId = itemManager.addMusic(basePrice, itemName, genre, vinyl);

        Item item = itemRepository.getItem(itemId);
        assertNotNull(item);

        assertEquals(basePrice, item.getBasePrice());
        assertEquals(itemName, item.getItemName());
//        assertEquals(genre, music.getGenre());
//        assertTrue(music.isVinyl());
    }

    @Test
    public void testAddMovie() {
        String itemName = "Action Movie";
        int basePrice = 150;
        int minutes = 120;
        boolean casette = false;

        UUID itemId = itemManager.addMovie(basePrice, itemName, minutes, casette);

        Item item = itemRepository.getItem(itemId);
        assertNotNull(item);
        assertTrue(item instanceof Movie);
        Movie movie = (Movie) item;
        assertEquals(basePrice, movie.getBasePrice());
        assertEquals(itemName, movie.getItemName());
//        assertEquals(minutes, movie.getMinutes());
//        assertFalse(movie.isCasette());
    }

    @Test
    public void testAddComics() {
        String itemName = "Superhero Comics";
        int basePrice = 80;
        int pageNumber = 50;

        UUID itemId = itemManager.addComics(basePrice, itemName, pageNumber);

        Item item = itemRepository.getItem(itemId);
        assertNotNull(item);
        assertTrue(item instanceof Comics);
        Comics comics = (Comics) item;
        assertEquals(basePrice, comics.getBasePrice());
//        assertEquals(itemName, comics.getItemName());
//        assertEquals(pageNumber, comics.getPageNumber());
    }

    @Test
    public void testUpdateItem() {
        UUID itemId = itemManager.addMusic(100, "Jazz Album", MusicGenre.Jazz, true);
        itemManager.updateItem(itemId, 120, "Updated Jazz Album");

        Item updatedItem = itemRepository.getItem(itemId);
        assertNotNull(updatedItem);
        assertEquals(120, updatedItem.getBasePrice());
        assertEquals("Updated Jazz Album", updatedItem.getItemName());
    }

    @Test
    public void testRemoveItem() {
        UUID itemId = itemManager.addMovie(150, "Action Movie", 120, false);
        itemManager.removeItem(itemId);

        Item removedItem = itemRepository.getItem(itemId);
        assertNull(removedItem);
    }

    @Test
    public void testSetAvailable() {
        UUID itemId = itemManager.addComics(80, "Superhero Comics", 50);
        itemManager.setUnavailable(itemId);
        itemManager.setAvailable(itemId);

        Item item = itemRepository.getItem(itemId);
        assertNotNull(item);
        assertTrue(item.isAvailable());
    }

    @Test
    public void testSetUnavailable() {
        UUID itemId = itemManager.addMusic(100, "Jazz Album", MusicGenre.Jazz, true);
        itemManager.setUnavailable(itemId);

        Item item = itemRepository.getItem(itemId);
        assertNotNull(item);
        assertFalse(item.isAvailable());
    }
}