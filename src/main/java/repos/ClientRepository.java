package repos;

import models.*;
import jakarta.persistence.*;


public class ClientRepository implements Repository<Client> {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");

    @Override
    public void create(Client client) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(client);
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public Client get(long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.find(Client.class, id);
        }
    }

    @Override
    public void update(Client client) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.merge(client);
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public void delete(Client client) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            Client managedClientType = entityManager.find(Client.class, client.getPersonalId());
            entityManager.remove(managedClientType);
            entityManager.getTransaction().commit();
        }
    }

    public void archive(long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            Client client = entityManager.find(Client.class, id);
            client.setArchive(true);
            entityManager.getTransaction().commit();
        }
    }

    public void unarchive(long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            Client client = entityManager.find(Client.class, id);
            client.setArchive(false);
            entityManager.getTransaction().commit();
        }
    }
}
