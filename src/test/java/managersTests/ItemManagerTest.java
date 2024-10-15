package managersTests;

import exceptions.ItemAvailableException;
import exceptions.ItemNotAvailableException;
import exceptions.LogicException;
import managers.*;
import models.*;
import org.junit.jupiter.api.*;

public class ItemManagerTest {
    private ItemManager itemManager;

    @BeforeEach
    void setUp() {
        itemManager = new ItemManager();
    }

    @Test
    void registerMusicTest() throws ItemAvailableException, LogicException {
        itemManager.registerMusic(1, 100, "Rock Album", MusicGenre.POP, true);
        Item item = itemManager.getItem(1);
        Assertions.assertNotNull(item);
        Assertions.assertInstanceOf(Music.class, item);
        Assertions.assertEquals("Rock Album", item.getItemName());
    }

    @Test
    void registerMovieTest() throws ItemAvailableException, LogicException {
        itemManager.registerMovie(1, 200, "Skazani", 120, false);
        Item item = itemManager.getItem(1);
        Assertions.assertNotNull(item);
        Assertions.assertInstanceOf(Movie.class, item);
        Assertions.assertEquals("Skazani", item.getItemName());
        Assertions.assertEquals(120, ((Movie) item).getMinutes());
    }

    @Test
    void registerComicsTest() throws ItemAvailableException, LogicException {
        itemManager.registerComics(1, 50, "Superhero Comics", 150);
        Item item = itemManager.getItem(1);
        Assertions.assertNotNull(item);
        Assertions.assertInstanceOf(Comics.class, item);
        Assertions.assertEquals("Superhero Comics", item.getItemName());
        Assertions.assertEquals(150, ((Comics) item).getPagesNumber());
    }

    @Test
    void deleteItemTest() throws ItemNotAvailableException, ItemAvailableException, LogicException {
        itemManager.registerMusic(1, 100, "Rock Album", MusicGenre.POP, true);
        Item item = itemManager.getItem(1);
        itemManager.deleteItem(1);
        Assertions.assertNull(itemManager.getItem(1));
    }

    @Test
    void deleteItemNotAvailableTest() throws ItemAvailableException, LogicException {
        Assertions.assertThrows(ItemNotAvailableException.class, () -> itemManager.deleteItem(512));
    }

    @Test
    void updateMusicTest() throws ItemAvailableException, LogicException, ItemNotAvailableException {
        itemManager.registerMusic(1, 100, "Rock Album", MusicGenre.POP, true);
        itemManager.updateItem(1, 120, "Updated Rock Album", MusicGenre.Jazz, true,
                null, null, null);
        Item item = itemManager.getItem(1);
        Assertions.assertInstanceOf(Music.class, item);
        Assertions.assertEquals(120, item.getBasePrice());
        Assertions.assertEquals("Updated Rock Album", item.getItemName());
        Assertions.assertEquals(MusicGenre.Jazz, ((Music) item).getGenre());
    }

    @Test
    void updateMovieTest() throws ItemNotAvailableException, ItemAvailableException, LogicException {
        itemManager.registerMovie(1, 200, "Kiler", 120, false);
        itemManager.updateItem(1, 220, "Updated Movie", null, null,
                150,true, null);
        Item item = itemManager.getItem(1);
        Assertions.assertInstanceOf(Movie.class, item);
        Assertions.assertEquals(220, item.getBasePrice());
        Assertions.assertEquals("Updated Movie", item.getItemName());
        Assertions.assertEquals(150, ((Movie) item).getMinutes());
        Assertions.assertTrue(((Movie) item).isCasette());
    }

    @Test
    void updateComicsTest() throws ItemAvailableException, LogicException, ItemNotAvailableException {
        itemManager.registerComics(1, 50, "Superhero Comics", 150);
        itemManager.updateItem(1, 60, "Updated Comics", null, null, null, null, 180);
        Item item = itemManager.getItem(1);
        Assertions.assertInstanceOf(Comics.class, item);
        Assertions.assertEquals(60, item.getBasePrice());
        Assertions.assertEquals("Updated Comics", item.getItemName());
        Assertions.assertEquals(180, ((Comics) item).getPagesNumber());
    }
}