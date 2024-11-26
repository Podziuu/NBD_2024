package repos;

import models.Item;
import org.bson.types.ObjectId;

public interface IItemRepository {
    ObjectId addItem(Item item);
    Item getItem(ObjectId id);
    void updateItem(Item item);
    void removeItem(ObjectId id);
}