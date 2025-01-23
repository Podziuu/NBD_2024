import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import config.MongoEntity;
import managers.ClientManager;
import managers.ItemManager;
import managers.RentManager;
import models.Client;
import models.Item;
import models.MusicGenre;
import models.Rent;
import org.bson.types.ObjectId;
import repos.ClientRepository;
import repos.ItemRepository;
import repos.RentRepository;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Producent producent = new Producent();
        MongoEntity mongoEntity = new MongoEntity();
        MongoDatabase database = mongoEntity.getDatabase();
        MongoCollection<Rent>  rentCollection = database.getCollection("rents", Rent.class);
        MongoCollection<Client> clientCollection = database.getCollection("clients", Client.class);
        MongoCollection<Item> itemCollection = database.getCollection("items", Item.class);

        RentRepository rentRepository = new RentRepository(rentCollection);
        ClientRepository clientRepository = new ClientRepository(clientCollection);
        ItemRepository itemRepository = new ItemRepository(itemCollection);

        ClientManager clientManager = new ClientManager(clientRepository);
        ItemManager itemManager = new ItemManager(itemRepository);
        RentManager rentManager = new RentManager(rentRepository, clientManager, itemManager);

        ObjectId clientId = clientManager.addClient("Jan", "Kowalski", 123456789, "DiamondMembership");
        Client client = clientManager.getClient(clientId);

        ObjectId itemId = itemManager.addMusic(100, "Album", MusicGenre.Metal, true);
        Item item = itemManager.getItem(itemId);

        LocalDateTime beginTime = LocalDateTime.now();
        ObjectId rentId = rentManager.rentItem(beginTime, client, item);

        Rent rent = rentManager.getRent(rentId);

        producent.sendRent(rent);
        Thread.sleep(10000);
    }
}
