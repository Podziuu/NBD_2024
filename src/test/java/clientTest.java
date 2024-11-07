import org.junit.jupiter.api.*;
import config.MongoEntity;

public class clientTest {
    private static MongoEntity mongoEntity;

    @BeforeAll
    static void setUp() {
        mongoEntity = new MongoEntity();
    }

    @AfterAll
    static void tearDown() throws Exception {
        mongoEntity.close();
    }

    @Test
    void test() {
        System.out.println("Test");
    }
}
