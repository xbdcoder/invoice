package com.invoice.core;

import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.EntityManager;

public class TransactionHelper {

    // Method to execute code inside a transaction
    public static void executeInTransaction(Session session, Runnable action) {
        Transaction transaction = null;
        try {
            // Start a transaction
            transaction = session.beginTransaction();

            // Execute the action inside the transaction
            action.run();

            // Commit the transaction if no exception occurs
            transaction.commit();
        } catch (Exception e) {
            // Rollback the transaction in case of any exception
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            // Optionally, close the session if not managed by a framework like Spring
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
