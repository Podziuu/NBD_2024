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

    public void createClient(long personalId, String firstName, String lastName, String clientType)
            throws ClientExistsException, IllegalArgumentException {
        if (clientRepository.get(personalId) != null) {
            throw new ClientExistsException();
        }
        ClientType type = clientTypeManager.getClientTypeByType(clientType);
        if (type == null) {
            throw new IllegalArgumentException("Invalid client type.");
        }
        clientRepository.create(new Client(personalId, firstName, lastName, type));
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
            throws ClientNotExistsException, IllegalArgumentException {
        if (clientRepository.get(id) == null) {
            throw new ClientNotExistsException();
        }
        ClientType type = clientTypeManager.getClientTypeByType(clientType);
        if (type == null) {
            throw new IllegalArgumentException("Invalid client type.");
        }
        clientRepository.update(new Client(id, firstName, lastName, type));
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
