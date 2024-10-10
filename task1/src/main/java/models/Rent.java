package models;

import java.time.LocalDateTime;

public class Rent {
    private String rentId;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private int rentCost;
    private boolean archive;
    private Client client;
    private Item item;

    public Rent(String rentId, LocalDateTime beginTime, Client client, Item item) {
        this.rentId = rentId;
        this.beginTime = beginTime;
        this.client = client;
        this.item = item;
        this.archive = false; // Domyślnie nie zarchiwizowane
    }

    public void endRent(LocalDateTime endTime) {
        this.endTime = endTime;
        // Możesz tu obliczyć rentCost na podstawie czasu wypożyczenia
    }

    public String getRentId() {
        return rentId;
    }

    public long getRentDays() {
        return java.time.Duration.between(beginTime, endTime).toDays();
    }

    public int getRentCost() {
        return rentCost;
    }

    public Client getClient() {
        return client;
    }

    public Item getItem() {
        return item;
    }

    public String getRentInfo() {
        return "Rent ID: " + rentId + ", Client: " + client.getFirstName() + ", Item: " + item.getItemName();
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }
}
