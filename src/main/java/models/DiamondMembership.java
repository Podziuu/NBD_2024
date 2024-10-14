package models;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("DiamondMembership")
public class DiamondMembership extends ClientType {

    public DiamondMembership() {
       super("DiamondMembership", 15, 30);
    }

    @Override
    public String getClientTypeInfo() {
        return "Diamond Membership: " + this.getMaxArticles() + " articles, discount: " + discount + "%";
    }
}
