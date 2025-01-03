package repos;

import models.Item;
import java.util.UUID;

public interface IItemRepository {
    UUID addItem(Item item);
    Item getItem(UUID id);
    void updateItem(Item item);
    void removeItem(UUID id);
}
