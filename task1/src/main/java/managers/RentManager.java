package managers;

import models.Client;
import models.Item;
import models.Rent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RentManager {
    private List<Rent> rents;

    public RentManager() {
        this.rents = new ArrayList<>();
    }

    public Rent rentItem(String rentId, LocalDateTime beginTime, Client client, Item item) {
        Rent rent = new Rent(rentId, beginTime, client, item);
        rents.add(rent);
        return rent;
    }

    public void returnItem(Item item) {
        rents.stream().filter(rent -> rent.getItem().equals(item)).findFirst().ifPresent(rent -> {
            rent.endRent(LocalDateTime.now());
            rent.setArchive(true);
        });
    }

    public List<Rent> getAllClientRents(Client client) {
        List<Rent> clientRents = new ArrayList<>();
        for (Rent rent : rents) {
            if (rent.getClient().equals(client)) {
                clientRents.add(rent);
            }
        }
        return clientRents;
    }

    public List<Rent> getAll() {
        return rents;
    }

    public Optional<Rent> find(String rentId) {
        return rents.stream().filter(rent -> rent.getRentId().equals(rentId)).findFirst();
    }

    public String report() {
        StringBuilder report = new StringBuilder();
        for (Rent rent : rents) {
            report.append(rent.getRentInfo()).append("\n");
        }
        return report.toString();
    }

    public List<Rent> getAllArchiveRents() {
        List<Rent> archivedRents = new ArrayList<>();
        for (Rent rent : rents) {
            if (rent.isArchive()) {
                archivedRents.add(rent);
            }
        }
        return archivedRents;
    }
}