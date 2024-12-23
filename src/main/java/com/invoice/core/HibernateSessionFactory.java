package com.invoice.core;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactory {

    private static SessionFactory sessionFactory;

    private HibernateSessionFactory() {
        // Private constructor to prevent instantiation
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            synchronized (HibernateSessionFactory.class) {
                if (sessionFactory == null) {
                    sessionFactory = new Configuration().configure().buildSessionFactory();
                }
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
