package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.bson.codecs.pojo.annotations.*;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Client {
    @BsonId
    private ObjectId id;
    @BsonProperty("personalId")
    private long personalID;
    @BsonProperty("firstName")
    private String firstName;
    @BsonProperty("lastName")
    private String lastName;
    @BsonProperty("archive")
    private boolean archive;
    @BsonProperty("clientType")
    private ClientType clientType;
    @BsonProperty("rents")
    private List<ObjectId> rents;

    @BsonCreator
    public Client(@BsonProperty("personalId") long personalID,
                  @BsonProperty("firstName") String firstName,
                  @BsonProperty("lastName") String lastName,
                  @BsonProperty("clientType") ClientType clientType) {
        this.personalID = personalID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.clientType = clientType;
        this.archive = false;
        this.rents = new ArrayList<>();
    }

    public Client() {

    }

    @BsonIgnore
    public String getInfo() {
        return "Klient: \n Imię: " + firstName + "\n Nazwisko: " + lastName + "\n Pesel: " + personalID;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void addRent(ObjectId rent) {
        List<ObjectId> updatedRents = new ArrayList<>(rents);
        updatedRents.add(rent);
        this.rents = updatedRents;
    }

    public void removeRent(ObjectId rent) {
        rents.remove(rent);
    }

    public void setRents(List<ObjectId> rents) {
        this.rents = rents;
    }

    public List<ObjectId> getRents() {
        return rents;
    }

    @BsonIgnore
    public int getRentsCount() {
        return rents.size();
    }

    public long getPersonalId() {
        return personalID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public boolean isArchive() {
        return archive;
    }
}