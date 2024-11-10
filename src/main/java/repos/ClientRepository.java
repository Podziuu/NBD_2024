package repos;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import models.Client;
import org.bson.types.ObjectId;

public class ClientRepository {
    private final MongoCollection<Client> clientCollection;

    public ClientRepository(MongoCollection<Client> clientCollection) {
        this.clientCollection = clientCollection;
    }

    public ObjectId addClient(Client client) {
        InsertOneResult result = clientCollection.insertOne(client);
        return result.getInsertedId().asObjectId().getValue();
    }

    public Client getClient(ObjectId id) {
        return clientCollection.find(Filters.eq("_id", id)).first();
    }

    public void updateClient(Client client) {
        clientCollection.replaceOne(Filters.eq("_id", client.getId()), client);
    }

    public void removeClient(ObjectId id) {
        clientCollection.deleteOne(Filters.eq("_id", id));
    }
}