package models;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Membership")
public class Membership extends ClientType {

    public Membership() {
        super("Membership", 10, 20);
    }

    @Override
    public String getClientTypeInfo() {
        return "Membership: " + this.getMaxArticles() + " articles, discount: " + discount + "%";
    }
}
