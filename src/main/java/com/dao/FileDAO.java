package com.dao;

import com.dao.GenericDAO;
import com.entity.File;
import com.exception.InternalServerError;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FileDAO extends GenericDAO<File> {
    private static final String DELETE_FILE = "DELETE FROM С_FILE WHERE ID=?";

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
