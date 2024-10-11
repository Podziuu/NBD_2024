package repos;

import exceptions.LogicException;
import models.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientRepository {
    private List<Client> clientRepositoryList;

    public ClientRepository() {
        clientRepositoryList = new ArrayList<>();
    }

    // Znalezienie klienta po jego ID
    public Client find(String id) {
        for (Client client : clientRepositoryList) {
            if (client.getPersonalId().equals(id)) {
                return client;
            }
        }
        return null; // W Javie używamy null zamiast NULL z C++
    }

    // Pobranie klienta po indeksie
    public Client get(int i) {
        if (i >= clientRepositoryList.size()) {
            return null;
        }
        return clientRepositoryList.get(i);
    }

    // Dodanie nowego klienta
    public void add(Client client) throws LogicException {
        if (client != null) {
            if (find(client.getPersonalId()) != null) {
                throw new LogicException("Client with this ID already exists in repository");
            }
            clientRepositoryList.add(client);
        }
    }

    // Usunięcie klienta
    public void remove(Client client) {
        if (client != null) {
            clientRepositoryList.removeIf(c -> c.equals(client)); // Użycie removeIf zamiast pętli for
        }
    }

    // Generowanie raportu z listy klientów
    public String report() {
        StringBuilder report = new StringBuilder();
        for (Client client : clientRepositoryList) {
            report.append(client.getInfo());
        }
        return report.toString();
    }

    // Pobranie liczby klientów w repozytorium
    public int getSize() {
        return clientRepositoryList.size();
    }

    // Pobranie listy wszystkich klientów
    public List<Client> getAll() {
        return new ArrayList<>(clientRepositoryList); // Zwrócenie kopii listy klientów
    }
}
