package com.dao;

import com.entity.File;
import com.entity.Storage;
import com.exception.InternalServerError;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

@Repository
public class StorageDAO extends GenericDAO<Storage> {
    private static final String DELETE_STORAGE = "DELETE FROM STORAGE WHERE ID=?";
    private static final String TRANSFER = "UPDATE С_FILE SET ID_STORAGE = ? WHERE ID_STORAGE=?";

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

    public void transferAll(long from,long to) throws InternalServerError {
        Transaction tr = null;
        try(Session session = createSessionFactory().openSession()) {
            NativeQuery query = session.createNativeQuery(TRANSFER);
            query.addEntity(File.class);
            query.setParameter(1,to);
            query.setParameter(2,from);
            tr = session.getTransaction();
            tr.begin();
            query.executeUpdate();
            tr.commit();
        }catch (HibernateException e){
            System.err.println(e.getMessage());
            if (tr != null)
                tr.rollback();
            throw new InternalServerError("Internal Server Error");
        }

    }
}
