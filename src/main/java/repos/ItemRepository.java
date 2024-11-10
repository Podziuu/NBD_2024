package repos;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import config.AbstractMongoEntity;
import config.MongoEntity;
import models.Item;

public class ItemRepository extends MongoEntity {

    @Override
    public void close() throws Exception {

    }

    public ItemRepository() {
        this.initDbConnection();
    }

    public void create(Item item) {
        MongoCollection<Item> collection = getDatabase().getCollection("items", Item.class);
        collection.insertOne(item);
    }

    public MongoCollection<Item> getItems() {
        return getDatabase().getCollection("items", Item.class);
    }

    public Item read(long id) {
        MongoCollection<Item> collection = getDatabase().getCollection("items", Item.class);
        Item item = collection.find(Filters.eq("_id", id)).first();
        return item;
    }

    public void update(Item item) {
        MongoCollection<Item> collection = getDatabase().getCollection("items", Item.class);
        BasicDBObject object = new BasicDBObject();
        object.put("_id", item.getItemId());
        collection.updateOne(object,
                Updates.combine(
                        Updates.set("base_price", item.getBasePrice()),
                        Updates.set("item_name", item.getItemName()),
                        Updates.set("available", item.isAvailable())
                )
        );
    }

    public void delete(long id) {
        MongoCollection<Item> collection = getDatabase().getCollection("items", Item.class);
        BasicDBObject object = new BasicDBObject();
        object.put("_id", id);
        collection.deleteOne(object);
    }
}
