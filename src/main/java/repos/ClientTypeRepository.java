package repos;

import models.*;
import jakarta.persistence.*;

public class ClientTypeRepository implements Repository<ClientType> {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");

    @Override
    public void create(ClientType clientType) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(clientType);
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public ClientType get(long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.find(ClientType.class, id);
        }
    }

    @Override
    public void update(ClientType clientType) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.merge(clientType);
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public void delete(ClientType clientType) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.remove(clientType);
            entityManager.getTransaction().commit();
        }
    }
}
