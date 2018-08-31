package com.dao;

import com.entity.File;
import com.exception.InternalServerError;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

@Repository
public class FileDAO extends GenericDAO<File> {
    private static final String SAVE = "INSERT INTO С_FILE(ID,FILE_NAME,FILE_FORMAT,FILE_SIZE,ID_STORAGE) " +
            "VALUES(FILE_SEQ.NEXTVAL,?,?,?, (SELECT ID FROM (SELECT ID " +
            "FROM STORAGE " +
            "WHERE ID = ? AND " +
            "FORMATS_SUPPORTED = ? AND " +
            "STORAGE_SIZE - COALESCE((SELECT SUM(FILE_SIZE) FROM С_FILE WHERE ID_STORAGE = ? ),0) > ?)WHERE ROWNUM = 1))";
    private static final String DELETE_FILE = "DELETE FROM С_FILE WHERE ID=?";

    @Override
    public File save(File file) throws InternalServerError {
        Transaction tr = null;
        try(Session session = createSessionFactory().openSession()) {
            NativeQuery query = session.createNativeQuery(SAVE);
            query.addEntity(File.class);
            query.setParameter(1,file.getName());
            query.setParameter(2,file.getFormat());
            query.setParameter(3,file.getSize());
            query.setParameter(4,file.getStorage().getId());
            query.setParameter(5,file.getFormat());
            query.setParameter(6,file.getStorage().getId());
            query.setParameter(7,file.getSize());
            tr = session.getTransaction();
            tr.begin();
            query.executeUpdate();
            tr.commit();
            return file;
        }catch (HibernateException e){
            System.err.println(e.getMessage());
            throw new InternalServerError("Internal Server Error");
        }
    }

    @Override
    public void delete(long id) throws InternalServerError {
        Transaction tr = null;
        try(Session session = createSessionFactory().openSession()) {
            NativeQuery query = session.createNativeQuery(DELETE_FILE);
            query.addEntity(File.class);
            query.setParameter(1,id);
            tr = session.getTransaction();
            tr.begin();
            query.executeUpdate();
            tr.commit();
        }catch (HibernateException e){
            System.err.println(e.getMessage());
            throw new InternalServerError("Internal Server Error");
        }
    }
}
