package com.dao;

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

    public T update(T t) throws InternalServerError {
        Transaction tr = null;
        try (Session session = createSessionFactory().openSession()){
            tr = session.getTransaction();
            tr.begin();
            session.update(t);
            tr.commit();
        }catch (HibernateException e) {
            System.err.println(e.getMessage());
            if (tr != null)
                tr.rollback();
            throw new InternalServerError("Internal Server Error");
        }
        return t;
    }

    abstract void delete(long id) throws InternalServerError;

    public T findById(Class<T> c,long id) throws InternalServerError {
        try(Session session = createSessionFactory().openSession()) {
            return c.cast(session.get(c, id));
        }catch (HibernateException e) {
            System.err.println(e.getMessage());
            throw new InternalServerError("Internal Server Error");
        }
    }


    public static SessionFactory createSessionFactory(){
        if (sessionFactory == null) {
            return new Configuration().configure().buildSessionFactory();}
        return sessionFactory;
    }
}
