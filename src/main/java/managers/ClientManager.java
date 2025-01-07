package managers;

import models.Client;
import models.ClientType;
import repos.ClientRepository;

import java.util.Map;
import java.util.UUID;

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

    public UUID addClient(String firstName, String lastName, long personalID, ClientType clientType) {
        Client client = new Client(UUID.randomUUID(), personalID, firstName, lastName, clientType);
        clientRepository.addClient(client);
        return client.getId();
    }

    public Client getClient(UUID id) {
        Client client = clientRepository.getClient(id);
        if (client == null) {
            throw new NullPointerException("Client not found");
        }
        return client;
    }

    public void updateClient(UUID id, String firstName, String lastName, ClientType clientType) {
        Client client = getClient(id);
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setClientType(clientType);
        clientRepository.updateClient(client);
    }

//    public void addRent(UUID clientId, UUID rentId) {
//        Client client = getClient(clientId);
//        client.addRent(rentId);
//        clientRepository.updateClient(client);
//    }
//
//    public void removeRent(UUID clientId, UUID rentId) {
//        Client client = getClient(clientId);
//        client.removeRent(rentId);
//        clientRepository.updateClient(client);
//    }

    public void removeClient(UUID id) {
        Client client = getClient(id);
        clientRepository.removeClient(id);
    }

    public void archiveClient(UUID id) {
        Client client = getClient(id);
        client.setArchive(true);
        clientRepository.updateClient(client);
    }

    public void unarchiveClient(UUID id) {
        Client client = getClient(id);
        client.setArchive(false);
        clientRepository.updateClient(client);
    }
}