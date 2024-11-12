package repos;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import models.Client;
import org.bson.types.ObjectId;

public class ClientRepository {
    private final MongoCollection<Client> clientCollection;

    public ClientRepository(MongoCollection<Client> clientCollection) {
        this.clientCollection = clientCollection;
    }

    public ObjectId addClient(Client client) {
        InsertOneResult result = clientCollection.insertOne(client);
        client.setId(result.getInsertedId().asObjectId().getValue());
        return result.getInsertedId().asObjectId().getValue();
    }

    public Client getClient(ObjectId id) {
        return clientCollection.find(Filters.eq("_id", id)).first();
    }

    public void updateClient(Client client) {
        BasicDBObject object = new BasicDBObject();
        object.put("_id", client.getId());
        clientCollection.replaceOne(object, client);
    }

    public void removeClient(ObjectId id) {
        BasicDBObject object = new BasicDBObject();
        object.put("_id", id);
        clientCollection.deleteOne(object);
    }
}