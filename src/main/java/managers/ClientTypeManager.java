package managers;

import repos.*;
import models.*;

public class ClientTypeManager {
    private final ClientTypeRepository clientTypeRepository;

    public ClientTypeManager() {
        this.clientTypeRepository = new ClientTypeRepository();
    }

    public void createClientType(ClientType clientType) {
        clientTypeRepository.create(clientType);
    }

    public void createBasicClientTypes() {
        createDiamondMembership();
        createMembership();
        createNoMembership();
    }

    public ClientType getClientType(long id) {
        return clientTypeRepository.get(id);
    }

    public ClientType getClientTypeByType(String type) {
        return clientTypeRepository.getByType(type);
    }

    public void updateClientType(ClientType clientType) {
        clientTypeRepository.update(clientType);
    }

    public void deleteClientType(ClientType clientType) {
        clientTypeRepository.delete(clientType);
    }

    public void createDiamondMembership() {
        clientTypeRepository.create(new DiamondMembership());
    }

    public void createMembership() {
        clientTypeRepository.create(new Membership());
    }

    public void createNoMembership() {
        clientTypeRepository.create(new NoMembership());
    }
}
