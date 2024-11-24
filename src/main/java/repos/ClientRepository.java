package repos;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import config.AbstractMongoEntity;
import models.Client;
import org.bson.types.ObjectId;

public class ClientRepository extends AbstractMongoEntity implements IClientRepository {
    private final MongoCollection<Client> clientCollection;

    public ClientRepository() {
        initDbConnection();
        this.clientCollection = database.getCollection("clients", Client.class);
    }

    @Override
    public ObjectId addClient(Client client) {
        InsertOneResult result = clientCollection.insertOne(client);
        client.setId(result.getInsertedId().asObjectId().getValue());
        return result.getInsertedId().asObjectId().getValue();
    }

    @Override
    public Client getClient(ObjectId id) {
        return clientCollection.find(Filters.eq("_id", id)).first();
    }

    @Override
    public void updateClient(Client client) {
        BasicDBObject object = new BasicDBObject();
        object.put("_id", client.getId());
        clientCollection.replaceOne(object, client);
    }

    @Override
    public void removeClient(ObjectId id) {
        BasicDBObject object = new BasicDBObject();
        object.put("_id", id);
        clientCollection.deleteOne(object);
    }

    public Client addRentToClient(ObjectId clientId, ObjectId rentId) {
        clientCollection.updateOne(
                Filters.eq("_id", clientId),
                Updates.push("rents", rentId)
        );

        Client updatedClient = clientCollection.find(Filters.eq("_id", clientId)).first();
        return updatedClient;
    }

    @Override
    public void close() throws Exception {
        mongoClient.close();
    }
}