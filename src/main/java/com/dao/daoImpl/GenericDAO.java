package com.dao.daoImpl;

import com.exception.InternalServerError;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public abstract class GenericDAO<T> {
    private static SessionFactory sessionFactory;

    public T save(T t) throws InternalServerError {
        Transaction tr = null;
        try(Session session = createSessionFactory().openSession()) {
            tr = session.getTransaction();
            tr.begin();
            session.save(t);
            tr.commit();
        }catch (HibernateException e) {
            System.err.println(e.getMessage());
            if (tr != null)
                tr.rollback();
            throw new InternalServerError("Internal Server Error");
        }
        System.out.println("Save is done");
        return t;
    }

    public static SessionFactory createSessionFactory(){
        if (sessionFactory == null) {
            return new Configuration().configure().buildSessionFactory();}
        return sessionFactory;
    }
}
