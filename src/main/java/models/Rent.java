package models;

import exceptions.ParameterException;
import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "rents")
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "begin_time")
    private LocalDateTime beginTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "rent_cost")
    private int rentCost;

    @Column(name = "archive")
    private boolean archive = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public Rent() {}

    public Rent(LocalDateTime beginTime, Client client, Item item) throws ParameterException {
        this.beginTime = (beginTime != null) ? beginTime : LocalDateTime.now();
        this.client = client;
        this.item = item;
        this.item.setAvailable(false);
    }

    public void endRent(LocalDateTime endTime) {
        if (this.endTime != null) {
            return;
        }

        if (endTime == null || endTime.isBefore(beginTime)) {
            this.endTime = beginTime;
        } else {
            this.endTime = endTime;
        }

        double cost = item.getActualPrice() * getRentDays();
        this.rentCost = client.getClientType().applyDiscount((int) Math.round(cost));

        this.item.setAvailable(true);
    }

    public long getRentDays() {
        if (endTime == null) {
            return 0;
        }
        Duration duration = Duration.between(beginTime, endTime);
        long hours = duration.toHours();

        return (hours % 24 == 0) ? hours / 24 : hours / 24 + 1;
    }

    public long getId() {
        return id;
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
        return "Rent ID: " + id + ", Client: " + client.getFirstName() + ", Item: " + item.getItemName();
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

    public void setClient(Client client) {
        this.client = client;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
