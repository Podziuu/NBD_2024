package repos;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import config.AbstractMongoEntity;
import config.MongoEntity;
import models.Item;
import org.bson.types.ObjectId;

public class ItemRepository {
    private final MongoCollection<Item> itemCollection;

    public ItemRepository(MongoCollection<Item> itemCollection) {
        this.itemCollection = itemCollection;
    }

    public ObjectId addItem(Item item) {
        InsertOneResult result = itemCollection.insertOne(item);
        item.setId(result.getInsertedId().asObjectId().getValue());
        return result.getInsertedId().asObjectId().getValue();
    }

    public Item getItem(ObjectId id) {
        return itemCollection.find(Filters.eq("_id", id)).first();
    }

    public void updateItem(Item item) {
        BasicDBObject object = new BasicDBObject();
        object.put("_id", item.getId());
        itemCollection.replaceOne(object, item);
    }

    public void removeItem(ObjectId id) {
        BasicDBObject object = new BasicDBObject();
        object.put("_id", id);
        itemCollection.deleteOne(object);
    }
}