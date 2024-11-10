package models;

import org.bson.codecs.pojo.annotations.*;
import org.bson.types.ObjectId;

public class Client {
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

    @BsonCreator
    public Client(@BsonProperty("personalId") long personalID,
                  @BsonProperty("firstName") String firstName,
                  @BsonProperty("lastName") String lastName,
                  @BsonProperty("clientType") ClientType clientType) {
        this.personalID = personalID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.clientType = clientType;
    }

    @BsonIgnore
    public String getInfo() {
        return "Klient: \n Imię: " + firstName + "\n Nazwisko: " + lastName + "\n Pesel: " + personalID;
    }

    public ObjectId getId() {
        return id;
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
