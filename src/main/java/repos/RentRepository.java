package repos;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import exceptions.LogicException;
import models.Rent;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class RentRepository {
    private final MongoCollection<Rent> rentCollection;

    public RentRepository(MongoCollection<Rent> rentCollection) {
        this.rentCollection = rentCollection;
    }

    public ObjectId addRent(Rent rent) {
        InsertOneResult result = rentCollection.insertOne(rent);
        rent.setRentId(result.getInsertedId().asObjectId().getValue());
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
}
