//package managers;
//
//import models.Client;
//import models.ClientType;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//public class ClientManager {
//    private List<Client> clients;
//
//    public ClientManager() {
//        this.clients = new ArrayList<>();
//    }
//
//    public String report() {
//        StringBuilder report = new StringBuilder();
//        for (Client client : clients) {
//            report.append(client.getInfo()).append("\n");
//        }
//        return report.toString();
//    }
//
//    public List<Client> getAll() {
//        return clients;
//    }
//
//    public Optional<Client> find(long personalId) {
//        return clients.stream().filter(client -> client.getPersonalId().equals(personalId)).findFirst();
//    }
//
//    public void registerClient(String firstName, String lastName, String personalId, ClientType clientType) {
//        Client newClient = new Client(personalId, firstName, lastName, clientType);
//        clients.add(newClient);
//    }
//}