package com.dao;

import com.entity.Storage;
import com.exception.InternalServerError;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

@Repository
public class StorageDAO extends GenericDAO<Storage> {
    private static final String DELETE_STORAGE = "DELETE FROM STORAGE WHERE ID=?";

    @Override
    public void delete(long id) throws InternalServerError {
        try( Session session = createSessionFactory().openSession()) {
            NativeQuery query = session.createNativeQuery(DELETE_STORAGE);
            query.addEntity(Storage.class);
            query.setParameter(1,id);
            query.executeUpdate();
        }catch (HibernateException e){
            System.err.println(e.getMessage());
            throw new InternalServerError("Internal Server Error");
        }

    }
}
