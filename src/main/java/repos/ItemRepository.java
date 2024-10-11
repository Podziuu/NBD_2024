package repos;

import exceptions.LogicException;
import models.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemRepository {
    private List<Item> itemRepositoryList;

    public ItemRepository() {
        itemRepositoryList = new ArrayList<>();
    }

    // Znalezienie itemu po jego ID
    public Item find(String id) {
        for (Item item : itemRepositoryList) {
            if (item.getItemId().equals(id)) {
                return item;
            }
        }
        return null; // Zwraca null, gdy nie znajdzie itemu
    }

    // Pobranie itemu po indeksie
    public Item get(int i) {
        if (i >= itemRepositoryList.size()) {
            return null; // Zwraca null, gdy indeks wykracza poza listę
        }
        return itemRepositoryList.get(i);
    }

    // Dodanie nowego itemu
    public void add(Item item) throws LogicException {
        if (item != null) {
            if (find(item.getItemId()) != null) {
                throw new LogicException("Item with this ID already exists in repository");
            }
            itemRepositoryList.add(item);
        }
    }

    // Usunięcie itemu
    public void remove(Item item) {
        if (item != null) {
            itemRepositoryList.removeIf(i -> i.equals(item)); // Usuwa item z listy
        }
    }

    // Generowanie raportu z listy itemów
    public String report() {
        StringBuilder report = new StringBuilder();
        for (Item item : itemRepositoryList) {
            report.append(item.getItemInfo()); // Dodaje informacje o każdym itemie do raportu
        }
        return report.toString();
    }

    // Pobranie liczby itemów w repozytorium
    public int getSize() {
        return itemRepositoryList.size();
    }

    // Pobranie wszystkich itemów w repozytorium
    public List<Item> getAll() {
        return new ArrayList<>(itemRepositoryList); // Zwraca kopię listy wszystkich itemów
    }
}
