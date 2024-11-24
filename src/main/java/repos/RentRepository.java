package repos;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import config.AbstractMongoEntity;
import models.Rent;
import org.bson.types.ObjectId;

public class RentRepository extends AbstractMongoEntity {
    private final MongoCollection<Rent> rentCollection;

    public RentRepository() {
        initDbConnection();
        this.rentCollection = database.getCollection("rents", Rent.class);
    }

    public ObjectId addRent(Rent rent) {
        InsertOneResult result = rentCollection.insertOne(rent);
        rent.setId(result.getInsertedId().asObjectId().getValue());
        return result.getInsertedId().asObjectId().getValue();
    }

    public Rent getRent(ObjectId id) {
        return rentCollection.find(Filters.eq("_id", id)).first();
    }

    public void updateRent(Rent rent) {
        BasicDBObject object = new BasicDBObject();
        object.put("_id", rent.getId());
        rentCollection.replaceOne(object, rent);
    }

    public void removeRent(ObjectId id) {
        BasicDBObject object = new BasicDBObject();
        object.put("_id", id);
        rentCollection.deleteOne(object);
    }

    @Override
    public void close() throws Exception {
        mongoClient.close();
    }
}