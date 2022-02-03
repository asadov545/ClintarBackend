package org.artisoft.dal.dao;

import java.util.HashMap;
import java.util.List;

public interface BaseDao<T> {
    T getById(long id);
    long insert(T t);
    boolean update(T t);
    boolean delete(long id);

    default List<T> filterData(HashMap<String, String> map){
        return null;
    }
    default List<T>  getAll(){
        return null;
    }

}
