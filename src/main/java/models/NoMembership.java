package models;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("NoMembership")
public class NoMembership extends ClientType {

    public NoMembership() {
        super("NoMembership", 5, 0);
    }

    @Override
    public String getClientTypeInfo() {
        return "No Membership: " + this.getMaxArticles() + " articles, no discount";
    }
}
