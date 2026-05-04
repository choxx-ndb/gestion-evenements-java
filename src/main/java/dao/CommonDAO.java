package dao;

import java.util.List;

public interface CommonDAO<T> {
    void add(T obj);
    List<T> selectAll();
    T getById(int id);
    void update(T obj);
    void delete(int id);
}