package repos;

import models.Client;
import java.util.UUID;

public interface IClientRepository {
    void addClient(Client client);
    Client getClient(UUID id);
    void updateClient(Client client);
    void removeClient(UUID id);
}
