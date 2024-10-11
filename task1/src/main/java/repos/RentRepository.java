package repos;

import exceptions.LogicException;
import models.Rent;

import java.util.ArrayList;
import java.util.List;

public class RentRepository {
    private List<Rent> rentRepositoryList;

    public RentRepository() {
        rentRepositoryList = new ArrayList<>();
    }

    // Znalezienie wynajmu po jego ID
    public Rent find(String id) {
        for (Rent rent : rentRepositoryList) {
            if (rent.getRentId().equals(id)) {
                return rent;
            }
        }
        return null; // Zwraca null, gdy nie znajdzie wynajmu
    }

    // Pobranie wynajmu po indeksie
    public Rent get(int i) {
        if (i >= rentRepositoryList.size()) {
            return null; // Zwraca null, gdy indeks wykracza poza listę
        }
        return rentRepositoryList.get(i);
    }

    // Dodanie nowego wynajmu
    public void add(Rent rent) throws LogicException {
        if (rent != null) {
            if (find(rent.getRentId()) != null) {
                throw new LogicException("Rent with this ID already exists in repository");
            }
            rentRepositoryList.add(rent);
        }
    }

    // Usunięcie wynajmu
    public void remove(Rent rent) {
        if (rent != null) {
            rentRepositoryList.removeIf(r -> r.equals(rent)); // Usuwa wynajem z listy
        }
    }

    // Generowanie raportu z listy wynajmów
    public String report() {
        StringBuilder report = new StringBuilder();
        for (Rent rent : rentRepositoryList) {
            report.append(rent.getRentInfo()); // Dodaje informacje o każdym wynajmie do raportu
        }
        return report.toString();
    }

    // Pobranie liczby wynajmów w repozytorium
    public int getSize() {
        return rentRepositoryList.size();
    }

    // Pobranie wszystkich wynajmów w repozytorium
    public List<Rent> getAll() {
        return new ArrayList<>(rentRepositoryList); // Zwraca kopię listy wszystkich wynajmów
    }
}
