
package repos;

import exceptions.LogicException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import models.Item;

public class ItemRepository implements Repository<Item> {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default" );

    @Override
    public void create(Item item) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(item);
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public Item get(long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.find(Item.class, id);
        }
    }

    @Override
    public void update(Item item) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.merge(item);
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public void delete(Item item) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            Item managedItem = entityManager.find(Item.class, item.getItemId());
            entityManager.remove(managedItem);
            entityManager.getTransaction().commit();
        }
    }
}
