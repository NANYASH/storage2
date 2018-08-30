package com.dao;

import com.entity.File;
import com.exception.InternalServerError;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FileDAO extends GenericDAO<File> {
    private static final String DELETE_FILE = "DELETE FROM С_FILE WHERE ID=?";
    private static final String FIND_SIZE_BY_STORAGE_ID = "SELECT FILE_SIZE FROM С_FILE WHERE ID_STORAGE = ?";

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

    public List<Long> findSizeByStorageId(long id) throws InternalServerError {
        try(Session session = createSessionFactory().openSession()) {
            NativeQuery query = session.createNativeQuery(FIND_SIZE_BY_STORAGE_ID);
            query.setParameter(1,id);
            List<BigDecimal> bigDecimals = query.getResultList();
            List<Long> size = new ArrayList<>();
            for (BigDecimal bigDecimal : bigDecimals){
                size.add(bigDecimal.longValue());
            }
            return size;

        }catch (HibernateException e){
            System.err.println(e.getMessage());
            throw new InternalServerError("Internal Server Error");
        }
    }
}
