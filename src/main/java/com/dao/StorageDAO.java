package com.dao;

import com.entity.Storage;
import com.exception.BadRequestException;
import com.exception.InternalServerError;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

@Repository
public class StorageDAO extends GenericDAO<Storage> {
    private static final String DELETE_STORAGE = "DELETE FROM STORAGE WHERE ID=? ";

    private static final String VALIDATE_STORAGE = "SELECT ID FROM STORAGE " +
            "WHERE ID = ? AND " +
            "CONTAINS(FORMATS_SUPPORTED,(SELECT FORMATS_SUPPORTED FROM STORAGE WHERE ID = ?)) > 0 AND " +
            "(STORAGE_SIZE - COALESCE((SELECT SUM(FILE_SIZE) FROM С_FILE WHERE ID_STORAGE = ?),0)) > " +
            "(NVL((SELECT SUM(FILE_SIZE) FROM С_FILE WHERE ID_STORAGE = ?),0))";

    private static final String TRANSFER_ALL = "UPDATE С_FILE SET ID_STORAGE = ? " +
            "WHERE ID_STORAGE = ?";


    public int transferAll(long from,long to) throws InternalServerError, BadRequestException {
        Transaction tr = null;
        int result = 0;
        try(Session session = createSessionFactory().openSession()) {
            NativeQuery validateQuery = session.createNativeQuery(VALIDATE_STORAGE);
            NativeQuery updateQuery = session.createNativeQuery(TRANSFER_ALL);
            validateQuery.setParameter(1,to);
            validateQuery.setParameter(2,from);
            validateQuery.setParameter(3,to);
            validateQuery.setParameter(4,from);
            tr = session.getTransaction();
            tr.begin();
            if (validateQuery.getSingleResult()!=null) {
                updateQuery.setParameter(1,to);
                updateQuery.setParameter(2,from);
                result = updateQuery.executeUpdate();
            }
            tr.commit();
            return result;
        }catch (HibernateException e){
            System.err.println(e.getMessage());
            throw new InternalServerError("Internal Server Error");
        }

    }

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
