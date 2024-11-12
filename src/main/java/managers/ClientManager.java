package managers;

import org.bson.types.ObjectId;
import repos.ClientRepository;

import models.Client;
import models.ClientType;
import models.DiamondMembership;
import models.Membership;
import models.NoMembership;

public class ClientManager {
    private final ClientRepository clientRepository;

    public ClientManager(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void addClient(String firstName, String lastName, long personalID, String clientType) {
        ClientType type = getClientType(clientType);
        clientRepository.addClient(new Client(personalID, firstName, lastName, type));
    }

    public Client getClient(ObjectId id) {
        return clientRepository.getClient(id);
    }

    public void updateClient(ObjectId id, String firstName, String lastName, String clientType) {
        Client client = clientRepository.getClient(id);
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setClientType(getClientType(clientType));
        clientRepository.updateClient(client);
    }

    public void removeClient(ObjectId id) {
        clientRepository.removeClient(id);
    }

    public void archiveClient(ObjectId id) {
        Client client = clientRepository.getClient(id);
        client.setArchive(true);
        clientRepository.updateClient(client);
    }

    public void unarchiveClient(ObjectId id) {
        Client client = clientRepository.getClient(id);
        client.setArchive(false);
        clientRepository.updateClient(client);
    }

    private ClientType getClientType(String clientType) {
        return switch (clientType) {
            case "DiamondMembership" -> new DiamondMembership();
            case "Membership" -> new Membership();
            case "NoMembership" -> new NoMembership();
            default -> throw new IllegalArgumentException("Invalid client type");
        };
    }
}