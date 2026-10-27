package repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Repository<T, ID> {
    private final ConcurrentHashMap<ID, T> storage = new ConcurrentHashMap<>();

    public void save(ID id, T entity) {
        storage.put(id, entity);
    }

    public Optional<T> findById(ID id) {
        return Optional.ofNullable(storage.get(id));
    }

    public Collection<T> findAll() {
        return storage.values();
    }

    public List<T> filter(Predicate<T> predicate) {
        return storage.values().stream().filter(predicate).collect(Collectors.toList());
    }

    public boolean deleteById(ID id) {
        return storage.remove(id) != null;
    }

    public void clear() {
        storage.clear();
    }
}