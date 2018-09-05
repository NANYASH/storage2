package com.dao;

import com.entity.File;
import com.exception.BadRequestException;
import com.exception.InternalServerError;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

@Repository
public class FileDAO extends GenericDAO<File> {
     private static final String VALIDATE_TRANSFER_FILE = "SELECT ID FROM STORAGE " +
            "WHERE ID = ? AND " +
            "CONTAINS(FORMATS_SUPPORTED,(SELECT FILE_FORMAT FROM С_FILE WHERE ID = ?)) > 0 AND " +
            "(STORAGE_SIZE - COALESCE((SELECT SUM(FILE_SIZE) FROM С_FILE WHERE ID_STORAGE = ?),0)) > (SELECT FILE_SIZE FROM С_FILE WHERE ID = ?)";
    private static final String TRANSFER_FILE = "UPDATE С_FILE SET ID_STORAGE = ? " +
            "WHERE ID_STORAGE = ? AND ID = ?";

    private static final String VALIDATE_PUT_FILE = "SELECT ID " +
            "FROM STORAGE " +
            "WHERE ID = ? AND " +
            "CONTAINS(FORMATS_SUPPORTED,?) > 0 AND " +
            "STORAGE_SIZE - NVL((SELECT SUM(FILE_SIZE) FROM С_FILE WHERE ID_STORAGE = ? ),0) > ?";


    private static final String PUT_FILE = "INSERT INTO С_FILE(ID,FILE_NAME,FILE_FORMAT,FILE_SIZE,ID_STORAGE) " +
            "VALUES(FILE_SEQ.NEXTVAL,?,?,?,?)";

    private static final String DELETE_FILE = "DELETE FROM С_FILE WHERE ID=?";

    @Override
    public File save(File file) throws InternalServerError {
        Transaction tr = null;
        try(Session session = createSessionFactory().openSession()) {
            NativeQuery validateQuery = session.createNativeQuery(VALIDATE_PUT_FILE);
            NativeQuery insertQuery = session.createNativeQuery(PUT_FILE);
            validateQuery.setParameter(1,file.getStorage().getId());
            validateQuery.setParameter(2,file.getFormat());
            validateQuery.setParameter(3,file.getStorage().getId());
            validateQuery.setParameter(4,file.getSize());
            insertQuery.setParameter(1,file.getName());
            insertQuery.setParameter(2,file.getFormat());
            insertQuery.setParameter(3,file.getSize());
            insertQuery.setParameter(4,file.getStorage().getId());
            tr = session.getTransaction();
            tr.begin();
            if (validateQuery.getSingleResult()!=null)
                insertQuery.executeUpdate();
            tr.commit();
            return file;
        }catch (HibernateException e){
            System.err.println(e.getMessage());
            throw new InternalServerError("Internal Server Error");
        }
    }

    public int transferFile(long from,long to, long fileId) throws InternalServerError, BadRequestException {
        Transaction tr = null;
        int result = 0;
        try (Session session = createSessionFactory().openSession()) {
            NativeQuery validateQuery = session.createNativeQuery(VALIDATE_TRANSFER_FILE);
            NativeQuery updateQuery = session.createNativeQuery(TRANSFER_FILE);
            validateQuery.setParameter(1, to);
            validateQuery.setParameter(2, fileId);
            validateQuery.setParameter(3, to);
            validateQuery.setParameter(4, fileId);
            updateQuery.setParameter(1,to);
            updateQuery.setParameter(2,from);
            updateQuery.setParameter(3,fileId);
            tr = session.getTransaction();
            tr.begin();
            if (validateQuery.getSingleResult()!=null)
                result = updateQuery.executeUpdate();
            tr.commit();
            return result;
        } catch (HibernateException e) {
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
