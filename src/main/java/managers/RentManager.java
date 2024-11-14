package managers;

import com.mongodb.client.ClientSession;
import config.MongoEntity;
import models.Client;
import models.Item;
import models.Rent;
import org.bson.types.ObjectId;
import repos.RentRepository;

import java.time.LocalDateTime;

public class RentManager {
    private final RentRepository rentRepository;
    ClientManager clientManager;
    ItemManager itemManager;
    private final MongoEntity mongoEntity;

    public RentManager(RentRepository rentRepository, ClientManager clientManager, ItemManager itemManager) {
        this.rentRepository = rentRepository;
        this.clientManager = clientManager;
        this.itemManager = itemManager;
        mongoEntity = new MongoEntity();
    }

    public ObjectId rentItem(LocalDateTime beginTime, Client client, Item item) {
        int max = client.getClientType().getMaxArticles();
        int rented = client.getRentsCount();
        if (rented >= max) {
            throw new IllegalArgumentException("Client has reached maximum number of rented items");
        }

        if (!item.isAvailable()) {
            throw new IllegalStateException("Item is already rented out.");
        }

        try (ClientSession session = mongoEntity.getMongoClient().startSession()) {
            session.startTransaction();
            itemManager.setUnavailable(item.getId());
            Rent rent = new Rent(beginTime, client, item);
            ObjectId id = rentRepository.addRent(rent);
            System.out.println("Rent added: " + id);
            clientManager.addRent(client.getId(), id);
            session.commitTransaction();
            return id;
        } catch (Exception e) {
            throw new RuntimeException("Failed to rent item: " + e.getMessage(), e);
        }
    }

    public void returnItem(ObjectId rentId) {
        try (ClientSession session = mongoEntity.getMongoClient().startSession()) {
            session.startTransaction();
            Rent rent = rentRepository.getRent(rentId);
            Item item = rent.getItem();
            itemManager.setAvailable(item.getId());
            rent.setEndTime(LocalDateTime.now());
            rent.setArchive(true);
            rentRepository.updateRent(rent);
            clientManager.removeRent(rent.getClient().getId(), rent.getId());
            session.commitTransaction();
        } catch (Exception e) {
            throw new RuntimeException("Failed to rent item: " + e.getMessage(), e);
        }
    }

    public void removeRent(ObjectId rentId) {
        Rent rent = rentRepository.getRent(rentId);
        rentRepository.removeRent(rentId);
        clientManager.removeRent(rent.getClient().getId(), rent.getId());
    }

    public Rent getRent(ObjectId rentId) {
        return rentRepository.getRent(rentId);
    }

}