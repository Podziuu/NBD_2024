package models;

import com.datastax.oss.driver.api.mapper.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@CqlName("clients_by_id")
public class Client {
    @PartitionKey
    @CqlName("client_id")
    private UUID id;

    @CqlName("personal_id")
    private long personalID;

    @CqlName("first_name")
    private String firstName;

    @CqlName("last_name")
    private String lastName;

    @CqlName("archive")
    private boolean archive;

    @ClusteringColumn(0)
    @CqlName("client_type")
    private ClientType clientType;

    @CqlName("rents")
    private List<UUID> rents;

    public Client(UUID id, long personalID, String firstName, String lastName, ClientType clientType) {
        this.id = id;
        this.personalID = personalID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.clientType = clientType;
        this.archive = false;
        this.rents = new ArrayList<>();
    }

    public Client() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getPersonalId() {
        return personalID;
    }

    public void setPersonalId(long personalId) {
        this.personalID = personalId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public List<UUID> getRents() {
        return rents;
    }

    public void setRents(List<UUID> rents) {
        this.rents = rents;
    }

    public void addRent(UUID rent) {
        this.rents.add(rent);
    }

    public void removeRent(UUID rent) {
        this.rents.remove(rent);
    }

    public int getRentsCount() {
        return rents.size();
    }

    public String getInfo() {
        return "Klient: \nImię: " + firstName + "\nNazwisko: " + lastName + "\nPesel: " + personalID;
    }
}