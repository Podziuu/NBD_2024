package managers;

import models.Client;
import models.ClientType;
import repos.ClientRepository;
import repos.RentRepository;

import java.util.Map;
import java.util.UUID;

public class ClientManager {
    private final ClientRepository clientRepository;
    private final RentRepository rentRepository;
    private static final Map<String, ClientType> clientTypes = Map.of(
            "DiamondMembership", ClientType.createDiamondMembership(),
            "Membership", ClientType.createMembership(),
            "NoMembership", ClientType.createNoMembership()
    );

    public ClientManager(ClientRepository clientRepository, RentRepository rentRepository) {
        this.clientRepository = clientRepository;
        this.rentRepository = rentRepository;
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

    public boolean hasReachedRentalLimit(UUID clientId) {
        Client client = getClient(clientId);
        ClientType clientType = client.getClientType();
        int maxAllowed = clientType.getMaxArticles();
        int currentRentals = rentRepository.getRentByClientId(clientId).size();
        return currentRentals >= maxAllowed;
    }

    public void removeClient(UUID id) {
        rentRepository.getRentByClientId(id).forEach(rent -> rentRepository.removeRent(rent.getId()));
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