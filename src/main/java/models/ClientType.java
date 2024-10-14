package models;

import jakarta.persistence.*;

@Entity
@Table(name = "client_types")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public class ClientType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    protected int maxArticles;
    protected int discount;
    @Column(name = "type_name")
    protected String typeName;

    public ClientType() {
    }

    public ClientType(String typeName, int maxArticles, int discount) {
        this.typeName = typeName;
        this.maxArticles = maxArticles;
        this.discount = discount;
    }

    public long getId() {
        return id;
    }

    public int getMaxArticles() {
        return maxArticles;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int applyDiscount(int price) {
        return price - (price * discount / 100);
    }

    public String getClientTypeInfo() {
        return "typeName" + ": " + maxArticles + " articles, discount: " + discount + "%";
    }
}
