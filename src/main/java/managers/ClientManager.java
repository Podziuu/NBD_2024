package managers;

import org.bson.types.ObjectId;
import repos.CachedClientRepository;
import repos.ClientRepository;

import models.Client;
import models.ClientType;

import java.util.Map;

public class ClientManager {
    private final ClientRepository clientRepository;
    private final CachedClientRepository cachedClientRepository;
    private static final Map<String, ClientType> clientTypes = Map.of(
            "DiamondMembership", ClientType.createDiamondMembership(),
            "Membership", ClientType.createMembership(),
            "NoMembership", ClientType.createNoMembership()
    );

    public ClientManager(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
        this.cachedClientRepository = new CachedClientRepository();
    }

    public ObjectId addClient(String firstName, String lastName, long personalID, String clientType) {
        ClientType type = getClientType(clientType);
        return cachedClientRepository.addClient(new Client(personalID, firstName, lastName, type));
    }

    public Client getClient(ObjectId id) {
        return cachedClientRepository.getClient(id);
    }

    public void updateClient(ObjectId id, String firstName, String lastName, String clientType) {
        if (cachedClientRepository.getClient(id) == null) {
            throw new NullPointerException("Client not found");
        }
        Client client = cachedClientRepository.getClient(id);
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setClientType(getClientType(clientType));
        cachedClientRepository.updateClient(client);
    }

    public void addRent(ObjectId id, ObjectId rentId) {
        if (cachedClientRepository.getClient(id) == null) {
            throw new NullPointerException("Client not found");
        }
        Client client = cachedClientRepository.getClient(id);
        cachedClientRepository.addRentToClient(client, rentId);
    }

    public void removeRent(ObjectId id, ObjectId rentId) {
        if (cachedClientRepository.getClient(id) == null) {
            throw new NullPointerException("Client not found");
        }
        Client client = cachedClientRepository.getClient(id);
        cachedClientRepository.removeRentFromClient(client, rentId);
    }

    public void removeClient(ObjectId id) {
        if (cachedClientRepository.getClient(id) == null) {
            throw new NullPointerException("Client not found");
        }
        cachedClientRepository.removeClient(id);
    }

    public void archiveClient(ObjectId id) {
        if (cachedClientRepository.getClient(id) == null) {
            throw new NullPointerException("Client not found");
        }
        Client client = cachedClientRepository.getClient(id);
        cachedClientRepository.archiveClient(client);
    }

    public void unarchiveClient(ObjectId id) {
        if (cachedClientRepository.getClient(id) == null) {
            throw new NullPointerException("Client not found");
        }
        Client client = cachedClientRepository.getClient(id);
        cachedClientRepository.unarchiveClient(client);
    }

    private ClientType getClientType(String clientType) {
        ClientType type = clientTypes.get(clientType);
        if (type == null) {
            throw new IllegalArgumentException("Invalid client type");
        }
        return type;
    }
}