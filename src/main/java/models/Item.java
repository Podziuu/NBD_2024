package models;

import jakarta.persistence.*;

@MappedSuperclass // Oznacza, że ta klasa nie będzie miała własnej tabeli w bazie danych
public abstract class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generowanie wartości identyfikatora
    private Long id;

    @Column(name = "base_price") // Nazwa kolumny w bazie danych
    private int basePrice;

    @Column(name = "item_name") // Nazwa kolumny w bazie danych
    private String itemName;
    private boolean available;

    public Item(Long itemId, int basePrice, String itemName) {
        this.id = itemId; // Możesz ustawić id, ale często lepiej pozwolić na automatyczne generowanie
        this.basePrice = basePrice;
        this.itemName = itemName;
    }

    // Domyślny konstruktor
    public Item() {}

    // Metoda do uzyskiwania informacji o przedmiocie
    public String getItemInfo() {
        return "ID: " + id + ", Name: " + itemName + ", Base Price: " + basePrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public long getItemId() {
        return id;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
