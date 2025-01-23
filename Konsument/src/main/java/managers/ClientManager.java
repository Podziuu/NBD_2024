package managers;

import org.bson.types.ObjectId;
import repos.ClientRepository;

import models.Client;
import models.ClientType;

import java.util.Map;

public class ClientManager {
    private final ClientRepository clientRepository;
    private static final Map<String, ClientType> clientTypes = Map.of(
            "DiamondMembership", ClientType.createDiamondMembership(),
            "Membership", ClientType.createMembership(),
            "NoMembership", ClientType.createNoMembership()
    );

    public ClientManager(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ObjectId addClient(String firstName, String lastName, long personalID, String clientType) {
        ClientType type = getClientType(clientType);
        return clientRepository.addClient(new Client(personalID, firstName, lastName, type));
    }

    public Client getClient(ObjectId id) {
        return clientRepository.getClient(id);
    }

    public void updateClient(ObjectId id, String firstName, String lastName, String clientType) {
        if (clientRepository.getClient(id) == null) {
            throw new NullPointerException("Client not found");
        }
        Client client = clientRepository.getClient(id);
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setClientType(getClientType(clientType));
        System.out.println(client.getInfo());
        clientRepository.updateClient(client);
    }

    public void addRent(ObjectId id, ObjectId rentId) {
        if (clientRepository.getClient(id) == null) {
            throw new NullPointerException("Client not found");
        }
        Client client = clientRepository.getClient(id);
        client.addRent(rentId);
        clientRepository.addRentToClient(client.getId(), rentId);
    }

    public void removeRent(ObjectId id, ObjectId rentId) {
        if (clientRepository.getClient(id) == null) {
            throw new NullPointerException("Client not found");
        }
        Client client = clientRepository.getClient(id);
        client.removeRent(rentId);
        clientRepository.updateClient(client);
    }

    public void removeClient(ObjectId id) {
        if (clientRepository.getClient(id) == null) {
            throw new NullPointerException("Client not found");
        }
        clientRepository.removeClient(id);
    }

    public void archiveClient(ObjectId id) {
        if (clientRepository.getClient(id) == null) {
            throw new NullPointerException("Client not found");
        }
        Client client = clientRepository.getClient(id);
        client.setArchive(true);
        clientRepository.updateClient(client);
    }

    public void unarchiveClient(ObjectId id) {
        if (clientRepository.getClient(id) == null) {
            throw new NullPointerException("Client not found");
        }
        Client client = clientRepository.getClient(id);
        client.setArchive(false);
        clientRepository.updateClient(client);
    }

    private ClientType getClientType(String clientType) {
        ClientType type = clientTypes.get(clientType);
        if (type == null) {
            throw new IllegalArgumentException("Invalid client type");
        }
        return type;
    }
}