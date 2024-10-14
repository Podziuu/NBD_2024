package managers;

import models.Client;
import models.ClientType;

import repos.ClientRepository;

import exceptions.ClientExistsException;
import exceptions.ClientNotExistsException;

public class ClientManager {
    private final ClientRepository clientRepository;
    private final ClientTypeManager clientTypeManager;

    public ClientManager() {
        this.clientRepository = new ClientRepository();
        this.clientTypeManager = new ClientTypeManager();
    }

    public void createClient(String firstName, String lastName, String clientType) {
        ClientType type = clientTypeManager.getClientTypeByType(clientType);
        clientRepository.create(new Client(firstName, lastName, type));
    }

    public Client getClient(long id) {
        return clientRepository.get(id);
    }

    public void deleteClient(long id) throws ClientNotExistsException {
        if (clientRepository.get(id) == null) {
            throw new ClientNotExistsException();
        }
        Client client = clientRepository.get(id);
        clientRepository.delete(client);
    }

    public void updateClient(long id, String firstName, String lastName, String clientType)
            throws ClientNotExistsException {
        if (clientRepository.get(id) == null) {
            throw new ClientNotExistsException();
        }
        ClientType type = clientTypeManager.getClientTypeByType(clientType);
        Client client = clientRepository.get(id);
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setClientType(type);
        clientRepository.update(client);
    }

    public void archiveClient(long id) throws ClientNotExistsException {
        if (clientRepository.get(id) == null) {
            throw new ClientNotExistsException();
        }
        clientRepository.archive(id);
    }

    public void unarchiveClient(long id) throws ClientNotExistsException {
        if (clientRepository.get(id) == null) {
            throw new ClientNotExistsException();
        }
        clientRepository.unarchive(id);
    }
}
