package models;

import java.util.Objects;

public class ClientType {
    private int maxArticles;
    private int discount;

    public ClientType(int maxArticles, int discount) {
        this.maxArticles = maxArticles;
        this.discount = discount;
    }

    public int getMaxArticles() {
        return maxArticles;
    }

    public static ClientType fromString(String clientTypeString) {
        switch (clientTypeString) {
            case "DiamondMembership":
                return ClientType.createDiamondMembership();
            case "Membership":
                return ClientType.createMembership();
            case "NoMembership":
                return ClientType.createNoMembership();
            default:
                throw new IllegalArgumentException("Invalid client type");
        }
    }

    @Override
    public String toString() {
        if (this.maxArticles == 15 && this.discount == 30) {
            return "DiamondMembership";
        } else if (this.maxArticles == 10 && this.discount == 20) {
            return "Membership";
        } else if (this.maxArticles == 2 && this.discount == 0) {
            return "NoMembership";
        }
        return "Unknown";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ClientType that = (ClientType) obj;
        return this.maxArticles == that.maxArticles && this.discount == that.discount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxArticles, discount);
    }

    public void setMaxArticles(int maxArticles) {
        this.maxArticles = maxArticles;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public static ClientType createDiamondMembership() {
        return new ClientType(15, 30);
    }

    public static ClientType createMembership() {
        return new ClientType(10, 20);
    }

    public static ClientType createNoMembership() {
        return new ClientType(2, 0);
    }
}