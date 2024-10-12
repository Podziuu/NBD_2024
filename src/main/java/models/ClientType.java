package models;

import jakarta.persistence.*;

@Entity
@Table(name = "client_types")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public class ClientType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Używaj Long zamiast String, aby łatwiej zarządzać ID

    protected int maxArticles;
    protected int discount;
    @Column(name = "type_name")
    protected String typeName;

    // Konstruktory
    public ClientType() {
    }

    public ClientType(String typeName, int maxArticles, int discount) {
        this.typeName = typeName;
        this.maxArticles = maxArticles;
        this.discount = discount;
    }

    // Gettery i settery
    public Long getId() {
        return id;
    }

//    public String getTypeName() {
//        return typeName;
//    }

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


//package models;
//
//import jakarta.persistence.*;
//
//@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//public class ClientType {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;
//    @Column(name = "max_articles")
//    protected int maxArticles;
//    protected int discount;
//
//    public ClientType() {
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public int getMaxArticles() {
//        return maxArticles;
//    }
//
//    public int applyDiscount(int price) {
//        return price;
//    }
//
//    public String getClientTypeInfo() {
//        return "\nMaksymalna ilość wypożyczonych artykułów: " + this.getMaxArticles();
//    }
//}
