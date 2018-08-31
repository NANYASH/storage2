package com.dao;

import com.entity.File;
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
    private static final String TRANSFER_FILE = "UPDATE С_FILE SET ID_STORAGE = " +
            "(SELECT ID FROM STORAGE " +
            "WHERE ID = ? AND " +
            "FORMATS_SUPPORTED = (SELECT FILE_FORMAT FROM С_FILE WHERE ID = ?) AND " +
            "(STORAGE_SIZE - COALESCE((SELECT SUM(FILE_SIZE) FROM С_FILE WHERE ID_STORAGE = ?),0)) > (COALESCE((SELECT SUM(FILE_SIZE) FROM С_FILE WHERE ID_STORAGE = ?),0))) " +
            "WHERE ID_STORAGE = ? AND ID = ?";
    private static final String TRANSFER_ALL = "UPDATE С_FILE SET ID_STORAGE = (SELECT ID FROM STORAGE " +
            "WHERE ID = ? AND " +
            "FORMATS_SUPPORTED = (SELECT FORMATS_SUPPORTED FROM STORAGE WHERE ID = ?) AND " +
            "(STORAGE_SIZE - COALESCE((SELECT SUM(FILE_SIZE) FROM С_FILE WHERE ID_STORAGE = ?),0)) > (COALESCE((SELECT SUM(FILE_SIZE) FROM С_FILE WHERE ID_STORAGE = ?),0))) " +
            "WHERE ID_STORAGE = ?";

    public boolean transferFile(long from,long to, long fileId) throws InternalServerError, BadRequestException {
        Transaction tr = null;
        int result;
        try (Session session = createSessionFactory().openSession()) {
            NativeQuery query = session.createNativeQuery(TRANSFER_FILE);
            query.addEntity(File.class);
            query.setParameter(1, to);
            query.setParameter(2, fileId);
            query.setParameter(3, to);
            query.setParameter(4, fileId);
            query.setParameter(5, from);
            query.setParameter(6, fileId);
            tr = session.getTransaction();
            tr.begin();
            result = query.executeUpdate();
            tr.commit();
            if (result!=0)return true;
            throw new BadRequestException("BadRequest exception");
        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            throw new InternalServerError("Internal Server Error");
        }
    }

    public void transferAll(long from,long to) throws InternalServerError {
        Transaction tr = null;
        try(Session session = createSessionFactory().openSession()) {
            NativeQuery query = session.createNativeQuery(TRANSFER_ALL);
            query.setParameter(1,to);
            query.setParameter(2,from);
            query.setParameter(3,to);
            query.setParameter(4,from);
            query.setParameter(5,from);
            tr = session.getTransaction();
            tr.begin();
            query.executeUpdate();
            tr.commit();
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
