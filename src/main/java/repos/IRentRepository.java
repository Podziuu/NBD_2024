package repos;

import models.Rent;

import java.util.UUID;

public interface IRentRepository {
    void addRent(Rent rent);
    Rent getRent(UUID id);
    void updateRent(Rent rent);
    void removeRent(UUID id);
}
