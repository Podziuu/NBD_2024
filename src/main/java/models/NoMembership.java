package models;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("NoMembership")
public class NoMembership extends ClientType {

    public NoMembership() {
        super(5, 0);
    }

//    @Override
//    public int applyDiscount(int price) {
//        return price; // Brak zniżki, zwracamy pełną cenę
//    }

    @Override
    public String getClientTypeInfo() {
        return "No Membership: " + this.getMaxArticles() + " articles, no discount";
    }
}
