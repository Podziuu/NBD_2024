package models;

public class Client {
    private String personalID;
    private String firstName;
    private String lastName;
    private boolean archive;
    private ClientType clientType;

    public Client(String personalID, String firstName, String lastName, ClientType clientType) {
        this.personalID = personalID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.clientType = clientType;
    }

    public String getInfo() {
        return "Klient: \n Imię: " + firstName + "\n Nazwisko: " + lastName + "\n Pesel: " + personalID;
    }

    public String getPersonalId() {
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
