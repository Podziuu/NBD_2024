package repos;

public interface Repository<T> {
    void create(T t);
    T get(long id);
    void update(T t);
    void delete(T t);
}
