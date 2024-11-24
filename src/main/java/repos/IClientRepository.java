package repos;

import models.Client;
import org.bson.types.ObjectId;

public interface IClientRepository {
    ObjectId addClient(Client client);
    Client getClient(ObjectId id);
    void updateClient(Client client);
    void removeClient(ObjectId id);
}
