package models;

import com.datastax.oss.driver.api.mapper.annotations.*;

import java.time.Instant;
import java.util.UUID;

public class Rent {
    @PartitionKey
    @CqlName("rent_id")
    private UUID id;

    @CqlName("begin_time")
    private Instant beginTime;

    @CqlName("end_time")
    private Instant endTime;

    @CqlName("rent_cost")
    private int rentCost;

    @CqlName("archive")
    private boolean archive;

    @CqlName("client_id")
    private UUID clientId;

    @CqlName("item_id")
    private UUID itemId;

    public Rent(UUID id, Instant beginTime, UUID clientId, UUID itemId) {
        this.id = id;
        this.beginTime = beginTime;
        this.clientId = clientId;
        this.itemId = itemId;
        this.archive = false;
    }

    public Rent() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Instant beginTime) {
        this.beginTime = beginTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public int getRentCost() {
        return rentCost;
    }

    public void setRentCost(int rentCost) {
        this.rentCost = rentCost;
    }

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public long getRentDays() {
        return java.time.Duration.between(beginTime, endTime).toDays();
    }

    public String getRentInfo() {
        return "Rent ID: " + id + ", Client ID: " + clientId + ", Item ID: " + itemId;
    }
}