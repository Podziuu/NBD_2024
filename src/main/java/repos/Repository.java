package repos;

public interface Repository<T> {
    long create(T t);
    T get(long id);
    void update(T t);
    void delete(T t);
}
